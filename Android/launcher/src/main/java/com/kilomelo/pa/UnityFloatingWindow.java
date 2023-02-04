package com.kilomelo.pa;

import android.app.Application;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hjq.xtoast.XToast;
import com.unity3d.player.UnityPlayer;

public class UnityFloatingWindow extends XToast {
    private static String TAG = UnityFloatingWindow.class.getSimpleName();
    public UnityFloatingWindow(Application application) {
        super(application);
        DebugUtils.MethodLog();
        Log.d(TAG, "set window format to RGB_8888");
        WindowManager.LayoutParams layoutParams = getWindowParams();
        layoutParams.format = PixelFormat.RGBA_8888;
        setWindowParams(layoutParams);
    }

    public UnityFloatingWindow bindUnityPlayer(UnityPlayer unityPlayer)
    {
        DebugUtils.MethodLog();
        ViewGroup decorView = (ViewGroup)getDecorView();
        if (null == decorView)
        {
            Log.e(TAG, "decorView of xtoast is null");
            return this;
        }
        decorView.addView(unityPlayer);
        return this;
    }
}
