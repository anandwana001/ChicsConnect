package and.com.chicsconnect.model;

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
    private String event_end_time;
    private String address;
    private String event_city;
    private String event_state;
    private String event_country;
    private String uid;
    private String creater_name;
    private String creator_phone_number;

    public Event() {
    }

    public Event(String event_name, String event_des, String event_type, String image, String fees, String event_date, String event_time, String event_end_time, String address, String event_city, String event_state, String event_country, String uid, String creater_name, String creator_phone_number) {
        this.event_name = event_name;
        this.event_des = event_des;
        this.event_type = event_type;
        this.image = image;
        this.fees = fees;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_end_time = event_time;
        this.address = address;
        this.event_city = event_city;
        this.event_state = event_state;
        this.event_country = event_country;
        this.uid = uid;
        this.creater_name = creater_name;
        this.creator_phone_number = creator_phone_number;
    }


    public String getEvent_end_time() {
        return event_end_time;
    }

    public void setEvent_end_time(String event_end_time) {
        this.event_end_time = event_end_time;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_des() {
        return event_des;
    }

    public void setEvent_des(String event_des) {
        this.event_des = event_des;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEvent_city() {
        return event_city;
    }

    public void setEvent_city(String event_city) {
        this.event_city = event_city;
    }

    public String getEvent_state() {
        return event_state;
    }

    public void setEvent_state(String event_state) {
        this.event_state = event_state;
    }

    public String getEvent_country() {
        return event_country;
    }

    public void setEvent_country(String event_country) {
        this.event_country = event_country;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreater_name() {
        return creater_name;
    }

    public void setCreater_name(String creater_name) {
        this.creater_name = creater_name;
    }

    public String getCreator_phone_number() {
        return creator_phone_number;
    }

    public void setCreator_phone_number(String creator_phone_number) {
        this.creator_phone_number = creator_phone_number;
    }

    public static Creator<Event> getCREATOR() {
        return Event.CREATOR;
    }

    protected Event(Parcel in) {
        event_name = in.readString();
        event_des = in.readString();
        event_type = in.readString();
        image = in.readString();
        fees = in.readString();
        event_date = in.readString();
        event_time = in.readString();
        event_end_time = in.readString();
        address = in.readString();
        event_city = in.readString();
        event_state = in.readString();
        event_country = in.readString();
        uid = in.readString();
        creater_name = in.readString();
        creator_phone_number = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event_name);
        dest.writeString(event_des);
        dest.writeString(event_type);
        dest.writeString(image);
        dest.writeString(fees);
        dest.writeString(event_date);
        dest.writeString(event_time);
        dest.writeString(event_end_time);
        dest.writeString(address);
        dest.writeString(event_city);
        dest.writeString(event_state);
        dest.writeString(event_country);
        dest.writeString(uid);
        dest.writeString(creater_name);
        dest.writeString(creator_phone_number);
    }
}