package com.wang.social.im.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.common.InputPositiveIntegerFilter;
import com.wang.social.im.di.component.DaggerActivityComponent;
import com.wang.social.im.mvp.model.SocialHomeModel;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 觅聊收费设置
 */
public class TeamChargeSettingActivity extends BasicAppActivity {

    public static final String EXTRA_TEAM = "team";

    @BindView(R2.id.gcs_toolbar)
    SocialToolbar gcsToolbar;
    @BindView(R2.id.gcs_title)
    TitleView gcsTitle;
    @BindView(R2.id.gcs_tv_charge_title)
    TextView gcsTvChargeTitle;
    @BindView(R2.id.gcs_rb_join_free)
    RadioButton gcsRbJoinFree;
    @BindView(R2.id.gcs_rb_pay_join)
    RadioButton gcsRbPayJoin;
    @BindView(R2.id.gcs_rg_join)
    RadioGroup gcsRgJoin;
    @BindView(R2.id.gcs_rb_gem_100)
    RadioButton gcsRbGem100;
    @BindView(R2.id.gcs_rb_gem_300)
    RadioButton gcsRbGem300;
    @BindView(R2.id.gcs_rb_gem_500)
    RadioButton gcsRbGem500;
    @BindView(R2.id.gcs_rg_gem)
    RadioGroup gcsRgGem;
    @BindView(R2.id.gcs_tv_custom_gem)
    TextView gcsTvCustomGem;
    @BindView(R2.id.gcs_et_gem)
    EditText gcsEtGem;
    @BindView(R2.id.gcs_tv_gem)
    TextView gcsTvGem;

    @Autowired
    TeamInfo team;

    @Inject
    IRepositoryManager mRepositoryManager;

    private Disposable disposable;

    private boolean mFromTextChange;

    public static void start(Activity activity, TeamInfo team, int requestCode) {
        Intent intent = new Intent(activity, TeamChargeSettingActivity.class);
        intent.putExtra(EXTRA_TEAM, team);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_social_charge_setting;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        gcsRbJoinFree.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        gcsEtGem.setFilters(new InputFilter[]{new InputPositiveIntegerFilter()});

        gcsToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:
                        if (!team.isFree()) {
                            if (team.getJoinCost() > IMConstants.SOCIAL_CHARGE_LIMIT_MAX || team.getJoinCost() < IMConstants.SOCIAL_CHARGE_LIMIT_MIN) {
                                ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_social_gem_limit_tip));
                                return;
                            }
                        } else {
                            finish();
                            return;
                        }
                        updateChargeStatus();
                        break;
                }
            }
        });

        gcsRgJoin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.gcs_rb_join_free:
                        team.setFree(true);
                        break;
                    case R.id.gcs_rb_pay_join:
                        team.setFree(false);
                        break;
                }
                toggleGemInput();
            }
        });

        gcsRgGem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sa_rb_gem_100:
                        team.setJoinCost(100);
                        break;
                    case R.id.sa_rb_gem_300:
                        team.setJoinCost(300);
                        break;
                    case R.id.sa_rb_gem_500:
                        team.setJoinCost(500);
                        break;
                }
                if (!mFromTextChange) {
                    gcsEtGem.setText("");
                }
                mFromTextChange = false;
            }
        });

        gcsEtGem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gcsRgGem.clearCheck();
                    if (TextUtils.isEmpty(gcsEtGem.getText().toString())) {
                        team.setJoinCost(0);
                    }
                }
                return false;
            }
        });

        gcsEtGem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mFromTextChange = true;
                    gcsRgGem.clearCheck();
                    team.setJoinCost(Integer.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void toggleGemInput() {
        gcsRgGem.setVisibility(team.isFree() ? View.GONE : View.VISIBLE);
        gcsTvCustomGem.setVisibility(team.isFree() ? View.GONE : View.VISIBLE);
        gcsEtGem.setVisibility(team.isFree() ? View.GONE : View.VISIBLE);
        gcsTvGem.setVisibility(team.isFree() ? View.GONE : View.VISIBLE);
    }

    private void updateChargeStatus() {
//        new SocialHomeModel(mRepositoryManager)
//                .updateSocialInfo(social)
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        TeamChargeSettingActivity.this.disposable = disposable;
//                        showLoadingDialog();
//                    }
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        dismissLoadingDialog();
//                    }
//                })
//                .subscribe(new ErrorHandleSubscriber<BaseJson>() {
//                    @Override
//                    public void onNext(BaseJson baseJson) {
//                        //修改完成
//                        Intent intent = new Intent();
//                        intent.putExtra(EXTRA_SOCIAL, team);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                });
    }
}
