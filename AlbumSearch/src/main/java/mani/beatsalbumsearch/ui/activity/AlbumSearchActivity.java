package mani.beatsalbumsearch.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import mani.beatsalbumsearch.R;
import mani.beatsalbumsearch.ui.fragment.AlbumSearchFragment;


public class AlbumSearchActivity extends AppCompatActivity {

    private final static String FRAGMENT_TAG = "album_search_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_album_search);

        // If fragment is not saved by system, then create a one. Else re-use.
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AlbumSearchFragment(), FRAGMENT_TAG)
                    .commit();
        }
    }
}
