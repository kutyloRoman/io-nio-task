package com.kutylo.task8;

import com.kutylo.task8.annotations.JabaColumn;
import com.kutylo.task8.annotations.JabaEntity;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringSubstitutor;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JabaTemplate<T> {
  private String targetTableName;
  private Map<String, String> columnMappings;
  private Class<T> targetClass;
  private Connection dbConnection;

  private String selectTemplate;
  private String insertTemplate;
  private String updateTemplate;
  private String deleteTemplate;

  private JabaTemplate(Class<T> targetClass, Connection connection) throws SQLException {
    this.targetClass = targetClass;
    this.dbConnection = connection;
  }

  public static <E> JabaTemplate<E> create(Class<E> targetClass, Connection connection) throws Exception {
    JabaTemplate<E> template = new JabaTemplate<>(targetClass, connection);

    JabaEntity entityMark = targetClass.getDeclaredAnnotation(JabaEntity.class);

    if (entityMark != null) {
      if (!entityMark.tableName().isEmpty()) {
        template.targetTableName = entityMark.tableName();
      } else {
        template.targetTableName = targetClass.getSimpleName().toLowerCase();
      }
    } else {
      throw new RuntimeException("Class " + targetClass.getName() + " is not marked as JabaEntity");
    }

    Field[] targetClassFields = targetClass.getDeclaredFields();
    template.columnMappings = new HashMap<>(targetClassFields.length);

    for (Field field : targetClassFields) {
      JabaColumn jabaColumn = field.getAnnotation(JabaColumn.class);

      if (jabaColumn != null) {
        if (!isTypeSupported(field.getType())) {
          throw new RuntimeException(
              "Type " + field.getType().getName() +
                  " is not supported (JabaEntity: " + template.targetTableName + ")"
          );
        }

        String fieldMapping = "";

        if (jabaColumn.columnName().isEmpty()) {
          fieldMapping = field.getName().toLowerCase();
        } else {
          fieldMapping = jabaColumn.columnName();
        }

        if (template.columnMappings.values().contains(fieldMapping)) {
          throw new RuntimeException(
              "Column '" + fieldMapping + "' is declared at least twice in " + template.targetTableName
          );
        }

        template.columnMappings.put(field.getName(), fieldMapping);
      }
    }

    template.insertTemplate =
        "INSERT INTO " +
            template.targetTableName +
            " (${fieldsToInsert}) VALUES (${valuesToInsert});";

    template.updateTemplate =
        "UPDATE " + template.targetTableName +
            " SET ${updatedValues} WHERE ${conditions};";

    template.deleteTemplate =
        "DELETE FROM " + template.targetTableName +
            " WHERE ${conditions};";

    template.selectTemplate =
        "SELECT * FROM " +
            template.targetTableName +
            " WHERE ${conditions};";


    return template;
  }

  public T search(T searchObject) throws Exception {
    String selectQuery = getSelectQuery(searchObject);

    JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
    rowSet.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);

    rowSet.setCommand(selectQuery);
    rowSet.execute();

    if (!rowSet.next()) {
      return null;
    }

    Object object = targetClass.newInstance();

    for (String fieldName : columnMappings.keySet()) {
      Field field = object.getClass().getDeclaredField(fieldName);

      field.setAccessible(true);
      field.set(object, rowSet.getObject(columnMappings.get(fieldName)));
    }

    return targetClass.cast(object);
  }

  public void save(T object) throws Exception {
    dbConnection
        .prepareStatement(getInsertQuery(object))
        .execute();
  }

  public int update(T searchObject, T updatedObject) throws Exception {
    return dbConnection
        .prepareStatement(getUpdateQuery(searchObject, updatedObject))
        .executeUpdate();
  }

  public void delete(T searchObject) throws Exception {
    String deleteQuery = getDeleteQuery(searchObject);

    PreparedStatement statement = dbConnection.prepareStatement(deleteQuery);
    statement.execute();
  }

  private String getSelectQuery(T searchObject) throws Exception {
    Map<String, String> gaps = new HashMap<>(1);
    List<Pair<String, String>> fieldsNameValues = getFieldsNameValueList(searchObject);

    gaps.put("conditions", String.join(
        " AND ",
        fieldsNameValues
            .stream()
            .map(fv -> fv.getKey() + " = " + fv.getValue())
            .collect(Collectors.toList())
    ));

    StringSubstitutor substitutor = new StringSubstitutor(gaps);
    return substitutor.replace(selectTemplate);
  }

  private String getInsertQuery(T object) throws Exception {
    Map<String, String> gaps = new HashMap<>(2);
    List<Pair<String, String>> fieldsNameValues = getFieldsNameValueList(object);

    gaps.put("fieldsToInsert", String.join(
        ", ",
        fieldsNameValues
            .stream()
            .map(Pair::getKey)
            .collect(Collectors.toList())
    ));

    gaps.put("valuesToInsert", String.join(
        ", ",
        fieldsNameValues
            .stream()
            .map(Pair::getValue)
            .collect(Collectors.toList())
    ));

    StringSubstitutor substitutor = new StringSubstitutor(gaps);
    return substitutor.replace(insertTemplate);
  }

  private String getUpdateQuery(T searchObject, T updatedObject) throws Exception {
    Map<String, String> gaps = new HashMap<>(2);
    List<Pair<String, String>> fieldsNameValues = getFieldsNameValueList(searchObject);
    List<Pair<String, String>> fieldsNameValuesForUpdatedObject = getFieldsNameValueList(updatedObject);

    gaps.put("updatedValues", String.join(
        ", ",
        fieldsNameValuesForUpdatedObject
            .stream()
            .map(fv -> fv.getKey() + " = " + fv.getValue())
            .collect(Collectors.toList())
    ));

    gaps.put("conditions", String.join(
        " AND ",
        fieldsNameValues
            .stream()
            .map(fv -> fv.getKey() + " = " + fv.getValue())
            .collect(Collectors.toList())
    ));

    StringSubstitutor substitutor = new StringSubstitutor(gaps);
    return substitutor.replace(updateTemplate);
  }

  private String getDeleteQuery(T searchObject) throws Exception {
    Map<String, String> gaps = new HashMap<>(2);
    List<Pair<String, String>> fieldsNameValues = getFieldsNameValueList(searchObject);

    gaps.put("conditions", String.join(
        " AND ",
        fieldsNameValues
            .stream()
            .map(fv -> fv.getKey() + " = " + fv.getValue())
            .collect(Collectors.toList())
    ));

    StringSubstitutor substitutor = new StringSubstitutor(gaps);
    return substitutor.replace(deleteTemplate);
  }

  private List<Pair<String, String>> getFieldsNameValueList(T object) throws Exception {
    List<Pair<String, String>> fieldsValuesList = new ArrayList<>();

    for (String fieldName : columnMappings.keySet()) {
      Field field = object.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);

      Object fieldValue = field.get(object);

      if (fieldValue != null) {
        fieldsValuesList.add(new MutablePair<>(columnMappings.get(fieldName), getSqlFriendlyValueString(fieldValue)));
      }
    }

    return fieldsValuesList;
  }

  private String getSqlFriendlyValueString(Object object) {
    if (object instanceof String) {
      return "'" + object.toString() + "'";
    }

    if (object instanceof Date) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      return "'" + sdf.format(object) + "'";
    }

    return object.toString();
  }


  private static boolean isTypeSupported(Class<?> type) {
    for (Class<?> supportedType : supportedTypes) {
      if (type.equals(supportedType)) {
        return true;
      }
    }

    return false;
  }

  // List of supported types
  private static Class<?>[] supportedTypes = {
      Long.class,
      Integer.class,
      Short.class,
      Float.class,
      Double.class,
      String.class,
      Date.class,
      Boolean.class,
      long.class,
      int.class,
      short.class,
      float.class,
      double.class,
      boolean.class
  };
}
