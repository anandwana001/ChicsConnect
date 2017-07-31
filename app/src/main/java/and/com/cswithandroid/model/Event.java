package and.com.cswithandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 11-06-2017.
 */

public class Event implements Parcelable {

    private String event_name;
    private String event_des;
    private String event_type;
    private String image;
    private String fees;
    private String event_date;
    private String event_time;
    private String address;
    private String event_city;
    private String event_state;
    private String event_country;
    private String uid;
    private String creater_name;

    public Event() {
    }

    public Event(String event_name, String event_des, String event_type, String image, String fees, String event_date,
                 String event_time, String address, String event_city, String event_state, String event_country, String uid, String creater_name) {
        this.event_name = event_name;
        this.event_des = event_des;
        this.event_type = event_type;
        this.image = image;
        this.fees = fees;
        this.event_date = event_date;
        this.event_time = event_time;
        this.address = address;
        this.event_city = event_city;
        this.event_state = event_state;
        this.event_country = event_country;
        this.uid = uid;
        this.creater_name = creater_name;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public void setCreater_name(String creater_name) {
        this.creater_name = creater_name;
    }

    public String getEvent_time() {
        return event_time;
    }

    public String getCreater_name() {
        return creater_name;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public void setTime(String time) {
        this.event_time = time;
    }

    public String getEvent_date() {
        return this.event_date;
    }

    public String getTime() {
        return this.event_time;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setEvent_des(String event_des) {
        this.event_des = event_des;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEvent_city(String event_city) {
        this.event_city = event_city;
    }

    public void setEvent_state(String event_state) {
        this.event_state = event_state;
    }

    public void setEvent_country(String event_country) {
        this.event_country = event_country;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEvent_name() {
        return this.event_name;
    }

    public String getEvent_des() {
        return this.event_des;
    }

    public String getEvent_type() {
        return this.event_type;
    }

    public String getImage() {
        return this.image;
    }

    public String getFees() {
        return this.fees;
    }

    public String getAddress() {
        return this.address;
    }

    public String getEvent_city() {
        return this.event_city;
    }

    public String getEvent_state() {
        return this.event_state;
    }

    public String getEvent_country() {
        return this.event_country;
    }

    public String getUid() {
        return this.uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.event_name);
        dest.writeString(this.event_des);
        dest.writeString(this.event_type);
        dest.writeString(this.image);
        dest.writeString(this.fees);
        dest.writeString(this.event_date);
        dest.writeString(this.event_time);
        dest.writeString(this.address);
        dest.writeString(this.event_city);
        dest.writeString(this.event_state);
        dest.writeString(this.event_country);
        dest.writeString(this.uid);
        dest.writeString(this.creater_name);
    }

    protected Event(Parcel in) {
        this.event_name = in.readString();
        this.event_des = in.readString();
        this.event_type = in.readString();
        this.image = in.readString();
        this.fees = in.readString();
        this.event_date = in.readString();
        this.event_time = in.readString();
        this.address = in.readString();
        this.event_city = in.readString();
        this.event_state = in.readString();
        this.event_country = in.readString();
        this.uid = in.readString();
        this.creater_name = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
