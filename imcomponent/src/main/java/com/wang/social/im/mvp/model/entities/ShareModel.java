package com.wang.social.im.mvp.model.entities;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Chao on 2017/8/4.
 */

public class ShareModel implements Comparable<ShareModel> {

    private List<ShareModel> nodes;
    private int isSelf;
    private int haveMoney;
    private int leven = 1;
    private int id = 0;
    private int Superid = 0;
    private int childNum = 0;//子节点总数
    private int currNum = 0;
    private String headerImage;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private int lineIndex;

    public int getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(int isSelf) {
        this.isSelf = isSelf;
    }

    public int getHaveMoney() {
        return haveMoney;
    }

    public void setHaveMoney(int haveMoney) {
        this.haveMoney = haveMoney;
    }

    public List<ShareModel> getNodes() {
        return nodes;
    }

    public int getSuperid() {
        return Superid;
    }

    public void setSuperid(int superid) {
        Superid = superid;
        childNum++;
    }

    public int getChildNum() {
        return childNum;
    }

    public void setChildNum(int childNum) {
        this.childNum = childNum;
    }

    public void setNodes(List<ShareModel> nodes) {
        this.nodes = nodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeven() {
        return leven;
    }

    public int getCurrNum() {
        return currNum;
    }

    public void setCurrNum(int currNum) {
        this.currNum = currNum;
    }

    public void setLeven(int leven) {
        this.leven = leven;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public void setLayout(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    @Override
    public int compareTo(@NonNull ShareModel o) {
        return this.currNum - o.getCurrNum();
    }

}
