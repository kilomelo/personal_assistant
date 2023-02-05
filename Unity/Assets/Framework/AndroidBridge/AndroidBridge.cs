using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using LitJson;

public class AndroidBridge : MonoBehaviour
{
    private static string TAG = "AndroidBridge";
    private static string GameObjectName = "AndroidBridge";

    private static AndroidBridge _instance;
    public static AndroidBridge Instance
    {
        get {
            if (null == _instance)
            {
                var gobj = GameObject.Find(GameObjectName);
                if (null == gobj)
                {
                    gobj = new GameObject(GameObjectName);
                    DontDestroyOnLoad(gobj);
                }
                _instance = gobj.GetComponent<AndroidBridge>();
            }
            if (null == _instance)
            {
                Debug.LogError($"{TAG} create instance failed.");
            }
            return _instance;
        }
    }

    private AndroidJavaObject _unityPlayerJavaObject;

    public void CallSync(string methodName, params object[] args)
    {
        Debug.Log($"{TAG} CallSync, method: {methodName}");
        if (string.IsNullOrEmpty(methodName))
        {
            Debug.LogError($"{TAG} CallSync args invalid, code 0");
            return;
        }
        // if (args.Length % 2 != 0)
        // {
        //     Debug.LogError($"{TAG} CallSync args invalid, code 1");
        //     return;
        // }
        #if UNITY_ANDROID && !UNITY_EDITOR
        if (null == _unityPlayerJavaObject) TryGetUnityPlayer();
        if (null != _unityPlayerJavaObject)
        #endif
        {
            var jsonStr = JsonMapper.ToJson(args);
            // var jsonData = new JsonData();
            // jsonData.Add(methodName);
            // jsonData.Add(jsonStr);
            // Debug.Log($"{jsonData.ToString()}");
            // return;
            // var jsonData = new JsonData();
            // jsonData["methodName"] = methodName;
            // Debug.Log($"{TAG} CallSync 1, jsonData: {jsonData}");
            // for (var i = 0; i < args.Length; i += 2)
            // {
            //     if (args[i].GetType() != typeof(string))
            //     {
            //         Debug.LogError($"{TAG} CallSync args invalid, code 2");
            //         return;
            //     }
            //     Debug.Log($"{TAG} argName: {args[i]}, type: {args[i + 1].GetType()}, value: {args[i + 1]}");
            //     jsonData[(string)args[i]] = new JsonData(args[i + 1]);
            //     // jsonData[(string)args[i]] = args[i + 1];
            // }
            // Debug.Log($"{TAG} CallSync, jsonData: {jsonData}");
            #if UNITY_ANDROID && !UNITY_EDITOR
            _unityPlayerJavaObject.Call("callFromUnitySync", methodName, jsonStr);
            #endif
        }
    }

    private void TryGetUnityPlayer()
    {
        try {
            using var unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            _unityPlayerJavaObject = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        }
        catch (Exception e) {
            Debug.LogError($"{TAG} get unity player instance failed.");
            Debug.LogError(e);
        }
    }

    

    #region life cycle
    // Start is called before the first frame update
    void Start()
    {
        Debug.Log($"{TAG} started");
    }

    void OnApplicationFocus(bool hasFocus)
    {
        Debug.Log($"{TAG} on application focus, hasFocus: {hasFocus}");
    }

    void OnApplicationPause(bool pauseStatus)
    {
        Debug.Log($"{TAG} on application pause, pauseStatus: {pauseStatus}");
    }
    #endregion
}
