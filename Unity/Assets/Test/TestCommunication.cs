using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class TestCommunication : MonoBehaviour
{
    public Button testButton;
    public TextMeshProUGUI testLabel;
    // Start is called before the first frame update
    void Start()
    {
        Debug.Log($"UnityTest Start");

        testButton.onClick.AddListener(() =>
        {
            Debug.Log("UnityTest on testBtn click");
            using var unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            using AndroidJavaObject jo = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
            jo.Call("runOnUiThread", new AndroidJavaRunnable(stopUnityGlobalFloatingWindow));
        });
    }

    void Update()
    {
        var screenWidth = Screen.width;
        var screenHeight = Screen.height;
        testLabel.text = $"w: {screenWidth}, h: {screenHeight}";

    }

    private void stopUnityGlobalFloatingWindow()
    {
        Debug.Log($"UnityTest stopUnityGlobalFloatingWindow");
        using var unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        using AndroidJavaObject jo = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        jo.Call("stopUnityGlobalFloatingWindow");
    }

    private void TestSetLabelText(string s)
    {
        Debug.Log($"UnityTest TestSetLabelText s: {s}");
        testLabel.text = s;
    }

    private void TestCase()
    {
        Debug.Log("UnityTest testBtn click");
        using var unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        using AndroidJavaObject jo = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        // 调用实例方法 
        var param = "text from unity";
        Debug.Log($"UnityTest call testMethod, param: {param}");
        jo.Call("testMethod", param);
        // 获取实例变量（非静态）
        var testString = jo.Get<string>("testString");
        Debug.Log($"UnityTest get testString: {testString}");
        testLabel.text = testString;
        // 设置实例 变量（非静态）
        var setValue = "text set by unity";
        Debug.Log($"UnityTest set testString: {setValue}");
        jo.Set<string>("testString", setValue);
        testString = jo.Get<string>("testString");
        Debug.Log($"UnityTest testString after set: {testString}");
        testLabel.text = testString;
        // 调用静态变量（非静态）
        param = "text from unity static";
        Debug.Log($"UnityTest call testMethodStatic, param: {param}");
        jo.CallStatic("testMethodStatic", param);
        // 获取静态变量
        var testStaticString = jo.GetStatic<string>("testStaticString");
        Debug.Log($"UnityTest get testStaticString: {testStaticString}");
        testLabel.text = testStaticString;
        // 设置静态变量 
        setValue = "static text set by unity";
        Debug.Log($"UnityTest set testStaticString: {setValue}");
        jo.SetStatic<string>("testStaticString", setValue);
        testStaticString = jo.GetStatic<string>("testStaticString");
        Debug.Log($"UnityTest testStaticString after set: {testStaticString}");
        testLabel.text = testStaticString;
    }
}
