package mani.beatsalbumsearch.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class AlbumRefs implements Parcelable {

    List<Artist> artists;

    List<Track> tracks;

    public AlbumRefs(Parcel in) {
        readFromParcel(in);
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    static final Parcelable.Creator<AlbumRefs> CREATOR
            = new Parcelable.Creator<AlbumRefs>() {

        public AlbumRefs createFromParcel(Parcel in) {
            return new AlbumRefs(in);
        }

        public AlbumRefs[] newArray(int size) {
            return new AlbumRefs[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(artists);
        dest.writeList(tracks);
    }

    private void readFromParcel(Parcel in) {
        artists = in.readArrayList(Artist.class.getClassLoader());
        tracks = in.readArrayList(Track.class.getClassLoader());
    }
}