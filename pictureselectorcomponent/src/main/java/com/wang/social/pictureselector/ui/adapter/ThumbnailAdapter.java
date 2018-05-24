package com.wang.social.pictureselector.ui.adapter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.model.Picture;
import com.wang.social.pictureselector.model.SelectorSpec;
import com.frame.component.view.CircularAutoSizeTextView;
import com.wang.social.pictureselector.ui.widget.ThumbnailLayout;

/**
 * Created by King on 2018/3/28.
 */

public class ThumbnailAdapter extends BaseRecycleCursorAdapter<ThumbnailAdapter.ViewHolder> {

    public interface ThumbnailActionListener {
        void onThumbnailClicked(Picture picture);
    }

    ThumbnailActionListener thumbnailActionListener;

    public interface ThumbnailSelectionChecker {
        /**
         * 检测图片是否被选中
         * @param picture
         * @return >0 表示选中，在选中列表中的位置
         */
        int selectionCheck(Picture picture);
    }
    ThumbnailSelectionChecker thumbnailSelectionChecker;

    public void setThumbnailSelectionChecker(ThumbnailSelectionChecker thumbnailSelectionChecker) {
        this.thumbnailSelectionChecker = thumbnailSelectionChecker;
    }

    /**
     * Constructor that flags always FLAG_REGISTER_CONTENT_OBSERVER.
     *
     * @param context The context
     *                This option is discouraged, as it results in Cursor queries
     *                being performed on the application's UI thread and thus can cause poor
     *                responsiveness or even Application Not Responding errors. As an alternative,
     *                use {@link LoaderManager} with a {@link CursorLoader}.
     * @param c       The cursor from which to get the data.
     */
    public ThumbnailAdapter(Context context, Cursor c) {
        this(context, c, null);
    }

    public ThumbnailAdapter(Context context, Cursor c, ThumbnailActionListener listener) {
        super(context, c);

        this.thumbnailActionListener = listener;
    }

    @Override
    public void onBindViewHolder(ThumbnailAdapter.ViewHolder holder, Cursor cursor) {
        final Picture picture = Picture.fromCursor(cursor);

        SelectorSpec.getInstance()
                .getImageEngine()
                .loadImage(getContext(), picture.getFilePath(), null, holder.imageView);

        int selectionIndex = -1;
        if (null != thumbnailSelectionChecker) {
            selectionIndex = thumbnailSelectionChecker.selectionCheck(picture);
        }

        if (selectionIndex >= 0) {
            holder.flag.setVisibility(View.VISIBLE);
            holder.flag.setText(Integer.toString(selectionIndex + 1));
        } else {
            holder.flag.setVisibility(View.GONE);
        }

        holder.thumbnailLayout.setTag(picture);
        holder.thumbnailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != thumbnailActionListener) {
                    if (view.getTag() instanceof Picture) {
                        thumbnailActionListener.onThumbnailClicked((Picture)view.getTag());
                    }
                }
            }
        });
    }

    @Override
    public ThumbnailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ps_thumbnail_item,
                parent,
                false);

        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ThumbnailLayout thumbnailLayout;
        ImageView imageView;
        CircularAutoSizeTextView flag;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnailLayout = itemView.findViewById(R.id.thumbnail_layout);
            imageView = itemView.findViewById(R.id.image_view);
            flag = itemView.findViewById(R.id.flag);
        }
    }
}
