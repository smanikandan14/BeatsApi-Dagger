package mani.beatsalbumsearch.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import mani.beatsalbumsearch.AlbumSearchApplication;
import mani.beatsalbumsearch.R;
import mani.beatsalbumsearch.adapter.AlbumTrackListAdapter;
import mani.beatsalbumsearch.interfaces.IBackPressedListener;
import mani.beatsalbumsearch.model.Album;
import mani.beatsalbumsearch.model.AlbumLookUpResult;
import mani.beatsalbumsearch.model.Artist;
import mani.beatsalbumsearch.model.ClientId;
import mani.beatsalbumsearch.model.Track;
import mani.beatsalbumsearch.model.TrackLookUpResult;
import mani.beatsalbumsearch.rest.AlbumSearchService;
import mani.beatsalbumsearch.ui.view.ObservableScrollView;
import mani.beatsalbumsearch.utils.DateTimeUtil;
import mani.beatsalbumsearch.utils.FontProvider;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Album Detail fragment which shows the details of the album
 * including album Image, and album by and
 * lists all the tracks in the album with its duration.
 *
 */
public class AlbumDetailsFragment extends Fragment implements
        ObservableScrollView.Callbacks,
        IBackPressedListener {

    @Optional @InjectView(R.id.scroll_view)
    ObservableScrollView mObservableScrollView;

    @InjectView(R.id.albumImage)
    ImageView mAlbumImage;

    @Optional @InjectView(R.id.placeholder)
    View mPlaceholderView;

    @InjectView(R.id.sticky)
    View mStickyView;

    @InjectView(R.id.stickyAlbumTitle)
    TextView mAlbumTitle;

    @InjectView(R.id.stickyAlbumDetail)
    TextView mAlbumDetail;

    @InjectView(R.id.stickyAlbumBy)
    TextView mAlbumBy;

    @InjectView(R.id.stickyAlbumArtist)
    TextView mAlbumArtist;

    @InjectView(R.id.albumTrackList)
    ListView mTrackListView;

    @InjectView(R.id.two_state_layout)
    View mProgressStateLayout;

    @InjectView(R.id.errorText)
    TextView mErrorText;

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Inject
    AlbumTrackListAdapter adapter;

    @Inject
    AlbumSearchService albumSearchService;

    @Inject @ClientId
    String clientId;

    @Inject
    FontProvider fontProvider;

    @Inject
    Resources resources;

    Subscription albumDetailSubscription;

    private Album albumDetail;

    public AlbumDetailsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        AlbumSearchApplication app = AlbumSearchApplication.get(this.getActivity());
        app.inject(this);

        if(savedInstanceState != null) {
            albumDetail = savedInstanceState.getParcelable("album");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setActionBar();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("album", albumDetail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_details, container, false);
        ButterKnife.inject(this, view);

        mAlbumTitle.setTypeface(fontProvider.getNueziBold());
        mAlbumDetail.setTypeface(fontProvider.getRegularFont());
        mAlbumBy.setTypeface(fontProvider.getRegularFont());
        mAlbumArtist.setTypeface(fontProvider.getBold());
        mErrorText.setTypeface(fontProvider.getRegularFont());

        mErrorText.setText(resources.getString(R.string.album_lookup_error_msg));

        // Set album image's width & height based on device screen sizes to maintain
        // equal coverage of space on all devices.
        setAlbumImageWidthAndHeight();

        // Register whether scroll view call back required.
        // We don't want the sticky effect on landscape mode.
        setObservableScrollViewCallBacks();

        ViewCompat.setTransitionName(mAlbumImage, AlbumSearchFragment.ALBUM_IMAGE_URL);
        String albumId = getActivity().getIntent().getStringExtra(AlbumSearchFragment.ALBUM_ID);

        // Load the album image. User size attribute to fetch high resolution image as the
        // album image size on the screen is bigger.
        Picasso.with(this.getActivity()).load(
                getActivity().getIntent().getStringExtra(AlbumSearchFragment.ALBUM_IMAGE_URL)
                        + "&size=large")
                .error(R.drawable.album_cover_default)
                .fit()
                .into(mAlbumImage);

        mObservableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onScrollChanged(mObservableScrollView.getScrollY());
                    }
                });

        mTrackListView.setAdapter(adapter);

        // Fetch the album details if album detail object is not persisted.
        if (albumDetail == null) {
            getAlbumDetails(albumId);
        } else {
            // This seems to be recreation of fragment and we have album details fetched already.
            // Show the content Hide the progress bar layout.
            mProgressStateLayout.setVisibility(View.GONE);
            List<Track> trackList = albumDetail.getRefs().getTracks();
            setStickyHeaderContent(albumDetail);
            adapter.replaceWith(trackList);
            // Since listview is used inside a scrollable component we need to set the height of
            // listview to avoid bad user experience of scrolling inside a scrolling problems.
            setHeightOfTrackListView();
            setHeightOfStickyHeader();
            setScrollViewToTop();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
        if (albumDetailSubscription != null &&
                albumDetailSubscription.isUnsubscribed() == false) {
            albumDetailSubscription.unsubscribe();
        }
    }

    private void setObservableScrollViewCallBacks() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        if (displaymetrics.heightPixels > displaymetrics.widthPixels) {
            mObservableScrollView.setCallbacks(this);
        }
    }

    private void setAlbumImageWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        int imageWidth;
        int imageHeight;

        if (width > height) {
            //Landscape: Take 50% of screen width
            imageWidth = (width * 50 )/100;
            imageHeight = height;
        } else {
            //Portrait: Take 90% of screen width
            imageWidth = width;
            imageHeight = (height * 40)/100;
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mAlbumImage.getLayoutParams();
        params.width = imageWidth;
        params.height = imageHeight;
    }

    private void setStickyHeaderContent(Album album) {

        mAlbumTitle.setText(album.getAlbumTitle());
        StringBuilder builder = new StringBuilder();
        String year = DateTimeUtil.convertDateInStringToYear(album.getRelease_date());
        if (year != null) {
            builder.append(year);
            builder.append(" - ");
        }
        builder.append(album.getTotalTracks()+" "+resources.getString(R.string.songs));
        builder.append(" - ");
        builder.append(DateTimeUtil.convertSecondsToDisplayTime(album.getDuration()));
        mAlbumDetail.setText(builder.toString());

        List<Artist> artistList = album.getRefs().getArtists();
        if(artistList != null) {
            builder.setLength(0);
            for (Artist artist: artistList) {
                builder.append(artist.getDisplay());
                builder.append(" ");
            }
            mAlbumArtist.setText(builder.toString());
        }
    }

    private void setHeightOfStickyHeader() {
        mStickyView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        int height = mStickyView.getMeasuredHeight();

        ViewGroup.LayoutParams params = mStickyView.getLayoutParams();
        params.height = height;
        mStickyView.setLayoutParams(params);
        if (mPlaceholderView != null) {
            params = mPlaceholderView.getLayoutParams();
            params.height = height;
            mPlaceholderView.setLayoutParams(params);
        }
    }

    public void setHeightOfTrackListView() {

        ListAdapter mAdapter = mTrackListView.getAdapter();
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }

        int totalHeight = 0;

        View mView = mAdapter.getView(0, null, mTrackListView);

        mView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        mView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        int height = mView.getMeasuredHeight();

        totalHeight = mAdapter.getCount() * height;
        ViewGroup.LayoutParams params = mTrackListView.getLayoutParams();
        params.height = totalHeight
                + (mTrackListView.getDividerHeight() * (mAdapter.getCount() - 1));
        mTrackListView.setLayoutParams(params);
    }

    private void setScrollViewToTop() {
        mStickyView.post(new Runnable() {
            @Override
            public void run() {
                mObservableScrollView.scrollTo(0, 0);
            }
        });
    }

    private void setActionBar() {
        AppCompatActivity activity = ((AppCompatActivity)getActivity());
        ActionBar mActionBar = activity.getSupportActionBar();
        mActionBar.setTitle("Album");
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exitAnimation();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void onBackPressed() {
        exitAnimation();
    }

    @Override
    public void onScrollChanged(int scrollY) {
        if (mStickyView != null && mPlaceholderView != null) {
            mStickyView.setTranslationY(Math.max(mPlaceholderView.getTop(), scrollY));
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent() {
    }

    private void getAlbumDetails(final String albumId) {

        Observable<AlbumLookUpResult> request = albumSearchService.
                getAlbumDetail(clientId, albumId).cache();

        albumDetailSubscription = request
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<AlbumLookUpResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                //Hide the progress and show the errorText.
                mProgressBar.setVisibility(View.GONE);
                mErrorText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(AlbumLookUpResult result) {
                // Once we have the album look up data, we need to fetch the
                // tracks information. So a zip observable is used to get
                // each track information with a delay of 200 ms and collect
                // the track information into a list.

                albumDetail = result.getData();
                // A throttle delay is set to avoid Maximum QPS error observed from server.
                Observable<Long> limiter = Observable.interval(100, TimeUnit.MILLISECONDS);

                Observable.zip(limiter, Observable.from(result.getData().getRefs().getTracks()),
                    new Func2<Long, Track, Track>() {
                        @Override
                        public Track call(Long aLong, Track integer) {
                            return integer;
                        }
                })
                .flatMap(new Func1<Track, Observable<TrackLookUpResult>>() {
                    @Override
                    public Observable<TrackLookUpResult> call(Track track) {
                        return albumSearchService.getTrackDetail(clientId, track.getId())
                                .onErrorReturn(new Func1<Throwable, TrackLookUpResult>() {
                                    @Override
                                    public TrackLookUpResult call(Throwable throwable) {
                                        return null;
                                    }
                       });
                    }
                }).map(new Func1<TrackLookUpResult, Track>() {
                    // Map TrackLookUpResult to Track object
                    @Override
                    public Track call(TrackLookUpResult trackLookUpResult) {
                        return trackLookUpResult.getData();
                    }
                })
                .toList() // Convert track objects into a list.
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Track>>() {
                    @Override
                    public void onCompleted() {
                        //show content & hide the progress.
                        mProgressStateLayout.setVisibility(View.GONE);
                        entryAnimation();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //Hide the progress and show the errorText.
                        mProgressBar.setVisibility(View.GONE);
                        mErrorText.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<Track> tracks) {
                        List<Track> trackList = albumDetail.getRefs().getTracks();
                        trackList.clear();
                        for (Track track : tracks) {
                            if (track != null) {
                                trackList.add(track);
                            }
                        }
                        setStickyHeaderContent(albumDetail);
                        adapter.replaceWith(trackList);
                        setHeightOfTrackListView();
                        setHeightOfStickyHeader();
                        setScrollViewToTop();
                    }
                });
            } // AlbumLookup OnNext
        });
    }

    private void entryAnimation() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mAlbumImage, View.ALPHA, 0.2f, 1f);
        alphaAnimation.setDuration(400);

        ObjectAnimator alphaAnimation1 = ObjectAnimator.ofFloat(mStickyView, View.ALPHA, 0.2f, 1f);
        alphaAnimation1.setDuration(600);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(alphaAnimation).with(alphaAnimation1);
        animSet.start();
    }

    private void exitAnimation() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mAlbumImage,View.ALPHA, 1f, 0f);
        alphaAnimation.setDuration(400);

        ObjectAnimator alphaAnimation1 = ObjectAnimator.ofFloat(mStickyView,View.ALPHA, 1f, 0f);
        alphaAnimation1.setDuration(400);

        ObjectAnimator alphaAnimation2 = ObjectAnimator.ofFloat(mTrackListView,View.ALPHA, 1f, 0f);
        alphaAnimation1.setDuration(400);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(alphaAnimation, alphaAnimation1, alphaAnimation2);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(getActivity() != null)
                    getActivity().finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
