package com.wang.social.im.mvp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.frame.base.BaseFragment;
import com.frame.component.entities.NewMoneyTreeGame;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.enums.ConversationType;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.GuidePageHelper;
import com.frame.component.helper.sound.AudioPlayManager;
import com.frame.component.helper.sound.AudioRecordManager;
import com.frame.component.ui.dialog.DialogLoading;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.moneytree.DialogCreateGame;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMImageType;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupManagerExt;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerConversationComponent;
import com.wang.social.im.di.modules.ConversationModule;
import com.wang.social.im.enums.ConnectionStatus;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.enums.MessageScope;
import com.wang.social.im.enums.MessageType;
import com.wang.social.im.helper.GroupHelper;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.contract.ConversationContract;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.model.entities.GameElemData;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.ShadowInfo;
import com.wang.social.im.mvp.model.entities.ShareElemData;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.presenter.ConversationPresenter;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;
import com.wang.social.im.mvp.ui.adapters.holders.BaseMessageViewHolder;
import com.wang.social.im.view.IMInputView;
import com.wang.social.im.view.plugin.PluginModule;
import com.wang.social.im.widget.EnvelopDialog;
import com.wang.social.im.widget.MessageHandlePopup;
import com.wang.social.im.widget.TeamFunPointPopup;
import com.wang.social.location.mvp.model.entities.LocationInfo;
import com.wang.social.location.mvp.ui.LocationActivity;
import com.wang.social.location.mvp.ui.LocationShowActivity;
import com.wang.social.pictureselector.ActivityPicturePreview;
import com.wang.social.pictureselector.PictureSelector;
import com.wang.social.pictureselector.helper.PhotoHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.app.hubert.guide.model.HighLight.Shape.OVAL;
import static com.frame.entities.EventBean.EVENT_NOTIFY_FRIEND_PROFILE;
import static com.frame.entities.EventBean.EVENT_NOTIFY_GROUP_MEMBER_PROFILE;

/**
 * ======================================
 * 会话界面
 * <p>
 * Create by ChenJing on 2018-04-03 15:14
 * ======================================
 */
public class ConversationFragment extends BaseFragment<ConversationPresenter> implements ConversationContract.View, IMInputView.IInputViewListener, BaseMessageViewHolder.OnHandleListener {

    //图片选择
    private static final int REQUEST_SELECT_PICTURE = 1000;
    //发红包
    private static final int REQUEST_CREATE_ENVELOP = 1001;
    //位置选择
    private static final int REQUEST_CREATE_LOCATION = 1002;
    //@用户
    private static final int REQUEST_ALERT_USER = 1003;

    @BindView(R2.id.fc_message_list)
    RecyclerView fcMessageList;
    @BindView(R2.id.fc_input)
    IMInputView fcInput;
    @BindView(R2.id.fc_fun_point)
    ImageView fcFunPoint;
    @BindView(R2.id.fc_message_loader)
    SpringView fcMessageLoader;

    @Inject
    Gson mGson;

    private WeakReference<DialogLoading> mLoading;

    private ConversationType mConversationType;
    private String mTargetId;

    private PhotoHelper mPhotoHelper;

    private MessageListAdapter mAdapter;
    private TIMConversation mConversation;
    private LinearLayoutManager mLayoutManager;

    private float mTouchDownY;
    private float mVoiceLastTouchY;
    private boolean mUpDirection;
    private float mOffsetLimit;

    private Funpoint mFunPoint;

    public static ConversationFragment newInstance(ConversationType conversationType, String targetId) {
        Bundle args = new Bundle();
        args.putInt("type", conversationType.ordinal());
        args.putString("targetId", targetId);
        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOffsetLimit = SizeUtils.dp2px(70);

        mLoading = new WeakReference<>(new DialogLoading(getActivity()));

        AudioRecordManager.getInstance().setRecordListener(new RecordListener());
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerConversationComponent.builder()
                .appComponent(appComponent)
                .conversationModule(new ConversationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_conversation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mConversationType = ConversationType.values()[getArguments().getInt("type")];
        mTargetId = getArguments().getString("targetId");

        TIMConversationType timConversationType;
        if (mConversationType == ConversationType.PRIVATE) {
            timConversationType = TIMConversationType.C2C;
        } else if (mConversationType == ConversationType.SOCIAL || mConversationType == ConversationType.TEAM || mConversationType == ConversationType.MIRROR) {
            timConversationType = TIMConversationType.Group;
        } else {
            throw new IllegalArgumentException("Unknown conversation type!");
        }

        distinctInit();

        mConversation = TIMManager.getInstance().getConversation(timConversationType, mTargetId);
        mPresenter.setConversationType(mConversationType);
        mPresenter.setConversation(mConversation);

        fcInput.setMInputViewListener(this);

        fcInput.setConversationType(mConversationType);
        mLayoutManager = new LinearLayoutManager(mActivity);
        fcMessageList.setLayoutManager(mLayoutManager);
        ((SimpleItemAnimator) fcMessageList.getItemAnimator()).setSupportsChangeAnimations(false);
        fcMessageList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int itemSpace = getResources().getDimensionPixelSize(R.dimen.im_msg_item_space);
                outRect.top = itemSpace;
                outRect.bottom = itemSpace;
            }
        });
        mAdapter = new MessageListAdapter(mTargetId, mConversationType);
        mAdapter.setHandleListener(this);
        fcMessageList.setAdapter(mAdapter);

        mPresenter.setAdapter(mAdapter);

        mPresenter.getHistoryMessage();

        setListener();

        if (mConversationType == ConversationType.TEAM) {
            fcFunPoint.setVisibility(View.VISIBLE);

            mPresenter.getFunPoint(ImHelper.imId2WangId(mTargetId));
            mPresenter.getTeamInfo(ImHelper.imId2WangId(mTargetId));

            NewbieGuide.with(this)
                    .setLabel("guide_team_point")
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(getParentFragment().getView().findViewById(R.id.toolbar_iv_right), OVAL, 0, 0)
                            .addHighLight(getView().findViewById(R.id.fc_fun_point), OVAL, 0, 0)
                            .setLayoutRes(R.layout.lay_guide_findchat, R.id.btn_go)
                            .setEverywhereCancelable(false)
                            .setEnterAnimation(GuidePageHelper.getEnterAnimation())
                            .setExitAnimation(GuidePageHelper.getExitAnimation()))
                    .show();
        } else if (mConversationType == ConversationType.SOCIAL) {
            mPresenter.getShadowInfo(ImHelper.imId2WangId(mTargetId));
        } else if (mConversationType == ConversationType.MIRROR) {
            mPresenter.getAnonymousInfo();
        }
    }

    private void distinctInit() {
        switch (mConversationType) {
            case MIRROR:
                fcInput.setBackgroundResource(R.drawable.im_bg_message_input_mirror);
                break;
            case TEAM:
                fcInput.setBackgroundResource(R.drawable.im_bg_message_input_team);
                break;
            default:
                fcInput.setBackgroundResource(R.drawable.im_bg_message_input);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        fcMessageLoader.setEnableFooter(false);
        fcMessageLoader.setHeader(new AliHeader(getContext()));

        fcMessageLoader.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHistoryMessage();
            }

            @Override
            public void onLoadmore() {

            }
        });
        fcMessageList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mTouchDownY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE && event.getY() - mTouchDownY > SizeUtils.dp2px(50)) {
                    fcInput.collapse();
                }
                return false;
            }
        });
        fcInput.getEditText().setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    if (source.equals("@")) {
                        AlertUserListActivity.start(getActivity(), REQUEST_ALERT_USER, ImHelper.imId2WangId(mTargetId));
                    }
                    return source;
                }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftInput(fcInput.getEditText());

        AudioPlayManager.getInstance().stopPlay();

        mPresenter.readMessages();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConversation = null;
        mConversationType = null;
        mAdapter = null;
        mLayoutManager = null;
        mTargetId = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPhotoHelper != null) {
            mPhotoHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PICTURE: //图片选择
                    String[] list = data.getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);
                    mPresenter.sendImageMessage(list, true);
                    break;
                case REQUEST_CREATE_ENVELOP://红包
                    long envelopId = data.getLongExtra("envelopId", 0L);
                    String message = data.getStringExtra("message");
                    mPresenter.sendEnvelopMessage(envelopId, message);
                    break;
                case REQUEST_CREATE_LOCATION://位置
                    LocationInfo locationInfo = (LocationInfo) data.getSerializableExtra(LocationActivity.RESULT_EXTRA_KEY);
                    mPresenter.sendLocationMessage(locationInfo);
                    break;
                case REQUEST_ALERT_USER://@用户
                    MemberInfo memberInfo = data.getParcelableExtra(AlertUserListActivity.EXTRA_MEMBER);
                    if (memberInfo != null) {
                        insertAlert(memberInfo.getNickname(), false);
                    }
                    break;
            }
        }
    }

    @OnClick(R2.id.fc_fun_point)
    public void onViewClicked() {
        TeamFunPointPopup window = new TeamFunPointPopup(getContext(), mFunPoint == null ? null : mFunPoint.getImgUrl(), mFunPoint == null ? null : mFunPoint.getNewsTitle(), new TeamFunPointPopup.OnMoreClickListener() {
            @Override
            public void onMoreClick() {
                TeamFunPointActivity.start(getContext(), ImHelper.imId2WangId(mTargetId), mPresenter.getTeamTag());
            }
        });
        window.showAsDropDown(fcFunPoint, -SizeUtils.dp2px(192), -SizeUtils.dp2px(169));
    }

    @Override
    public void showLoading() {
        if (mLoading.get() == null) {
            mLoading = new WeakReference<>(new DialogLoading(getActivity()));
        }
        mLoading.get().show();
    }

    @Override
    public void hideLoading() {
        if (mLoading.get() != null) {
            mLoading.get().dismiss();
        }
    }

    @Override
    public void showMessages(List<UIMessage> uiMessages) {
        boolean needScroll = true;
        int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
        if (mAdapter.getData() != null && mAdapter.getItemCount() - lastVisiblePosition > 3) {
            needScroll = false;
        }
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            mAdapter.refreshData(uiMessages);
        } else {
            mAdapter.addItem(uiMessages);
        }
        //如果最新消息没有显示在可见区域则不进行滚动
        if (needScroll) {
            fcMessageList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void showMessage(UIMessage uiMessage) {
        if (mAdapter.getData() == null) {
            List<UIMessage> list = new ArrayList<>();
            list.add(uiMessage);
            mAdapter.refreshData(list);
        } else {
            mAdapter.addItem(uiMessage);
        }

        fcMessageList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void insertMessages(List<UIMessage> uiMessages) {
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            mAdapter.refreshData(uiMessages);

            fcMessageList.scrollToPosition(mAdapter.getData().size() - 1);
        } else {
            mAdapter.getData().addAll(0, uiMessages);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshMessage(UIMessage uiMessage) {
        int position = mAdapter.findPosition(uiMessage);
        if (position != -1) {
            int startPosition = mLayoutManager.findFirstVisibleItemPosition();
            int endPosition = mLayoutManager.findLastVisibleItemPosition();
            if (position >= startPosition && position <= endPosition) {
                mAdapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void showEnvelopDialog(UIMessage uiMessage, EnvelopInfo envelopInfo) {
        //如果已经领取了红包则直接进入详情
        if (envelopInfo.getGotDiamond() > 0) {
            EnvelopDetailActivity.start(getActivity(), envelopInfo);
        } else {
            EnvelopDialog envelopDialog = new EnvelopDialog(getActivity(), uiMessage, envelopInfo);
            envelopDialog.show();
        }
    }

    @Override
    public void showFunPoint(Funpoint funpoint) {
        mFunPoint = funpoint;
    }

    @Override
    public void onShadowChanged(ShadowInfo shadowInfo) {
        fcInput.updateShadowText(shadowInfo.getStatus() == ShadowInfo.STATUS_OPEN ? R.string.im_chat_input_plugin_shadow_close : R.string.im_chat_input_plugin_shadow);
    }

    @Override
    public void hideMessageLoad() {
        fcMessageLoader.onFinishFreshAndLoadDelay();
    }

    @Override
    public void showCreateGameDialog() {
        DialogCreateGame.createFromGroup(this, getChildFragmentManager(), Integer.parseInt(ImHelper.imId2WangId(mTargetId)), new DialogCreateGame.CreateGameCallback() {
            @Override
            public boolean onCreateSuccess(NewMoneyTreeGame newMoneyTreeGame) {
                return true;
            }

            @Override
            public void onPayCreateGameSuccess(int roomId, int diamond) {
                mPresenter.sendGameMessage(String.valueOf(roomId), diamond);
                CommonHelper.GameHelper.startGameRoom(getContext(), roomId);
            }
        });
    }

    @Override
    public void gotoGameRoom(String roomId) {
        CommonHelper.GameHelper.startGameRoom(getContext(), Integer.valueOf(roomId));
    }

    @Override
    public void onPluginClick(PluginModule pluginModule) {
        switch (pluginModule.getPluginType()) {
            case IMAGE: //图片选择
                PictureSelector.from(ConversationFragment.this)
                        .maxSelection(9)
                        .forResult(REQUEST_SELECT_PICTURE);
                break;
            case SHOOT: //拍摄
                new RxPermissions(getActivity())
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    if (mPhotoHelper == null) {
                                        mPhotoHelper = new PhotoHelper(getActivity(), new PhotoHelper.OnPhotoCallback() {
                                            @Override
                                            public void onResult(String path) {
                                                mPresenter.sendImageMessage(new String[]{path}, false);
                                            }
                                        });
                                        mPhotoHelper.setClip(false);
                                    }
                                    mPhotoHelper.startCamera();
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    ToastUtil.showToastShort("请打开相机权限");
                                }
                            }
                        });
                break;
            case REDPACKET: //红包
                switch (mConversationType) {
                    case PRIVATE:
                        CreateSingleEnvelopActivity.start(getActivity(), REQUEST_CREATE_ENVELOP);
                        break;
                    case TEAM:
                    case SOCIAL:
                        int memberNum = GroupHelper.getInstance().getGroupProfile(mTargetId).getMemberNum();
                        CreateMultiEnvelopActivity.start(getActivity(), mTargetId, memberNum, REQUEST_CREATE_ENVELOP);
                        break;
                }
                break;
            case LOCATION:
                new RxPermissions(getActivity())
                        .requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    Intent intent = new Intent(getActivity(), LocationActivity.class);
                                    startActivityForResult(intent, REQUEST_CREATE_LOCATION);
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    ToastUtil.showToastShort(UIUtil.getString(com.wang.social.location.R.string.loc_toast_open_location_permission));
                                }
                            }
                        });
                break;
            case SHADOW:
                if (mPresenter.getShadowInfo(ImHelper.imId2WangId(mTargetId)) == null) {
                    ShadowInfo shadowInfo = new ShadowInfo();
                    shadowInfo.setSocialId(ImHelper.imId2WangId(mTargetId));
                    ShadowSettingActivity.start(getContext(), shadowInfo);
                } else {
                    mPresenter.updateShadowStatus(ImHelper.imId2WangId(mTargetId));
                }
                break;
            case GAME:
                mPresenter.checkHasGame(ImHelper.imId2WangId(mTargetId));
                break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onVoiceInputTouch(View view, MotionEvent event) {
        new RxPermissions(getActivity())
                .requestEach(Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                AudioRecordManager.getInstance().startRecord(view);
                                mVoiceLastTouchY = event.getY();
                                mUpDirection = false;
                                ((Button) view).setText(R.string.im_voice_input_hover);
                            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                if (mVoiceLastTouchY - event.getY() > mOffsetLimit && !mUpDirection) {
                                    AudioRecordManager.getInstance().willCancelRecord();
                                    mUpDirection = true;
                                    ((Button) view).setText(R.string.im_voice_input);
                                } else if (event.getY() - mVoiceLastTouchY > -mOffsetLimit && mUpDirection) {
                                    AudioRecordManager.getInstance().continueRecord();
                                    mUpDirection = false;
                                    ((Button) view).setText(R.string.im_voice_input_hover);
                                }
                            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                                AudioRecordManager.getInstance().stopRecord();
                                ((Button) view).setText(R.string.im_voice_input);
                            }
                        }
                    }
                });
    }

    @Override
    public void onEmotionClick(String codeName, String showName) {
        mPresenter.sendFaceMessage(Integer.parseInt(codeName));
    }

    @Override
    public void onSendClick(String content) {
        mPresenter.sendTextMessage(content);
    }

    @Override
    public void onInputViewExpanded() {
        int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
        if (mAdapter.getItemCount() - lastVisiblePosition < 5) {
            fcMessageList.scrollToPosition(mAdapter.getData().size() - 1);
        }
    }

    @Override
    public void onInputViewCollapsed() {
    }

    @Override
    public void onErrorClick(int position, UIMessage uiMessage) {
        //将此条消息重新发送
        mConversation.sendMessage(uiMessage.getTimMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContentClick(View view, UIMessage uiMessage, int position) {
        switch (uiMessage.getMessageType()) {
            case IMAGE:
                List<String> images = new ArrayList<>();
                int currentIndex = 0;
                for (UIMessage message : mAdapter.getData()) {
                    if (message.getMessageType() == MessageType.IMAGE) {
                        TIMImageElem imageElem = (TIMImageElem) message.getMessageElem(TIMImageElem.class);
                        if (imageElem != null) {
                            for (TIMImage image : imageElem.getImageList()) {
                                if (image.getType() == TIMImageType.Original) {
                                    images.add(image.getUrl());
                                    break;
                                }
                            }
                        }
                        if (message == uiMessage && images.size() > 0) {
                            currentIndex = images.size() - 1;
                        }
                    }
                }
                String[] imageArr = new String[images.size()];
                ActivityPicturePreview.startBrowse(getActivity(), currentIndex, images.toArray(imageArr));
                break;
            case LOCATION:
                TIMLocationElem locationElem = (TIMLocationElem) uiMessage.getMessageElem(TIMLocationElem.class);
                LocationShowActivity.start(getActivity(), locationElem.getLatitude(), locationElem.getLongitude());
                break;
            case RED_ENVELOP:
                mPresenter.getEnvelopInfo(uiMessage);
                break;
            case GAME_TREE:
                GameElemData gameElem = (GameElemData) uiMessage.getCustomMessageElemData(CustomElemType.GAME, GameElemData.class, mGson);
                CommonHelper.GameHelper.startGameRoom(getContext(), Integer.parseInt(gameElem.getRoomId()));
                break;
            case SHARE:
                //获取自定义消息类型
                TIMCustomElem customElem = (TIMCustomElem) uiMessage.getTimMessage().getElement(0);
                CustomElemType elemType = CustomElemType.getElemType(customElem);
                if (elemType == null) {
                    return;
                }
                ShareElemData elemData = (ShareElemData) uiMessage.getCustomMessageElemData(elemType, ShareElemData.class, mGson);
                switch (elemType) {
                    case SHARE_TOPIC:
                        CommonHelper.TopicHelper.startTopicDetail(getContext(), Integer.valueOf(elemData.getObjectId()));
                        break;
                    case SHARE_FUN_SHOW:
                        CommonHelper.FunshowHelper.startDetailActivity(getContext(), Integer.valueOf(elemData.getObjectId()));
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onContentLongClick(View view, UIMessage uiMessage, int position) {
        boolean showTop = false;
        if (position > mLayoutManager.findFirstVisibleItemPosition()) {
            showTop = true;
        }
        MessageHandlePopup handlePopup = new MessageHandlePopup(mActivity, uiMessage.getTimMessage().isSelf(), uiMessage, showTop);
        handlePopup.setHandleListener(new MessageHandlePopup.OnHandleListener() {
            @Override
            public void onDelete(UIMessage uiMessage) {
                mPresenter.deleteMessage(uiMessage);
            }

            @Override
            public void onWithdraw(UIMessage uiMessage) {
                mPresenter.withDrawMessage(uiMessage);
            }
        });

        int y = 0;
        if (showTop) {
            y = -(view.getHeight() + SizeUtils.dp2px(18));
        }
        int x = view.getWidth() / 2;
        if (uiMessage.getTimMessage().isSelf()) {
            x = x - getResources().getDimensionPixelOffset(R.dimen.im_msg_handle_popup_width) / 2;
        } else {
            x = x - getResources().getDimensionPixelOffset(R.dimen.im_msg_handle_popup_width_narrow) / 2;
        }
        handlePopup.showAsDropDown(view, x, y);
    }

    @Override
    public void onPortraitClick(View view, UIMessage uiMessage, int position) {

    }

    @Override
    public void onPortraitLongClick(View view, UIMessage uiMessage, int position) {
        if (mConversationType == ConversationType.TEAM ||
                mConversationType == ConversationType.SOCIAL) {
            String nickname = uiMessage.getNickname(mConversationType);
            insertAlert(TextUtils.isEmpty(nickname) ? " " : nickname, true);
        }
    }

    private void insertAlert(String nickname, boolean addAlert) {
        EditText editText = fcInput.getEditText();
        if (editText != null) {
            Editable editable = editText.getText();
            String alertMessage;
            if (addAlert) {
                alertMessage = "@" + nickname;
            } else {
                alertMessage = "" + nickname;
            }
            editable.insert(editText.getSelectionStart(), alertMessage);
        }
    }

    /**
     * 刷新用户信息
     *
     * @param identity
     * @param nickname
     * @param portrait
     */
    private void refreshUserInfo(String identity, String nickname, String portrait) {
        if (mAdapter != null && mAdapter.getData() != null) {
            for (UIMessage uiMessage : mAdapter.getData()) {
                if (uiMessage.getTimMessage().getSender().equals(identity) && uiMessage.getMessageScope() == MessageScope.NORMAL) {
                    uiMessage.setNickname(nickname);
                    uiMessage.setPortrait(portrait);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EVENT_NOTIFY_GROUP_MEMBER_PROFILE) {
            String groupId = (String) event.get("groupId");
            if (mAdapter != null && groupId.equals(mTargetId)) {
                refreshUserInfo((String) event.get("identity"), (String) event.get("nickname"), (String) event.get("portrait"));
            }
        } else if (event.getEvent() == EVENT_NOTIFY_FRIEND_PROFILE) {
            String friendId = (String) event.get("identity");
            if (mAdapter != null && friendId.equals(mTargetId)) {
                refreshUserInfo((String) event.get("identity"), (String) event.get("nickname"), (String) event.get("portrait"));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageRevoke(TIMMessageLocator messageLocator) {
        if (mAdapter == null) {
            return;
        }
        for (UIMessage uiMessage : mAdapter.getData()) {
            TIMMessageExt messageExt = new TIMMessageExt(uiMessage.getTimMessage());
            if (messageExt.checkEquals(messageLocator)) {
                uiMessage.refresh();
                refreshMessage(uiMessage);
                break;
            }
        }
    }

    /**
     * 断线重连时，同步撤回消息
     *
     * @param status
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onConnectionStatusChanged(ConnectionStatus status) {
        if (mConversationType != ConversationType.PRIVATE && status == ConnectionStatus.CONNECTED) {
            TIMConversationExt ext = new TIMConversationExt(mConversation);
            ext.syncMsgRevokedNotification(null);
        }
    }

    /**
     * 消息发生改变
     *
     * @param uiMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageChanged(UIMessage uiMessage) {
        refreshMessage(uiMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageClear(EventBean eventBean) {
        if (eventBean.getEvent() == EventBean.EVENT_NOTIFY_CLEAR_MESSAGE) {//消息清空
            if (eventBean.get("groupId").equals(mTargetId)) {
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class RecordListener implements AudioRecordManager.OnRecordListener {

        private PopupWindow mRecordWindow;
        private ImageView mStateIV;
        private TextView mStateTV;
        private TextView mTimerTV;

        @Override
        public void initView(View root) {
            LayoutInflater inflater = LayoutInflater.from(root.getContext());
            View view = inflater.inflate(R.layout.im_input_voice_pop, (ViewGroup) null);
            this.mStateIV = view.findViewById(R.id.im_audio_state_image);
            this.mStateTV = view.findViewById(R.id.im_audio_state_text);
            this.mTimerTV = view.findViewById(R.id.im_audio_timer);
            this.mRecordWindow = new PopupWindow(view, -1, -1);
            this.mRecordWindow.showAtLocation(root, 17, 0, 0);
            this.mRecordWindow.setFocusable(true);
            this.mRecordWindow.setOutsideTouchable(false);
            this.mRecordWindow.setTouchable(false);
        }

        @Override
        public void onTimeOut(int counter) {
            if (this.mRecordWindow != null) {
                this.mStateIV.setVisibility(View.GONE);
                this.mStateTV.setVisibility(View.VISIBLE);
                this.mStateTV.setText(R.string.im_input_voice_rec);
                this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                this.mTimerTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onRecording() {
            if (this.mRecordWindow != null) {
                this.mStateIV.setVisibility(View.VISIBLE);
                this.mStateIV.setImageResource(R.drawable.im_volume_1);
                this.mStateTV.setVisibility(View.VISIBLE);
                this.mStateTV.setText(R.string.im_input_voice_rec);
                this.mTimerTV.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancel() {
            if (this.mRecordWindow != null) {
                this.mTimerTV.setVisibility(View.GONE);
                this.mStateIV.setVisibility(View.VISIBLE);
                this.mStateIV.setImageResource(R.drawable.im_volume_cancel);
                this.mStateTV.setVisibility(View.VISIBLE);
                this.mStateTV.setText(R.string.im_input_voice_cancel);
                this.mStateTV.setBackgroundResource(R.drawable.im_corner_voice_style);
            }
        }

        @Override
        public void onCompleted(int duration, String path) {
            TIMSoundElem soundElem = new TIMSoundElem();
            soundElem.setDuration(duration);
            soundElem.setPath(path);
            mPresenter.sendSoundMessage(soundElem);
        }

        @Override
        public void onDBChanged(int db) {
            switch (db / 5) {
                case 0:
                    this.mStateIV.setImageResource(R.drawable.im_volume_1);
                    break;
                case 1:
                    this.mStateIV.setImageResource(R.drawable.im_volume_2);
                    break;
                case 2:
                    this.mStateIV.setImageResource(R.drawable.im_volume_3);
                    break;
                case 3:
                    this.mStateIV.setImageResource(R.drawable.im_volume_4);
                    break;
                case 4:
                    this.mStateIV.setImageResource(R.drawable.im_volume_5);
                    break;
                case 5:
                    this.mStateIV.setImageResource(R.drawable.im_volume_6);
                    break;
                case 6:
                    this.mStateIV.setImageResource(R.drawable.im_volume_7);
                    break;
                default:
                    this.mStateIV.setImageResource(R.drawable.im_volume_8);
            }
        }

        @Override
        public void tooShort() {
            mStateIV.setImageResource(R.drawable.im_volume_wraning);
            mStateTV.setText(R.string.im_input_voice_short);
        }

        @Override
        public void onDestroy() {
            this.mRecordWindow.dismiss();
            this.mRecordWindow = null;
            this.mStateIV = null;
            this.mStateTV = null;
            this.mTimerTV = null;
        }
    }
}