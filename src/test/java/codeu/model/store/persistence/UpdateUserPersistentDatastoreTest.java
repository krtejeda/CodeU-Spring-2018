package codeu.model.store.persistence;

import codeu.model.data.user.User;
import codeu.model.data.user.UserGroup;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Test {@link UpdateUserPersistentDatastore}
 *
 * @author Elle Tojaroon (etojaroo@codeustudents.com)
 */
public class UpdateUserPersistentDatastoreTest {

  private PersistentDataStore persistentDataStore;
  private DatastoreService datastore;
  private final LocalServiceTestHelper appEngineTestHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private User user;

  @Before
  public void setup() {
    appEngineTestHelper.setUp();
    persistentDataStore = new PersistentDataStore();
    datastore = DatastoreServiceFactory.getDatastoreService();

    user =
        new User(
            UUID.randomUUID(),
            "test_username_one",
            "password one",
            Instant.ofEpochMilli(1000),
            UserGroup.REGULAR_USER);

    // save user
    persistentDataStore.writeThrough(user);
  }

  @After
  public void tearDown() {
    appEngineTestHelper.tearDown();
  }

  @Test
  public void testUpdate_none() throws PersistentDataStoreException {
    new UpdateUserPersistentDatastore.Builder(datastore, user.getName())
        .build()
        .update();

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what there was no update
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(user.getId(), resultUserOne.getId());
    Assert.assertEquals(user.getName(), resultUserOne.getName());
    Assert.assertEquals(user.getPassword(), resultUserOne.getPassword());
    Assert.assertEquals(user.getCreationTime(), resultUserOne.getCreationTime());
    Assert.assertEquals(user.getDescription(), resultUserOne.getDescription());
  }

  @Test
  public void testUpdate_description() throws PersistentDataStoreException {
    String newDescription = "new description";
    new UpdateUserPersistentDatastore.Builder(datastore, user.getName())
        .setNewDescription(newDescription)
        .build()
        .update();

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what the update was saved
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(user.getId(), resultUserOne.getId());
    Assert.assertEquals(user.getName(), resultUserOne.getName());
    Assert.assertEquals(user.getPassword(), resultUserOne.getPassword());
    Assert.assertEquals(user.getCreationTime(), resultUserOne.getCreationTime());
    Assert.assertEquals(newDescription, resultUserOne.getDescription());
  }

  @Test
  public void testUpdate_username() throws PersistentDataStoreException {
    String newUsername = "spongebob";
    new UpdateUserPersistentDatastore.Builder(datastore, user.getName())
        .setNewUsername(newUsername)
        .build()
        .update();

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what the update was saved
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(user.getId(), resultUserOne.getId());
    Assert.assertEquals(newUsername, resultUserOne.getName());
    Assert.assertEquals(user.getPassword(), resultUserOne.getPassword());
    Assert.assertEquals(user.getCreationTime(), resultUserOne.getCreationTime());
    Assert.assertEquals(user.getDescription(), resultUserOne.getDescription());
  }

  @Test
  public void testUpdate_password() throws PersistentDataStoreException {
    String newPassword = "squarepants";
    new UpdateUserPersistentDatastore.Builder(datastore, user.getName())
        .setNewPassword(newPassword)
        .build()
        .update();

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what the update was saved
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(user.getId(), resultUserOne.getId());
    Assert.assertEquals(user.getName(), resultUserOne.getName());
    Assert.assertTrue(BCrypt.checkpw(newPassword, resultUserOne.getPassword()));
    Assert.assertEquals(user.getCreationTime(), resultUserOne.getCreationTime());
    Assert.assertEquals(user.getDescription(), resultUserOne.getDescription());
  }

  @Test
  public void testUpdate_all() throws PersistentDataStoreException {
    String newUsername = "spongebob";
    String newPassword = "patrick";
    String newDescription = "squidward";
    new UpdateUserPersistentDatastore.Builder(datastore, user.getName())
        .setNewUsername(newUsername)
        .setNewPassword(newPassword)
        .setNewDescription(newDescription)
        .build()
        .update();

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what the updates were saved
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(user.getId(), resultUserOne.getId());
    Assert.assertEquals(newUsername, resultUserOne.getName());
    Assert.assertTrue(BCrypt.checkpw(newPassword, resultUserOne.getPassword()));
    Assert.assertEquals(user.getCreationTime(), resultUserOne.getCreationTime());
    Assert.assertEquals(newDescription, resultUserOne.getDescription());
  }
}
