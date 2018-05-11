package com.frame.component.ui.acticity.PersonalCard.model.entities.DTO;

import com.frame.component.ui.acticity.PersonalCard.model.entities.EntitiesUtil;
import com.frame.component.ui.acticity.PersonalCard.model.entities.TalkBean;
import com.frame.component.ui.acticity.PersonalCard.model.entities.TalkBeanList;
import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TalkBeanListDTO implements Mapper<TalkBeanList> {
    private Integer current;
    private Integer total;
    private Integer pages;
    private Integer size;
    private List<TalkBean> list;

    @Override
    public TalkBeanList transform() {
        TalkBeanList object = new TalkBeanList();

        object.setCurrent(EntitiesUtil.assertNotNull(current));
        object.setTotal(EntitiesUtil.assertNotNull(total));
        object.setPages(EntitiesUtil.assertNotNull(pages));
        object.setSize(EntitiesUtil.assertNotNull(size));
        object.setList(null == list ? new ArrayList<>() : list);

        return object;
    }
}
