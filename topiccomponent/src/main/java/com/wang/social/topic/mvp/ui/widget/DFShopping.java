package com.wang.social.topic.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.entities.Topic;
import com.frame.component.helper.NetAccountBalanceHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;
import com.wang.social.topic.mvp.ui.TopicDetailActivity;

public class DFShopping extends DialogFragment {
    public static DFShopping showDialog(FragmentManager manager, Context context, IView iView, Topic topic) {
        if (null == topic) return null;

        DFShopping dialog = new DFShopping();
        dialog.setContext(context);
        dialog.setIView(iView);
        dialog.setTopic(topic);
        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, topic.getTitle());

        return dialog;
    }

    private Context mContext;
    private View mView;
    private IView mIView;
    private Topic mTopic;
    private TextView mWalletTV;
    private TextView mConfirmTV;

    public void setContext(Context context) {
        mContext = context;
    }

    public void setIView(IView IView) {
        mIView = IView;
    }

    public void setTopic(Topic topic) {
        mTopic = topic;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(
                R.layout.topic_dialog_shopping, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.TopicDialogStyle);
        dialog.setContentView(mView);

        dialog.setCancelable(true);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        mView.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.80), LinearLayout.LayoutParams.WRAP_CONTENT));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (null != mTopic) {
            TextView priceTV = mView.findViewById(R.id.price_text_view);
//            priceTV.setText(Integer.toString(mTopic.getPrice()));
        }

        mConfirmTV = mView.findViewById(R.id.ok_text_view);
        mWalletTV = mView.findViewById(R.id.your_wallet_text_view);

        mView.findViewById(R.id.cancel_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DFShopping.this.dismiss();
                    }
                });

        // 获取钻石数量
        NetAccountBalanceHelper.newInstance().accountBalance(mIView, false, new NetAccountBalanceHelper.FindMyWalletCallback() {
            @Override
            public void onWallet(int diamondNum) {
                mWalletTV.setVisibility(View.VISIBLE);
                mWalletTV.setText(String.format(getContext().getString(R.string.topic_balance_format), diamondNum));

//                if (diamondNum < mTopic.getPrice()) {
//                    mConfirmTV.setText(getContext().getResources().getString(R.string.topic_dialog_shopping_charge));
//
//                    mView.findViewById(R.id.ok_text_view)
//                            .setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    //
//                                    ToastUtil.showToastLong("去充值");
//
//                                    DFShopping.this.dismiss();
//                                }
//                            });
//                } else {
//                    mConfirmTV.setText(getContext().getResources().getString(R.string.topic_dialog_shopping_ok));
//
//                    mView.findViewById(R.id.ok_text_view)
//                            .setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    NetPayStoneHelper.newInstance()
//                                            .netPayTopic(mIView, mTopic.getTopicId(), mTopic.getPrice(),
//                                                    new NetPayStoneHelper.OnStonePayCallback() {
//                                                        @Override
//                                                        public void success() {
//                                                            TopicDetailActivity.start(mContext, mTopic.getTopicId(), mTopic.getUserId());
//                                                        }
//                                                    });
//
//                                    DFShopping.this.dismiss();
//                                }
//                            });
//                }
            }
        });

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
