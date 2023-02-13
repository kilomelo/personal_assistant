using UnityEngine;
using UnityEngine.SceneManagement;

namespace Framework
{
    public class Main : MonoBehaviour
    {
        private static string TAG = typeof(Main).ToString();
        [SerializeField] private string _firstScene;

        private void Start()
        {
            Debug.Log($"{TAG} Start");
            if (string.IsNullOrEmpty(_firstScene)) {
                Debug.Log($"{TAG} first scene is empty");
                return;
            }
            SceneManager.LoadSceneAsync(_firstScene, LoadSceneMode.Additive);
        }
    }
}