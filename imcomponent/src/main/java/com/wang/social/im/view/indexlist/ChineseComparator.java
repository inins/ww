package com.wang.social.im.view.indexlist;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-11 17:14
 * ============================================
 */
public class ChineseComparator<T extends IndexableEntity> implements Comparator<EntityWrapper<T>> {

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
        Collator collator = Collator.getInstance(Locale.CANADA);
        int flag;
        if (collator.compare(lhs, rhs) < 0) {
            flag = -1;
        } else if (collator.compare(lhs, rhs) > 0) {
            flag = 1;
        } else {
            flag = 0;
        }
        return flag;
    }
}
