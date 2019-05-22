package com.casc.pgkg.helper.net.param;

import com.casc.pgkg.MyParams;
import com.casc.pgkg.MyVars;
import com.casc.pgkg.helper.SpHelper;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MsgStack {

    @SerializedName("user_id")
    private long userID;

    @SerializedName("reader_id")
    private String readerID;

    @SerializedName("package_id")
    private int packageID;

    @SerializedName("package_time")
    private long packageTime;

    @SerializedName("package_flag")
    private int packageFlag;

    @SerializedName("bucket_list")
    private List<Bucket> buckets = new ArrayList<>();

    public MsgStack(int packageID, int packageFlag) {
        this.userID = MyVars.user.getUserId();
        this.readerID = SpHelper.getString(MyParams.S_READER_ID);
        this.packageID = packageID;
        this.packageTime = System.currentTimeMillis();
        this.packageFlag = packageFlag;
    }

    public void addBucket(String epc, long time, int flag) {
        buckets.add(new Bucket(epc, time, flag));
    }

    private class Bucket {

        private String epc;

        private long time;

        private int flag;

        private Bucket(String epc, long time, int flag) {
            this.epc = epc;
            this.time = time;
            this.flag = flag;
        }
    }
}
