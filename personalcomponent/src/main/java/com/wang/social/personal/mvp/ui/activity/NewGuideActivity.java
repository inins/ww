package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.NetParam;
import com.frame.component.common.SimpleTextWatcher;
import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.AppValiHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.helper.PicPhotoHelperEx;
import com.wang.social.personal.mvp.entities.CommonEntity;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomGender;
import com.wang.social.personal.mvp.ui.dialog.DialogDatePicker;
import com.wang.social.pictureselector.helper.PhotoHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

@RouteNode(path = "/newGuide", desc = "新用户引导页面")
public class NewGuideActivity extends BasicAppNoDiActivity implements PhotoHelper.OnPhotoCallback {

    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.edit_name)
    EditText editName;
    @BindView(R2.id.text_birthday)
    TextView textBirthday;
    @BindView(R2.id.text_gender)
    TextView textGender;
    @BindView(R2.id.btn_go)
    Button btnGo;
    private PicPhotoHelperEx photoHelperEx;

    private DialogBottomGender dialogGender;
    private DialogDatePicker dialogDate;

    private String path;

    public static void start(Context context) {
        Intent intent = new Intent(context, NewGuideActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_newguide;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        toolbar.bringToFront();
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextLight(this);
        photoHelperEx = PicPhotoHelperEx.newInstance(this, this).needOfficialPhoto(true);
        dialogGender = new DialogBottomGender(this);
        dialogDate = new DialogDatePicker(this);

        dialogGender.setOnGenderSelectListener(gender -> {
            textGender.setText(gender);
            dialogGender.dismiss();
            setBtnStatus();
        });
        dialogDate.setOnDateChooseListener((year, month, day, astro, showDate) -> {
            textBirthday.setText(showDate);
            setBtnStatus();
        });
        btnGo.setEnabled(false);
        editName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setBtnStatus();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoHelperEx.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_birthday) {
            dialogDate.setDate(textBirthday.getText().toString());
            dialogDate.show();
        } else if (i == R.id.text_gender) {
            dialogGender.show();
        } else if (i == R.id.img_header) {
            photoHelperEx.showDefaultDialog();
        } else if (i == R.id.btn_go) {
            String name = editName.getText().toString();
            String birthday = textBirthday.getText().toString();
            String gender = textGender.getText().toString();
            Integer sex = TextUtils.isEmpty(gender) ? null : ("男".equals(gender) ? 0 : 1);

            String msg = AppValiHelper.newGuide(name, path, sex, birthday);
            if (msg != null) {
                ToastUtil.showToastShort(msg);
            } else {
                uploadAndCommit(name, path, sex, birthday);
            }
        }
    }

    private void setBtnStatus() {
        String name = editName.getText().toString();
        String birthday = textBirthday.getText().toString();
        String gender = textGender.getText().toString();
        Integer sex = TextUtils.isEmpty(gender) ? null : ("男".equals(gender) ? 0 : 1);

        String msg = AppValiHelper.newGuide(name, path, sex, birthday);
        btnGo.setEnabled(msg == null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonHelper.AppHelper.startHomeActivity(this);
    }

    @Override
    public void onResult(String path) {
        ImageLoaderHelper.loadCircleImg(imgHeader, path);
        this.path = path;
        setBtnStatus();
    }

    private void uploadAndCommit(String nickname, String path, Integer sex, String birthday) {
        QiNiuManager.newInstance().uploadFile(this, path, new QiNiuManager.OnSingleUploadListener() {
            @Override
            public void onSuccess(String url) {
                updateUserInfo(nickname, url, sex, birthday);
            }

            @Override
            public void onFail() {
                ToastUtil.showToastShort("上传失败");
            }
        });
    }

    private void updateUserInfo(String nickname, String avatar, Integer sex, String birthday) {
        Map<String, Object> map = new NetParam()
                .put("nickname", nickname)
                .put("avatar", avatar)
                .put("sex", sex)
                .put("birthday", birthday)
                .build();
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(UserService.class).updateUserInfo(map),
                new ErrorHandleSubscriber<BaseJson<CommonEntity>>() {
                    @Override
                    public void onNext(BaseJson<CommonEntity> basejson) {
                        //保存修改的用户数据
                        User user = AppDataHelper.getUser();
                        if (user == null) return;
                        if (!TextUtils.isEmpty(nickname)) user.setNickname(nickname);
                        if (!TextUtils.isEmpty(avatar)) {
                            user.setAvatar(avatar);
                        }
                        if (sex != null) user.setSex(sex);
                        if (!TextUtils.isEmpty(birthday)) user.setBirthday(birthday);
                        AppDataHelper.saveUser(user);
                        //通知其他组件
                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_USERINFO_CHANGE));
                        //进入推荐用户/圈子页面
                        NewGuideRecommendActivity.start(NewGuideActivity.this);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }
}
