package mani.beatsalbumsearch.test;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mani.beatsalbumsearch.test.util.SynchronousExecutor;
import retrofit.Endpoint;
import retrofit.RestAdapter;
import retrofit.client.Client;

/**
 * Test module which overrides the real objects.
 * Used for testing purposes.
 */

@Module (
        injects = {
                AlbumSearchServiceTest.class,
                AlbumDetailServiceTest.class,
                TrackLookUpServiceTest.class
        },
        overrides =  true,
        library = true,
        complete =  false
)

public class TestAlbumSearchModule {

    @Provides @Singleton
    Client provideMockClient() {
        return Mockito.mock(Client.class);
    }

    @Provides @Singleton
    SynchronousExecutor provideSynchronousExecutor() {
        return new SynchronousExecutor();
    }

    @Provides @Singleton
    RestAdapter provideRestAdapter(Client client, Endpoint endpoint) {
        return new RestAdapter.Builder() //
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(endpoint) //
                //To execute synchronously for testing purposes.
                .setExecutors(Mockito.spy(new SynchronousExecutor()),
                        Mockito.spy(new SynchronousExecutor()))
                .setClient(client) //
                .build();
    }


}
