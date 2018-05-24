package com.wang.social.topic.mvp.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Data;

@Data
public class Comment implements Parcelable {
    private int commentId;
    private int userId;
    private String avatar;
    private String nickname;
    private String content;
    private int supportTotal;
    private int isSupport;
    private long createTime;
    private int replyNum;
    private int targetUserId;
    private String targetNickname;
    private List<Comment> commentReply;

    protected Comment(Parcel in) {
        commentId = in.readInt();
        userId = in.readInt();
        avatar = in.readString();
        nickname = in.readString();
        content = in.readString();
        supportTotal = in.readInt();
        isSupport = in.readInt();
        createTime = in.readLong();
        replyNum = in.readInt();
        targetUserId = in.readInt();
        targetNickname = in.readString();
        commentReply = in.createTypedArrayList(Comment.CREATOR);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commentId);
        dest.writeInt(userId);
        dest.writeString(avatar);
        dest.writeString(nickname);
        dest.writeString(content);
        dest.writeInt(supportTotal);
        dest.writeInt(isSupport);
        dest.writeLong(createTime);
        dest.writeInt(replyNum);
        dest.writeInt(targetUserId);
        dest.writeString(targetNickname);
        dest.writeTypedList(commentReply);
    }
}
