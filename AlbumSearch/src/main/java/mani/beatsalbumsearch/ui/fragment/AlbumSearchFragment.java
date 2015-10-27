package mani.beatsalbumsearch.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import mani.beatsalbumsearch.AlbumSearchApplication;
import mani.beatsalbumsearch.R;
import mani.beatsalbumsearch.adapter.AlbumsSearchListAdapter;
import mani.beatsalbumsearch.model.Album;
import mani.beatsalbumsearch.model.AlbumSearchResult;
import mani.beatsalbumsearch.model.ClientId;
import mani.beatsalbumsearch.rest.AlbumSearchService;
import mani.beatsalbumsearch.ui.activity.AlbumDetailsActivity;
import mani.beatsalbumsearch.ui.view.EndlessListView;
import mani.beatsalbumsearch.utils.FontProvider;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by maniselvaraj on 26/10/14.
 */
public class AlbumSearchFragment extends Fragment {

    public static String ALBUM_IMAGE_URL = "album_image_url";
    public static String ALBUM_ID = "album_id";

    @InjectView(R.id.searchResultsList)
    EndlessListView mSearchListView;

    @InjectView(R.id.albumTitle)
    TextView mAlbumTitle;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.searchEdit)
    EditText mSearchEdit;

    @InjectView(R.id.clearSearchIcon)
    ImageView mClearSearchIcon;

    @InjectView(R.id.search_layout)
    ViewGroup mSearchParentLayout;

    @Inject
    AlbumSearchService albumSearchService;

    @Inject @ClientId
    String clientId;

    @Inject
    AlbumsSearchListAdapter adapter;

    @Inject
    FontProvider fontProvider;

    String mCurrentSearchQuery;

    Subscription albumSearchSubscription;

    Subscription searchTextChangeSubscription;

    private PublishSubject<Observable<String>> searchTextEmitterSubject;

    public AlbumSearchFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AlbumSearchApplication app = AlbumSearchApplication.get(this.getActivity());
        app.inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_search, container, false);
        ButterKnife.inject(this, view);
        mAlbumTitle.setTypeface(fontProvider.getMediumFont());

        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = (Album) adapter.getItem(position);
                launch(AlbumSearchFragment.this.getActivity(),
                        view.findViewById(R.id.albumThumbnail),
                        album);
            }
        });

        mSearchListView.setAdapter(adapter);
        mSearchListView.setListener(new EndlessListView.EndlessListener() {
            @Override
            public void loadData(int offset) {
                doAlbumSearch(mCurrentSearchQuery, offset);
            }
        });

        //Improve the user experience of touching on clear search text icon.
        mSearchParentLayout.post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                mClearSearchIcon.getHitRect(bounds);
                bounds.left -= 100;
                bounds.right += 100;
                bounds.top -= 100;
                bounds.bottom += 100;
                TouchDelegate delegate = new TouchDelegate(bounds, mClearSearchIcon);
                ((View)mClearSearchIcon.getParent()).setTouchDelegate(delegate);
            }
        });

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not interested
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    searchTextEmitterSubject.onNext(getSearchTextObservable(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Not interested
            }
        });

        searchTextEmitterSubject = PublishSubject.create();
        // debounce is used to drop items emitted by source observable
        // that are followed by newer items before a timeout could happen.
        searchTextChangeSubscription = AndroidObservable.bindFragment(this,
                Observable.switchOnNext(searchTextEmitterSubject))
                .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe(getSearchTextChangeObserver());

        return view;
    }

    public static void launch(Activity activity, View transitionView, Album album) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, ALBUM_IMAGE_URL);
        Intent intent = new Intent(activity, AlbumDetailsActivity.class);
        intent.putExtra(ALBUM_IMAGE_URL, album.getImageUrl());
        intent.putExtra(ALBUM_ID, album.getId());
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @OnClick(R.id.clearSearchIcon)
    public void onClearSearchClick(View v) {
        mSearchEdit.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
        if(albumSearchSubscription != null &&
            albumSearchSubscription.isUnsubscribed() == false) {
            albumSearchSubscription.unsubscribe();
        }

        if(searchTextChangeSubscription != null &&
                searchTextChangeSubscription.isUnsubscribed() == false) {
            searchTextChangeSubscription.unsubscribe();
        }
    }

    private void setActionBar() {
        AppCompatActivity activity = ((AppCompatActivity)getActivity());
        if (mToolbar != null) {
            activity.setSupportActionBar(mToolbar);
        }
        ActionBar mActionBar = activity.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    // Method creates a new Observable that emits search string on to the subscriber.
    private Observable<String> getSearchTextObservable(final String searchQuery) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(searchQuery);
            }
        }).subscribeOn(Schedulers.computation());
    }

    private Observer<String> getSearchTextChangeObserver() {

        return new Observer<String>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                //Ignore as the current search query stays on screen.
            }

            @Override
            public void onNext(String query) {
                if (query.length() > 1 &&
                        (mCurrentSearchQuery != null &&
                        mCurrentSearchQuery.equals(query)) == false ) {
                    mCurrentSearchQuery = query;
                    doAlbumSearch(query, 0);
                }
            }
        };
    }

    private void doAlbumSearch(final String searchQuery, final int offset) {
        Observable<AlbumSearchResult> request = albumSearchService.
                searchAlbum(clientId, searchQuery, "album", offset).cache();

        // Add valid image url for all the albums.
        albumSearchSubscription = request.map(new Func1<AlbumSearchResult, AlbumSearchResult>() {
            @Override
            public AlbumSearchResult call(AlbumSearchResult albumSearchResult) {
                // Update the album image url.
                List<Album> albumList = albumSearchResult.getAlbums();
                for (Album album : albumList) {
                    album.setImageUrl("https://partner.api.beatsmusic.com/v1/api/albums/" + album.getId() +
                            "/images/default?client_id=" + clientId);
                }
                return albumSearchResult;
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<AlbumSearchResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(AlbumSearchResult albumSearchResult) {
                if (offset == 0) {
                    mSearchListView.setAdapter(adapter);
                    mSearchListView.addNewData(albumSearchResult.getAlbums(), albumSearchResult.getInfo());
                } else {
                    mSearchListView.addToExistingData(albumSearchResult.getAlbums(), albumSearchResult.getInfo());
                }
            }
        });
    }
}
