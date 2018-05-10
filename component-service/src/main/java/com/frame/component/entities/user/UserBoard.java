package com.frame.component.entities.user;

import com.frame.component.entities.Tag;
import com.frame.component.entities.photo.Photo;
import com.frame.component.view.bannerview.Image;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UserBoard {

    /**
     * birthday : 788976000000
     * province : 510000
     * cityName : 成都市
     * city : 510100
     * picList : [{"photoUrl":"http://resouce.dongdongwedding.com/Data/wangwang/iOS-2018-05-02-ATj12DM6.png","userPhotoId":1,"state":"1","createDate":1525937363000},{"photoUrl":"http://resouce.dongdongwedding.com/Data/wangwang/iOS-2018-05-02-ATj12DM6.png","userPhotoId":2,"state":"1","createDate":1525937397000},{"photoUrl":"http://resouce.dongdongwedding.com/Data/wangwang/iOS-2018-05-02-ATj12DM6.png","userPhotoId":3,"state":"1","createDate":1525937408000}]
     * sex : 1
     * nickname : 飘飘飘香
     * autograph : 这只蜘蛛很奇葩，什么也么留下。。。
     * avatar : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * provinceName : 四川省
     * userId : 10000
     * tags : [{"id":2,"tagId":3,"tagName":"电影","isIndexShow":0},{"id":3,"tagId":4,"tagName":"电视剧","isIndexShow":1},{"id":6,"tagId":6,"tagName":"直播","isIndexShow":0},{"id":4,"tagId":5,"tagName":"综艺","isIndexShow":0}]
     */

    private long birthday;
    private String province;
    private String cityName;
    private String city;
    private int sex;
    private String nickname;
    private String autograph;
    private String avatar;
    private String provinceName;
    private int userId;
    private List<Tag> tags;
    private List<Photo> picList;

    public boolean isMale() {
        return sex == 0;
    }

    public String getTagText() {
        if (StrUtil.isEmpty(tags)) return "";
        String tagText = "";
        for (Tag tag : tags) {
            tagText += tag.getTagName() + ",";
        }
        return StrUtil.subLastChart(tagText, ",");
    }

    public List<Image> getBannerImageList() {
        List<Image> images = new ArrayList<>();
        if (!StrUtil.isEmpty(picList)) {
            for (Photo photo : picList) {
                images.add(new Image(photo.getPhotoUrl()));
            }
        }
        return images;
    }
}
