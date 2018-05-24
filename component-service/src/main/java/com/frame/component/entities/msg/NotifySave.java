package com.frame.component.entities.msg;

import java.io.Serializable;

import lombok.Data;

@Data
public class NotifySave implements Serializable{
    private int sysMsgCount;
    private int friendCount;
    private int groupCount;
    private int joinCount;
    private int zanCount;
    private int evaCount;
    private int aiteCount;

    @Override
    public String toString() {
        return "NotifySave{" +
                "sysMsgCount=" + sysMsgCount +
                ", friendCount=" + friendCount +
                ", groupCount=" + groupCount +
                ", joinCount=" + joinCount +
                ", zanCount=" + zanCount +
                ", evaCount=" + evaCount +
                ", aiteCount=" + aiteCount +
                '}';
    }
}
