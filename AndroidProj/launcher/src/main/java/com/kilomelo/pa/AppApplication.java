package com.kilomelo.pa;

import android.app.Application;
import android.util.Log;

//import com.hjq.toast.ToastUtils;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/XToast
 *    time   : 2021/01/24
 *    desc   : 应用入口
 */
public final class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("AppApplication", "onCreate");
//        ToastUtils.init(this);
    }
}