package mani.beatsalbumsearch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maniselvaraj on 1/11/14.
 */
public class Artist implements Parcelable {

    String id;

    String display;

    public Artist(Parcel in) {
        readFromParcel(in);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    static final Parcelable.Creator<Artist> CREATOR
            = new Parcelable.Creator<Artist>() {

        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(display);
    }

    private void readFromParcel(Parcel in) {
        id = in.readString();
        display = in.readString();
    }

}
