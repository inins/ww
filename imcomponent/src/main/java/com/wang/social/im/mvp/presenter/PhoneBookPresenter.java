package com.wang.social.im.mvp.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.BaseListJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.BasePresenter;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.helper.RepositoryHelper;
import com.wang.social.im.interfaces.ImCallBack;
import com.wang.social.im.mvp.contract.PhoneBookContract;
import com.wang.social.im.mvp.model.entities.ContactCheckResult;
import com.wang.social.im.mvp.model.entities.PhoneContact;
import com.wang.social.im.mvp.model.entities.dto.ContactCheckResultDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 15:49
 * ============================================
 */
@ActivityScope
public class PhoneBookPresenter extends BasePresenter<PhoneBookContract.Model, PhoneBookContract.View> {

    @Inject
    ApiHelper mApiHelper;

    @Inject
    public PhoneBookPresenter(PhoneBookContract.Model model, PhoneBookContract.View view) {
        super(model, view);
    }

    public void getContacts(Context context) {
        List<PhoneContact> list = new ArrayList<>();
        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor info = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{String.valueOf(id)}, null);

                while (info != null && info.moveToNext()) {
                    //读取通讯录的号码
                    String number = info.getString(info.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = info.getString(info.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    if (TextUtils.isEmpty(number))
                        continue;
                    PhoneContact contact = new PhoneContact();
                    contact.setName(name);
                    contact.setPhoneNumber(formatMobileNumber(number));
                    list.add(contact);
                }
                info.close();
            }
        }
        cursor.close();
        checkJoinStatus(list);
    }

    private String formatMobileNumber(String num2) {
        String num;
        if (num2 != null) {
            // 有的通讯录格式为135-1568-1234
            num = num2.replaceAll("-", "");
            // 有的通讯录格式中- 变成了空格
            num = num.replaceAll(" ", "");
            if (num.startsWith("+86")) {
                num = num.substring(3);
            } else if (num.startsWith("86")) {
                num = num.substring(2);
            } else if (num.startsWith("86")) {
                num = num.substring(2);
            }
        } else {
            num = "";
        }
        // 有些手机号码格式 86后有空格
        return num.trim();
    }

    private void checkJoinStatus(List<PhoneContact> contacts) {
        StringBuilder phones = new StringBuilder();
        for (PhoneContact contact : contacts) {
            phones.append(contact.getPhoneNumber()).append(",");
        }
        if (phones.length() > 0) {
            phones.deleteCharAt(phones.length() - 1);
        }
        mModel.checkPhoneBook(phones.toString()).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .map(new Function<BaseJson<ListDataDTO<ContactCheckResultDTO, ContactCheckResult>>, List<ContactCheckResult>>() {
                    @Override
                    public List<ContactCheckResult> apply(BaseJson<ListDataDTO<ContactCheckResultDTO, ContactCheckResult>> t) throws Exception {
                        return t.getData().transform().getList();
                    }
                })
                .map(new Function<List<ContactCheckResult>, List<PhoneContact>>() {
                    @Override
                    public List<PhoneContact> apply(List<ContactCheckResult> contactCheckResults) throws Exception {
                        for (ContactCheckResult contactCheckResult : contactCheckResults) {
                            for (PhoneContact contact : contacts) {
                                if (contact.getPhoneNumber().equals(contactCheckResult.getPhoneNumber())) {
                                    contact.setJoined(!TextUtils.isEmpty(contactCheckResult.getUserId()));
                                    contact.setFriend(contactCheckResult.isFriend());
                                    contact.setUserId(TextUtils.isEmpty(contactCheckResult.getUserId()) ? "" : contactCheckResult.getUserId());
                                    break;
                                }
                            }
                        }
                        return contacts;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<PhoneContact>>() {
                    @Override
                    public void onNext(List<PhoneContact> contacts) {
                        mRootView.showContacts(contacts);
                    }
                });
    }

    public void friendRequest(String userId, String reason){
        mRootView.showLoading();
        RepositoryHelper.getInstance().sendFriendlyApply(mRootView, userId, reason, new ImCallBack() {
            @Override
            public void onSuccess(Object object) {
                mRootView.hideLoading();
                ToastUtil.showToastShort("申请成功");
            }

            @Override
            public boolean onFail(Throwable e) {
                mRootView.hideLoading();
                return super.onFail(e);
            }
        });
    }
}