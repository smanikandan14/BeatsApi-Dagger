package mani.beatsalbumsearch.modules;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mani.beatsalbumsearch.model.ClientId;
import mani.beatsalbumsearch.rest.AlbumSearchService;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

/**
 *
 * Proivdes necessary objects requried for making REST api call.
 * Base end point, client id and REST adapter are provided.
 *
 */
@Module(
        complete = false,
        library = true
)

public final class ApiModule {
    public static final String PRODUCTION_API_URL = "https://partner.api.beatsmusic.com/v1/api/";
    private static final String CLIENT_ID = "qtv3jd27hk45ymsmhhfsbc9q";

    @Provides @Singleton @ClientId
    String provideClientId() {
        return CLIENT_ID;
    }

    @Provides @Singleton
    Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(PRODUCTION_API_URL);
    }

    @Provides @Singleton
    Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }

    @Provides @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client) {
        return new RestAdapter.Builder() //
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(client) //
                .setEndpoint(endpoint) //
                .build();
    }

    @Provides @Singleton
    AlbumSearchService provideAlbumSearchService(RestAdapter restAdapter) {
        return restAdapter.create(AlbumSearchService.class);
    }
}
