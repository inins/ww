package com.wang.social.socialize.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wang.social.socialize.R;
import com.wang.social.socialize.SocializeUtil;
import com.wang.social.socialize.widget.adapter.ShareItemAdapter;


public class DialogShare extends DialogFragment {
    public final static String TAG = DialogShare.class.getSimpleName().toString();

    public static void shareWeb(FragmentManager fragmentManager,
                                String url, String title, String content, String imageUrl) {
        new DialogShare()
                .setShareUrl(url)
                .setShareTitle(title)
                .setShareContent(content)
                .setShareImageUrl(imageUrl)
                .show(fragmentManager, TAG);
    }

    public final static int[] NAME_RES = {
            R.string.socialize_wx_contacts,
            R.string.socialize_wx_friends,
            R.string.socialize_qq_contacts,
            R.string.socialize_qq_zone,
            R.string.socialize_sina_weibo
    };

    public final static int[] ICON_RES = {
            R.drawable.socialize_wechat,
            R.drawable.socialize_quan,
            R.drawable.socialize_qq_friend,
            R.drawable.socialize_qzone,
            R.drawable.socialize_weibo
    };

    private ShareItemAdapter.DataProvider dataProvider = new ShareItemAdapter.DataProvider() {
        @Override
        public int getNameRes(int position) {
            return NAME_RES[position];
        }

        @Override
        public int getIconRes(int position) {
            return ICON_RES[position];
        }

        @Override
        public int getTag() {
            return 0;
        }

        @Override
        public int getItemCount() {
            return Math.min(NAME_RES.length, ICON_RES.length);
        }
    };

    private ShareItemAdapter.ClickListener clickListener = new ShareItemAdapter.ClickListener() {
        @Override
        public void onClick(int position) {
            SocializeUtil.umShareWeb(getActivity(), position,
                    getShareUrl(), getShareTitle(), getShareContent(), getShareImageUrl());

            dismiss();
        }
    };

    String shareUrl;
    String shareTitle;
    String shareContent;
    String shareImageUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public DialogShare setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;

        return this;
    }

    public DialogShare setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;

        return this;
    }

    public DialogShare setShareContent(String shareContent) {
        this.shareContent = shareContent;

        return this;
    }

    public DialogShare setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;

        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.socialize_dialog_share, null);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        initView(view);

        Dialog dialog = new Dialog(getActivity(), R.style.common_PopupDialog);
        dialog.setContentView(view);

        // 在屏幕底部
        dialog.getWindow().setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);
        // 点击外部消失
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    private void initView(View view) {
        if (null == view) return;

        view.findViewById(R.id.cancel_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ShareItemAdapter(getContext(), dataProvider, clickListener));
    }
}
