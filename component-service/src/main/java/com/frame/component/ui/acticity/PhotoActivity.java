package com.frame.component.ui.acticity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.List;


public class PhotoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PhotoPagerAdapter adapter;

    private String[] urls;
    private int position;


    public static void start(Context context, List<String> urlList) {
        String[] urls = urlList.toArray(new String[]{});
        start(context, urls, 0);
    }

    public static void start(Context context, List<String> urlList, int position) {
        String[] urls = urlList.toArray(new String[]{});
        start(context, urls, position);
    }

    public static void start(Context context, String[] urls) {
        start(context, urls, 0);
    }

    public static void start(Context context, String[] urls, int position) {
        if (StrUtil.isEmpty(urls)) {
            ToastUtil.showToastShort("错误：数据缺失");
            return;
        }
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra("urls", urls);
        intent.putExtra("position", position);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.scale_in_scale, R.anim.scale_stay);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.scale_out_scale);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //禁止横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题  必须在setContentView()方法之前调用
        setContentView(R.layout.activity_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
        initBase();
        initView();
        initCtrl();
    }

    private void initBase() {
        if (getIntent().hasExtra("urls")) {
            urls = getIntent().getStringArrayExtra("urls");
        }
        if (getIntent().hasExtra("position")) {
            position = getIntent().getIntExtra("position", 0);
        }
        //检查position是否合法，防止越界
        if (position < 0 || position > urls.length - 1) {
            position = 0;
        }
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager_photo);
    }

    private void initCtrl() {
        adapter = new PhotoPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position, false);
    }


    class PhotoPagerAdapter extends PagerAdapter {

//        private final String[] urls = new String[]{"http://p3.music.126.net/DBX_g8rz6JKQmM3pE_S48g==/7816428162104034.jpg",
//                "http://p3.music.126.net/DBX_g8rz6JKQmM3pE_S48g==/7816428162104034.jpg",
//                "http://img2.imgtn.bdimg.com/it/u=3906012622,1432147910&fm=21&gp=0.jpg",
//                "http://www.123.com/no.jpg"
//        };

        @Override
        public int getCount() {
            return urls.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setScaleType(ImageView.ScaleType.CENTER);

            loadImg(photoView, urls[position]);

            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private void loadImg(PhotoView photoView, String url) {
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        ImageLoaderHelper.loadImg(photoView, url);
//        Glide.with(this).load(GlideUtil.getRealImgPath(url)).error(R.drawable.default_bk_img).crossFade().into(new GlideDrawableImageViewTarget(photoView) {
//            @Override
//            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
//                super.onResourceReady(drawable, anim);
//                //在这里添加一些图片加载完成的操作
//                attacher.update();
//            }
//        });
    }
}
