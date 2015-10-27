package mani.beatsalbumsearch.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

import mani.beatsalbumsearch.adapter.EndlessAdapter;
import mani.beatsalbumsearch.interfaces.IEndlessListView;
import mani.beatsalbumsearch.model.Info;

/**
 *  Provides endless loading of listview items using OnScrollListener.
 *  Implements the IEndlessListView so that whoever using this listview
 *  can pass in the information of new loaded data and pagination
 *  information. This is implemented in mind very specific beats api's which
 *  provides pagination information on response.
 *
 */
public class EndlessListView<T> extends ListView implements
        IEndlessListView<T>, AbsListView.OnScrollListener {

    public static interface EndlessListener {
        public void loadData(int offset) ;
    }

    private boolean isLoading;
    private EndlessAdapter adapter;
    private EndlessListener listener;
    private Info totalItemsInfo;
    private int visibleThreshold = 5;

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
    }

    public EndlessListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
    }

    public void setListener(EndlessListener listener) {
        this.listener = listener;
    }

    public void setAdapter(EndlessAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    public void addNewData(List<T> data, Info info) {
        adapter.addNewData(data);
        totalItemsInfo = info;
        isLoading = false;
    }

    public void addToExistingData(List<T> data, Info info) {
        adapter.addToExistingData(data);
        totalItemsInfo = info;
        isLoading = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (getAdapter() == null)
            return ;

        if (getAdapter().getCount() == 0)
            return ;

        //totalItems of the listview is compared with the pagination (Info) information.
        if ( totalItemsInfo != null &&
                totalItemCount >= totalItemsInfo.getTotal()) {
            return;
        }

        // Find how much items the use has scroll so far. Add the threshhold limit after
        // which we expect to load more items.
        int threshHold = visibleItemCount + firstVisibleItem + visibleThreshold;

        if (threshHold >= totalItemCount && !isLoading) {
            // It is time to add new data. We call the listener
            isLoading = true;
            listener.loadData(totalItemsInfo.getOffset()+1);
        }
    }
}
