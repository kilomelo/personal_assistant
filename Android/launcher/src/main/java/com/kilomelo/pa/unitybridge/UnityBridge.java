package com.kilomelo.pa.unitybridge;

import android.util.Log;

import com.kilomelo.pa.DebugUtils;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.function.Function;

public class UnityBridge {
    private static String TAG = UnityBridge.class.getSimpleName();
    private static UnityBridge instance;
    public static UnityBridge getInstance()
    {
        if (null == instance) instance = new UnityBridge();
        return instance;
    }
    private Dictionary callbackDic;
    public void register(String name, Function<String, String> func)
    {
        Log.d(TAG, "register, name: " + name);
        if (null == callbackDic) callbackDic = new Hashtable();
        callbackDic.put(name, func);
    }

    public void unregister(String name)
    {
        Log.d(TAG, "unregister, name: " + name);
        if (null == callbackDic) return;
        callbackDic.remove(name);
    }
    public void callFromUnity(String methodName, String params)
    {
        DebugUtils.MethodLog();
        if (null == methodName)
        {
            Log.e(TAG, "callFromUnity with null methodName");
            return;
        }
        Function<String, String> method = (Function<String, String>)callbackDic.get(methodName);
        if (null != method)
        {
//            method.
        }
    }
}
