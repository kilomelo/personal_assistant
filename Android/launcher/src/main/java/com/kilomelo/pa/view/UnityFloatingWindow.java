package com.kilomelo.pa.view;

import android.app.Application;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hjq.xtoast.XToast;
import com.kilomelo.pa.DebugUtils;
import com.kilomelo.pa.R;
import com.kilomelo.pa.unitybridge.UnityBridge;
import com.unity3d.player.UnityPlayer;

public class UnityFloatingWindow extends XToast {
    private static String TAG = UnityFloatingWindow.class.getSimpleName();

    enum State
    {
        collapsed,
        expanded,
    }

    private UFWDraggable mDraggable;
    private UnityPlayer mUnityPlayer;
    private int mCollapseWidth = 200;
    private int mCollapseHeight = 200;
    private int mExpandWidth = 400;
    private int mExpandHeight = 600;
    private State mState;

    public UnityFloatingWindow(Application application, UnityPlayer unityPlayer) {
        super(application);
        DebugUtils.methodLog();
        setContentView(R.layout.window_unity_float);
        Log.d(TAG, "set window format to RGB_8888");
        WindowManager.LayoutParams layoutParams = getWindowParams();
        layoutParams.format = PixelFormat.RGBA_8888;
        setWindowParams(layoutParams);
        setGravity(Gravity.TOP | Gravity.START);
        // 设置指定的拖拽规则
        mDraggable = new UFWDraggable();
        mDraggable.deserializeLocation();
        setDraggable(mDraggable);

        mUnityPlayer = unityPlayer;
        mState = State.collapsed;

        UnityBridge.getInstance().register("expand", this::expand);
        UnityBridge.getInstance().register("collapse", this::collapse);
    }

    @Override
    public void show() {
        super.show();
        DebugUtils.methodLog();
        if (null == mUnityPlayer) {
            Log.e(TAG, "unity player not assign");
            return;
        }
        if (null != mDraggable) mDraggable.relocate();

        setSize(mCollapseWidth, mCollapseHeight);

        mUnityPlayer.pause();
        if(mUnityPlayer.getParent() != null)
        {
            Log.d(TAG, "remove unity player from parent, parent: " + mUnityPlayer.getParent().toString());
            ((ViewGroup)mUnityPlayer.getParent()).removeView(mUnityPlayer);
        }
        ViewGroup decorView = (ViewGroup)getDecorView();
        if (null == decorView)
        {
            Log.e(TAG, "decorView of xtoast is null");
        }
        else decorView.addView(mUnityPlayer);

        mUnityPlayer.resume();
        mUnityPlayer.windowFocusChanged(true);
    }

    @Override
    public void cancel() {
        Log.w(TAG, "do not call cancel on unity floating window, call feeUnity instead");
    }

    // 释放unity到另一个view
    public void freeUnity(ViewGroup vg) {
        DebugUtils.methodLog("vg: " + vg);
        mUnityPlayer.pause();
        if(mUnityPlayer.getParent() != null)
        {
            Log.d(TAG, "remove unity player from parent, parent: " + mUnityPlayer.getParent().toString());
            ((ViewGroup)mUnityPlayer.getParent()).removeView(mUnityPlayer);
        }
        vg.addView(mUnityPlayer);
        mUnityPlayer.resume();
        super.cancel();
//        mUnityPlayer.windowFocusChanged(true);
    }

    public void setSize(int x, int y)
    {
        DebugUtils.methodLog("x: " + x + " y: " + y);
//        if (null != mUnityPlayer) {
//            mUnityPlayer.pause();
//        }
        setWidth(x);
        setHeight(y);
        postUpdate();
//        if (null != mUnityPlayer) {
//            mUnityPlayer.resume();
//        }
    }
    //region business

    public void setConfig(int collapseWidth, int collapseHeight, int expandWidth, int expandHeight)
    {
        DebugUtils.methodLog("collapseWidth: " + collapseWidth + " collapseHeight: " + collapseHeight + " expandWidth: " + expandWidth + " expandHeight: " + expandHeight);
        mCollapseWidth = collapseWidth;
        mCollapseHeight = collapseHeight;
        mExpandWidth = expandWidth;
        mExpandHeight = expandHeight;
    }

    private String expand(String params)
    {
        DebugUtils.methodLog();
        mState = State.expanded;
        setSize(mExpandWidth, mExpandHeight);
        return UnityBridge.COMMON_SUCCEEDED;
    }
    private String collapse(String params)
    {
        DebugUtils.methodLog();
        mState = State.collapsed;
        setSize(mCollapseWidth, mCollapseHeight);
        return UnityBridge.COMMON_SUCCEEDED;
    }
    //endregion
}
