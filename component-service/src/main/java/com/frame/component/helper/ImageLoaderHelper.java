package com.frame.component.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.frame.component.service.R;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by liaoinstan
 * 为mImageLoader提供简单的静态方法调用
 * ImageLoaderHelper.loadCircleImg(imageView ,url);
 * 使用Test方法将随机加载一张网络图片
 * ImageLoaderHelper.loadCircleImgTest(imageView);
 */

public class ImageLoaderHelper {

    ///////////////// 加载方法 ///////////////

    //加载网络图，并设置占位图
    public static void loadCircleImg(ImageView imageView, int errorSrc, String url) {
        getImageLoader().loadImage(Utils.getContext(), ImageConfigImpl.
                builder()
                .imageView(imageView)
                .placeholder(errorSrc)
                .errorPic(errorSrc)
                .isCircle(true)
                .url(url)
                .build());
    }

    //上面方法的重载
    public static void loadCircleImg(ImageView imageView, String url) {
        loadCircleImg(imageView, R.drawable.ic_default_header, url);
    }

    public static void loadImg(ImageView imageView, int errorSrc, String url) {
        getImageLoader().loadImage(Utils.getContext(), ImageConfigImpl.
                builder()
                .imageView(imageView)
                .placeholder(errorSrc)
                .errorPic(errorSrc)
                .isCircle(false)
                .url(url)
                .build());
    }

    //上面方法的重载
    public static void loadImg(ImageView imageView, String url) {
        loadImg(imageView, R.drawable.img_placeholder_rect, url);
    }

    public static void loadCircleImg(ImageView imageView, int src) {
        RequestOptions myOptions = new RequestOptions()
                .circleCrop();
        Glide.with(Utils.getContext())
                .load(src)
                .apply(myOptions)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(imageView);
    }

    public static void loadImg(ImageView imageView, int src) {
        Glide.with(Utils.getContext())
                .load(src)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(imageView);
    }

    //加载一张图进行高斯模糊处理
    //暂时使用Glide，后面有时间封装到ImageLoader里面
    public static void loadBlurImg(ImageView imageView, String url) {
        getImageLoader().loadImage(Utils.getContext(), ImageConfigImpl.
                builder()
                .imageView(imageView)
                .placeholder(R.drawable.img_placeholder_rect)
                .errorPic(R.drawable.img_placeholder_rect)
                .isCircle(false)
                .transformation(new BlurTransformation(50))
                .url(url)
                .build());
    }

    public static ImageLoader getImageLoader() {
        return FrameUtils.obtainAppComponentFromContext(Utils.getContext()).imageLoader();
    }

    //#########################################
    //########## 以下方法仅用于测试
    //#########################################
    private static List<String> urls = new ArrayList<String>() {{
//        add("https://a.cdnsbn.com/images/products/l/20857459814.jpg");
//        add("https://a.cdnsbn.com/images/products/l/10005703602.jpg");
//        add("https://a.cdnsbn.com/images/products/l/12834780402.jpg");
//        add("https://b.cdnsbn.com/images/products/l/15403480402.jpg");
//        add("https://b.cdnsbn.com/images/products/l/05766096301.jpg");
//        add("https://c.cdnsbn.com/images/products/l/04010986801.jpg");
//        add("https://c.cdnsbn.com/images/products/l/16588798103.jpg");
//        add("https://c.cdnsbn.com/images/products/l/11440582501.jpg");
//        add("https://d.cdnsbn.com/images/products/l/07983430803.jpg");
//        add("https://d.cdnsbn.com/images/products/l/09104380001.jpg");
//        add("https://d.cdnsbn.com/images/products/l/16277591101.jpg");
        add("http://pic17.nipic.com/20110828/5252423_172406917043_2.jpg");
        add("https://tse3-mm.cn.bing.net/th?id=OIP.WtKvaL2mgGBhoUT28zqD-QHaEo&p=0&o=5&pid=1.1");
        add("https://tse4-mm.cn.bing.net/th?id=OIP.rfU4rTmf0ZhSLLgIULkw3QHaEo&p=0&o=5&pid=1.1");
        add("http://i-7.vcimg.com/trim/48b866104e7efc1ffd7367e7423296c11060910/trim.jpg");
        add("https://tse4-mm.cn.bing.net/th?id=OIP.a-_FMTyk7MSfskBTYJWt0AHaEo&p=0&o=5&pid=1.1");
        add("https://tse2-mm.cn.bing.net/th?id=OIP.JYIX3pvRuiCLCOrvgLU4ywHaEK&p=0&o=5&pid=1.1");
        add("https://tse3-mm.cn.bing.net/th?id=OIP.XzZcrXAIrxTtUH97rMlNGQHaEo&p=0&o=5&pid=1.1");
        add("http://photos.tuchong.com/23552/f/624083.jpg");
        add("http://img.taopic.com/uploads/allimg/140819/235001-140QZSP628.jpg");
        add("http://pic28.nipic.com/20130417/3123560_212434335136_2.jpg");
        add("http://img14.3lian.com/201504/30/192fffb4d0ad2efd65ffcb609a3c0f47.jpg");
        add("https://tse4-mm.cn.bing.net/th?id=OIP.0OaHeoBAOfe1PJv1Yyz59wHaEK&p=0&o=5&pid=1.1");
        add("https://tse3-mm.cn.bing.net/th?id=OIP.2T79Y7p4SF-Go6hvY1_zJgHaE8&p=0&o=5&pid=1.1");
    }};

    //随机加载一张网络图片（圆形）
    public static void loadCircleImgTest(ImageView imageView) {
        if (imageView == null) return;
        loadCircleImg(imageView, R.drawable.ic_default_header, urls.get(new Random(imageView.hashCode()).nextInt(urls.size() - 1)));
    }

    //随机加载一张网络图片
    public static void loadImgTest(ImageView imageView) {
        if (imageView == null) return;
        loadImg(imageView, R.drawable.img_placeholder_rect, urls.get(new Random(imageView.hashCode()).nextInt(urls.size() - 1)));
    }

    //随机加载一张网络图片，以position作为随机种子
    public static void loadImgTestByPosition(ImageView imageView, int position) {
        if (imageView == null) return;
        loadImg(imageView, R.drawable.img_placeholder_rect, urls.get(new Random(position).nextInt(urls.size() - 1)));
    }

    //获取一张随机的网络图片
    public static String getRandomImg() {
        return urls.get(new Random().nextInt(urls.size() - 1));
    }
}
