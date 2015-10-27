package mani.beatsalbumsearch;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import mani.beatsalbumsearch.modules.AlbumSearchModule;
import mani.beatsalbumsearch.modules.ApiModule;
import mani.beatsalbumsearch.modules.DataModule;
import mani.beatsalbumsearch.modules.UIModule;

/**
 *
 *  Dagger (Depedency Injection Frameowrk) is used to build the necessary
 *  object graph when the application is first started.
 *
 */
public class AlbumSearchApplication extends Application {

    private ObjectGraph objectGraph;

    @Override public void onCreate() {
        super.onCreate();
        buildObjectGraphAndInject();
    }

    public void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(
                new AlbumSearchModule(this),
                new ApiModule(),
                new DataModule(),
                new UIModule());
        objectGraph.inject(this);

    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static AlbumSearchApplication get(Context context) {
        return (AlbumSearchApplication) context.getApplicationContext();
    }
}
