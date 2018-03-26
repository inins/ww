package com.frame.integration;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.Message;
import android.os.Process;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by Bo on 2018-03-14.
 */
@Singleton
public class AppManager {

    protected final String TAG = this.getClass().getSimpleName();
    //当为true时表示Activity不需要加入到Activity容器中进行统一管理(默认false)
    public static final String IS_NOT_ADD_ACTIVITY_LIST = "is_not_add_activity_list";
    public static final String APP_MANAGER_MESSAGE = "app_manager_message";
    public static final int START_ACTIVITY = 1000;
    public static final int SHOW_SNACKBAR = 1001;
    public static final int KIALL_ALL = 1002;
    public static final int APP_EXIT = 1003;

    @Inject
    Application mApplication;
    //管理所有存活的 Activity (保存顺序为创建顺序，可能跟任务栈中的顺序不一致)
    private List<Activity> mActivityList;
    //当前在前台的Activity
    private Activity mCurrentActivity;
    //提供给外部扩展 onReceive 方法
    private HandleListener mHandleListener;

    @Inject
    public AppManager() {
    }

    @Inject
    void init() {
        EventBus.getDefault().register(this);
    }

    /**
     * 通过{@link EventBus}事件，远程控制执行对应方法
     *
     * @param message
     */
    @Subscriber(tag = APP_MANAGER_MESSAGE, mode = ThreadMode.MAIN)
    public void onReceive(Message message) {
        switch (message.what) {
            case START_ACTIVITY:
                if (message.obj != null) {
                    dispatchStart(message);
                }
                break;
            case SHOW_SNACKBAR:
                if (message.obj != null) {
                    showSnackbar(((String) message.obj), message.arg1 != 0);
                }
                break;
            case KIALL_ALL:
                killAll();
                break;
            case APP_EXIT:
                exitApp();
                break;
            default:
                Timber.tag(TAG).w("The message.what not match!");
                break;
        }
        if (mHandleListener != null){
            mHandleListener.handleMessage(this, message);
        }
    }

    /**
     * 通过此方法遥控{@link AppManager},让{@link #onReceive(Message)}执行对应方法
     *
     * @param message
     */
    public static void post(Message message) {
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    /**
     * 让前台的{@link Activity},使用{@link android.support.design.widget.Snackbar} 显示内容
     *
     * @param message
     * @param isLong
     */
    private void showSnackbar(String message, boolean isLong) {
        if (getCurrentActivity() == null) {
            Timber.tag(TAG).w("mCurrentActivity == null when showSnackbar()");
            return;
        }
        View view = getCurrentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, message, isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
    }

    /**
     * 提供给外部扩展{@link AppManager#onReceive(Message)}方法
     *
     * @param mHandleListener
     */
    public void setmHandleListener(HandleListener mHandleListener) {
        this.mHandleListener = mHandleListener;
    }

    /**
     * 根据{@code message} 中 {@code obj}的内容打开指定页面
     *
     * @param message
     */
    private void dispatchStart(Message message) {
        if (message.obj instanceof Intent) {
            startActivity((Intent) message.obj);
        } else if (message.obj instanceof Class) {
            startActivity((Class) message.obj);
        }
    }

    /**
     * 返回一个存储未销毁的{@link Activity} 的集合
     *
     * @return
     */
    public List<Activity> getActivityList() {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        return mActivityList;
    }

    /**
     * 添加{@link Activity}到集合
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        synchronized (AppManager.class) {
            List<Activity> activities = getActivityList();
            if (!activities.contains(activity)) {
                activities.add(activity);
            }
        }
    }

    /**
     * 删除集合中指定的{@link Activity}
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when removeActivity()");
            return;
        }
        synchronized (AppManager.class) {
            if (mActivityList.contains(activity)) {
                mActivityList.remove(activity);
            }
        }
    }

    /**
     * 关闭指定{@link Activity} class 的所有实例
     *
     * @param activityClass
     */
    public void killActivity(Class<?> activityClass) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when killActivity()");
            return;
        }
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                if (next.getClass().equals(activityClass)) {
                    iterator.remove();
                    next.finish();
                }
            }
        }
    }

    /**
     * 检索指定{@link Activity} 实例是否存活
     *
     * @param activity
     * @return
     */
    public boolean activityInstanceIsLive(Activity activity) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when activityInstanceIsLive()");
            return false;
        }
        return mActivityList.contains(activity);
    }

    /**
     * 检索指定{@link Activity} class 是否存活
     *
     * @param activityClass
     * @return
     */
    public boolean activityClassIsLive(Class<?> activityClass) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when activityClassIsLive()");
            return false;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定{@link Activity} class 的实例, 没有则返回 null(同一个{@link Activity} class 可能存在多个实例， 返回最初创建的实例)
     *
     * @param activityClass
     * @return
     */
    public Activity findActivity(Class<?> activityClass) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when findActivity()");
            return null;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 获取最近启动的一个{@link Activity}
     * <p>
     * Tips: mActivityList 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     *
     * @return
     */
    public Activity getTopActivity() {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when getTopActivity()");
            return null;
        }
        return mActivityList.size() > 0 ? mActivityList.get(mActivityList.size() - 1) : null;
    }

    /**
     * 将在前台的 {@link Activity} 赋值给 {@code currentActivity}, 注意此方法是在 {@link Activity#onResume} 方法执行时将栈顶的 {@link Activity} 赋值给 {@code currentActivity}
     * 所以在栈顶的 {@link Activity} 执行 {@link Activity#onCreate} 方法时使用 {@link #getCurrentActivity()} 获取的就不是当前栈顶的 {@link Activity}, 可能是上一个 {@link Activity}
     * 如果在 App 启动第一个 {@link Activity} 执行 {@link Activity#onCreate} 方法时使用 {@link #getCurrentActivity()} 则会出现返回为 {@code null} 的情况
     * 想避免这种情况请使用 {@link #getTopActivity()}
     *
     * @param currentActivity
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    /**
     * 获取在前台的{@link Activity}
     * 保证获取到的{@link Activity}正处于可见状态（即未调用{@link Activity#onStop()},所以当此{@link Activity}调用{@link Activity#onStop()}方法之后
     * 没有其他的{@link Activity}回到前台，则可能返回null）
     * <p>
     * Example usage:
     * 使用场景比较适合, 只需要在可见状态的 {@link Activity} 上执行的操作
     * 如当后台 {@link Service} 执行某个任务时, 需要让前台 {@link Activity} ,做出某种响应操作或其他操作,如弹出 {@link Dialog}, 这时在 {@link Service} 中就可以使用 {@link #getCurrentActivity()}
     * 如果返回为 {@code null}, 说明没有前台 {@link Activity} (用户返回桌面或者打开了其他 App 会出现此状况), 则不做任何操作, 不为 {@code null}, 则弹出 {@link Dialog}
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * 关闭所有{@link Activity}
     */
    public void killAll() {
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                iterator.remove();
                next.finish();
            }
        }
    }

    /**
     * 关闭除指定的{@link Activity} class 外的所有{@link Activity}
     *
     * @param excludeActivityClasses
     */
    public void killAll(Class<?>... excludeActivityClasses) {
        List<Class<?>> excludeList = Arrays.asList(excludeActivityClasses);
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                if (excludeList.contains(next.getClass()))
                    continue;
                iterator.remove();
                next.finish();
            }
        }
    }

    /**
     * 关闭所有 {@link Activity},排除指定的 {@link Activity}
     *
     * @param excludeActivityName {@link Activity} 的完整全路径
     */
    public void killAll(String... excludeActivityName) {
        List<String> excludeList = Arrays.asList(excludeActivityName);
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();

                if (excludeList.contains(next.getClass().getName()))
                    continue;

                iterator.remove();
                next.finish();
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            killAll();
            Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 让栈顶的{@link android.app.Activity} 打开指定的{@link android.app.Activity}
     *
     * @param intent
     */
    public void startActivity(Intent intent) {
        if (getTopActivity() == null) {
            Timber.tag(TAG).w("mCurrentActivity == null when startActivity(Intent)");
            //当前没有前台Activity， 使用new_task模式启动Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mApplication.startActivity(intent);
            return;
        }
        getTopActivity().startActivity(intent);
    }

    /**
     * 让栈顶的{@link android.app.Activity} 打开指定的{@link android.app.Activity}
     *
     * @param activityClass
     */
    public void startActivity(Class activityClass) {
        startActivity(new Intent(mApplication, activityClass));
    }

    /**
     * 释放资源
     */
    public void release() {
        EventBus.getDefault().unregister(this);
        if (mActivityList != null) {
            mActivityList.clear();
        }
        mActivityList = null;
        mHandleListener = null;
        mCurrentActivity = null;
        mApplication = null;
    }

    /**
     * 提供一个回掉监听， 让外部扩展新事件
     */
    public interface HandleListener {
        void handleMessage(AppManager appManager, Message message);
    }
}
