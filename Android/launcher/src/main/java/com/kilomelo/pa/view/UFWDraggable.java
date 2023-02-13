package com.kilomelo.pa.view;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hjq.xtoast.draggable.SpringDraggable;
import com.kilomelo.pa.DebugUtils;
import com.kilomelo.pa.persistentdata.PersistentData;

public class UFWDraggable extends SpringDraggable {
    private static String TAG = UFWDraggable.class.getSimpleName();
    private static int mX;
    private static int mY;
    @Override
    protected void updateLocation(int x, int y) {
        super.updateLocation(x, y);
        mX = x;
        mY = y;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                serializeLocation();
                break;
            default:
                break;
        }
        return super.onTouch(v, event);
    }

    public void relocate()
    {
        DebugUtils.methodLog();
        updateLocation(mX, mY);
    }
    public void deserializeLocation()
    {
        DebugUtils.methodLog();
        SharedPreferences sharedPreferences = PersistentData.getInstance().getSharedPreferences();
        if (null != sharedPreferences)
        {
            mX = sharedPreferences.getInt("x", 0);
            mY = sharedPreferences.getInt("y", 0);
            Log.d(TAG, "deserialize Location from sharedPreferences, x: " + mX + " y: " + mY);
        }
    }
    public void serializeLocation()
    {
        DebugUtils.methodLog();
        SharedPreferences sharedPreferences = PersistentData.getInstance().getSharedPreferences();
        if (null != sharedPreferences)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (null != editor) {
                Log.d(TAG, "serialize Location to sharedPreferences, x: " + mX + " y: " + mY);
                editor.putInt("x", mX);
                editor.putInt("y", mY);
                editor.commit();
            }
        }
    }
}
