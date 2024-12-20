package Entities;

public abstract class User {
    private int userID;
    private String userName;

    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
