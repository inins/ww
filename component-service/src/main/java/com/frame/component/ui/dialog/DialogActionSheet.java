package com.frame.component.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.utils.SizeUtils;

public class DialogActionSheet extends DialogFragment implements View.OnClickListener {

    public interface ActionSheetListener {
        void onItemSelected(int position, String text);
    }

    public static DialogActionSheet show(FragmentManager manager, String[] list) {
        if (null == list || list.length <= 0) {
            new Throwable("list 不能为空");
        }

        DialogActionSheet dialog = new DialogActionSheet();

        dialog.setList(list);

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "");

        return dialog;
    }

    private String[] mList;
    private LinearLayout mContentLayout;
    private TextView mCancelTV;
    private ActionSheetListener mActionSheetListener;

    public void setActionSheetListener(ActionSheetListener actionSheetListener) {
        mActionSheetListener = actionSheetListener;
    }

    public void setList(String[] list) {
        mList = list;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.im_personal_card_dialog_action_sheet, null, false);

        mContentLayout = view.findViewById(R.id.content_layout);
        mCancelTV = view.findViewById(R.id.cancel_text_view);

        if (null != mList) {
            final float scale = getActivity().getResources().getDisplayMetrics().density;
            for (int i = 0; i < mList.length; i++) {
                if (i > 0) {
                    LinearLayout divider = new LinearLayout(view.getContext());
                    divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(1)));
                    divider.setBackgroundColor(getResources().getColor(R.color.common_list_divider));
                    mContentLayout.addView(divider);
                }

                String str = mList[i];
                TextView textView = new TextView(getContext());

                int height = mCancelTV.getMinHeight();
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

                textView.setTextSize(mCancelTV.getTextSize() / scale);
                textView.setTextColor(mCancelTV.getTextColors());
                textView.setText(str);
                textView.setGravity(Gravity.CENTER);
                textView.setSingleLine(true);

                textView.setTag(i);

                textView.setOnClickListener(this);

                mContentLayout.addView(textView);
            }
        }

        mCancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogActionSheet.this.dismiss();
            }
        });

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        window.setAttributes(lp);

        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.MATCH_PARENT));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

    @Override
    public void onClick(View v) {
        if (null != mActionSheetListener && v instanceof TextView) {
            mActionSheetListener.onItemSelected((int) v.getTag(), ((TextView) v).getText().toString());
        }
        dismiss();
    }
}
