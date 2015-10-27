package mani.beatsalbumsearch.modules;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import mani.beatsalbumsearch.adapter.AlbumTrackListAdapter;
import mani.beatsalbumsearch.adapter.AlbumsSearchListAdapter;
import mani.beatsalbumsearch.ui.activity.AlbumSearchActivity;
import mani.beatsalbumsearch.ui.fragment.AlbumDetailsFragment;
import mani.beatsalbumsearch.ui.fragment.AlbumSearchFragment;

/**
 * This Module defines what are the UI classes that require necessary objects for
 * its dependencies.
 *
 */
@Module (
        injects = {
            AlbumSearchActivity.class,
            AlbumSearchFragment.class,
            AlbumDetailsFragment.class,
            AlbumsSearchListAdapter.class,
            AlbumTrackListAdapter.class
        },
        includes = {
                AlbumSearchModule.class,
                ApiModule.class,
                DataModule.class
        }
)
public class UIModule {

    @Provides
    AlbumsSearchListAdapter provideAlbumListAdapter(Application application) {
       return new AlbumsSearchListAdapter(application);
    }

    @Provides
    AlbumTrackListAdapter provideAlbumTrackListAdapter(Application application) {
        return new AlbumTrackListAdapter(application);
    }

}
