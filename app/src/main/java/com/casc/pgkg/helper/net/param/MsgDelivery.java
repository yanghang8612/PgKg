package com.casc.pgkg.helper.net.param;

import com.casc.pgkg.MyParams;
import com.casc.pgkg.MyVars;
import com.casc.pgkg.helper.SpHelper;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MsgDelivery {

    @SerializedName("user_id")
    private long userID;

    @SerializedName("reader_id")
    private String readerID;

    @SerializedName("bucket_epc")
    private String bucketEPC;

    @SerializedName("bucket_qrcode")
    private String bucketQRCode;

    @SerializedName("delivery_reason")
    private String deliveryReason;

    public MsgDelivery(String bucketEPC, String bucketQRCode, String deliveryReason) {
        this.userID = MyVars.user.getUserId();
        this.readerID = SpHelper.getString(MyParams.S_READER_ID);
        this.bucketEPC = bucketEPC;
        this.bucketQRCode = bucketQRCode;
        this.deliveryReason = deliveryReason;
    }
}
