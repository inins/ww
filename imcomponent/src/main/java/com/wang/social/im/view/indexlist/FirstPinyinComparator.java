package com.wang.social.im.view.indexlist;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-11 17:14
 * ============================================
 */
public class FirstPinyinComparator<T extends IndexableEntity> implements Comparator<EntityWrapper<T>> {
    @Override
    public int compare(EntityWrapper<T> o1, EntityWrapper<T> o2) {
        String lhsIndexName = o1.getIndexByField();
        String rhsIndexName = o2.getIndexByField();
        if (lhsIndexName == null) {
            lhsIndexName = "";
        }
        if (rhsIndexName == null) {
            rhsIndexName = "";
        }
        return compareIndexName(lhsIndexName, rhsIndexName);
    }

    private int compareIndexName(String lhs, String rhs) {
        int index = 0;
        String lhsWord = getWord(lhs, index);
        String rhsWord = getWord(rhs, index);
        return lhsWord.compareTo(rhsWord);
    }

    @NonNull
    private String getWord(String indexName, int index) {
        if (indexName.length() < (index + 1)) return "";
        String firstWord;
        if (PinyinUtil.matchingPolyphone(indexName)) {
            firstWord = PinyinUtil.getPingYin(PinyinUtil.getPolyphoneRealHanzi(indexName).substring(index, index + 1));
        } else {
            firstWord = PinyinUtil.getPingYin(indexName.substring(index, index + 1));
        }
        return firstWord;
    }
}
