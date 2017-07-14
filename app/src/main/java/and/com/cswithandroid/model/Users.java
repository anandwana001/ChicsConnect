package and.com.cswithandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 29-06-2017.
 */

public class Users implements Parcelable {

    private String UserName;
    private String UserImage;
    private String UserBio;
    private String emailid;

    public Users() {
    }

    public Users(String userName, String userImage, String userBio, String emailid) {
        this.UserName = userName;
        this.UserImage = userImage;
        this.UserBio = userBio;
        this.emailid = emailid;
    }

    protected Users(Parcel in) {
        UserName = in.readString();
        UserImage = in.readString();
        UserBio = in.readString();
        emailid = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public void setUserImage(String userImage) {
        this.UserImage = userImage;
    }

    public void setUserBio(String userBio) {
        this.UserBio = userBio;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
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

    public String getEmailid() {
        return this.emailid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UserName);
        dest.writeString(UserImage);
        dest.writeString(UserBio);
        dest.writeString(emailid);
    }
}
