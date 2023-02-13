package com.kilomelo.pa.unitybridge;

import android.text.TextUtils;
import android.util.Log;
import com.kilomelo.pa.DebugUtils;
import com.unity3d.player.UnityPlayer;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.function.Function;

public class UnityBridge {
    private static String TAG = UnityBridge.class.getSimpleName();
    private static UnityBridge mInstance;
    public static UnityBridge getInstance()
    {
        if (null == mInstance) mInstance = new UnityBridge();
        return mInstance;
    }
    private Dictionary<String, Function<String, String>> mCallbackDic;
    private UnityPlayer mUnityPlayer;

    public void init(UnityPlayer unityPlayer)
    {
        DebugUtils.methodLog();
        mUnityPlayer = unityPlayer;
    }
    public void register(String name, Function<String, String> func)
    {
        DebugUtils.methodLog("name: " + name);
        if (null == mCallbackDic) mCallbackDic = new Hashtable<String, Function<String, String>>();
        mCallbackDic.put(name, func);
    }

    public void unregister(String name)
    {
        DebugUtils.methodLog("name: " + name);
        if (null == mCallbackDic) return;
        mCallbackDic.remove(name);
    }
    private String callFromUnitySync(String methodName, String params)
    {
        DebugUtils.methodLog("methodName: " + methodName + " params: " + params);
        if (TextUtils.isEmpty(methodName))
        {
            Log.e(TAG, "callFromUnitySync with null or empty methodName");
            return null;
        }
        Function<String, String> method = mCallbackDic.get(methodName);
        if (null != method)
        {
            return method.apply(params);
        }
        else
        {
            Log.e(TAG, "method: " + methodName + " not registered.");
        }
        return null;
    }

    private String callFromUnitySyncOnUiThread(String methodName, String params)
    {
        DebugUtils.methodLog("methodName: " + methodName + " params: " + params);

        if (TextUtils.isEmpty(methodName))
        {
            Log.e(TAG, "callFromUnitySyncOnUiThread with null or empty methodName");
            return null;
        }
        Function<String, String> method = mCallbackDic.get(methodName);
        if (null != method)
        {
            if (null == mUnityPlayer)
            {
                Log.e(TAG, "unity activity is null");
                return null;
            }
            mUnityPlayer.currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    method.apply(params);
                }
            });
            return null;
        }
        else
        {
            Log.e(TAG, "method: " + methodName + " not registered.");
        }
        return null;
    }
}
