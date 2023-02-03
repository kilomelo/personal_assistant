package com.kilomelo.pa;

import android.util.Log;

public class DebugUtils {
    // 打印函数调用日志开关
    public static boolean DEBUG_DEF_METHOD_LOG = true;
    // 打印函数调用日志
    public static void MethodLog() {
        if (!DEBUG_DEF_METHOD_LOG) return;
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        String fullClassName = ste.getClassName();
        Log.d(fullClassName.substring(fullClassName.lastIndexOf('.') + 1), ste.getMethodName());
    }
}