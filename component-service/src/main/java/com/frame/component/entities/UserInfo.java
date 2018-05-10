package com.frame.component.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-10 16:00
 * ============================================
 */
@Data
@NoArgsConstructor
public class UserInfo implements Parcelable {

    private String userId;
    private String portrait;
    private String nickname;
    private Gender gender = Gender.SECRET;
    private long birthMills;
    private String ageRange;
    private String constellation;
    private long registerTime;
    private List<Tag> tags;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.portrait);
        dest.writeString(this.nickname);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeLong(this.birthMills);
        dest.writeString(this.ageRange);
        dest.writeString(this.constellation);
        dest.writeLong(this.registerTime);
        dest.writeTypedList(this.tags);
    }

    protected UserInfo(Parcel in) {
        this.userId = in.readString();
        this.portrait = in.readString();
        this.nickname = in.readString();
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.birthMills = in.readLong();
        this.ageRange = in.readString();
        this.constellation = in.readString();
        this.registerTime = in.readLong();
        this.tags = in.createTypedArrayList(Tag.CREATOR);
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
