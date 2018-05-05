package com.wang.social.im.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-03 10:55
 * ============================================
 */
@Data
@NoArgsConstructor
public class SocialAttribute implements Parcelable {

    public enum GenderLimit {
        /*
            不限
         */
        UNLIMITED,
        /*
            男
         */
        MALE,
        /*
            女
         */
        FEMALE
    }

    public enum AgeLimit {
        /*
            不限
         */
        UNLIMITED,
        /*
            90后
         */
        AFTER_90,
        /*
            95后
         */
        AFTER_95,
        /*
            00后
         */
        AFTER_00,
        /*
            其他
         */
        OTHER
    }

    //是否公开
    private boolean open;
    private boolean charge;
    private int gem;
    private GenderLimit genderLimit;
    private List<AgeLimit> ageLimit = new ArrayList<>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.open ? (byte) 1 : (byte) 0);
        dest.writeByte(this.charge ? (byte) 1 : (byte) 0);
        dest.writeInt(this.gem);
        dest.writeInt(this.genderLimit == null ? -1 : this.genderLimit.ordinal());
        dest.writeList(this.ageLimit);
    }

    protected SocialAttribute(Parcel in) {
        this.open = in.readByte() != 0;
        this.charge = in.readByte() != 0;
        this.gem = in.readInt();
        int tmpGenderLimit = in.readInt();
        this.genderLimit = tmpGenderLimit == -1 ? null : GenderLimit.values()[tmpGenderLimit];
        this.ageLimit = new ArrayList<AgeLimit>();
        in.readList(this.ageLimit, AgeLimit.class.getClassLoader());
    }

    public static final Creator<SocialAttribute> CREATOR = new Creator<SocialAttribute>() {
        @Override
        public SocialAttribute createFromParcel(Parcel source) {
            return new SocialAttribute(source);
        }

        @Override
        public SocialAttribute[] newArray(int size) {
            return new SocialAttribute[size];
        }
    };
}
