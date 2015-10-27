package mani.beatsalbumsearch.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mani.beatsalbumsearch.AlbumSearchApplication;
import mani.beatsalbumsearch.R;
import mani.beatsalbumsearch.model.Album;
import mani.beatsalbumsearch.modules.ForApplication;
import mani.beatsalbumsearch.utils.FontProvider;

/**
 * Adapter holds the album search result data and to generate search result list item views.
 */
public class AlbumsSearchListAdapter extends EndlessAdapter<Album> {

    private List<Album> albumList = Collections.emptyList();

    @Inject
    Picasso picasso;

    @Inject
    LayoutInflater mInflater;

    @Inject @ForApplication
    Context mContext;

    @Inject
    FontProvider mFontProvider;


    public AlbumsSearchListAdapter(Application application) {
        ((AlbumSearchApplication)application).inject(this);
    }

    @Override
    public void addNewData(List<Album> data) {
        albumList = data;
        notifyDataSetChanged();
    }

    @Override
    public void addToExistingData(List<Album> data) {
        if (albumList.size() == 0) {
            albumList = data;
        } else {
            albumList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void replaceWith(List<Album> albums) {
        albumList = albums;
        notifyDataSetChanged();
    }

    public void appendWith(List<Album> albums) {
        if (albumList.size() == 0) {
            albumList = albums;
        } else {
            albumList.addAll(albums);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.album_search_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Album album = albumList.get(position);
        holder.albumTitle.setText(album.getAlbumDisplay());
        holder.albumArtist.setText(album.getAlbumArtist());
        holder.albumThumbnail.setTag(Integer.valueOf(position));
        Picasso.with(mContext)
                .load(album.getImageUrl())
                .into(holder.albumThumbnail);
        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.albumTitle)
        TextView albumTitle;

        @InjectView(R.id.albumArtist)
        TextView albumArtist;

        @InjectView(R.id.albumThumbnail)
        ImageView albumThumbnail;


        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
            albumTitle.setTypeface(mFontProvider.getBold());
            albumArtist.setTypeface(mFontProvider.getMediumFont());
        }
    }

}
