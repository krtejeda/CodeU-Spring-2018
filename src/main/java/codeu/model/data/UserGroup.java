package codeu.model.data;

public enum UserGroup {
  ROOT(0),
  ADMIN(1),
  REGULAR_USER(2),
  BOTS(3);

  private int id;

  UserGroup(int id) {
    this.id = id;
  }

  public int id() {
    return id;
  }
}
