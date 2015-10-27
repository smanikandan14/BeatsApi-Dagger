package mani.beatsalbumsearch.test;

import java.util.List;

import mani.beatsalbumsearch.model.Album;
import mani.beatsalbumsearch.model.AlbumSearchResult;
import mani.beatsalbumsearch.model.Info;
import rx.Observable;
import rx.Subscriber;

public class AlbumSearchServiceTest extends BaseRestServiceTest {

    private String testJson1 = "test_album_search_response1.json";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void testAndroidTestCaseSetupProperly() {
        super.testAndroidTestCaseSetupProperly();
    }

    public void testAlbumSearchService() throws Exception {

        // Load the test response json
        String testResponseJson = new FileUtils().readFromAssetsFolder(testJson1);

        mockResponseWithCodeAndContent(200, testResponseJson);

        Subscriber<AlbumSearchResult> subscriber = new Subscriber<AlbumSearchResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                assertTrue("Error cannot happen for test response", false);
            }

            @Override
            public void onNext(AlbumSearchResult albumSearchResult) {
                //Album detail.
                List<Album> albumList = albumSearchResult.getAlbums();
                assertNotNull(albumList);
                assertEquals("Incorrect album list size found ", 1, albumList.size());
                assertEquals(1,2);
                Album album = albumList.get(0);
                assertEquals("Incorrect album id found ", "al33225285", album.getId());
                assertEquals("Incorrect album display name found ", "Kiss Or Kill", album.getAlbumDisplay());
                assertEquals("Incorrect album artist name found ", "EndeverafteR", album.getAlbumArtist());

                //Info
                Info pagination = albumSearchResult.getInfo();
                assertNotNull(pagination);
                assertEquals("Info object count not correct", 20, pagination.getCount());
                assertEquals("Info object count not correct", 0, pagination.getOffset());
                assertEquals("Info object count not correct", 3102, pagination.getTotal());
            }
        };

        Observable test = albumSearchService.searchAlbum("asd","123","album",0);
        test.subscribe(subscriber);
    }
}
