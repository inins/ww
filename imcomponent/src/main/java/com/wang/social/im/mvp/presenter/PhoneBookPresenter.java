package com.wang.social.im.mvp.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.frame.di.scope.ActivityScope;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.PhoneBookContract;
import com.wang.social.im.mvp.model.entities.PhoneContact;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 15:49
 * ============================================
 */
@ActivityScope
public class PhoneBookPresenter extends BasePresenter<PhoneBookContract.Model, PhoneBookContract.View> {

    @Inject
    public PhoneBookPresenter(PhoneBookContract.Model model, PhoneBookContract.View view) {
        super(model, view);
    }

    public void getContacts(Context context) {
        mRootView.hideLoading();
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
        mRootView.showContacts(list);
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
}
