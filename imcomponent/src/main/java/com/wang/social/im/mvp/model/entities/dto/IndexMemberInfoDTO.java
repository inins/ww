package com.wang.social.im.mvp.model.entities.dto;

import com.frame.component.entities.dto.TagDTO;
import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;
import com.frame.utils.TimeUtils;
import com.wang.social.im.enums.MessageNotifyType;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.model.entities.IndexMemberInfo;
import com.wang.social.im.mvp.model.entities.MemberInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 17:33
 * ============================================
 */
public class IndexMemberInfoDTO implements Mapper<IndexMemberInfo> {

    private String groupId;
    private String userId;
    private String memberName;
    private String memberHeadUrl;
    private int memberRole;
    private int receiveMsgType;//0全部1不提示2只提示@
    private long birthday;
    private int sex;
    private int isFriend; //0：非好友，1：好友
    private List<TagDTO> tags;

    @Override
    public IndexMemberInfo transform() {
        IndexMemberInfo memberInfo = new IndexMemberInfo();
        memberInfo.setGroupId(groupId == null ? "-1" : groupId);
        memberInfo.setMemberId(userId == null ? "-1" : userId);
        memberInfo.setNickname(memberName == null ? "" : memberName);
        memberInfo.setPortrait(memberHeadUrl == null ? "" : memberHeadUrl);
        memberInfo.setRole(memberRole == 1 ? MemberInfo.ROLE_MASTER : MemberInfo.ROLE_MEMBER);
        switch (receiveMsgType) {
            case 0:
                memberInfo.setNotifyType(MessageNotifyType.ALL);
                break;
            case 1:
                memberInfo.setNotifyType(MessageNotifyType.NONE);
                break;
            case 2:
                memberInfo.setNotifyType(MessageNotifyType.ALERT);
                break;
        }
        List<Tag> tagList = new ArrayList<>();
        if (tags != null) {
            for (TagDTO tagDTO : tags) {
                tagList.add(tagDTO.transform());
            }
        }
        memberInfo.setTags(tagList);
        memberInfo.setBirthMills(birthday);
        //计算年龄范围
        memberInfo.setAgeRange(ImHelper.getAgeRange(birthday));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(birthday);
        memberInfo.setConstellation(TimeUtils.getAstro(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
        //性别判断
        memberInfo.setGender(sex == 0 ? Gender.MALE : Gender.FEMALE);
        memberInfo.setFriendly(isFriend == 1);
        return memberInfo;
    }
}
