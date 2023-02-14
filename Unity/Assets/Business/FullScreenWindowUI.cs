using System;
using Framework;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

namespace pANDa
{

    public class FullScreenWindowUI : MonoBehaviour
    {
        private static string TAG = typeof(FullScreenWindowUI).ToString();

        [SerializeField] private Button btn1;
        [SerializeField] private Button btn2;

        private bool _readyToStartFloatWindow = false;
        // Start is called before the first frame update
        void Start()
        {
            _readyToStartFloatWindow = false;
            btn1.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn1 clicked");
                AndroidBridge.Instance.CallAsyncOnAndroidUiThread("startUnityGlobalFloatingWindow", args =>
                {
                    Debug.Log($"{TAG} startUnityGlobalFloatingWindow callback, args: {args}");
                    if (string.Compare(AndroidBridge.COMMON_SUCCEEDED, args, StringComparison.Ordinal) == 0)
                    {
                        _readyToStartFloatWindow = true;
                    }
                    else
                    {
                        Debug.LogError($"{TAG} startUnityGlobalFloatingWindow failed, result: {args}");
                    }
                });
            });
            btn2.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn2 clicked");
                // AndroidBridge.Instance.CallSync("collapse");
            });
            
        }

        private void Update()
        {
            if (_readyToStartFloatWindow)
            {
                Debug.Log($"{TAG} start float window");
                SceneManager.UnloadSceneAsync("fullScreenWindow");
                SceneManager.LoadSceneAsync("floatWindow", LoadSceneMode.Additive);
            }
        }
    }
}