package mani.beatsalbumsearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlbumSearchResult {

    String code;
    @SerializedName("data")
    List<Album> albums;

    Info info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
