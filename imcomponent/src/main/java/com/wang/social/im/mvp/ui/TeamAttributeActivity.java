package com.wang.social.im.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.common.InputPositiveIntegerFilter;
import com.wang.social.im.mvp.model.entities.TeamAttribute;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 觅聊属性设置
 */
public class TeamAttributeActivity extends BasicAppActivity {

    public static final String EXTRA_ATTR = "attr";

    @BindView(R2.id.ta_toolbar)
    SocialToolbar taToolbar;
    @BindView(R2.id.ta_iv_join_free)
    ImageView taIvJoinFree;
    @BindView(R2.id.ta_tv_join_free)
    TextView taTvJoinFree;
    @BindView(R2.id.ta_tv_join_free_tip)
    TextView taTvJoinFreeTip;
    @BindView(R2.id.ta_iv_join_charge)
    ImageView taIvJoinCharge;
    @BindView(R2.id.ta_tv_join_charge)
    TextView taTvJoinCharge;
    @BindView(R2.id.ta_tv_join_charge_tip)
    TextView taTvJoinChargeTip;
    @BindView(R2.id.ta_rg_gem)
    RadioGroup taRgGem;
    @BindView(R2.id.ta_tv_custom_gem)
    TextView taTvCustomGem;
    @BindView(R2.id.ta_et_gem)
    EditText taEtGem;
    @BindView(R2.id.ta_tv_gem)
    TextView taTvGem;

    private TeamAttribute mAttribute;

    public static void start(Activity activity, int requestCode, TeamAttribute attribute) {
        Intent intent = new Intent(activity, TeamAttributeActivity.class);
        intent.putExtra(EXTRA_ATTR, attribute);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_team_attribute;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mAttribute = getIntent().getParcelableExtra(EXTRA_ATTR);
        if (mAttribute == null) {
            mAttribute = new TeamAttribute();
        }
        toggleJoin();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        taEtGem.setFilters(new InputFilter[]{new InputPositiveIntegerFilter()});

        taRgGem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ta_rb_gem_100:
                        mAttribute.setGem(100);
                        break;
                    case R.id.ta_rb_gem_300:
                        mAttribute.setGem(300);
                        break;
                    case R.id.ta_rb_gem_500:
                        mAttribute.setGem(500);
                        break;
                }
            }
        });

        taEtGem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    taRgGem.clearCheck();
                }
                return false;
            }
        });

        taToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType){
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:
                        if (mAttribute.isCharge()) {
                            if (mAttribute.getGem() > IMConstants.SOCIAL_CHARGE_LIMIT_MAX || mAttribute.getGem() < IMConstants.SOCIAL_CHARGE_LIMIT_MIN) {
                                ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_social_gem_limit_tip));
                                return;
                            }
                        }
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_ATTR, mAttribute);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });
    }

    @OnClick({R2.id.ta_cl_free, R2.id.ta_cl_charge, R2.id.sa_tv_notice})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ta_cl_free || view.getId() == R.id.ta_cl_charge) { //免费/付费加入
            mAttribute.setCharge(!mAttribute.isCharge());
            toggleJoin();
        } else if (view.getId() == R.id.sa_tv_notice) { //付费须知

        }
    }

    private void toggleJoin() {
        int unselectedColor = ContextCompat.getColor(this, R.color.common_text_blank);
        int unselectedColorGray = ContextCompat.getColor(this, R.color.common_text_dark);
        int selectColor = ContextCompat.getColor(this, R.color.common_colorAccent);
        int selectColorGray = ContextCompat.getColor(this, R.color.common_blue_gray);
        if (mAttribute.isCharge()) {
            taIvJoinFree.setImageResource(R.drawable.common_ic_check);
            taIvJoinCharge.setImageResource(R.drawable.common_ic_check_hot);
            taTvJoinCharge.setTextColor(selectColor);
            taTvJoinChargeTip.setTextColor(selectColorGray);
            taTvJoinFree.setTextColor(unselectedColor);
            taTvJoinFreeTip.setTextColor(unselectedColorGray);

            taTvCustomGem.setVisibility(View.VISIBLE);
            taEtGem.setVisibility(View.VISIBLE);
            taTvGem.setVisibility(View.VISIBLE);
        } else {
            taIvJoinFree.setImageResource(R.drawable.common_ic_check_hot);
            taIvJoinCharge.setImageResource(R.drawable.common_ic_check);
            taTvJoinFree.setTextColor(selectColor);
            taTvJoinFreeTip.setTextColor(selectColorGray);
            taTvJoinCharge.setTextColor(unselectedColor);
            taTvJoinChargeTip.setTextColor(unselectedColorGray);

            taTvCustomGem.setVisibility(View.GONE);
            taEtGem.setVisibility(View.GONE);
            taTvGem.setVisibility(View.GONE);
        }
    }
}
