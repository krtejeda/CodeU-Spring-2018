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

package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

public class UserTest {

  @Test
  public void testCreate_defaultDescription() {
    UUID id = UUID.randomUUID();
    String name = "test_username";
    Instant creation = Instant.now();
    String passwordHash = BCrypt.hashpw("password", BCrypt.gensalt());

    User user = new User(id, name, passwordHash, creation, UserGroup.REGULAR_USER);

    Assert.assertEquals(id, user.getId());
    Assert.assertEquals(name, user.getName());
    Assert.assertEquals(passwordHash, user.getPassword());
    Assert.assertEquals(creation, user.getCreationTime());
    Assert.assertEquals(User.getDefaultDescription(name), user.getDescription());
  }

  @Test
  public void testCreate_givenDescription() {
    UUID id = UUID.randomUUID();
    String name = "test_username";
    Instant creation = Instant.now();
    String password = "password";
    String description = "test description";

    User user = new User(id, name, password, creation, description, UserGroup.REGULAR_USER);

    Assert.assertEquals(id, user.getId());
    Assert.assertEquals(name, user.getName());
    Assert.assertEquals(password, user.getPassword());
    Assert.assertEquals(creation, user.getCreationTime());
    Assert.assertEquals(description, user.getDescription());
  }
}
