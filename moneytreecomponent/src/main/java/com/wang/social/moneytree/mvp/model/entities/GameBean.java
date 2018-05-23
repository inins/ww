package com.wang.social.moneytree.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameBean implements Parcelable {
    /**
     * roomId：游戏房间id；
     * roomAvatar:房间头像；
     * roomNickname:房间昵称；
     * diamond：参与游戏付费钻数；
     * peopleNum：满足游戏开始的人数；
     * gameId:当前进行的游戏id；
     * groupId:创建游戏的群id；
     * joinNum：已加入游戏的人数；
     * reset Time：游戏重置时间（单位为S）；
     * gameCreator:游戏创建者;
     * joinRoomId：该用户加入的房间号
     */
    private int roomId;
    private int gameId;
    private String groupId;
    private int joinNum;
    private int gameType;
    private int gameCreator;
    private String timeLen;
    private int peopleNum;
    private int isJoined;
    private int diamond;
    private String roomAvatar;
    private String roomNickname;
    private int resetTime;

    public void copyRoomMsg(RoomMsg roomMsg) {
        setGroupId(Integer.toString(roomMsg.getGroupId()));
        setPeopleNum(roomMsg.getPeopleNum());
        setJoinNum(roomMsg.getJoinNum());
        setDiamond(roomMsg.getDiamond());
        setIsJoined(roomMsg.getIsJoin());
        setRoomNickname(roomMsg.getRoomName());
        setResetTime(roomMsg.getResetTime());
        setGameId(roomMsg.getGameId());
    }

    protected GameBean(Parcel in) {
        roomId = in.readInt();
        gameId = in.readInt();
        groupId = in.readString();
        joinNum = in.readInt();
        gameType = in.readInt();
        gameCreator = in.readInt();
        timeLen = in.readString();
        peopleNum = in.readInt();
        isJoined = in.readInt();
        diamond = in.readInt();
        roomAvatar = in.readString();
        roomNickname = in.readString();
        resetTime = in.readInt();
    }

    public static final Creator<GameBean> CREATOR = new Creator<GameBean>() {
        @Override
        public GameBean createFromParcel(Parcel in) {
            return new GameBean(in);
        }

        @Override
        public GameBean[] newArray(int size) {
            return new GameBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roomId);
        dest.writeInt(gameId);
        dest.writeString(groupId);
        dest.writeInt(joinNum);
        dest.writeInt(gameType);
        dest.writeInt(gameCreator);
        dest.writeString(timeLen);
        dest.writeInt(peopleNum);
        dest.writeInt(isJoined);
        dest.writeInt(diamond);
        dest.writeString(roomAvatar);
        dest.writeString(roomNickname);
        dest.writeInt(resetTime);
    }
}
