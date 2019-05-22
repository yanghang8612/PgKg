package com.casc.pgkg;

import com.casc.pgkg.backend.TagReader;
import com.casc.pgkg.bean.ApiConfig;
import com.casc.pgkg.bean.Config;
import com.casc.pgkg.bean.User;
import com.casc.pgkg.helper.SpHelper;
import com.casc.pgkg.message.MultiStatusMessage;
import com.google.gson.Gson;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MyVars {

    private MyVars(){}

    private static TagReader preReader = null;

    public static TagReader usbReader = null;

    public static TagReader bleReader = null;

    public static User user = null;

    public static ApiConfig api = new Gson().fromJson(
            SpHelper.getString(MyParams.S_API_JSON), ApiConfig.class);

    public static Config config = new Gson().fromJson(
            SpHelper.getString(MyParams.S_CONFIG_JSON), Config.class);

    public static ScheduledExecutorService executor = Executors.newScheduledThreadPool(15);

    public static MultiStatusMessage status = new MultiStatusMessage();

    public static TagReader getReader() {
        if (usbReader.isConnected()) {
            if (preReader != usbReader) {
                preReader = usbReader;
                usbReader.start();
                bleReader.stop();
            }
            return usbReader;
        } else {
            if (preReader != bleReader) {
                preReader = bleReader;
                usbReader.stop();
                bleReader.start();
            }
            return bleReader;
        }
    }
}
