// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data.user;

import java.time.Instant;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private String password;
  private final Instant creation;
  private String description;
  private UserGroup group;

  /** TODO(Elle) create a builder pattern for user
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   * @param description description to show on user's profile page
   * @param group {@link UserGroup} this user is in
   */
  public User(
      UUID id,
      String name,
      String password,
      Instant creation,
      String description,
      UserGroup group)
  {
    this.id = id;
    this.name = name;
    this.password = password;
    this.creation = creation;
    this.description = description;
    this.group = group;
  }

  /**
   * Constructs a new User. Description is set to default.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String password, Instant creation, UserGroup group) {
    this(id, name, password, creation, getDefaultDescription(name), group);
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /**
   * Returns the password of this User.
   *
   * @throws IllegalAccessException if User's group is {@code UserGroup.BOT}
   */
  public String getPassword() throws IllegalAccessException {
    if (group == UserGroup.BOT) {
      throw new IllegalAccessException();
    }

    return password;
  }

  /** Sets password of this User. */
  public void setPassword(String newPassword) {
    this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public static String getDefaultDescription(String name) {
    return "Hi, I'm " + name + ".";
  }

  public void setGroup(UserGroup group) {
    this.group = group;
  }

  public UserGroup group() {
    return group;
  }
}
