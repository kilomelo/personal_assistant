using UnityEngine;
using System;
using LitJson;

namespace Framework
{

    public class AndroidBridge : MonoBehaviour
    {
        private static string TAG = "AndroidBridge";
        private static string GameObjectName = "AndroidBridge";

        private static AndroidBridge _instance;

        public static AndroidBridge Instance
        {
            get
            {
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

        private AndroidJavaObject _unityBridgeObj;

        public void Register(string name, Action<string> method)
        {

        }

        public void Unregister(string name)
        {

        }

        // 同步调用java方法
        public string CallSync(string methodName, params object[] args)
        {
            Debug.Log($"{TAG} CallSync, method: {methodName}");
            return _callSync(methodName, false, args);
        }

        // 同步调用java方法，在ui线程执行
        public string CallSyncOnUiThread(string methodName, params object[] args)
        {
            Debug.Log($"{TAG} CallSyncOnUiThread, method: {methodName}");
            return _callSync(methodName, true, args);
        }

        private string _callSync(string methodName, bool runOnUiThread, params object[] args)
        {
            if (string.IsNullOrEmpty(methodName))
            {
                Debug.LogError($"{TAG} CallSync args invalid, code 0");
                return null;
            }

            if (args.Length % 2 != 0)
            {
                Debug.LogError($"{TAG} CallSync args invalid, code 1");
                return null;
            }
#if UNITY_ANDROID && !UNITY_EDITOR
        if (null == _unityBridgeObj) TryGetUnityBridge();
        if (null != _unityBridgeObj)
#endif
            {
                var jsonData = new JsonData();
                for (var i = 0; i < args.Length; i += 2)
                {
                    if (args[i].GetType() != typeof(string))
                    {
                        Debug.LogError($"{TAG} CallSync args invalid, code 2");
                        return null;
                    }

                    Debug.Log($"{TAG} argName: {args[i]}, type: {args[i + 1].GetType()}, value: {args[i + 1]}");
                    if (jsonData.ContainsKey((string)args[i]))
                    {
                        Debug.LogError($"{TAG} CallSync params name dumplicated, name: {args[i]}");
                        continue;
                    }
                    if (args[i + 1] is float)
                    {
                        var fValuae = (float)args[i + 1];
                        jsonData[(string)args[i]] = new JsonData(fValuae);
                    }
                    else
                    {
                        jsonData[(string)args[i]] = new JsonData(args[i + 1]);
                    }
                }

                Debug.Log($"{TAG} CallSync, jsonData: {jsonData.ToJson()}");
#if UNITY_ANDROID && !UNITY_EDITOR
            var javaMethodName = runOnUiThread ? "callFromUnitySyncOnUiThread" : "callFromUnitySync";
            var returnValue = _unityBridgeObj.Call<string>(javaMethodName, methodName, jsonData.ToJson());
            Debug.Log($"{TAG} CallSync, return {returnValue}");
            return returnValue;
#endif
            }
            Debug.LogError($"{TAG} CallSync failed.");
            return null;
        }

        private void TryGetUnityBridge()
        {
            try
            {
                using var unityBridgeClass = new AndroidJavaClass("com.kilomelo.pa.unitybridge.UnityBridge");
                _unityBridgeObj = unityBridgeClass.CallStatic<AndroidJavaObject>("getInstance");
            }
            catch (Exception e)
            {
                Debug.LogError($"{TAG} get unity bridge instance failed.");
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
}