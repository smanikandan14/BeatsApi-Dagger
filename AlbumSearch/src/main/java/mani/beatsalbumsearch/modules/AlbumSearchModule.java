package mani.beatsalbumsearch.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mani.beatsalbumsearch.AlbumSearchApplication;
import mani.beatsalbumsearch.utils.FontProvider;

import static android.content.Context.MODE_PRIVATE;

/**
 * Module to load all application related dependencies.
 * Most of the objects provided here are singleton that can be used across anywhere
 * in the application
 */
@Module (
        injects = {
            AlbumSearchApplication.class
        },
        library = true
)
public class AlbumSearchModule {

    private final AlbumSearchApplication app;

    public AlbumSearchModule(AlbumSearchApplication app) {
        this.app = app;
    }

    @Provides @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides @Singleton @ForApplication
    Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides @Singleton
    Resources provideResources() {
        return app.getResources();
    }

    @Provides @Singleton
    AssetManager provideAssets() {
        return app.getAssets();
    }

    @Provides @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("album_search", MODE_PRIVATE);
    }

    @Provides @Singleton
    FontProvider provideFont(AssetManager assetManager) {
        return new FontProvider(assetManager);
    }

    @Provides @Singleton
    LayoutInflater provideLayoutInflater(@ForApplication Context context) {
        return LayoutInflater.from(context);
    }

}
