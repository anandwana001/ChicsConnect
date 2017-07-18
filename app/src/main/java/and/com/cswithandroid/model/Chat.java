package and.com.cswithandroid.model;

/**
 * Created by dell on 23-06-2017.
 */

public class Chat {

    private String text;
    private String name;
    private String reciever;


    public Chat() {
    }

    public Chat(String text, String name, String reciever) {
        this.text = text;
        this.name = name;
        this.reciever = reciever;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getReciever() {
        return reciever;
    }
}
