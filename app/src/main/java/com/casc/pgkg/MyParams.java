package com.casc.pgkg;

import java.util.HashMap;
import java.util.Map;

public class MyParams {

    private MyParams() {
    }

    public static final String SOFT_CODE = "A02";

    // 网络连接参数配置
    public static final int NET_CONNECT_TIMEOUT = 2; // s
    public static final int NET_RW_TIMEOUT = 2; // s
    public static final int CONFIG_UPDATE_INTERVAL = 5 * 1000; // ms
    public static final int INTERNET_STATUS_CHECK_INTERVAL = 990; // ms
    public static final int PLATFORM_STATUS_CHECK_INTERVAL = 3 * 1000; // ms
    // 用户配置
    public static final String S_USERNAME = "username";
    public static final String S_PASSWORD = "password";
    // 运维人员配置
    public static final String S_POWER = "power"; // 发射功率
    public static final String S_Q_VALUE = "q_value"; // Q值
    public static final String S_DEVICE_ADDR = "device_cloud_addr"; // 设备运管云接口地址
    public static final String S_READER_ID = "reader_id"; // 读写器ID
    public static final String S_LONGITUDE = "longitude"; // 经度
    public static final String S_LATITUDE = "latitude"; // 纬度
    public static final String S_HEIGHT = "height"; // 高度
    // 平台软件外部接口，统一使用一个json字符串存储
    public static final String S_API_JSON = "api_json";
    public static final String S_CONFIG_JSON = "config_json";
    public static final Map<String, String> CONFIG_DEFAULT_MAP = new HashMap<>();
    static {
        CONFIG_DEFAULT_MAP.put(S_USERNAME, "");
        CONFIG_DEFAULT_MAP.put(S_PASSWORD, "");
        CONFIG_DEFAULT_MAP.put(S_POWER, "15dBm");
        CONFIG_DEFAULT_MAP.put(S_Q_VALUE, "0");
        CONFIG_DEFAULT_MAP.put(S_DEVICE_ADDR, "http://59.252.101.154");
        CONFIG_DEFAULT_MAP.put(S_READER_ID, "2000000010");
        CONFIG_DEFAULT_MAP.put(S_LONGITUDE, "121.39");
        CONFIG_DEFAULT_MAP.put(S_LATITUDE, "37.52");
        CONFIG_DEFAULT_MAP.put(S_HEIGHT, "922.88");
        CONFIG_DEFAULT_MAP.put(S_API_JSON, "{}");
        CONFIG_DEFAULT_MAP.put(S_CONFIG_JSON, "{}");
    }

    public static final boolean PRINT_COMMAND = true;
    public static final boolean PRINT_JSON = true;

}
