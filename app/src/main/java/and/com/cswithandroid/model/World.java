package and.com.cswithandroid.model;

/**
 * Created by dell on 28-07-2017.
 */

public class World {

    private String Username;
    private String Userpic;
    private String Caption;
    private String Photourl;
    private String TimeStamp;

    public World() {
    }

    public World(String username, String userpic, String caption, String photourl, String timeStamp) {
        Username = username;
        Userpic = userpic;
        Caption = caption;
        Photourl = photourl;
        TimeStamp = timeStamp;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setUserpic(String userpic) {
        Userpic = userpic;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public void setPhotourl(String photourl) {
        Photourl = photourl;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getUsername() {
        return Username;
    }

    public String getUserpic() {
        return Userpic;
    }

    public String getCaption() {
        return Caption;
    }

    public String getPhotourl() {
        return Photourl;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }
}
