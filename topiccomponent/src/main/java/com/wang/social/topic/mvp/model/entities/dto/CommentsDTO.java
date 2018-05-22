package com.wang.social.topic.mvp.model.entities.dto;

import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;
import com.wang.social.topic.mvp.model.entities.Comment;
import com.wang.social.topic.mvp.model.entities.Comments;

import java.util.ArrayList;
import java.util.List;

public class CommentsDTO implements Mapper<Comments> {
//    private int total;
//    private int size;
//    private int pages;
//    private int current;
    private Integer count;
    List<Comment> list;

    @Override
    public Comments transform() {
        Comments object = new Comments();

//        object.setTotal(total);
//        object.setSize(size);
//        object.setPages(pages);
//        object.setCurrent(current);
        object.setCount(EntitiesUtil.assertNotNull(count));
        object.setList(null == list ? new ArrayList<>() : list);

        return object;
    }
}
