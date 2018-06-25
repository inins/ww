package com.frame.component.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaoinstan on 2017/8/11.
 */

public class ListUtil {

    //调用list的get方法，处理越界和空问题，如果越界或者空返回null
    public static <T> T get(List<T> list, int position) {
        if (list == null) return null;
        if (position < 0) return null;
        if (list.size() > position) {
            return list.get(position);
        } else {
            return null;
        }
    }

    //取列表最后一个，如果越界或者空返回null
    public static <T> T getLast(List<T> list) {
        return get(list, list.size() - 1);
    }

    //取列表前n个，如果越界或者空返回null
    public static <T> List<T> getFirst(List<T> list, int n) {
        if (list == null) return null;
        if (list.size() <= n) {
            return list;
        } else {
            //如果list实现了序列化，截取出来的sublist是没有序列化的，一定要注意这种情况
            List<T> subList = list.subList(0, n);
            ArrayList<T> rets = new ArrayList<>();
            rets.addAll(subList);
            return rets;
        }
    }

    //移除一个项，自动验空和越界
    public static void remove(List list, int position) {
        if (list == null) return;
        if (position < 0) return;
        if (list.size() > position) {
            list.remove(position);
        } else {
            return;
        }
    }

    //把一个List列表转换为ArrayList
    public static <T> ArrayList<T> transArrayList(List<T> fromList) {
        if (fromList instanceof ArrayList) {
            return (ArrayList<T>) fromList;
        } else {
            ArrayList<T> toList = new ArrayList<>();
            for (T t : fromList) {
                toList.add(t);
            }
            return toList;
        }
    }
}
