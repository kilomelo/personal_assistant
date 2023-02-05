package com.kilomelo.pa;

import android.app.Application;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hjq.xtoast.XToast;
import com.unity3d.player.UnityPlayer;

public class UnityFloatingWindow extends XToast {
    private static String TAG = UnityFloatingWindow.class.getSimpleName();
    private UnityPlayer mUnityPlayer;


    public UnityFloatingWindow(Application application) {
        super(application);
        DebugUtils.MethodLog();
        Log.d(TAG, "set window format to RGB_8888");
        WindowManager.LayoutParams layoutParams = getWindowParams();
        layoutParams.format = PixelFormat.RGBA_8888;
        setWindowParams(layoutParams);

        mUnityPlayer = new UnityPlayer(application);

        bindUnityPlayer(mUnityPlayer);
    }

    public UnityPlayer getUnityPlayer()
    {
        return mUnityPlayer;
    }

    private UnityFloatingWindow bindUnityPlayer(UnityPlayer unityPlayer)
    {
        DebugUtils.MethodLog();

        if (null == unityPlayer)
        {
            Log.e(TAG, "bind unity player failed, unity player is null");
            return this;
        }
        mUnityPlayer = unityPlayer;

//        mUnityPlayer.pause();
//        if(mUnityPlayer.getParent() != null)
//        {
//            Log.d(TAG, "remove unity player from parent, parent: " + unityPlayer.getParent().toString());
//            ((ViewGroup)mUnityPlayer.getParent()).removeView(mUnityPlayer);
//        }
        ViewGroup decorView = (ViewGroup)getDecorView();
        if (null == decorView)
        {
            Log.e(TAG, "decorView of UnityFloatingWindow is null");
        }
        else decorView.addView(mUnityPlayer);
//        mUnityPlayer.resume();
        setHeight(400);
        setWidth(400);
//        decorView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(TAG, "unity view touched");
//                mUnityPlayer.injectEvent(event);
//                return false;
//            }
//        });
        return this;
    }
}
