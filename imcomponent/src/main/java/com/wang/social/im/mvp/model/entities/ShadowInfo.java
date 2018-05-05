package com.wang.social.im.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.wang.social.im.enums.Gender;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-04 19:30
 * ============================================
 */
@Data
@NoArgsConstructor
public class ShadowInfo implements Parcelable {

    private String socialId;
    private String portrait;
    private String nickname;
    private Gender gender;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.socialId);
        dest.writeString(this.portrait);
        dest.writeString(this.nickname);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
    }

    protected ShadowInfo(Parcel in) {
        this.socialId = in.readString();
        this.portrait = in.readString();
        this.nickname = in.readString();
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
    }

    public static final Parcelable.Creator<ShadowInfo> CREATOR = new Parcelable.Creator<ShadowInfo>() {
        @Override
        public ShadowInfo createFromParcel(Parcel source) {
            return new ShadowInfo(source);
        }

        @Override
        public ShadowInfo[] newArray(int size) {
            return new ShadowInfo[size];
        }
    };
}