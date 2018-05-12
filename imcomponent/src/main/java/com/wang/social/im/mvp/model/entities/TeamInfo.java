package com.wang.social.im.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================
 * 觅聊实体
 * <p>
 * Create by ChenJing on 2018-04-28 17:04
 * ============================================
 */
@Data
@NoArgsConstructor
public class TeamInfo implements Parcelable {

    private String teamId;
    private String name;
    private String cover;
    private int memberSize;
    private boolean isFree;
    private int joinCost;
    private boolean joined;
    private boolean validation;
    private List<Tag> tags;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teamId);
        dest.writeString(this.name);
        dest.writeString(this.cover);
        dest.writeInt(this.memberSize);
        dest.writeByte(this.isFree ? (byte) 1 : (byte) 0);
        dest.writeInt(this.joinCost);
        dest.writeByte(this.joined ? (byte) 1 : (byte) 0);
        dest.writeByte(this.validation ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.tags);
    }

    protected TeamInfo(Parcel in) {
        this.teamId = in.readString();
        this.name = in.readString();
        this.cover = in.readString();
        this.memberSize = in.readInt();
        this.isFree = in.readByte() != 0;
        this.joinCost = in.readInt();
        this.joined = in.readByte() != 0;
        this.validation = in.readByte() != 0;
        this.tags = in.createTypedArrayList(Tag.CREATOR);
    }

    public static final Parcelable.Creator<TeamInfo> CREATOR = new Parcelable.Creator<TeamInfo>() {
        @Override
        public TeamInfo createFromParcel(Parcel source) {
            return new TeamInfo(source);
        }

        @Override
        public TeamInfo[] newArray(int size) {
            return new TeamInfo[size];
        }
    };
}
