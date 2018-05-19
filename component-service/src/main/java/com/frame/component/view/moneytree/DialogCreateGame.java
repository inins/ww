package com.frame.component.view.moneytree;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.NewMoneyTreeGame;
import com.frame.component.entities.dto.NewMoneyTreeGameDTO;
import com.frame.component.service.R;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.component.view.DialogPay;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

import io.reactivex.Observable;

public class DialogCreateGame extends DialogFragment {

    public interface CreateGameCallback {
        /**
         * 创建游戏成功
         * @param newMoneyTreeGame 返回参数
         * @return 是否需要弹出支付对话框
         */
        boolean onCreateSuccess(NewMoneyTreeGame newMoneyTreeGame);

        /**
         * 创建游戏支付成功
         */
        void onPayCreateGameSuccess();
    }

    /**
     * 从趣聊群创建游戏
     * @param iView IView
     * @param manager FragmentManager
     * @param groupId 趣聊id
     * @param callback 回调
     * @return 创建游戏对话框
     */
    public static DialogCreateGame createFromGroup(IView iView, FragmentManager manager, int groupId, CreateGameCallback callback) {
        return showDialog(iView, manager, groupId, callback);
    }

    /**
     * 从广场游戏和活动创建游戏
     * @param iView IView
     * @param manager FragmentManager
     * @param callback 回调
     * @return 创建游戏对话框
     */
    public static DialogCreateGame createFromSquare(IView iView, FragmentManager manager, CreateGameCallback callback) {
        return showDialog(iView, manager, -1, callback);
    }

    private static DialogCreateGame showDialog(IView iView, FragmentManager manager, int groupId, CreateGameCallback callback) {
        DialogCreateGame dialog = new DialogCreateGame();
        dialog.setIView(iView);
        dialog.setFragmentManager(manager);
        dialog.setCreateGameCallback(callback);
        dialog.setGroupId(groupId);

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "");

        return dialog;
    }

    // 时间
    private EditText mTimeET;
    // 人数
    private EditText mNumberET;
    private TextView mNumberTV;
    // 钻数
    private EditText mPriceET;
    // 不限人数
    private CheckBox mUnlimitedCB;
    private IView mIView;
    private CreateGameCallback mCreateGameCallback;

    private final static int MAX_TIME = 300;
    private final static int MIN_NUM = 2;

    private ApiHelper mApiHelper;
    private IRepositoryManager mRepositoryManager;
    private FragmentManager mFragmentManager;
    private int mGroupId = -1;

    public DialogCreateGame() {
        mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext())
                .apiHelper();
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext())
                .repoitoryManager();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void setGroupId(int groupId) {
        mGroupId = groupId;
    }

    public void setIView(IView IView) {
        mIView = IView;
    }

    public void setCreateGameCallback(CreateGameCallback createGameCallback) {
        mCreateGameCallback = createGameCallback;
    }

    private void setTimeText(int time) {
        mTimeET.setText(Integer.toString(time));
        mTimeET.setSelection(mTimeET.getText().length());
    }

    private void setNumberText(int num) {
        mNumberET.setText(Integer.toString(num));
        mNumberET.setSelection(mNumberET.getText().length());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.mt_dialog_create_game, null, false);

        mTimeET = view.findViewById(R.id.time_edit_text);
        mNumberET = view.findViewById(R.id.number_of_people_edit_text);
        mNumberTV = view.findViewById(R.id.number_of_people_text_view);
        mPriceET = view.findViewById(R.id.price_edit_text);
        mUnlimitedCB = view.findViewById(R.id.unlimited_check_box);

        // 默认值
        setTimeText(10);
        setNumberText(2);

        // 不限人数
        mUnlimitedCB.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                mNumberET.setVisibility(View.GONE);
                mNumberTV.setVisibility(View.VISIBLE);
            } else {
                mNumberET.setVisibility(View.VISIBLE);
                mNumberTV.setVisibility(View.GONE);
            }
        });

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 关闭
        view.findViewById(R.id.shutdown_image_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogCreateGame.this.dismiss();
                    }
                });
        // 创建游戏
        view.findViewById(R.id.create_game_text_view)
                .setOnClickListener((View v) -> {
                    if (checkInput()) {
                        createGame();
                    }
                });

        // 选择时间
        view.findViewById(R.id.time_select_image_view)
                .setOnClickListener(
                        v -> DialogTimePicker.show(getChildFragmentManager())
                                .setListener(position -> setTime(position)));

        return dialog;
    }

    private void setTime(int position) {
        switch (position) {
            case 0:
                mTimeET.requestFocus();
                KeyboardUtils.showSoftInput(mTimeET);
                break;
            case 1:
//                mTimeET.setText("1");
                setTimeText(1);
                break;
            case 2:
//                mTimeET.setText("2");
                setTimeText(2);
                break;
            case 3:
//                mTimeET.setText("5");
                setTimeText(5);
                break;
            case 4:
//                mTimeET.setText("10");
                setTimeText(10);
                break;
        }

        mTimeET.setSelection(mTimeET.getText().length());
    }

    private int mResetTime;
    private int mPeopleNum;
    private int mPrice;

    private boolean checkInput() {

        try {
            mResetTime = Integer.parseInt(mTimeET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToastLong("请输入游戏时间");
            return false;
        }

        if (mResetTime > MAX_TIME) {
            ToastUtil.showToastLong(String.format("游戏时间不超过%1d分钟", MAX_TIME));
            setTimeText(MAX_TIME);
            return false;
        }

        if (!mUnlimitedCB.isChecked()) {
            try {
                mPeopleNum = Integer.parseInt(mNumberET.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastLong("请输入游戏人数");
                return false;
            }

            if (mPeopleNum < MIN_NUM) {
                ToastUtil.showToastLong(String.format("游戏人数最低%1d人", MIN_NUM));
                setNumberText(MIN_NUM);
                return false;
            }
        }

        try {
            mPrice = Integer.parseInt(mPriceET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToastLong("请输入支付钻石");
            return false;
        }

        return true;
    }

    /**
     * 创建游戏
     */
    private void createGame() {
        // 分钟转为秒
        doCreateGame(
                mGroupId,
                mGroupId > 1 ? 1 : 2,
                mUnlimitedCB.isChecked() ? 2 : 1,
                mResetTime * 60,
                mPrice,
                mUnlimitedCB.isChecked() ? 0 : mPeopleNum);
    }

    /**
     * 创建游戏
     *
     * @param groupId   群ID (在群中创建时必传)
     * @param type      创建类型（1：通过群，2：用户）
     * @param gameType  游戏类型（1：人数，2：时间）
     * @param resetTime 重置时长(s)
     * @param diamond   钻石数
     * @param peopleNum 开始人数 (gameType=1时必传)
     */
    public void doCreateGame(int groupId, int type, int gameType,
                           int resetTime, int diamond, int peopleNum) {
        mApiHelper.execute(mIView,
                netCreateGame(groupId, type, gameType, resetTime, diamond, peopleNum),
                new ErrorHandleSubscriber<NewMoneyTreeGame>() {
                    @Override
                    public void onNext(NewMoneyTreeGame newMoneyTreeGame) {
                        ToastUtil.showToastLong("创建游戏成功");

                        // 创建游戏成功
                        if (null != mCreateGameCallback) {
                            if (mCreateGameCallback.onCreateSuccess(newMoneyTreeGame)) {
                                // 弹出支付对话框
                                showPayDialog(newMoneyTreeGame);
                            }
                        }

                        DialogCreateGame.this.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                },
                disposable -> mIView.showLoading(),
                () -> mIView.hideLoading());
    }

    /**
     * 创建游戏网络请求
     * @param groupId 群ID (在群中创建时必传)
     * @param type 创建类型（1：通过群，2：用户）
     * @param gameType 游戏类型（1：人数，2：时间）
     * @param resetTime 重置时长(s)
     * @param diamond 钻石数
     * @param peopleNum 开始人数 (gameType=1时必传)
     *
     */
    public Observable<BaseJson<NewMoneyTreeGameDTO>> netCreateGame(
            int groupId, int type, int gameType, int resetTime, int diamond, int peopleNum) {
        NetParam netParam = new NetParam();
        if (type == 1) {
            netParam.put("groupId", groupId);
        }
        netParam.put("type", type);
        netParam.put("gameType", gameType);
        netParam.put("resetTime", resetTime);
        netParam.put("diamond", diamond);
        if (gameType == 1) {
            netParam.put("peopleNum", peopleNum);
        }
        netParam.put("v", "2.0.0");

        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .createGame(netParam.build());
    }

    /**
     * 弹出支付对话框
     * @param newMoneyTreeGame 游戏信息
     */
    public void showPayDialog(NewMoneyTreeGame newMoneyTreeGame) {
        String[] strings = {
                "需要支付",
                Integer.toString(newMoneyTreeGame.getDiamond()),
                "钻进行游戏"};
        int[] colors = {
                ContextCompat.getColor(getContext(), R.color.common_text_blank),
                ContextCompat.getColor(getContext(), R.color.common_blue_deep),
                ContextCompat.getColor(getContext(), R.color.common_text_blank)
        };
        SpannableStringBuilder titleText = SpannableStringUtil.createV2(strings, colors);
        DialogPay.showPay(mIView,
                mFragmentManager,
                titleText,
                "您当前余额为%1d钻",
                getString(R.string.common_cancel),
                "立即支付",
                "立即充值",
                newMoneyTreeGame.getDiamond(),
                newMoneyTreeGame.getBalance(),
                () -> payCreateGame(newMoneyTreeGame));
    }

    /**
     * 游戏支付
     * @param newMoneyTreeGame 游戏信息
     */
    public void payCreateGame(NewMoneyTreeGame newMoneyTreeGame) {
        PayHelper.newInstance()
                .payCreateGame(mIView, newMoneyTreeGame.getApplyId(), newMoneyTreeGame.getDiamond(),
                new ErrorHandleSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtil.showToastLong("支付成功");

                        if (null != mCreateGameCallback) {
                            mCreateGameCallback.onPayCreateGameSuccess();
                        }
                    }
                },
                disposable -> mIView.showLoading(),
                        () -> mIView.hideLoading());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mApiHelper = null;
    }
}
