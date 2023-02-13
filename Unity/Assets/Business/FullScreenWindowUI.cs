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
        // Start is called before the first frame update
        void Start()
        {
            btn1.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn1 clicked");
                var ret = AndroidBridge.Instance.CallSyncOnUiThread("startUnityGlobalFloatingWindow");
                if (string.Compare(ret, AndroidBridge.COMMON_SUCCEEDED) == 0) {
                    SceneManager.UnloadSceneAsync("fullScreenWindow");
                    SceneManager.LoadSceneAsync("floatWindow", LoadSceneMode.Additive);
                }
                else {
                    Debug.LogError($"{TAG} startUnityGlobalFloatingWindow failed, ret: {ret}");
                }
            });
            btn2.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn2 clicked");
                // AndroidBridge.Instance.CallSync("collapse");
            });
            
        }
    }
}