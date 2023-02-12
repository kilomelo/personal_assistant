package com.kilomelo.pa.view;

import android.app.Application;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.hjq.xtoast.XToast;
import com.kilomelo.pa.DebugUtils;

public class UnityFloatingWindow extends XToast {
    private static String TAG = UnityFloatingWindow.class.getSimpleName();

    private UFWDraggable mDraggable;
    public UnityFloatingWindow(Application application) {
        super(application);
        DebugUtils.MethodLog();
        Log.d(TAG, "set window format to RGB_8888");
        WindowManager.LayoutParams layoutParams = getWindowParams();
        layoutParams.format = PixelFormat.RGBA_8888;
        setWindowParams(layoutParams);
        setGravity(Gravity.TOP | Gravity.START);
        // 设置指定的拖拽规则
        mDraggable = new UFWDraggable();
        mDraggable.deserializeLocation();
        setDraggable(mDraggable);
    }

    @Override
    public void show() {
        super.show();
        DebugUtils.MethodLog();
        if (null != mDraggable) mDraggable.relocate();
    }

    //    public UnityFloatingWindow bindUnityPlayer(UnityPlayer unityPlayer)
//    {
//        DebugUtils.MethodLog();
//        ViewGroup decorView = (ViewGroup)getDecorView();
//        if (null == decorView)
//        {
//            Log.e(TAG, "decorView of xtoast is null");
//            return this;
//        }
//        decorView.addView(unityPlayer);
//        return this;
//    }
}
