package com.wang.social.im.view.indexlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.wang.social.im.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YoKey on 16/10/6.
 */
class IndexBar extends View {
    private int mTotalHeight;
    private float mTextSpace;

    private List<String> mIndexList = new ArrayList<>();
    // 首字母 到 mIndexList 的映射
    private HashMap<String, Integer> mMapping = new HashMap<>();
    private ArrayList<EntityWrapper> mDatas;

    private int mSelectionPosition;
    private float mIndexHeight;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mFocusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mFocusBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public IndexBar(Context context) {
        super(context);
    }

    void init(Drawable barBg, int barTextColor, int barFocusTextColor, int barFocusBgColor, float barTextSize, float textSpace) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(barBg);
        } else {
            setBackgroundDrawable(barBg);
        }

        this.mTextSpace = textSpace;

        mPaint.setColor(barTextColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(barTextSize);

        mFocusPaint.setTextAlign(Paint.Align.CENTER);
        mFocusPaint.setTextSize(barTextSize);
        mFocusPaint.setColor(barFocusTextColor);

        mFocusBgPaint.setColor(barFocusBgColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mIndexList.size() > 0) {
            mTotalHeight = (int) (mPaint.getTextSize() * mIndexList.size() + (mIndexList.size() + 1) * mTextSpace);
        }

        if (mTotalHeight > height) {
            mTotalHeight = height;
        }

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mTotalHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIndexList.size() == 0) return;

        mIndexHeight = ((float) getHeight()) / mIndexList.size();

        for (int i = 0; i < mIndexList.size(); i++) {
            if (mSelectionPosition == i) {
                //画选中的背景
                canvas.drawCircle(getWidth() / 2,
                        mIndexHeight * 0.5f + mIndexHeight * i,
                        mIndexHeight / 2,
                        mFocusBgPaint);
                canvas.drawText(mIndexList.get(i), getWidth() / 2, mIndexHeight * 0.71f + mIndexHeight * i, mFocusPaint);
            } else {
                canvas.drawText(mIndexList.get(i), getWidth() / 2, mIndexHeight * 0.71f + mIndexHeight * i, mPaint);
            }
        }
    }

    int getPositionForPointY(float y) {
        if (mIndexList.size() <= 0) return -1;

        int position = (int) (y / mIndexHeight);

        if (position < 0) {
            position = 0;
        } else if (position > mIndexList.size() - 1) {
            position = mIndexList.size() - 1;
        }

        return position;
    }


    int getSelectionPosition() {
        return mSelectionPosition;
    }

    void setSelectionPosition(int position) {
        this.mSelectionPosition = position;
        invalidate();
    }

    int getFirstRecyclerViewPositionBySelection() {
        String index = mIndexList.get(mSelectionPosition);
        if (mMapping.containsKey(index)) {
            return mMapping.get(index);
        }
        return -1;
    }

    List<String> getIndexList() {
        return mIndexList;
    }

    void setDatas(boolean showAllLetter, ArrayList<EntityWrapper> datas) {
        this.mDatas = datas;
        this.mIndexList.clear();
        this.mMapping.clear();

        ArrayList<String> tempHeaderList = null;
        if (showAllLetter) {
            mIndexList = Arrays.asList(getResources().getStringArray(R.array.im_indexable_letter));
            mIndexList = new ArrayList<>(mIndexList);
            tempHeaderList = new ArrayList<>();
        }
        for (int i = 0; i < datas.size(); i++) {
            EntityWrapper wrapper = datas.get(i);
            if (wrapper.getItemType() == EntityWrapper.TYPE_TITLE || wrapper.getIndexTitle() == null) {
                String index = wrapper.getIndex();
                if (!TextUtils.isEmpty(index)) {
                    if (!showAllLetter) {
                        mIndexList.add(index);
                    } else {
                        if (IndexableLayout.INDEX_SIGN.equals(index)) {
                            mIndexList.add(IndexableLayout.INDEX_SIGN);
                        } else if (mIndexList.indexOf(index) < 0) {
                            if (wrapper.getHeaderFooterType() == EntityWrapper.TYPE_HEADER && tempHeaderList.indexOf(index) < 0) {
                                tempHeaderList.add(index);
                            } else if (wrapper.getHeaderFooterType() == EntityWrapper.TYPE_FOOTER) {
                                mIndexList.add(index);
                            }
                        }
                    }
                    if (!mMapping.containsKey(index)) {
                        mMapping.put(index, i);
                    }
                }
            }
        }
        if (showAllLetter) {
            mIndexList.addAll(0, tempHeaderList);
        }
        requestLayout();
    }

    void setSelection(int firstVisibleItemPosition) {
        if (mDatas == null || mDatas.size() <= firstVisibleItemPosition || firstVisibleItemPosition < 0)
            return;
        EntityWrapper wrapper = mDatas.get(firstVisibleItemPosition);
        int position = mIndexList.indexOf(wrapper.getIndex());

        if (mSelectionPosition != position && position >= 0) {
            mSelectionPosition = position;
            invalidate();
        }
    }

    String getSelectionStr() {
        for (String s : mIndexList) {
            if (mIndexList.indexOf(s) == mSelectionPosition) {
                return s;
            }
        }
        return "";
    }

    void setSelection(String indexStr) {
        for (String s : mIndexList) {
            if (s.equals(indexStr)) {
                setSelectionPosition(mIndexList.indexOf(s));
                break;
            }
        }
    }
}
