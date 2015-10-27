package mani.beatsalbumsearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Album implements Parcelable {

    String id;

    @SerializedName("display")
    String albumDisplay;

    @SerializedName("detail")
    String albumArtist;

    @SerializedName("title")
    String albumTitle;

    @SerializedName("total_tracks")
    int totalTracks;

    int duration;

    AlbumRefs refs;

    String imageUrl;

    String release_date;

    public Album(Parcel in) {
        readFromParcel(in);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumDisplay() {
        return albumDisplay;
    }

    public void setAlbumDisplay(String albumDisplay) {
        this.albumDisplay = albumDisplay;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public AlbumRefs getRefs() {
        return refs;
    }

    public void setRefs(AlbumRefs refs) {
        this.refs = refs;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    static final Parcelable.Creator<Album> CREATOR
            = new Parcelable.Creator<Album>() {

        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(albumTitle);
        dest.writeString(albumArtist);
        dest.writeInt(duration);
        dest.writeInt(totalTracks);
        dest.writeParcelable(refs,flags);
        dest.writeString(imageUrl);
        dest.writeString(release_date);
    }

    private void readFromParcel(Parcel in) {
        id = in.readString();
        albumTitle = in.readString();
        albumArtist = in.readString();
        duration = in.readInt();
        totalTracks = in.readInt();
        refs = in.readParcelable(AlbumRefs.class.getClassLoader());
        imageUrl = in.readString();
        release_date = in.readString();
    }
}
