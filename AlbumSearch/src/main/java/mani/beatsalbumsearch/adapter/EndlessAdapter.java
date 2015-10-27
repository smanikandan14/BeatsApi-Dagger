package mani.beatsalbumsearch.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Used by EndlessListView to update the data that the adapter should use.
 * Uses generics so that list of any type can be supplied to adapter.
 *
 */
public abstract class EndlessAdapter<T> extends BaseAdapter {

    public abstract void addNewData(List<T> data);

    public abstract void addToExistingData(List<T> data);
}
