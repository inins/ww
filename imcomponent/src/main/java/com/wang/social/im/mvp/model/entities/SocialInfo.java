package com.wang.social.im.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.frame.component.api.Api;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.utils.StrUtil;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================
 * 趣聊信息
 * <p>
 * Create by ChenJing on 2018-05-02 9:29
 * ============================================
 */
@Data
@NoArgsConstructor
public class SocialInfo implements Parcelable {

    private String socialId;
    private String name;
    private String cover;
    private String avatar;
    private String desc;
    private SocialAttribute attr;
    private boolean createTeam;
    private List<Tag> tags;
    //分身信息
    private ShadowInfo shadowInfo;
    //我在趣聊中的信息
    private MemberInfo memberInfo;
    private int memberNum;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.socialId);
        dest.writeString(this.name);
        dest.writeString(this.cover);
        dest.writeString(this.avatar);
        dest.writeString(this.desc);
        dest.writeParcelable(this.attr, flags);
        dest.writeByte(this.createTeam ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.tags);
        dest.writeParcelable(this.shadowInfo, flags);
        dest.writeParcelable(this.memberInfo, flags);
        dest.writeInt(this.memberNum);
    }

    protected SocialInfo(Parcel in) {
        this.socialId = in.readString();
        this.name = in.readString();
        this.cover = in.readString();
        this.avatar = in.readString();
        this.desc = in.readString();
        this.attr = in.readParcelable(SocialAttribute.class.getClassLoader());
        this.createTeam = in.readByte() != 0;
        this.tags = in.createTypedArrayList(Tag.CREATOR);
        this.shadowInfo = in.readParcelable(ShadowInfo.class.getClassLoader());
        this.memberInfo = in.readParcelable(MemberInfo.class.getClassLoader());
        this.memberNum = in.readInt();
    }

    public static final Parcelable.Creator<SocialInfo> CREATOR = new Parcelable.Creator<SocialInfo>() {
        @Override
        public SocialInfo createFromParcel(Parcel source) {
            return new SocialInfo(source);
        }

        @Override
        public SocialInfo[] newArray(int size) {
            return new SocialInfo[size];
        }
    };

    //获取标签字符串 #xx #xx #xx
    public String getTagText() {
        if (StrUtil.isEmpty(tags)) return "";
        String tagText = "";
        for (Tag tag : tags) {
            tagText += "#" + tag.getTagName() + " ";
        }
        return tagText.trim();
    }

    //获取二维码图片地址
    public String getQrcodeImg(){
        return Api.DOMAIN + Api.GROUP_QRCODE + "?v=2.0.0&groupId=" + socialId;
    }
}