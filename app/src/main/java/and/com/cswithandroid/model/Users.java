package and.com.cswithandroid.model;

/**
 * Created by dell on 29-06-2017.
 */

public class Users {

    private String UserName;
    private String UserImage;
    private String UserBio;

    public Users() {
    }

    public Users(String userName, String userImage, String userBio) {
        this.UserName = userName;
        this.UserImage = userImage;
        this.UserBio = userBio;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public void setUserImage(String userImage) {
        this.UserImage = userImage;
    }

    public void setUserBio(String userBio) {
        this.UserBio = userBio;
    }

    public String getUserName() {
        return this.UserName;
    }

    public String getUserImage() {
        return this.UserImage;
    }

    public String getUserBio() {
        return this.UserBio;
    }
}
