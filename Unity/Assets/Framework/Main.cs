using UnityEngine;
using UnityEngine.SceneManagement;

namespace Framework
{
    public class Main : MonoBehaviour
    {
        private static string TAG = typeof(Main).ToString();

        private void Start()
        {
            Debug.Log($"{TAG} Start");
            SceneManager.LoadSceneAsync("fullScreenWindow", LoadSceneMode.Additive);
        }
    }
}