package and.com.chicsconnect.model;

/**
 * Created by dell on 28-07-2017.
 */

public class World {

    private String Username;
    private String Userpic;
    private String caption;
    private String image;
    private Long TimeStamp;
    private String uid;

    public World() {
    }

    public World(String username, String userpic, String caption, String image, Long timeStamp, String uid) {
        Username = username;
        Userpic = userpic;
        this.caption = caption;
        this.image = image;
        TimeStamp = timeStamp;
        this.uid = uid;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setUserpic(String userpic) {
        Userpic = userpic;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return Username;
    }

    public String getUserpic() {
        return Userpic;
    }

    public String getCaption() {
        return caption;
    }

    public String getImage() {
        return image;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public String getUid() {
        return uid;
    }
}
