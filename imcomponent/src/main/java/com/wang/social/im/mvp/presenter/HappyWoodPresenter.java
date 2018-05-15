package com.wang.social.im.mvp.presenter;

import android.graphics.PointF;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.SizeUtils;
import com.wang.social.im.mvp.contract.HappyWoodContract;
import com.wang.social.im.mvp.model.entities.ShareInfo;
import com.wang.social.im.mvp.model.entities.ShareModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 17:35
 * ============================================
 */
@ActivityScope
public class HappyWoodPresenter extends BasePresenter<HappyWoodContract.Model, HappyWoodContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public HappyWoodPresenter(HappyWoodContract.Model model, HappyWoodContract.View view) {
        super(model, view);
    }

    public void getShareInfo(String type, String id) {
        mApiHelper.execute(mRootView, mModel.getShareInfo(type, id),
                new ErrorHandleSubscriber<ShareInfo>() {
                    @Override
                    public void onNext(ShareInfo shareInfo) {
                        mRootView.showShareInfo(shareInfo);
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });
    }

    public void getTreeData(String type, String id) {
        mApiHelper.execute(mRootView, mModel.getTreeData(type, id),
                new ErrorHandleSubscriber<ShareModel>() {
                    @Override
                    public void onNext(ShareModel model) {
                        List<ShareModel> models = new ArrayList<ShareModel>();
                        models.add(model);
                        getList(models, -1);
                        sort();
                        mRootView.showTree(list, levmap, width, maxle, startCenter);
                    }
                });
    }

    public int le = 1;
    public int maxle = 1;
    public int width = 0;
    private int SIZE = SizeUtils.dp2px(60);
    private int INTERVAL = SizeUtils.dp2px(40);
    private int LEFTINTERVAL = SizeUtils.dp2px(15);
    private PointF startCenter;

    public void sort() {
        for (int i = 1; i <= levmap.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getLeven() == i) {
                    currNum++;
                    list.get(j).setCurrNum(currNum);
                }

            }
        }
        Collections.sort(list);
        for (int i = 1; i <= levmap.size(); i++) {
            int index = 0;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getLeven() == i) {
                    index++;
                    list.get(j).setLineIndex(index);
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : levmap.entrySet()) {
            width = Math.max(entry.getValue(), width);
        }
        int temp = 0;
        int start = 0;
        for (int i = 0; i < list.size(); i++, start++) {
            if (temp != list.get(i).getLeven()) {
                start = 0;
                temp = list.get(i).getLeven();
            }
            int left = start * SIZE + LEFTINTERVAL * start + (width - levmap.get(temp)) * SIZE / 2;
            list.get(i).setLayout(left, INTERVAL * temp + SIZE * temp, left + SIZE, temp * INTERVAL + SIZE * (temp + 1));
        }
        for (int k = 0; k < /*levmap.size()*/2; k++) {
            for (int i = 0; i < list.size(); i++) {
                ShareModel shareModel = null;
                if (list.get(i).getSuperid() != -1) {
                    shareModel = getSuper(list.get(i).getSuperid());
                }
                if (shareModel != null && shareModel.getLeft() < list.get(i).getLeft() && shareModel.getNodes().get(0) == list.get(i)) {
                    shareModel.setLayout(list.get(i).getLeft(), shareModel.getTop(), list.get(i).getLeft() + SIZE, shareModel.getBottom());//移动当前父View位置
                    int currLe = shareModel.getLeven();
                    int startIndex = 0;
                    for (int j = 0; j < list.size(); j++) {//将所有父View后面的View移动
                        if (list.get(j).getLeven() == currLe && list.get(j).getCurrNum() > shareModel.getCurrNum()) {
                            startIndex++;
                            list.get(j).setLayout(startIndex * LEFTINTERVAL + startIndex * SIZE + list.get(i).getLeft(), shareModel.getTop(), startIndex * LEFTINTERVAL + startIndex * SIZE + list.get(i).getLeft() + SIZE, shareModel.getBottom());
                        }
                    }
                }
                if (list.get(i).getIsSelf() == 1) {
                    startCenter = new PointF(list.get(i).getLeft(), list.get(i).getTop());
                }
            }
        }
    }

    public ShareModel getSuper(int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                return list.get(i);
            }
        }
        return null;
    }

    public Map<Integer, Integer> levmap = new HashMap<>();
    public List<ShareModel> list = new ArrayList<>();
    public int currNum;
    public int id;

    public void getList(List<ShareModel> shareModel, int ids) {
        for (int i = 0; i < shareModel.size(); i++) {
            id++;
            shareModel.get(i).setLeven(le);
            shareModel.get(i).setId(id);
            shareModel.get(i).setSuperid(ids);
            List<ShareModel> nodes = shareModel.get(i).getNodes();
            //shareModel.get(i).setNodes(null);
            list.add(shareModel.get(i));
            if (nodes != null) {
                System.out.println("目前深度" + le);
                le++;
                maxle = Math.max(maxle, le);
                getList(nodes, shareModel.get(i).getId());
                shareModel.get(i).setChildNum(nodes.size());
                le--;
            } else {
                System.out.println("目前深度" + le);
            }
            if (levmap.containsKey(le)) {
                levmap.put(le, levmap.get(le) + 1);
                width = Math.max(levmap.get(le), width);
            } else {
                levmap.put(le, 1);
            }
            System.out.println("当前数据：" + shareModel.get(i).getHeaderImage());
        }
    }
}
