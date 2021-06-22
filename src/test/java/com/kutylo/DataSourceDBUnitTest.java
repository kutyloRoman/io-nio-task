package com.kutylo;

import com.kutylo.task4_7.model.User;
import com.kutylo.task4_7.repository.UserRepository;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.List;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;

@RunWith(JUnit4.class)
public class DataSourceDBUnitTest extends DataSourceBasedDBTestCase {
  @Override
  protected DataSource getDataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setURL(
        "jdbc:postgresql://ec2-34-240-75-196.eu-west-1.compute.amazonaws.com:5432/dc15s0tpcukaul");
    dataSource.setUser("hbfytnzcpsvvsj");
    dataSource.setPassword("2102dd1dbd0cc69822699a75a0e1b9e06566fb2d4bd35c04c94f65a01402d7f5");
    return dataSource;
  }

  @Override
  protected IDataSet getDataSet() throws Exception {
    return new FlatXmlDataSetBuilder().build(ClassLoader.getSystemResourceAsStream("data.xml"));
  }

  @Override
  protected DatabaseOperation getSetUpOperation() {
    return DatabaseOperation.REFRESH;
  }

  @Override
  protected DatabaseOperation getTearDownOperation() {
    return DatabaseOperation.DELETE_ALL;
  }

  @Test
  public void getAllUsersTest() throws Exception {
    UserRepository repository = new UserRepository();
    List<User> users = repository.getAllUser();

    IDataSet actualData = getConnection().createDataSet();
    ITable actualTable = actualData.getTable("user_account");

    assertEquals(actualTable.getRowCount(), users.size());
  }

  @Test
  public void insertUserTest() throws Exception {
    User user = User.newBuilder()
        .setId(9)
        .setPhone("phone-test")
        .setImgUrl("imgUrl")
        .setEmail("test@insert")
        .setPassword("pass")
        .setUsername("TestInsert")
        .build();

    String[] excludedColumns = {"id", "user_role"};
    UserRepository repository = new UserRepository();
    repository.saveUser(user);

    IDataSet actualData = getConnection().createDataSet();
    ITable actualTable = actualData.getTable("user_account");

    IDataSet expectedData = new FlatXmlDataSetBuilder().build(ClassLoader.getSystemResourceAsStream("user-insert.xml"));
    ITable expectedTable = expectedData.getTable("USERS");

    assertEqualsIgnoreCols(actualTable, expectedTable, excludedColumns);
    assertEquals(actualTable.getRowCount(), actualTable.getRowCount());
  }

  @Test
  public void updateUserTest() throws Exception {
    User user = User.newBuilder()
        .setId(9)
        .setPhone("phone-test-update")
        .setImgUrl("imgUrl")
        .setEmail("test-update@insert")
        .setPassword("pass")
        .setUsername("TestUpdate")
        .build();

    UserRepository repository = new UserRepository();
    repository.updateUser(user);
    String[] excludedColumns = {"id", "user_role"};

    IDataSet actualData = getConnection().createDataSet();
    ITable actualTable = actualData.getTable("user_account");

    IDataSet expectedData = new FlatXmlDataSetBuilder().build(ClassLoader.getSystemResourceAsStream("user-update.xml"));
    ITable expectedTable = expectedData.getTable("USERS");

    assertEqualsIgnoreCols(actualTable, expectedTable, excludedColumns);
    assertEquals(actualTable.getRowCount(), actualTable.getRowCount());
  }

  @Test
  public void deleteUserByIdTest() throws Exception {
    UserRepository repository = new UserRepository();
    repository.deleteUserById(9);

    IDataSet actualData = getConnection().createDataSet();
    ITable actualTable = actualData.getTable("user_account");
    String[] excludedColumns = {"id", "user_role"};

    IDataSet expectedData = new FlatXmlDataSetBuilder().build(ClassLoader.getSystemResourceAsStream("user-delete.xml"));
    ITable expectedTable = expectedData.getTable("USERS");

    assertEqualsIgnoreCols(actualTable, expectedTable, excludedColumns);
    assertEquals(actualTable.getRowCount(), actualTable.getRowCount());

  }
}
