/**
 * Created by Matteo on 02/12/16.
 */
public class User {
    private String username;
    private String password;
    private String userID;

    public User(String username, String password, String userID) {
        this.username = username;
        this.password = password;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        if(user.userID == userID) return true;
        else return false;
    }
}
