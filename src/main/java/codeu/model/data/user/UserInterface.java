package codeu.model.data.user;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a registered user
 */
public interface UserInterface {

  /** Returns the ID of this User. */
  UUID getId();

  /** Returns the username of this User. */
  String getName();

  /** Returns the creation time of this User. */
  Instant getCreationTime();

  /** Returns description of this User. */
  String getDescription();

  /** Sets description of this User. */
  void setDescription(String description);

  /** Returns UserGroup of this User. */
  UserGroup group();
}
