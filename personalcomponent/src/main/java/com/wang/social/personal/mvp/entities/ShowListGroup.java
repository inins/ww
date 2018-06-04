package com.wang.social.personal.mvp.entities;

import com.frame.component.entities.BaseSelectBean;
import com.frame.component.entities.dto.GroupBeanDTO;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by Administrator on 2018/3/30.
 */

@Data
public class ShowListGroup extends BaseSelectBean implements Serializable {

    private int id;
    private String title;

    public ShowListGroup() {
    }

    public ShowListGroup(String title) {
        this.title = title;
    }

    public static ShowListGroup fromGroupBeanDTO(GroupBeanDTO dto) {
        ShowListGroup showListGroup = new ShowListGroup();
        showListGroup.setTitle(dto.getGroupName());
        showListGroup.setId(dto.getId());
        return showListGroup;
    }

    public static List<ShowListGroup> fromGroupBeanDTOList(List<GroupBeanDTO> list) {
        List<ShowListGroup> groupList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (GroupBeanDTO bean : list) {
                groupList.add(fromGroupBeanDTO(bean));
            }
        }
        return groupList;
    }

    public static void setSelectList(List<ShowListGroup> groupList, List<Integer> ids) {
        if (StrUtil.isEmpty(groupList) || StrUtil.isEmpty(ids)) return;
        for (ShowListGroup group : groupList) {
            group.setSelect(isGroupInIds(group, ids));
        }
    }

    private static boolean isGroupInIds(ShowListGroup group, List<Integer> ids) {
        if (StrUtil.isEmpty(ids)) return false;
        for (int id : ids) {
            if (group.id == id) {
                return true;
            }
        }
        return false;
    }
}
