package com.wang.social.moneytree.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lombok.Data;

@Data
public class GameRecord implements Parcelable {
    public final static int TYPE_PLAYING = 0;
    public final static int TYPE_WIN = 1;
    public final static int TYPE_LOSE = 2;
    public final static int TYPE_TIE = 3;
    public final static int TYPE_FAILED = 4;

    @IntDef({
            TYPE_PLAYING,
            TYPE_WIN,
            TYPE_LOSE,
            TYPE_TIE,
            TYPE_FAILED
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface RecordType {}

    /**
     * isFinish:是否已完成(0：未完成，1：已完成)，
     * diamond:钻石数，
     * gameType:游戏类型（1：人数，2：时间）；
     * needPeapleNum：还差多少人；
     * timeLen:剩余时长；
     * gotDiamond:得到钻石；
     * type:(0:未结束；1:赢；2:输；3:平；4:游戏失败)
     */
    private int gameId;
    private String roomNickname;
    private int peopleNum;
    private int diamond;
    private int groupId;
    private int roomId;
    private String roomAvatar;
    private int isFinish;
    private int gameType;
    private int joinNum;
    private int timeLen;
    private int gotDiamond;
    private int type;
    private long createTime;




    protected GameRecord(Parcel in) {
        gameId = in.readInt();
        roomNickname = in.readString();
        peopleNum = in.readInt();
        diamond = in.readInt();
        groupId = in.readInt();
        roomId = in.readInt();
        roomAvatar = in.readString();
        isFinish = in.readInt();
        gameType = in.readInt();
        joinNum = in.readInt();
        timeLen = in.readInt();
        gotDiamond = in.readInt();
        type = in.readInt();
        createTime = in.readLong();
    }

    public static final Creator<GameRecord> CREATOR = new Creator<GameRecord>() {
        @Override
        public GameRecord createFromParcel(Parcel in) {
            return new GameRecord(in);
        }

        @Override
        public GameRecord[] newArray(int size) {
            return new GameRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gameId);
        dest.writeString(roomNickname);
        dest.writeInt(peopleNum);
        dest.writeInt(diamond);
        dest.writeInt(groupId);
        dest.writeInt(roomId);
        dest.writeString(roomAvatar);
        dest.writeInt(isFinish);
        dest.writeInt(gameType);
        dest.writeInt(joinNum);
        dest.writeInt(timeLen);
        dest.writeInt(gotDiamond);
        dest.writeInt(type);
        dest.writeLong(createTime);
    }
}
