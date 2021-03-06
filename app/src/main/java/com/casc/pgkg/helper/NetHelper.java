package com.casc.pgkg.helper;

import com.casc.pgkg.MyParams;
import com.casc.pgkg.MyVars;
import com.casc.pgkg.helper.net.NetInterface;
import com.casc.pgkg.helper.net.param.MsgDelivery;
import com.casc.pgkg.helper.net.param.MsgLog;
import com.casc.pgkg.helper.net.param.MsgStack;
import com.casc.pgkg.helper.net.param.Reply;
import com.casc.pgkg.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetHelper {

    private final String TAG = NetHelper.class.getSimpleName();

    private final NetInterface netInterface;

    private static class SingletonHolder{
        private static final NetHelper instance = new NetHelper();
    }

    private NetHelper() {
        this.netInterface = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SpHelper.getString(MyParams.S_DEVICE_ADDR))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(MyParams.NET_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(MyParams.NET_RW_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(MyParams.NET_RW_TIMEOUT, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(NetInterface.class);
    }

    public static NetHelper getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 以下为运管云的接口
     */
    public Call<Reply> sendHeartbeat() {
        return netInterface.get(SpHelper.getString(MyParams.S_DEVICE_ADDR) + "/api/platform/status",
                CommonUtils.generateRequestHeader("02"), new HashMap<String, Object>());
    }

    public Call<Reply> getApiConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("soft_id", SpHelper.getString(MyParams.S_READER_ID));
        map.put("soft_type", MyParams.SOFT_CODE);
        return netInterface.get(SpHelper.getString(MyParams.S_DEVICE_ADDR) + "/api/device/configs",
                CommonUtils.generateRequestHeader("02"), map);
    }

    public Call<Reply> uploadLogMsg(MsgLog msg) {
        return netInterface.post(SpHelper.getString(MyParams.S_DEVICE_ADDR) + "/api/device/logs",
                CommonUtils.generateRequestHeader("02"), CommonUtils.generateRequestBody(msg));
    }

    /**
     * 以下为数据云的接口
     */
    public Call<Reply> getConfig() {
        return netInterface.get(MyVars.api.getDataCloudAddr() + "/api/sq/configs/pad",
                CommonUtils.generateRequestHeader("02"), generateReaderIDMap());
    }

    public Call<Reply> checkStackOrSingle(String epcStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("bucket_epc", epcStr);
        return netInterface.get(MyVars.api.getDataCloudAddr() + "/api/sq/produce/packages",
                CommonUtils.generateRequestHeader("02"), map);
    }

    public Call<Reply> modifyStack(MsgStack msg) {
        return netInterface.put(MyVars.api.getDataCloudAddr() + "/api/sq/produce/packages",
                CommonUtils.generateRequestHeader("02"), CommonUtils.generateRequestBody(msg));
    }

    public Call<Reply> uploadDeliveryMsg(MsgDelivery msg) {
        return netInterface.post(MyVars.api.getDataCloudAddr() + "/api/sq/produce/abnormal_delivery",
                CommonUtils.generateRequestHeader("02"), CommonUtils.generateRequestBody(msg));
    }

    /**
     * 以下为Web云的接口
     */
    public Call<Reply> getAuthorization(String username, String password) {
        Map<String, String> header = CommonUtils.generateRequestHeader("02");
        header.put("username", username);
        header.put("password", password);
        return netInterface.post(MyVars.api.getWebCloudAddr() + "/login",
                header, CommonUtils.generateRequestBody("{}"));
    }

    private Map<String, Object> generateReaderIDMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("reader_id", SpHelper.getString(MyParams.S_READER_ID));
        return map;
    }
}
