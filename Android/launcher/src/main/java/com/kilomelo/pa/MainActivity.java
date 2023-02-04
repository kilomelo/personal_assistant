package com.kilomelo.pa;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.hjq.toast.ToastUtils;
import com.hjq.xtoast.XToast;
import com.hjq.xtoast.draggable.MovingDraggable;
import com.hjq.xtoast.draggable.SpringDraggable;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import com.unity3d.player.MultiWindowSupport;
import com.unity3d.player.UnityPlayer;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = MainActivity.class.getSimpleName();
    private FrameLayout fm;
    private UnityPlayer mUnityPlayer;

    private UnityFloatingWindow mUnityFloatingWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtils.MethodLog();
        setContentView(R.layout.activity_main);

        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.showUFWBtn).setOnClickListener(this);

        mUnityPlayer = new UnityPlayer(this);

        fm = findViewById(R.id.fm);
        fm.addView(mUnityPlayer);
    }

    //region life cycle callback
    @Override protected void onDestroy ()
    {
        DebugUtils.MethodLog();

        if (null != mUnityPlayer) mUnityPlayer.destroy();
        super.onDestroy();
    }

    @Override protected void onStop()
    {
        super.onStop();
        DebugUtils.MethodLog();

        if (!MultiWindowSupport.getAllowResizableWindow(this))
            return;

        if (null != mUnityPlayer) mUnityPlayer.pause();
    }

    @Override protected void onStart()
    {
        super.onStart();
        DebugUtils.MethodLog();

        if (!MultiWindowSupport.getAllowResizableWindow(this))
            return;

        if (null != mUnityPlayer) mUnityPlayer.resume();
    }

    @Override protected void onPause()
    {
        super.onPause();
        DebugUtils.MethodLog();

        if (MultiWindowSupport.getAllowResizableWindow(this))
            return;

        if (null != mUnityPlayer) mUnityPlayer.pause();
    }

    @Override protected void onResume()
    {
        super.onResume();
        DebugUtils.MethodLog();

        if (MultiWindowSupport.getAllowResizableWindow(this))
            return;

        if (null != mUnityPlayer) mUnityPlayer.resume();
    }

    @Override public void onLowMemory()
    {
        super.onLowMemory();
        DebugUtils.MethodLog();

        if (null != mUnityPlayer) mUnityPlayer.lowMemory();
    }

    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        DebugUtils.MethodLog();

        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            if (null != mUnityPlayer) mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        DebugUtils.MethodLog();

        if (null != mUnityPlayer) mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        DebugUtils.MethodLog();

        if (null != mUnityPlayer) mUnityPlayer.windowFocusChanged(hasFocus);
    }
    //endregion

    @Override
    public void onClick(View view) {
        DebugUtils.MethodLog();

        int viewId = view.getId();
        if (viewId == R.id.button2) {
            Log.i(TAG, "button2 clicked");
            if (null != mUnityPlayer) mUnityPlayer.UnitySendMessage("AndroidBridge", "TestSetLabelText", "text from android");
        }
        else if (viewId == R.id.button)
        {
            Log.i(TAG, "button clicked");
            testXDialogCase(view);
        }
        else if (viewId == R.id.showUFWBtn)
        {
            Log.i(TAG, "show unity float window button clicked");
            startUnityGlobalFloatingWindow(getApplication());
        }
    }
    public void stopUnityGlobalFloatingWindow() {
        DebugUtils.MethodLog();
        if (null == mUnityFloatingWindow) {
            return;
        }
        if (null == mUnityPlayer) {
            return;
        }
        mUnityPlayer.pause();

        if(mUnityPlayer.getParent() != null)
        {
            Log.d(TAG, "remove unity player from parent, parent: " + mUnityPlayer.getParent().toString());
            ((ViewGroup)mUnityPlayer.getParent()).removeView(mUnityPlayer);
        }

        fm = findViewById(R.id.fm);
        fm.addView(mUnityPlayer);
        mUnityFloatingWindow.cancel();
        mUnityPlayer.resume();
    }

    public void startUnityGlobalFloatingWindow(Application application) {
        DebugUtils.MethodLog();
        mUnityFloatingWindow = new UnityFloatingWindow(application);
        // 传入 Application 表示这个是一个全局的 Toast
        mUnityFloatingWindow.setContentView(R.layout.window_hint)
                .setGravity(Gravity.END | Gravity.BOTTOM)
//                .setYOffset(200)
//                .setText(android.R.id.message, "Unity全局浮窗")
                // 设置指定的拖拽规则
                .setDraggable(new MovingDraggable())
                .setOnClickListener(android.R.id.icon, new XToast.OnClickListener<ImageView>() {

                    @Override
                    public void onClick(XToast<?> toast, ImageView view) {
                        ToastUtils.show("我被点击了");
//                        toast.cancel();
                    }
                });
        mUnityPlayer.pause();
        if(mUnityPlayer.getParent() != null)
        {
            Log.d(TAG, "remove unity player from parent, parent: " + mUnityPlayer.getParent().toString());
            ((ViewGroup)mUnityPlayer.getParent()).removeView(mUnityPlayer);
        }
        ViewGroup decorView = (ViewGroup)mUnityFloatingWindow.getDecorView();
        if (null == decorView)
        {
            Log.e(TAG, "decorView of xtoast is null");
        }
        else decorView.addView(mUnityPlayer);
        mUnityPlayer.resume();
        mUnityFloatingWindow.setHeight(400);
        mUnityFloatingWindow.setWidth(400);

        mUnityFloatingWindow.show();

    }

    //region test case
    static int idx = 0;
    private void testXDialogCase(View v){

        Log.d(TAG, "testXToastCase, idx: " + idx);
        switch (idx)
        {
            case 0:
                new XToast<>(this)
                        .setDuration(1000)
                        .setContentView(R.layout.window_hint)
                        .setAnimStyle(R.style.TopAnimStyle)
                        .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                        .setText(android.R.id.message, "这个动画是不是很骚")
                        .show();
                break;
            case 1:
                new XToast<>(this)
                        .setDuration(1000)
                        .setContentView(R.layout.window_hint)
                        .setAnimStyle(R.style.IOSAnimStyle)
                        .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_error)
                        .setText(android.R.id.message, "一秒后自动消失")
                        .show();
                break;
            case 2:
                new XToast<>(this)
                        .setContentView(R.layout.window_hint)
                        .setAnimStyle(R.style.IOSAnimStyle)
                        .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                        .setText(android.R.id.message, "点我消失")
                        // 设置外层是否能被触摸
                        .setOutsideTouchable(false)
                        // 设置窗口背景阴影强度
                        .setBackgroundDimAmount(0.5f)
                        .setOnClickListener(android.R.id.message, new XToast.OnClickListener<TextView>() {

                            @Override
                            public void onClick(XToast<?> toast, TextView view) {
                                toast.cancel();
                            }
                        })
                        .show();
                break;
            case 3:
                new XToast<>(this)
                        .setDuration(3000)
                        .setContentView(R.layout.window_hint)
                        .setAnimStyle(R.style.IOSAnimStyle)
                        .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_warning)
                        .setText(android.R.id.message, "请注意下方 Snackbar")
                        .setOnToastLifecycle(new XToast.OnLifecycle() {

                            @Override
                            public void onShow(XToast<?> toast) {
                                Snackbar.make(getWindow().getDecorView(), "显示回调", Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDismiss(XToast<?> toast) {
                                Snackbar.make(getWindow().getDecorView(), "消失回调", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case 4:
                new XToast<>(this)
                        .setContentView(R.layout.window_hint)
                        .setAnimStyle(R.style.IOSAnimStyle)
                        .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                        .setText(android.R.id.message, "点我点我点我")
                        .setOnClickListener(android.R.id.message, new XToast.OnClickListener<TextView>() {

                            @Override
                            public void onClick(final XToast<?> toast, TextView view) {
                                view.setText("不错，很听话");
                                toast.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 1000);
                            }
                        })
                        .show();
                break;
            case 5:
                new XToast<>(this)
                        .setContentView(R.layout.window_hint)
                        .setAnimStyle(R.style.RightAnimStyle)
                        .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                        .setDuration(2000)
                        .setText(android.R.id.message, "位置算得准不准")
                        .setOnClickListener(android.R.id.message, new XToast.OnClickListener<TextView>() {

                            @Override
                            public void onClick(final XToast<?> toast, TextView view) {
                                toast.cancel();
                            }
                        })
                        .showAsDropDown(v, Gravity.BOTTOM);
                break;
            case 6:
                new XToast<>(this)
                        .setContentView(R.layout.window_input)
                        .setAnimStyle(R.style.BottomAnimStyle)
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                        .setOnClickListener(R.id.tv_window_close, new XToast.OnClickListener<TextView>() {

                            @Override
                            public void onClick(final XToast<?> toast, TextView view) {
                                toast.cancel();
                            }
                        })
                        .show();
                break;
            case 7:
                new XToast<>(this)
                        .setContentView(R.layout.window_hint)
                        .setAnimStyle(R.style.IOSAnimStyle)
                        .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_finish)
                        .setText(android.R.id.message, "点我消失")
                        // 设置成可拖拽的
                        .setDraggable(new MovingDraggable())
                        .setOnClickListener(android.R.id.message, new XToast.OnClickListener<TextView>() {

                            @Override
                            public void onClick(XToast<?> toast, TextView view) {
                                toast.cancel();
                            }
                        })
                        .show();
                break;
            case 8:
                XXPermissions.with(this)
                        .permission(Permission.SYSTEM_ALERT_WINDOW)
                        .request(new OnPermissionCallback() {

                            @Override
                            public void onGranted(List<String> granted, boolean all) {
                                showGlobalWindow(getApplication());
                            }

                            @Override
                            public void onDenied(List<String> denied, boolean never) {
                                new XToast<>(MainActivity.this)
                                        .setDuration(1000)
                                        .setContentView(R.layout.window_hint)
                                        .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_error)
                                        .setText(android.R.id.message, "请先授予悬浮窗权限")
                                        .show();
                            }
                        });
                break;
            case 9:
                new XToast<>(this)
                        .setDuration(1000)
                        // 将 ToastUtils 中的 View 转移给 XToast 来显示
                        .setContentView(ToastUtils.getStyle().createView(this))
                        .setAnimStyle(R.style.ScaleAnimStyle)
                        .setText(android.R.id.message, "就问你溜不溜")
                        .setGravity(Gravity.BOTTOM)
                        .setYOffset(100)
                        .show();
                break;
        }
        idx++;
        if (idx >= 10) idx = 0;
    }

    /**
     * 显示全局弹窗
     */
    public static void showGlobalWindow(Application application) {
        // 传入 Application 表示这个是一个全局的 Toast
        new XToast<>(application)
                .setContentView(R.layout.window_hint)
                .setGravity(Gravity.END | Gravity.BOTTOM)
                .setYOffset(200)
                .setText(android.R.id.message, "全局弹窗")
                // 设置指定的拖拽规则
                .setDraggable(new SpringDraggable())
                .setOnClickListener(android.R.id.icon, new XToast.OnClickListener<ImageView>() {

                    @Override
                    public void onClick(XToast<?> toast, ImageView view) {
                        ToastUtils.show("我被点击了");
                        toast.cancel();
                        // 点击后跳转到拨打电话界面
                        // Intent intent = new Intent(Intent.ACTION_DIAL);
                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // toast.startActivity(intent);
                        // 安卓 10 在后台跳转 Activity 需要额外适配
                        // https://developer.android.google.cn/about/versions/10/privacy/changes#background-activity-starts
                    }
                })
                .show();
    }

    public void testMethod(String param)
    {
        Log.i(TAG, "testMethod, param: " + param);
    }
    public static void testMethodStatic(String param)
    {
        Log.i(TAG, "testMethodStatic, param: " + param);
    }
    public String testString = "orig test string";
    public static String testStaticString = "orig test static string";
    //endregion
    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
//    @Override public boolean dispatchKeyEvent(KeyEvent event)
//    {
//        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
//            return mUnityPlayer.injectEvent(event);
//        return super.dispatchKeyEvent(event);
//    }
//
//    // Pass any events not handled by (unfocused) views straight to UnityPlayer
//    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
//    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
//    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
//    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}