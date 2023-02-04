package com.kilomelo.pa;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.xtoast.XToast;
import com.unity3d.player.UnityPlayer;

public class UnityFloatWindow extends XToast {
    private static String TAG = MainActivity.class.getSimpleName();
    public UnityFloatWindow(Application application) {
        super(application);
        DebugUtils.MethodLog();
    }

    public UnityFloatWindow bindUnityPlayer(UnityPlayer unityPlayer)
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
