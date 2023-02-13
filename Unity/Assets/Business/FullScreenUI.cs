using Framework;
using UnityEngine;
using UnityEngine.UI;

namespace pANDa
{

    public class FullScreenUI : MonoBehaviour
    {
        private static string TAG = typeof(FullScreenUI).ToString();

        [SerializeField] private Button btn1;
        [SerializeField] private Button btn2;
        // Start is called before the first frame update
        void Start()
        {
            btn1.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn1 clicked");
                AndroidBridge.Instance.CallSync("expand");
            });
            btn1.onClick.AddListener(() =>
            {
                Debug.Log($"{TAG} btn2 clicked");
                AndroidBridge.Instance.CallSync("collapse");
            });
        }
    }
}