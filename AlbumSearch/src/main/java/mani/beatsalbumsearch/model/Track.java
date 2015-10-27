package mani.beatsalbumsearch.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

    String id;

    String title;

    String display;

    int duration;

    public Track(Parcel in) {
        readFromParcel(in);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    static final Parcelable.Creator<Track> CREATOR
            = new Parcelable.Creator<Track>() {

        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeInt(duration);
    }

    private void readFromParcel(Parcel in) {
        id = in.readString();
        title = in.readString();
        duration = in.readInt();
    }
}
