package com.frame.component.entities.dto;

import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-07 9:43
 * ============================================
 */
public class TagDTO implements Mapper<Tag>{

    private int tagId;
    private String tagName;

    @Override
    public Tag transform() {
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setTagName(tagName == null ? "" : tagName);
        return tag;
    }
}