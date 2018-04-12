package com.wang.social.login.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tag implements Parcelable {
    private int id;
    private String tagName;
    // state:是否已选为个人标签(0：不是，1：是)
    private int state;
    // isInterest:是否为个人兴趣推荐标签(0:不是，1：是)
    private int isInterest;

    /**
     * 是否是个人标签
     */
    public boolean isPersonalTag() {
        return getState() == 1;
    }

    /**
     * 自定义的状态，2 表示用户点击选择了的标签
     */
    public boolean isPersonalSelectedTag() { return getState() == 2; }

    /**
     * 是否是个人兴趣标签
     */
    public boolean isInterest() {
        return getIsInterest() == 1;
    }

    /**
     * 标签点击(不管是什么模式，直接都设置数据)
     * 个人标签模式下已经是个人标签的标签不可选择，
     * 所以这里加了一个状态2，表示是用户点击添加的标签
     */
    public void clickTag() {
        state = state == 0 ? 2 : 0;
        isInterest = isInterest == 0 ? 1 : 0;
    }

    /**
     * 取消选择，个人标签不可取消
     */
    public void setUnselected() {
        state = state == 1 ? 1 : 0;
        isInterest = 0;
    }

    /**
     * 选中
     */
    public void setSelected() {
        state = 2;
        isInterest = 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.tagName);
        dest.writeInt(this.state);
    }

    protected Tag(Parcel in) {
        id = in.readInt();
        tagName = in.readString();
        state = in.readInt();
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}
