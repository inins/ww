package com.wang.social.pictureselector.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.model.Album;

/**
 * Created by King on 2018/3/28.
 */

public class AlbumAdapter extends CursorAdapter {
    private final LayoutInflater layoutInflater;

    public interface SelectListener {
        void onAlbumSelect(Album album);
    }

    SelectListener mSelectListener;

    public AlbumAdapter(Context context, Cursor c) {
        super(context, c, false);

        layoutInflater = LayoutInflater.from(context);
    }

    public void setSelectListener(SelectListener listener) {
        this.mSelectListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.ps_album_list_item, parent, false);
        AlbumViewHolder albumViewHolder = new AlbumViewHolder(view);
        view.setTag(albumViewHolder);
        return view;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (!getCursor().moveToPosition(position)) {
//            throw new IllegalStateException("couldn't move cursor to position " + position);
//        }
//
//        if (convertView == null) {
//            convertView =
//                    layoutInflater.inflate(R.layout.ps_item_album_selected,
//                            parent, false);
//        }
//        TextView albumName = convertView.findViewById(android.R.id.text1);
//        Album album = Album.valueOf(getCursor());
//        albumName.setText(album.getDisplayName());
//        return convertView;
//    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AlbumViewHolder viewHolder = (AlbumViewHolder) view.getTag();
        Album album = Album.valueOf(cursor);
        viewHolder.albumTitle.setText(album.getDisplayName());
        viewHolder.photoCount.setText(Long.toString(album.getCount()));

        viewHolder.rootView.setTag(R.id.ps_tag_entity, album);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag(R.id.ps_tag_entity) instanceof Album && null != mSelectListener) {
                    mSelectListener.onAlbumSelect((Album) v.getTag(R.id.ps_tag_entity));
                }
            }
        });
    }


    static class AlbumViewHolder {
        View rootView;
        TextView albumTitle;
        TextView photoCount;

        public AlbumViewHolder(View itemView) {
            rootView = itemView.findViewById(R.id.root_view);
            albumTitle = itemView.findViewById(R.id.album_name);
            photoCount = itemView.findViewById(R.id.photo_count);
        }
    }
}
