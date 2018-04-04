package com.wang.social.pictureselector.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.social.pictureselector.PictureSelector;
import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.model.SelectorSpec;
import com.wang.social.pictureselector.ui.widget.ClipImageLayout;

/**
 * Created by King on 2018/3/29.
 */

public class FragmentPictureClip extends Fragment {
    View rootView;
    ClipImageLayout clipImageLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ps_fragment_picture_clip, container, false);

        clipImageLayout = rootView.findViewById(R.id.clip_image_layout);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String filepath = getActivity().getIntent().getStringExtra(PictureSelector.NAME_FILE_PATH);
        filepath = filepath == null ? "" : filepath;

        SelectorSpec.getInstance()
                .getImageEngine()
                .loadImage(getContext(), filepath, null, clipImageLayout.getZoomImageView());
    }

    public void clip() {
        String filepath = clipImageLayout.clip();

        Intent intent = new Intent();
        intent.putExtra(PictureSelector.NAME_FILE_PATH, filepath);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
