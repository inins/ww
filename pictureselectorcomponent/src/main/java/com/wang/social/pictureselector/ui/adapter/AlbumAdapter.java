package com.wang.social.pictureselector.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.model.Album;

/**
 * Created by King on 2018/3/28.
 */

public class AlbumAdapter extends CursorAdapter {
    private final LayoutInflater layoutInflater;

    public AlbumAdapter(Context context, Cursor c) {
        super(context, c, false);

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.ps_album_list_item, parent, false);
        AlbumViewHolder albumViewHolder = new AlbumViewHolder(view);
        view.setTag(albumViewHolder);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!getCursor().moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        if (convertView == null) {
            convertView =
                    layoutInflater.inflate(R.layout.ps_item_album_selected,
                            parent, false);
        }
        TextView albumName = convertView.findViewById(android.R.id.text1);
        Album album = Album.valueOf(getCursor());
        albumName.setText(album.getDisplayName());
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AlbumViewHolder viewHolder = (AlbumViewHolder) view.getTag();
        Album album = Album.valueOf(cursor);
        viewHolder.albumTitle.setText(album.getDisplayName());
        viewHolder.photoCount.setText(Long.toString(album.getCount()));
    }


    static class AlbumViewHolder {
        TextView albumTitle;
        TextView photoCount;

        public AlbumViewHolder(View itemView) {
            albumTitle = itemView.findViewById(R.id.album_name);
            photoCount = itemView.findViewById(R.id.photo_count);
        }
    }
}
