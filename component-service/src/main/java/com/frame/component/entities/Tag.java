package com.frame.component.entities;

import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Tag implements Serializable {

    /**
     * id : 4
     * tagId : 5
     * tagName : 综艺
     * isIndexShow : 0
     */

    private int id;
    private int tagId;
    private String tagName;
    private int isIndexShow;


    public static String getTagText(List<Tag> tags) {
        if (StrUtil.isEmpty(tags)) return "";
        String tagText = "";
        for (Tag tag : tags) {
            tagText += "#" + tag.getTagName() + " ";
        }
        return tagText.trim();
    }
}
