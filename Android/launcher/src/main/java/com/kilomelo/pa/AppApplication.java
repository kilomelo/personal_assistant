package com.kilomelo.pa;

import android.app.Application;
import com.hjq.toast.ToastUtils;

public final class AppApplication extends Application {
    private static String TAG = AppApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        DebugUtils.methodLog();

        ToastUtils.init(this);
    }
}