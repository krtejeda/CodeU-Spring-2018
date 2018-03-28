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
  private final String newUsername;
  private final String newPassword;
  private final String newDescription;

  /**
   * Sets up required and optional fields for {@link UpdateUserPersistentDatastore} to be updated.
   */
  public static class Builder {
    // Optional parameter
    private String currentUsername;

    // Optional parameters. Default is set to null.
    private String newUsername;
    private String newPassword;
    private String newDescription;

    public Builder(String currentUsername) {
      this.currentUsername = currentUsername;
    }

    public Builder setNewUsername(String newUsername) {
      this.newUsername = newUsername;
      return this;
    }

    public Builder setNewPassword(String newPassword) {
      this.newPassword = newPassword;
      return this;
    }

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
    datastore = DatastoreServiceFactory.getDatastoreService();
    currentUsername = builder.currentUsername;
    newUsername = builder.newUsername;
    newPassword = builder.newPassword;
    newDescription = builder.newDescription;
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
    if (newUsername != null) {
      retrievedEntity.setProperty("username", newUsername);
    }
    if (newPassword != null) {
      retrievedEntity.setProperty("password", newPassword);
    }
    if (newDescription != null) {
      retrievedEntity.setProperty("description", newDescription);
    }
    datastore.put(retrievedEntity);
    return true;
  }
}
