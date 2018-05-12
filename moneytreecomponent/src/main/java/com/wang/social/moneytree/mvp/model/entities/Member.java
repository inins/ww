package com.wang.social.moneytree.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Member implements Parcelable {
    private long birthday;
    private String constellation;
    private int sex;
    private String nickname;
    private String avatar;
    private int userId;
    private int age;
    private List<Tag> tags;


    protected Member(Parcel in) {
        birthday = in.readLong();
        constellation = in.readString();
        sex = in.readInt();
        nickname = in.readString();
        avatar = in.readString();
        userId = in.readInt();
        age = in.readInt();
        tags = in.createTypedArrayList(Tag.CREATOR);
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(birthday);
        dest.writeString(constellation);
        dest.writeInt(sex);
        dest.writeString(nickname);
        dest.writeString(avatar);
        dest.writeInt(userId);
        dest.writeInt(age);
        dest.writeTypedList(tags);
    }
}
