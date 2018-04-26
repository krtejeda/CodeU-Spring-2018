package codeu.model.store.basic;

import codeu.model.data.user.User;
import codeu.model.data.user.UserGroup;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class stores admin users, using in-memory data structures to hold values
 * and automatically loads from and saves to PersistentStorageAgent.
 * It's a singleton so all servlet classes can access the same instance.
 *
 * @author Elle Tojaroon (etojaroo@codeustudents.com)
 */
public class AdminStore {

  private static AdminStore instance;
  private UserStore userStore;

  /** The in-memory admins. */
  private Map<String, User> admins;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private AdminStore(UserStore userStore) {
    setUserStore(userStore);
    admins = new HashMap<>();
    List<User> users = userStore.getUsers();
    setDefaultAdmin(users);
    setAdmins(users);
  }

  private void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Set isAdmin of the oldest user by creation time to true
   * @param users all users
   */
  private void setDefaultAdmin(Collection<User> users) {
    users.stream()
        .min(Comparator.comparing(User::getCreationTime))
        .ifPresent(user -> user.setGroup(UserGroup.ADMIN));
  }

  /**
   * Returns the singleton instance of AdminStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static AdminStore getInstance(UserStore userStore) {
    if (instance == null) {
      instance = new AdminStore(userStore);
    }
    return instance;
  }

  /**
   * Initialize admin users
   * @param users all users
   */
  private void setAdmins(Collection<User> users) {
    admins =
      users.stream()
        .filter(user -> user.group() == UserGroup.ADMIN)
        .collect(Collectors.toMap(User::getName, Function.identity()));
  }

  public void addAdmin(User user) {
    admins.put(user.getName(), user);
    userStore.updateUserIsAdmin(user.getName(), true);
  }

  public void removeAdmin(String username) {
    admins.remove(username);
    userStore.updateUserIsAdmin(username, false);
  }

  public boolean isAdmin(String username) {
    return admins.containsKey(username);
  }

  public ImmutableList<String> getAllAdminNames() {
    return ImmutableList.copyOf(admins.keySet());
  }
}
