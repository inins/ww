package com.wang.social.pictureselector.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.social.pictureselector.PictureSelector;
import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.ui.adapter.PreviewAdapter;

/**
 * Created by King on 2018/3/29.
 *
 * 图片预览
 */

public class FragmentPicturePreview extends Fragment {
    View rootView;
    ViewPager viewPager;
    PreviewAdapter previewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ps_fragment_picture_preview, container, false);

        viewPager = rootView.findViewById(R.id.view_pager);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] files = getActivity().getIntent().getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);

        if (null != files && files.length > 0) {
            previewAdapter = new PreviewAdapter(getContext(), files);
            viewPager.setAdapter(previewAdapter);
        }
    }
}
