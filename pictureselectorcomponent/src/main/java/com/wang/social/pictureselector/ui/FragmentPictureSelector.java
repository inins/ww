package com.wang.social.pictureselector.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.frame.component.view.SocialToolbar;
import com.wang.social.pictureselector.ActivityPictureClip;
import com.wang.social.pictureselector.PictureSelector;
import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.controller.AlbumController;
import com.wang.social.pictureselector.controller.IControllerListener;
import com.wang.social.pictureselector.controller.PictureController;
import com.wang.social.pictureselector.model.Album;
import com.wang.social.pictureselector.model.Picture;
import com.wang.social.pictureselector.model.SelectorSpec;
import com.wang.social.pictureselector.ui.adapter.AlbumAdapter;
import com.wang.social.pictureselector.ui.adapter.ThumbnailAdapter;
import com.wang.social.pictureselector.ui.widget.ThumbnailDecoration;
import com.wang.social.pictureselector.ui.widget.XSpinner;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by King on 2018/3/29.
 */

public class FragmentPictureSelector extends Fragment {
    // Fragment的View
    View rootView;
    // 显示文件夹的Spinner控件和Adapter
//    AppCompatSpinner appCompatSpinner;
    XSpinner mXSpinner;
    AlbumAdapter albumAdapter;
    TextView mSpinnerTV;

    // 相册和图片数据加载
    private final AlbumController albumController = new AlbumController();
    private final PictureController pictureController = new PictureController();

    // 显示预览图的RecyclerView和Adapter
    RecyclerView recyclerView;
    ThumbnailAdapter thumbnailAdapter;

    Button confirmBtn;
    SocialToolbar toolbar;

    // 文件夹数据加载监听
    private IControllerListener albumControllerListener = new IControllerListener() {
        @Override
        public void onLoadFinished(Cursor cursor) {
            albumAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset() {
            albumAdapter.swapCursor(null);
        }
    };

    // 图片数据加载监听
    private IControllerListener pictureControllerListener = new IControllerListener() {
        @Override
        public void onLoadFinished(Cursor cursor) {
            thumbnailAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset() {
            thumbnailAdapter.swapCursor(null);
        }
    };

//    // 文件夹选择监听
//    private AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
//            Album album = Album.valueOf(cursor);
//
//            // 选中了文件夹，重新加载图片数据
//            pictureController.resetLoad(album);
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {     }
//    };

    private AlbumAdapter.SelectListener mAlbumSelectListener = new AlbumAdapter.SelectListener() {
        @Override
        public void onAlbumSelect(Album album) {
            if (null != album) {
                mSpinnerTV.setText(album.getDisplayName());
                // 选中了文件夹，重新加载图片数据
                pictureController.resetLoad(album);
            }

            mXSpinner.dismiss();
        }
    };

    // 选中图片路径列表
    List<String> selectedList = new ArrayList<>();

    // 图片点击事件监听
    ThumbnailAdapter.ThumbnailActionListener thumbnailActionListener = new ThumbnailAdapter.ThumbnailActionListener() {
        @Override
        public void onThumbnailClicked(Picture picture) {
            // 如果是单选，则直接返回或者跳转到裁切
            if (SelectorSpec.getInstance().getMaxSelection() <= 1) {
                if (SelectorSpec.getInstance().isClip()) {
                    // 跳转到裁切
                    Intent intent = new Intent(getActivity(), ActivityPictureClip.class);
                    intent.putExtra(PictureSelector.NAME_FILE_PATH, picture.getFilePath());
                    startActivityForResult(intent, PictureSelector.REQUEST_CODE_CLIP);
                } else {
                    // 直接返回
                    finishWithResult(picture.getFilePath());
                }

                return;
            }

            if (selectedList.contains(picture.getFilePath())) {
                selectedList.remove(picture.getFilePath());
            } else {
                if (selectedList.size() < SelectorSpec.getInstance().getMaxSelection()) {
                    selectedList.add(picture.getFilePath());
                } else {
                    // 超出图片数量
                    Toast.makeText(getActivity(),
                            String.format(getString(R.string.ps_max_prompt),
                                    SelectorSpec.getInstance().getMaxSelection()),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }

            // 选中列表里有数据确认按钮才能点击
            if (selectedList.size() > 0) {
                confirmBtn.setVisibility(View.VISIBLE);
                confirmBtn.setEnabled(true);
                confirmBtn.setText(getString(R.string.ps_confirm) +" (" + selectedList.size() + ")");
            } else {
                confirmBtn.setVisibility(View.GONE);
                confirmBtn.setEnabled(false);
            }

            thumbnailAdapter.notifyDataSetChanged();
        }
    };

    ThumbnailAdapter.ThumbnailSelectionChecker thumbnailSelectionChecker = new ThumbnailAdapter.ThumbnailSelectionChecker() {
        @Override
        public int selectionCheck(Picture picture) {
            return selectedList.indexOf(picture.getFilePath());
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ps_fragment_picture_selector, container, false);

//        appCompatSpinner = rootView.findViewById(R.id.spinner);
        mXSpinner = rootView.findViewById(R.id.x_spinner);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        mSpinnerTV = rootView.findViewById(R.id.spinner_text_view);

        // toolbar 左边图标退出
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    getActivity().finish();
                }
            }
        });

        confirmBtn = rootView.findViewById(R.id.confirm_btn);
        // 确定按钮
        if (SelectorSpec.getInstance().getMaxSelection() > 1) {
            confirmBtn.setVisibility(View.VISIBLE);
            confirmBtn.setEnabled(false);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishWithResult(selectedList);
                }
            });
        } else {
            confirmBtn.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SelectorSpec.getInstance().getSpanCount()));
        thumbnailAdapter = new ThumbnailAdapter(getActivity(), null, thumbnailActionListener);
        recyclerView.setAdapter(thumbnailAdapter);
        recyclerView.addItemDecoration(new ThumbnailDecoration(10, Color.TRANSPARENT, true));
        thumbnailAdapter.setThumbnailSelectionChecker(thumbnailSelectionChecker);

        albumAdapter = new AlbumAdapter(getActivity(), null);
        albumAdapter.setSelectListener(mAlbumSelectListener);
//        appCompatSpinner.setAdapter(albumAdapter);
//        appCompatSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        mXSpinner.init(albumAdapter);

        albumController.init(getActivity(), albumControllerListener);
        pictureController.init(getActivity(), pictureControllerListener);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 加载文件夹数据
        albumController.loadAlbums();
        // 加载所有图片
        pictureController.loadAllPhoto(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        albumController.onDestroy();
        pictureController.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == PictureSelector.REQUEST_CODE_CLIP) {
                // 裁切后的图片路径
                String filepath = data.getStringExtra(PictureSelector.NAME_FILE_PATH);

                finishWithResult(filepath);
            }
        }

    }

    private void finishWithResult(String filepath) {
        String[] list = { filepath };
        finishWithResult(list);
    }

    private void finishWithResult(String[] list) {
        Intent intent = new Intent();
        intent.putExtra(PictureSelector.NAME_FILE_PATH_LIST, list);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void finishWithResult(List<String> list) {
        String[] files = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            files[i] = list.get(i);
        }

        finishWithResult(files);
    }
}
