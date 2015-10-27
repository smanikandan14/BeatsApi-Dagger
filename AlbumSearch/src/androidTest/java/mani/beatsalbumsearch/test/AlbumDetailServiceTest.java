package mani.beatsalbumsearch.test;

import android.test.UiThreadTest;

import mani.beatsalbumsearch.model.Album;
import mani.beatsalbumsearch.model.AlbumLookUpResult;
import rx.Observable;
import rx.Subscriber;

public class AlbumDetailServiceTest extends BaseRestServiceTest {

    private String testJson1 = "test_album_detail_response1.json";

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

    @UiThreadTest
    public void testAlbumDetailService() throws Exception {
        // Load the test response json
        String testResponseJson = new FileUtils().readFromAssetsFolder(testJson1);

        mockResponseWithCodeAndContent(200, testResponseJson);

        Subscriber<AlbumLookUpResult> subscriber = new Subscriber<AlbumLookUpResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                assertTrue("Error cannot happen for test response", false);
            }

            @Override
            public void onNext(AlbumLookUpResult albumLookUpResult) {
                //Album detail.
                Album album = albumLookUpResult.getData();
                assertNotNull(album);

                assertEquals("Incorrect album id found ", "al90600053", album.getId());
                assertEquals("Incorrect album title found ", "Kitty Hawk", album.getAlbumTitle());
                assertEquals("Incorrect album total tracks found ", 12, album.getTotalTracks());
                assertEquals("Incorrect album duration found ", 2802, album.getDuration());
                assertEquals("Incorrect album duration found ", "2013-10-29", album.getRelease_date());
            }
        };

        Observable test = albumSearchService.getAlbumDetail("asd", "123");
        test.subscribe(subscriber);
    }

}
