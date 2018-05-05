package com.wang.social.im.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================
 * 觅聊属性
 * <p>
 * Create by ChenJing on 2018-05-03 14:53
 * ============================================
 */
@Data
@NoArgsConstructor
public class TeamAttribute implements Parcelable {

    private boolean charge;
    private int gem;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.charge ? (byte) 1 : (byte) 0);
        dest.writeInt(this.gem);
    }

    protected TeamAttribute(Parcel in) {
        this.charge = in.readByte() != 0;
        this.gem = in.readInt();
    }

    public static final Parcelable.Creator<TeamAttribute> CREATOR = new Parcelable.Creator<TeamAttribute>() {
        @Override
        public TeamAttribute createFromParcel(Parcel source) {
            return new TeamAttribute(source);
        }

        @Override
        public TeamAttribute[] newArray(int size) {
            return new TeamAttribute[size];
        }
    };
}
