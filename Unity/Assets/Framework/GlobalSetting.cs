using UnityEngine;

namespace Framework
{
    public class GlobalSetting : MonoBehaviour
    {
        [SerializeField] private Vector2Int _floatWindowCollapse;
        [SerializeField] private Vector2Int _floatWindowExpand;
        [SerializeField] private string _syncSettingBridgeName = "syncSettings";

        private void Start()
        {
            AndroidBridge.Instance.CallSync(_syncSettingBridgeName,
                "floatWindowCollapseWidth", _floatWindowCollapse.x,
                "floatWindowCollapseHeight", _floatWindowCollapse.y,
                "floatWindowExpandWidth", _floatWindowExpand.x,
                "floatWindowExpandHeight", _floatWindowExpand.y);
        }
    }
}