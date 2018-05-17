package com.wang.social.pictureselector.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wang.social.pictureselector.ActivityPicturePreview;
import com.wang.social.pictureselector.PictureSelector;
import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.ui.adapter.PreviewAdapter;
import com.wang.social.pictureselector.ui.widget.DotIndicator;

import static com.wang.social.pictureselector.PictureSelector.NAME_TYPE;
import static com.wang.social.pictureselector.PictureSelector.TYPE_BROWSE;

/**
 * Created by King on 2018/3/29.
 *
 * 图片预览
 */

public class FragmentPicturePreview extends Fragment {
    View rootView;
    ViewPager viewPager;
    PreviewAdapter previewAdapter;
    DotIndicator mDotIndicator;
    ImageView mDownloadIV;
    View mButtonLayout;
    String[] files;
    private int mCurrent;
    private int mType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ps_fragment_picture_preview, container, false);

        viewPager = rootView.findViewById(R.id.view_pager);
        mDotIndicator = rootView.findViewById(R.id.dot_indicator_view);
        mDownloadIV = rootView.findViewById(R.id.download_image_view);
        mButtonLayout = rootView.findViewById(R.id.button_layout);

        rootView.findViewById(R.id.cancel_text_view)
                .setOnClickListener(v -> getActivity().finish());

        rootView.findViewById(R.id.confirm_text_view)
                .setOnClickListener(v -> {});

        // 图片下载按钮点击
        mDownloadIV.setOnClickListener(v -> {});

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null != getArguments()) {
            mType = getArguments().getInt(NAME_TYPE);
        }

        if (mType == TYPE_BROWSE) {
            // 隐藏底部按钮
            mButtonLayout.setVisibility(View.GONE);
            // 单击退出
            viewPager.setOnClickListener(v -> getActivity().finish());
        } else {
            mDownloadIV.setVisibility(View.GONE);
        }

        files = getActivity().getIntent().getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);
        mCurrent = getActivity().getIntent().getIntExtra(PictureSelector.NAME_CURRENT, 0);

        if (null != files && files.length > 0) {
            previewAdapter = new PreviewAdapter(getContext(), files);
            viewPager.setAdapter(previewAdapter);

            viewPager.setCurrentItem(mCurrent);

            mDotIndicator.resetView(files.length, mCurrent);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mDotIndicator.select(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void finishWithResult(String[] list) {
        Intent intent = new Intent();
        intent.putExtra(PictureSelector.NAME_FILE_PATH_LIST, list);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
