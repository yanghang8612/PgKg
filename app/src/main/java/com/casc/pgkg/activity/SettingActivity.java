package com.casc.pgkg.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.casc.pgkg.MyParams;
import com.casc.pgkg.R;
import com.casc.pgkg.helper.SpHelper;
import com.casc.pgkg.message.ParamsChangedMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();

    public static void actionStart(Context context) {
        if (ActivityCollector.topNotOf(SettingActivity.class)) {
            Intent intent = new Intent(context, SettingActivity.class);
            context.startActivity(intent);
        }
    }

    @BindView(R.id.et_device_addr) EditText mDeviceAddrEt;
    @BindView(R.id.et_reader_id) EditText mReaderIdEt;

    @OnClick(R.id.ibtn_close) void onCloseImageButtonClicked() {
        finish();
    }

    @OnClick(R.id.btn_save_setting) void onSaveSettingButtonClicked() {
        SpHelper.setParam(MyParams.S_DEVICE_ADDR, mDeviceAddrEt.getText().toString());
        SpHelper.setParam(MyParams.S_READER_ID, mReaderIdEt.getText().toString());
        EventBus.getDefault().post(new ParamsChangedMessage());
        finish();
    }

    @Override
    protected void initActivity() {
        mDeviceAddrEt.setText(SpHelper.getString(MyParams.S_DEVICE_ADDR));
        mReaderIdEt.setText(SpHelper.getString(MyParams.S_READER_ID));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }
}
