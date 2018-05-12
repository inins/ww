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
import com.wang.social.im.mvp.model.entities.ShadowInfo;
import com.wang.social.im.mvp.model.entities.SocialInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 趣聊收费设置
 */
public class SocialChargeSettingActivity extends BasicAppActivity {

    public static final String EXTRA_SOCIAL = "social";

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
    SocialInfo social;

    @Inject
    IRepositoryManager mRepositoryManager;

    private Disposable disposable;

    private boolean mFromTextChange;

    public static void start(Activity activity, SocialInfo social, int requestCode) {
        Intent intent = new Intent(activity, SocialChargeSettingActivity.class);
        intent.putExtra(EXTRA_SOCIAL, social);
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
        if (social != null) {
            if (social.getAttr().isCharge()) {
                gcsRgJoin.check(gcsRbPayJoin.getId());

                if (social.getAttr().getGem() == 100) {
                    gcsRbGem100.setChecked(true);
                } else if (social.getAttr().getGem() == 300) {
                    gcsRbGem300.setChecked(true);
                } else if (social.getAttr().getGem() == 500) {
                    gcsRbGem500.setChecked(true);
                } else {
                    gcsEtGem.setText(String.valueOf(social.getAttr().getGem()));
                }

                gcsRbJoinFree.setEnabled(false);
                gcsRbPayJoin.setEnabled(false);
                gcsRbGem100.setEnabled(false);
                gcsRbGem300.setEnabled(false);
                gcsRbGem500.setEnabled(false);
                gcsEtGem.setEnabled(false);
            } else {
                gcsRgJoin.check(gcsRbJoinFree.getId());
            }
            toggleGemInput();
        }
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
                        if (!gcsRbJoinFree.isEnabled()) {
                            finish();
                            return;
                        }
                        if (social.getAttr().isCharge()) {
                            if (social.getAttr().getGem() > IMConstants.SOCIAL_CHARGE_LIMIT_MAX || social.getAttr().getGem() < IMConstants.SOCIAL_CHARGE_LIMIT_MIN) {
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
                        social.getAttr().setCharge(false);
                        break;
                    case R.id.gcs_rb_pay_join:
                        social.getAttr().setCharge(true);
                        break;
                }
                toggleGemInput();
            }
        });

        gcsRgGem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.gcs_rb_gem_100:
                        social.getAttr().setGem(100);
                        break;
                    case R.id.gcs_rb_gem_300:
                        social.getAttr().setGem(300);
                        break;
                    case R.id.gcs_rb_gem_500:
                        social.getAttr().setGem(500);
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
                        social.getAttr().setGem(0);
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
                    social.getAttr().setGem(Integer.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void toggleGemInput() {
        gcsRgGem.setVisibility(social.getAttr().isCharge() ? View.VISIBLE : View.GONE);
        gcsTvCustomGem.setVisibility(social.getAttr().isCharge() ? View.VISIBLE : View.GONE);
        gcsEtGem.setVisibility(social.getAttr().isCharge() ? View.VISIBLE : View.GONE);
        gcsTvGem.setVisibility(social.getAttr().isCharge() ? View.VISIBLE : View.GONE);
    }

    private void updateChargeStatus() {
        new SocialHomeModel(mRepositoryManager)
                .updateSocialInfo(social)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        SocialChargeSettingActivity.this.disposable = disposable;
                        showLoadingDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoadingDialog();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson baseJson) {
                        //修改完成
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_SOCIAL, social);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }
}
