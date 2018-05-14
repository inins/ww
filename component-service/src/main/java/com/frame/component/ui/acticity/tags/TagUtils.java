package com.frame.component.ui.acticity.tags;

import android.text.TextUtils;

import java.util.List;

import timber.log.Timber;

public class TagUtils {

    public static String formatTagIds(List<Tag> list) {
        String tagIds = "";

        for (int i = 0; i < list.size(); i++) {
            Tag tag = list.get(i);
            Timber.i(tag.getId() + " " + tag.getTagName());
            tagIds = tagIds + tag.getId();
            if (i < list.size() - 1) {
                tagIds = tagIds + ",";
            }
        }

        return tagIds;
    }

    public static String formatTagNames(List<Tag> list) {
        String tagNames = "";

        for (int i = 0; i < list.size(); i++) {
            Tag tag = list.get(i);
            Timber.i(tag.getId() + " " + tag.getTagName());
            tagNames = tagNames + " #" + tag.getTagName();
            if (i < list.size() - 1) {
                tagNames = tagNames + ",";
            }
        }

        return tagNames;
    }
}
