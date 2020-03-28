package manualtests.fks;

public class AbstractApplicantVO {

  // Properties

  private int id;
  private int sid;
  private int referrerId;
  private int friendId;

  // Accessors

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getSid() {
    return sid;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public int getReferrerId() {
    return referrerId;
  }

  public void setReferrerId(int referrerId) {
    this.referrerId = referrerId;
  }

  public int getFriendId() {
    return friendId;
  }

  public void setFriendId(int friendId) {
    this.friendId = friendId;
  }

}
