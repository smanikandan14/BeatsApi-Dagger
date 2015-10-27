package mani.beatsalbumsearch.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FontProvider {
    private final String NEUZEI_BOLD = "fonts/NeuzeitGrotesk.ttf";
    private final String ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
    private final String ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
    private final String ROBOTO_NORMAL = "fonts/Roboto-Regular.ttf";

    private Typeface sHelveticaBoldFont;
    private Typeface sHelveticaMediumFont;
    private Typeface sHelveticaRegularFont;

    private AssetManager assets;

    public FontProvider(AssetManager assetManager) {
        assets = assetManager;
    }

    public Typeface getBold() {
        if (sHelveticaBoldFont == null) {
            sHelveticaBoldFont = Typeface.createFromAsset(assets, ROBOTO_BOLD);
        }
        return sHelveticaBoldFont;
    }

    public Typeface getNueziBold() {
        if (sHelveticaBoldFont == null) {
            sHelveticaBoldFont = Typeface.createFromAsset(assets, NEUZEI_BOLD);
        }
        return sHelveticaBoldFont;
    }

    public Typeface getMediumFont() {
        if (sHelveticaMediumFont == null) {
            sHelveticaMediumFont = Typeface.createFromAsset(assets, ROBOTO_MEDIUM);
        }
        return sHelveticaMediumFont;
    }

    public Typeface getRegularFont() {
        if (sHelveticaRegularFont == null) {
            sHelveticaRegularFont = Typeface.createFromAsset(assets, ROBOTO_NORMAL);
        }
        return sHelveticaRegularFont;
    }
}