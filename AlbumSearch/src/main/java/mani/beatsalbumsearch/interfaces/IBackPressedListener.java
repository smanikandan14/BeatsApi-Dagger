package mani.beatsalbumsearch.interfaces;

/**
 * Listener implemented by fragments to capture back key pressed event on
 * activity. Activity can inform the fragments with this call back.
 */
public interface IBackPressedListener {
    void onBackPressed();
}
