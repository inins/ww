package com.wang.social.im.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.imsdk.TIMManager;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================
 * 红包信息
 * <p>
 * Create by ChenJing on 2018-04-24 14:31
 * ============================================
 */
@Data
@NoArgsConstructor
public class EnvelopInfo implements Parcelable {

    public enum Status{
        /*
            还可以领取
         */
        LIVING,
        /*
            过期了
         */
        OVERDUE,
        /*
            抢完了
         */
        EMPTY
    }

    public enum EnvelopType{

        /*
            个人红包
         */
        PRIVATE,
        /*
            等额红包
         */
        EQUAL,
        /*
            拼手气红包
         */
        SPELL
    }

    private long envelopId; //红包ID
    private Status status; //红包状态
    private EnvelopType type; //红包类型
    private String fromUid; //发送人用户ID
    private String fromPortrait; //发送人头像
    private String fromNickname; //发送人昵称
    private String message; //消息
    private int diamond; //红包总钻石数
    private int gotDiamond; //获得钻石数
    private int lastDiamond; //剩余钻石数
    private int count; //总个数
    private int lastCount; //剩余个数
    private long spendTime; //花费时间

    /**
     * 检测是否是自己发的红包
     * @return
     */
    public boolean isSelf(){
        return TIMManager.getInstance().getLoginUser().equals(fromUid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.envelopId);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.fromUid);
        dest.writeString(this.fromPortrait);
        dest.writeString(this.fromNickname);
        dest.writeString(this.message);
        dest.writeInt(this.diamond);
        dest.writeInt(this.gotDiamond);
        dest.writeInt(this.lastDiamond);
        dest.writeInt(this.count);
        dest.writeInt(this.lastCount);
        dest.writeLong(this.spendTime);
    }

    protected EnvelopInfo(Parcel in) {
        this.envelopId = in.readLong();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : Status.values()[tmpStatus];
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : EnvelopType.values()[tmpType];
        this.fromUid = in.readString();
        this.fromPortrait = in.readString();
        this.fromNickname = in.readString();
        this.message = in.readString();
        this.diamond = in.readInt();
        this.gotDiamond = in.readInt();
        this.lastDiamond = in.readInt();
        this.count = in.readInt();
        this.lastCount = in.readInt();
        this.spendTime = in.readLong();
    }

    public static final Parcelable.Creator<EnvelopInfo> CREATOR = new Parcelable.Creator<EnvelopInfo>() {
        @Override
        public EnvelopInfo createFromParcel(Parcel source) {
            return new EnvelopInfo(source);
        }

        @Override
        public EnvelopInfo[] newArray(int size) {
            return new EnvelopInfo[size];
        }
    };
}