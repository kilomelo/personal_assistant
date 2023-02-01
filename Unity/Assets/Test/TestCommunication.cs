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
            Debug.Log("UnityTest testBtn click");
        });
    }

    private void TestSetLabelText(string s)
    {
        Debug.Log($"UnityTest TestSetLabelText s: {s}");
        testLabel.text = s;
    }
}
