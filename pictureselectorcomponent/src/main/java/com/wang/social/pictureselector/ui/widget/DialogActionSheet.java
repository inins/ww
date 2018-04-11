package com.wang.social.pictureselector.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.frame.utils.SizeUtils;
import com.wang.social.pictureselector.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by king on 2017/12/31.
 */

public class DialogActionSheet extends DialogFragment {
    private String titleText;
    private String cancelText;

    private List<SheetItem> itemList = new ArrayList<>();

    private boolean showTitle;
    // 默认存在cancel按钮
    private boolean showCancel = true;

    public static DialogActionSheet newInstance() {
        return new DialogActionSheet();
    }

    public interface ClickListener {
        void onClick(DialogActionSheet dialog, View v);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.ps_dialog_action_sheet, null);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 根据配置重规划View的显示
        resetView(view);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);

        // 在屏幕底部
        dialog.getWindow().setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);
        // 点击外部不消失
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public DialogActionSheet setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            showTitle = true;
            titleText = title;
        } else {
            showTitle = false;
        }

        return this;
    }

    public DialogActionSheet setCancel(String text) {
        if (!TextUtils.isEmpty(text)) {
            showCancel = true;
            cancelText = text;
        } else {
            showCancel = false;
        }

        return this;
    }

    public DialogActionSheet addItem(String text, ClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            itemList.add(new SheetItem(text, listener));
        }

        return this;
    }

    private void resetView(View view) {
        if (null == view) return;

        TextView titleTextView = view.findViewById(R.id.title_tv);
        if (showTitle) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(titleText);
        } else {
            titleTextView.setVisibility(View.GONE);
        }

        TextView cancelTextView = view.findViewById(R.id.cancel_tv);
        if (showCancel) {
            cancelTextView.setVisibility(View.VISIBLE);
            if ((null != cancelText) && (cancelText.length() > 0)) {
                cancelTextView.setText(cancelText);
            }
            cancelTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            cancelTextView.setVisibility(View.GONE);
        }

        ScrollView scrollView = view.findViewById(R.id.scroll_view);
        if (itemList.size() > 0) {
            scrollView.setVisibility(View.VISIBLE);

            LinearLayout contentLayout = view.findViewById(R.id.content_layout);

            final float scale = getActivity().getResources().getDisplayMetrics().density;

            for (int i = 0; i < itemList.size(); i++) {
                final SheetItem item = itemList.get(i);

                if (null != item) {
                    TextView textView = new TextView(view.getContext());
                    // 读取xml文件里面设置的 取消 文字的信息来设置文字信息
                    textView.setTextSize(cancelTextView.getTextSize() / scale);
                    textView.setMinimumHeight(cancelTextView.getMinHeight());
                    textView.setTextColor(cancelTextView.getTextColors());
                    textView.setText(item.text);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.listener.onClick(DialogActionSheet.this, v);

                            dismiss();
                        }
                    });
                    textView.setGravity(Gravity.CENTER);
                    textView.setSingleLine(true);

                    contentLayout.addView(textView);
                }

                // 添加分隔线
                if (i < (itemList.size() - 1)) {
                    LinearLayout divider = new LinearLayout(view.getContext());
                    divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            SizeUtils.dp2px(1)));
                    divider.setBackgroundColor(Color.parseColor("#E5E5E5"));
                    contentLayout.addView(divider);
                }
            }
        } else {
            scrollView.setVisibility(View.GONE);
        }
    }

    class SheetItem {
        String text;
        ClickListener listener;

        public SheetItem(String text, ClickListener listener) {
            this.text = text;
            this.listener = listener;
        }
    }
}
