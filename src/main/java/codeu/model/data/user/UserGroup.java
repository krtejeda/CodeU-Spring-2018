package codeu.model.data.user;

public enum UserGroup {
  ROOT(0),
  ADMIN(1),
  REGULAR_USER(2),
  BOT(3);

  private int id;

  UserGroup(int id) {
    this.id = id;
  }

  public int id() {
    return id;
  }
}
