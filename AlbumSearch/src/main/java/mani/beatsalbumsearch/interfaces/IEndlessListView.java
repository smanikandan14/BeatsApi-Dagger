package mani.beatsalbumsearch.interfaces;

import java.util.List;

import mani.beatsalbumsearch.model.Info;

/**
 *  This interface forces the listview to get the data and pagination information
 *  of the last loaded data from network.
 *
 */
public interface IEndlessListView<T> {

    void addNewData(List<T> data, Info info);

    void addToExistingData(List<T> data, Info info);
}
