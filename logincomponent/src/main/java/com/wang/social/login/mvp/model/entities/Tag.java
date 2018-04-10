package com.wang.social.login.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tag implements Parcelable {
    private int id;
    private String tagName;
    // state:是否已选为个人标签(0：不是，1：是)
    private int state;

    public boolean isPersonalTag() {
        return getState() == 1;
    }
    public void clickTag() {
        state = state == 0 ? 1 : 0;
    }
    public void setUnselected() {
        state = 0;
    }
    public void setSelected() { state = 1; }
    public String getPrintString() {
        return Integer.toString(id) + " " + tagName + " " + state;
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
