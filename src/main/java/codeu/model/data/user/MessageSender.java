package codeu.model.data.user;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents something that is able to send messages to a chat
 */
public interface MessageSender {

  /** Returns the ID of this User. */
  UUID getId();

  /** Returns the username of this User. */
  String getName();

  /** Returns the creation time of this User. */
  Instant getCreationTime();

  // TODO remove description and make description store
  /** Returns description of this User. */
  String getDescription();

  /** Sets description of this User. */
  void setDescription(String description);

  /** Returns UserGroup of this User. */
  UserGroup group();
}
