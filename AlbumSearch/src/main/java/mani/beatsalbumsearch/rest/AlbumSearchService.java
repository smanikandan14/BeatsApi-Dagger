package mani.beatsalbumsearch.rest;

import mani.beatsalbumsearch.model.AlbumLookUpResult;
import mani.beatsalbumsearch.model.AlbumSearchResult;
import mani.beatsalbumsearch.model.TrackLookUpResult;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Interface required by RetroFit framework to define the
 * rest calls.
 *
 * These interface returns observables.
 *
 * Rest Calls provided are
 * - Album Search
 * - Album LookUp
 * - Track LookUp
 */
public interface AlbumSearchService {

    @GET("/search")
    Observable<AlbumSearchResult> searchAlbum(
                @Query("client_id") String clientId,
                @Query("q") String query,
                @Query("type") String type,
                @Query("offset") int offset);

    @GET("/albums/{id}")
    Observable<AlbumLookUpResult> getAlbumDetail(
            @Query("client_id") String clientId,
            @Path("id") String albumId);

    @GET("/tracks/{id}")
    Observable<TrackLookUpResult> getTrackDetail(
            @Query("client_id") String clientId,
            @Path("id") String trackId);

}
