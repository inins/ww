package com.wang.social.topic.mvp.model.entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.wang.social.topic.R;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Template implements Parcelable {
    private int id;
    private String name;
    private String url;
    private String freeState;
    private String freeMoney;

    public static Template newDefault(Context context) {
        Template template = new Template();
        template.setId(-1);
        template.setName(context.getResources().getString(R.string.topic_template_default));
        return template;
    }

    protected Template(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
        freeState = in.readString();
        freeMoney = in.readString();
    }

    public static final Creator<Template> CREATOR = new Creator<Template>() {
        @Override
        public Template createFromParcel(Parcel in) {
            return new Template(in);
        }

        @Override
        public Template[] newArray(int size) {
            return new Template[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(freeState);
        dest.writeString(freeMoney);
    }
}
