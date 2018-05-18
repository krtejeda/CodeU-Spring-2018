package codeu.model.store.basic;

import codeu.model.data.user.User;
import codeu.model.data.user.UserGroup;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
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

  /** The in-memory admins. */
  private Map<String, User> adminByName;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private AdminStore() {
    adminByName = new HashMap<>();
  }

  /**
   * Returns the singleton instance of AdminStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static AdminStore getInstance() {
    if (instance == null) {
      instance = new AdminStore();
    }
    return instance;
  }

  /**
   * Set isAdmin of the oldest user by creation time to true
   * @param users all users
   */
  public AdminStore setRootFrom(Collection<User> users) {
    users.stream()
        .min(Comparator.comparing(User::getCreationTime))
        .ifPresent(user -> user.setGroup(UserGroup.ROOT));
    return instance;
  }

  /**
   * Initialize admin users
   * @param users all users
   */
  public AdminStore setAdminsFrom(Collection<User> users) {
    adminByName =
      users.stream()
        .filter(user -> user.group() == UserGroup.ADMIN)
        .collect(Collectors.toMap(User::getName, Function.identity()));
    return instance;
  }

  public void setRoot(User user) {
    user.setGroup(UserGroup.ROOT);
    adminByName.put(user.getName(), user);
  }

  public void addAdmin(User user) {
    user.setGroup(UserGroup.ADMIN);
    adminByName.put(user.getName(), user);
  }

  public void removeAdmin(User user) {
    adminByName.remove(user.getName());
    user.setGroup(UserGroup.REGULAR_USER);
  }

  public boolean isAdmin(String username) {
    return adminByName.containsKey(username);
  }

  public ImmutableList<String> getAllAdminNames() {
    return ImmutableList.copyOf(adminByName.keySet());
  }
}
