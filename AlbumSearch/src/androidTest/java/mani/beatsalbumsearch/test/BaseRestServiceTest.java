package mani.beatsalbumsearch.test;

import android.test.AndroidTestCase;

import org.mockito.Matchers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import mani.beatsalbumsearch.AlbumSearchApplication;
import mani.beatsalbumsearch.modules.AlbumSearchModule;
import mani.beatsalbumsearch.modules.ApiModule;
import mani.beatsalbumsearch.modules.DataModule;
import mani.beatsalbumsearch.modules.UIModule;
import mani.beatsalbumsearch.rest.AlbumSearchService;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static org.mockito.Mockito.when;

/**
 * Base rest service test class which sets up the
 * dependency objects and mock methods to generate the response.
 */
public class BaseRestServiceTest extends AndroidTestCase {

    private static final List<Header> NO_HEADERS = Collections.emptyList();

    @Inject
    protected AlbumSearchService albumSearchService;

    @Inject
    protected Client mockClient;

    ObjectGraph objectGraph;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Required for mockito to successfully mock a class. Below exception is thrown othewise.
        //ava.lang.IllegalArgumentException: dexcache == null
        // (and no default could be found; consider setting the 'dexmaker.dexcache' system property)
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());

        if ( objectGraph == null) {
            objectGraph = ObjectGraph.create(
                    new AlbumSearchModule((AlbumSearchApplication)
                            getContext().getApplicationContext()),
                    new ApiModule(),
                    new DataModule(),
                    new UIModule(),
                    new TestAlbumSearchModule());

            objectGraph.inject(this);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected void mockResponseWithCodeAndContent(int httpCode, String content) throws IOException {
        Response response = createResponseWithCodeAndJson(httpCode, content);
        when(mockClient.execute(Matchers.<Request>anyObject())).thenReturn(response);
    }

    private Response createResponseWithCodeAndJson(int responseCode, String json) {
        return new Response("http://example.com",
                responseCode,
                "nothing",
                Collections.EMPTY_LIST,
                new TypedByteArray("application/json", json.getBytes()));
    }

}
