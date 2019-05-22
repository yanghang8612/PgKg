package com.casc.pgkg.message;

import com.casc.pgkg.MyParams;
import com.casc.pgkg.helper.SpHelper;
import com.casc.pgkg.utils.CommonUtils;

public class PollingResultMessage {

    public boolean isRead;

    public int rssi;

    public byte[] epc;

    public PollingResultMessage() {
        this.isRead = false;
    }

    public PollingResultMessage(String epc) {
        this(CommonUtils.hexToBytes(epc));
    }

    public PollingResultMessage(byte[] epc) {
        this((byte) 0, epc);
    }

    public PollingResultMessage(byte rssi, byte[] epc) {
        this.isRead = true;
        this.rssi = (int) rssi + SpHelper.getInt(MyParams.S_POWER);
        this.epc = epc;
    }

}
