package com.wang.social.im.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;
import com.wang.social.im.enums.MessageNotifyType;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 17:35
 * ============================================
 */
@Data
@NoArgsConstructor
public class MemberInfo implements Parcelable {

    //群成员
    public static final int ROLE_MEMBER = 0x001;
    //群主
    public static final int ROLE_MASTER = 0x002;

    private String groupId;
    private String memberId;
    private String nickname;
    private String portrait;
    private String ageRange;
    private Gender gender;
    private String constellation;
    //消息提醒类型
    private MessageNotifyType notifyType;
    //角色
    private int role;
    private List<Tag> tags;
    //生日时间戳
    private long birthMills;
    //是否为好友关系
    private boolean friendly;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupId);
        dest.writeString(this.memberId);
        dest.writeString(this.nickname);
        dest.writeString(this.portrait);
        dest.writeString(this.ageRange);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeString(this.constellation);
        dest.writeInt(this.notifyType == null ? -1 : this.notifyType.ordinal());
        dest.writeInt(this.role);
        dest.writeTypedList(this.tags);
        dest.writeLong(this.birthMills);
        dest.writeByte(this.friendly ? (byte) 1 : (byte) 0);
    }

    protected MemberInfo(Parcel in) {
        this.groupId = in.readString();
        this.memberId = in.readString();
        this.nickname = in.readString();
        this.portrait = in.readString();
        this.ageRange = in.readString();
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.constellation = in.readString();
        int tmpNotifyType = in.readInt();
        this.notifyType = tmpNotifyType == -1 ? null : MessageNotifyType.values()[tmpNotifyType];
        this.role = in.readInt();
        this.tags = in.createTypedArrayList(Tag.CREATOR);
        this.birthMills = in.readLong();
        this.friendly = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MemberInfo> CREATOR = new Parcelable.Creator<MemberInfo>() {
        @Override
        public MemberInfo createFromParcel(Parcel source) {
            return new MemberInfo(source);
        }

        @Override
        public MemberInfo[] newArray(int size) {
            return new MemberInfo[size];
        }
    };
}
