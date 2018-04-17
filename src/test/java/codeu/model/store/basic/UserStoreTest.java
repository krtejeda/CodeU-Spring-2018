package codeu.model.store.basic;

import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class UserStoreTest {

  private UserStore userStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final User USER_ONE =
      new User(UUID.randomUUID(), "test_username_one", "password one", Instant.ofEpochMilli(1000));
  private final User USER_TWO =
      new User(UUID.randomUUID(), "test_username_two", "password two", Instant.ofEpochMilli(2000));
  private final User USER_THREE =
      new User(UUID.randomUUID(), "test_username_three", "password three", Instant.ofEpochMilli(3000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    userStore = UserStore.getTestInstance(mockPersistentStorageAgent);

    final List<User> userList = new ArrayList<>();
    userList.add(USER_ONE);
    userList.add(USER_TWO);
    userList.add(USER_THREE);
    userStore.setUsers(userList);
  }

  @Test
  public void testGetUser_byUsername_found() {
    User resultUser = userStore.getUser(USER_ONE.getName());

    assertEquals(USER_ONE, resultUser);
  }

  @Test
  public void testGetUser_byId_found() {
    User resultUser = userStore.getUser(USER_ONE.getId());

    assertEquals(USER_ONE, resultUser);
  }

  @Test
  public void testGetUser_byUsername_notFound() {
    User resultUser = userStore.getUser("fake username");

    Assert.assertNull(resultUser);
  }

  @Test
  public void testGetUser_byId_notFound() {
    User resultUser = userStore.getUser(UUID.randomUUID());

    Assert.assertNull(resultUser);
  }

  @Test
  public void testAddUser() {
    User inputUser = new User(UUID.randomUUID(), "test_username", "password", Instant.now());

    userStore.addUser(inputUser);
    User resultUser = userStore.getUser("test_username");

    assertEquals(inputUser, resultUser);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputUser);
  }

  @Test
  public void testIsUserRegistered_true() {
    Assert.assertTrue(userStore.isUserRegistered(USER_ONE.getName()));
  }

  @Test
  public void testIsUserRegistered_false() {
    Assert.assertFalse(userStore.isUserRegistered("fake username"));
  }

  @Test
  public void testUpdateUserDescription_userNotRegistered() {
    User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username_three",
            "password",
            Instant.now());
    String newDescription = "new description";
    userStore = Mockito.mock(UserStore.class);
    Mockito
        .when(userStore.isUserRegistered(fakeUser.getName()))
        .thenReturn(false);
    Mockito
        .when(
            userStore.updateUserDescription(
                fakeUser,
                newDescription))
        .thenCallRealMethod();
    Assert.assertFalse(userStore.updateUserDescription(
        fakeUser,
        newDescription));
  }

  @Test
  public void testUpdateUserDescription_updateSucessful() {
    String newDescription = "new description";
    Mockito
        .when(
            mockPersistentStorageAgent.updateUserDescription(USER_ONE.getName(), newDescription))
        .thenReturn(true);

    Assert.assertTrue(
        userStore.updateUserDescription(
            USER_ONE,
            newDescription));
    Mockito.verify(mockPersistentStorageAgent)
        .updateUserDescription(USER_ONE.getName(), newDescription);
  }

  @Test
  public void testUpdateUserDescription_updateFailed() {
    String newDescription = "new description";
    Mockito
        .when(
            mockPersistentStorageAgent.updateUserDescription(USER_ONE.getName(), newDescription))
        .thenReturn(false);

    Assert.assertFalse(
        userStore.updateUserDescription(
            USER_ONE,
            newDescription));
    Mockito.verify(mockPersistentStorageAgent)
        .updateUserDescription(USER_ONE.getName(), newDescription);
  }

  @Test
  public void testUpdateUserPassword_UserNotRegistered() {
    User fakeUser =
            new User(
                UUID.randomUUID(),
                "test_username_three",
                "password",
                Instant.now());
    String newPassword = "new password";
    userStore = Mockito.mock(UserStore.class);
    Mockito
        .when(userStore.isUserRegistered(fakeUser.getName()))
        .thenReturn(false);
    Mockito
        .when(
            userStore.updateUserPassword(
                fakeUser,
                newPassword))
        .thenCallRealMethod();
    Assert.assertFalse(userStore.updateUserPassword(
        fakeUser,
        newPassword));
  }


  @Test
  public void testUpdateUserPassword_UpdateSuccessful() {
    String newPassword = "new password";
    Mockito
        .when(
            mockPersistentStorageAgent.updateUserPassword(USER_ONE.getName(), newPassword))
        .thenReturn(true);

    Assert.assertTrue(
        userStore.updateUserPassword(
            USER_ONE,
            newPassword));
    Mockito.verify(mockPersistentStorageAgent)
        .updateUserPassword(USER_ONE.getName(), newPassword);
  }

  @Test
  public void testUpdateUserPassword_UpdateFailed() {
    String newPassword = "new password";
    Mockito
        .when(
            mockPersistentStorageAgent.updateUserPassword(USER_ONE.getName(), newPassword))
        .thenReturn(false);

    Assert.assertFalse(
        userStore.updateUserPassword(
            USER_ONE,
            newPassword));
    Mockito.verify(mockPersistentStorageAgent)
        .updateUserPassword(USER_ONE.getName(), newPassword);
  }


  private void assertEquals(User expectedUser, User actualUser) {
    Assert.assertEquals(expectedUser.getId(), actualUser.getId());
    Assert.assertEquals(expectedUser.getName(), actualUser.getName());
    Assert.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    Assert.assertEquals(expectedUser.getCreationTime(), actualUser.getCreationTime());
  }
}
