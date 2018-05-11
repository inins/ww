package com.frame.component.ui.acticity.PersonalCard.ui.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserStatistics;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class TabViewProvider implements SmartTabLayout.TabProvider {

    Context mContext;
    UserStatistics mUserStatistics;

    private static String[] titles = { "好友", "趣聊", "趣晒", "话题" };

    public TabViewProvider(Context context, UserStatistics userStatistics) {
        mContext = context.getApplicationContext();
        mUserStatistics = userStatistics;
    }

    private int getNumber(int position) {
        if (null == mUserStatistics) return 0;

        switch (position % 4) {
            case 0:
                // 好友总数
                return mUserStatistics.getFriendCount();
            case 1:
                // 趣聊总数
                return mUserStatistics.getGroupCount();
            case 2:
                // 趣晒总数
                return mUserStatistics.getTalkCount();
            case 3:
                // 话题总数
                return mUserStatistics.getTopicCount();
        }

        return 0;
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.personal_card_tab_layout, container, false);
        TextView numTV = view.findViewById(R.id.number_text_view);
        numTV.setText(Integer.toString(getNumber(position)));

        TextView nameTV = view.findViewById(R.id.name_text_view);
        nameTV.setText(titles[position % titles.length]);

        return view;
    }
}
