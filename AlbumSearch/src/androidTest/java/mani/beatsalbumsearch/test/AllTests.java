package mani.beatsalbumsearch.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("AlbumSearchTests");
        suite.addTestSuite(AlbumSearchServiceTest.class);
        suite.addTestSuite(AlbumDetailServiceTest.class);
        suite.addTestSuite(TrackLookUpServiceTest.class);
        return suite;
    }
}
