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
    private String UserDesignation;

    protected Users(Parcel in) {
        UserName = in.readString();
        UserImage = in.readString();
        UserBio = in.readString();
        emailid = in.readString();
        UserDesignation = in.readString();
    }

    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

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
        dest.writeString(UserDesignation);
    }

    public Users() {
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

    public String getUserDesignation() {
        return this.UserDesignation;
    }

    public static Parcelable.Creator<Users> getCREATOR() {
        return Users.CREATOR;
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

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public void setUserDesignation(String userDesignation) {
        this.UserDesignation = userDesignation;
    }
}
