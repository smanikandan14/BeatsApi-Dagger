package mani.beatsalbumsearch.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mani.beatsalbumsearch.AlbumSearchApplication;
import mani.beatsalbumsearch.R;
import mani.beatsalbumsearch.model.Track;
import mani.beatsalbumsearch.utils.DateTimeUtil;
import mani.beatsalbumsearch.utils.FontProvider;

/**
 * Adapter holds the album's tracks data and to generate search track list item views.
 *
 */
public class AlbumTrackListAdapter extends EndlessAdapter<Track> {

    private List<Track> albumTracksList = Collections.emptyList();

    @Inject
    LayoutInflater mInflater;

    @Inject
    FontProvider mFontProvider;

    public AlbumTrackListAdapter(Application application) {
        ((AlbumSearchApplication)application).inject(this);
    }

    public void addNewData(List<Track> data) {

    }

    public void addToExistingData(List<Track> data) {

    }

    public void replaceWith(List<Track> tracks) {
        albumTracksList = tracks;
        notifyDataSetChanged();
    }

    public void appendWith(List<Track> tracks) {
        if (albumTracksList.size() == 0) {
            albumTracksList = tracks;
        } else {
            albumTracksList.addAll(tracks);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return albumTracksList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumTracksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.album_track_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Track track = albumTracksList.get(position);
        holder.trackTitle.setText(track.getTitle());
        if (holder.trackDuration != null)
            holder.trackDuration.setText(
                    DateTimeUtil.convertSecondsToDisplayTime(track.getDuration()));
        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.trackTitle)
        TextView trackTitle;

        @InjectView(R.id.trackDuration)
        TextView trackDuration;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
            trackTitle.setTypeface(mFontProvider.getBold());
            trackDuration.setTypeface(mFontProvider.getMediumFont());
        }
    }


}
