using Framework;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

namespace pANDa
{

    public class FloatWindowUI : MonoBehaviour
    {
        private static string TAG = typeof(FloatWindowUI).ToString();

        [SerializeField] private Button btn1;
        [SerializeField] private Button btn2;
        [SerializeField] private Button btn3;

        // Start is called before the first frame update
        void Start()
        {
            btn1.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn1 clicked");
                AndroidBridge.Instance.CallSyncOnUiThread("collapse");
            });
            btn2.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn2 clicked");
                AndroidBridge.Instance.CallSyncOnUiThread("expand");
            });
            btn3.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn3 clicked");
                #if UNITY_ANDROID && !UNITY_EDITOR
                var ret = AndroidBridge.Instance.CallSyncOnUiThread("stopUnityGlobalFloatingWindow");
                #endif
                #if UNITY_EDITOR
                var ret = AndroidBridge.COMMON_SUCCEEDED;
                #endif
                if (string.Compare(ret, AndroidBridge.COMMON_SUCCEEDED) == 0) {
                    SceneManager.UnloadSceneAsync("floatWindow");
                    SceneManager.LoadSceneAsync("fullScreenWindow", LoadSceneMode.Additive);
                }
                else {
                    Debug.LogError($"{TAG} startUnityGlobalFloatingWindow failed, ret: {ret}");
                }
            });
        }
    }
}