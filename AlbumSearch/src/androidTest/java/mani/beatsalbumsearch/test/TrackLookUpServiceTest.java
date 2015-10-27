package mani.beatsalbumsearch.test;

import mani.beatsalbumsearch.model.Track;
import mani.beatsalbumsearch.model.TrackLookUpResult;
import rx.Observable;
import rx.Subscriber;

public class TrackLookUpServiceTest extends BaseRestServiceTest {
    private String testJson1 = "test_track_lookup_response1.json";

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

    public void testTrackLookUpService() throws Exception {

        // Load the test response json
        String testResponseJson = new FileUtils().readFromAssetsFolder(testJson1);

        mockResponseWithCodeAndContent(200, testResponseJson);

        Subscriber<TrackLookUpResult> subscriber = new Subscriber<TrackLookUpResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                assertTrue("Error cannot happen for test response", false);
            }

            @Override
            public void onNext(TrackLookUpResult trackLookUpResult) {

                //Track detail.
                Track track = trackLookUpResult.getData();
                assertNotNull(track);

                assertEquals("Incorrect track id found ", "tr90600189", track.getId());
                assertEquals("Incorrect album title found ", "Foot Falls", track.getTitle());
                assertEquals("Incorrect album duration found ", 254, track.getDuration());
            }
        };

        Observable test = albumSearchService.getTrackDetail("asd", "123");
        test.subscribe(subscriber);
    }

}
