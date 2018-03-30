package codeu.model.store.persistence;

import codeu.model.data.User;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This class handles all update operations to {@link User}
 * with Google App Engine's Datastore service.
 *
 * @author Elle Tojaroon (etojaroo@codeustudents.com)
 */
public class UpdateUserPersistentDatastore {

  // Handle to Google AppEngine's Datastore service.
  private DatastoreService datastore;

  private final String currentUsername;
  private final Optional<String> newUsername;
  private final Optional<String> newPassword;
  private final Optional<String> newDescription;

  /**
   * Sets up required and optional fields for {@link UpdateUserPersistentDatastore} to be updated.
   */
  public static class Builder {
    // Required parameter
    private DatastoreService datastore;
    private String currentUsername;

    // Optional parameters. Default is set to null.
    private String newUsername;
    private String newPassword;
    private String newDescription;

    public Builder(DatastoreService datastore, String currentUsername) {
      this.datastore = datastore;
      this.currentUsername = currentUsername;
    }

    /**
     * Sets username field to {@code newUsername}
     * @param newUsername   new username to set
     * @return  this Builder
     */
    public Builder setNewUsername(String newUsername) {
      this.newUsername = newUsername;
      return this;
    }

    /**
     * Sets password field to {@code newPassword} encrypted
     * @param newPassword   new password to encrypt and set
     * @return  this Builder
     */
    public Builder setNewPassword(String newPassword) {
      this.newPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
      return this;
    }

    /**
     * Sets description field to {@code newDescription} encrypted
     * @param newDescription   new description to encrypt and set
     * @return  this Builder
     */
    public Builder setNewDescription(String newDescription) {
      this.newDescription = newDescription;
      return this;
    }

    public UpdateUserPersistentDatastore build() {
      return new UpdateUserPersistentDatastore(this);
    }
  }

  /**
   * Constructs a new PersistentDataStore and sets up its state to begin loading objects from the
   * Datastore service.
   */
  private UpdateUserPersistentDatastore(Builder builder) {
    datastore = builder.datastore;
    currentUsername = builder.currentUsername;
    newUsername = Optional.ofNullable(builder.newUsername);
    newPassword = Optional.ofNullable(builder.newPassword);
    newDescription = Optional.ofNullable(builder.newDescription);
  }

  /**
   * Loads User object with {@code currentUsername} from the Datastore service,
   * update provided fields, and returns true if the operation is successful, false otherwise.
   * Fields that are not provided will not be updated.
   */
  public boolean update() {
    Filter propertyFilter =
        new FilterPredicate("username", FilterOperator.EQUAL, currentUsername);
    Query query = new Query("chat-users").setFilter(propertyFilter);
    PreparedQuery pq = datastore.prepare(query);
    Entity retrievedEntity = pq.asSingleEntity();
    if (retrievedEntity == null) {
      return false;
    }
    if (newUsername.isPresent()) {
      retrievedEntity.setProperty("username", newUsername.get());
    }
    if (newPassword.isPresent()) {
      retrievedEntity.setProperty("password", newPassword.get());
    }
    if (newDescription.isPresent()) {
      retrievedEntity.setProperty("description", newDescription.get());
    }
    datastore.put(retrievedEntity);
    return true;
  }
}
