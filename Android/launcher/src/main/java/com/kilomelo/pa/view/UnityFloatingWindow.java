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

    private UFWDraggable mDraggable;
    private UnityPlayer mUnityPlayer;
    public UnityFloatingWindow(Application application, UnityPlayer unityPlayer) {
        super(application);
        DebugUtils.methodLog();
        setContentView(R.layout.window_unity_float);
        setWidth(400);
        setHeight(400);
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

        UnityBridge.getInstance().register("syncSettings", this::syncSettings);
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
//        mUnityPlayer.windowFocusChanged(true);
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

    //region business
    private String syncSettings(String params)
    {
        DebugUtils.methodLog("params: " + params);
        return null;
    }
    //endregion
}
