package com.frame.component.entities;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillBoard implements Parcelable {
    /**
     * 参数名字	参数类型	说明	是否必须
     billboardId	Integer	广告ID	√
     picUrl	String	图片URL	√
     linkUrl	String	链接URL	√
     linkType	Integer	链接类型	√

     跳转需求

     1）H5,内置浏览器打开

     2）应用内页面：话题详情、趣晒详情

     3）H5，外部浏览器打开

     4）拦截点击行为配置规则由技术制定


     */
    private int billboardId;
    private String picUrl;
    private String linkUrl;
    private String linkType;

    protected BillBoard(Parcel in) {
        billboardId = in.readInt();
        picUrl = in.readString();
        linkUrl = in.readString();
        linkType = in.readString();
    }

    public static final Creator<BillBoard> CREATOR = new Creator<BillBoard>() {
        @Override
        public BillBoard createFromParcel(Parcel in) {
            return new BillBoard(in);
        }

        @Override
        public BillBoard[] newArray(int size) {
            return new BillBoard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(billboardId);
        dest.writeString(picUrl);
        dest.writeString(linkUrl);
        dest.writeString(linkType);
    }
}
