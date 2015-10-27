package mani.beatsalbumsearch.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import mani.beatsalbumsearch.R;
import mani.beatsalbumsearch.interfaces.IBackPressedListener;
import mani.beatsalbumsearch.ui.fragment.AlbumDetailsFragment;

public class AlbumDetailsActivity extends AppCompatActivity {

    private final static String FRAGMENT_TAG = "album_details_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_tracks);
        // If fragment is not saved by system, then create a one. Else re-use.
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AlbumDetailsFragment(), FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment != null && fragment instanceof IBackPressedListener) {
            ((IBackPressedListener) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
