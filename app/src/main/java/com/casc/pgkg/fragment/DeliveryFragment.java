package com.casc.pgkg.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.casc.pgkg.MyVars;
import com.casc.pgkg.R;
import com.casc.pgkg.activity.ActivityCollector;
import com.casc.pgkg.activity.MainActivity;
import com.casc.pgkg.helper.NetHelper;
import com.casc.pgkg.helper.net.NetAdapter;
import com.casc.pgkg.helper.net.param.MsgDelivery;
import com.casc.pgkg.helper.net.param.Reply;
import com.casc.pgkg.message.ConfigUpdatedMessage;
import com.casc.pgkg.message.PollingResultMessage;
import com.casc.pgkg.utils.CommonUtils;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class DeliveryFragment extends BaseFragment {

    private static final String TAG = DeliveryFragment.class.getSimpleName();

    private static final int FOUND_READ_COUNT = 20;
    private static final int MAX_READ_NONE_COUNT = 20;

    private static final int MSG_FOUND = 0;
    private static final int MSG_LOST = 1;

    private String mPreScannedEPC, mFoundBucket;

    private int mReadCount, mReadNoneCount;

    private InnerHandler mHandler = new InnerHandler(this);

    @BindView(R.id.tv_product_name) TextView mProductTv;
    @BindView(R.id.tv_body_code) TextView mBodyCodeTv;
    @BindView(R.id.nsp_delivery_reason) NiceSpinner mReasonSp;

    @OnClick(R.id.btn_commit)
    void onCommitButtonClicked() {
        if (mFoundBucket != null) {
            showDialog("确认提交该出库信息吗", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    MsgDelivery msg = new MsgDelivery(mFoundBucket, "", mReasonSp.getText().toString());
                    NetHelper.getInstance().uploadDeliveryMsg(msg).enqueue(new NetAdapter() {
                        @Override
                        public void onSuccess(Reply reply) {
                            showToast("提交成功");
                        }

                        @Override
                        public void onFail(String msg) {
                            showToast(msg);
                        }
                    });
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConfigUpdatedMessage msg) {
        if (!MyVars.config.getReasons().isEmpty()) {
            mReasonSp.attachDataSource(MyVars.config.getReasons());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PollingResultMessage msg) {
        if (!isVisible() && ActivityCollector.topNotOf(MainActivity.class)) return;
        if (msg.isRead) {
            String epcStr = CommonUtils.bytesToHex(msg.epc);
            switch (CommonUtils.validEPC(msg.epc)) {
                case BUCKET:
                    mReadNoneCount = 0;
                    if (epcStr.equals(mPreScannedEPC)) {
                        mReadCount += 1;
                    } else {
                        mReadCount = 1;
                    }
                    if (mReadCount >= FOUND_READ_COUNT) {
                        mFoundBucket = epcStr;
                        Message.obtain(mHandler, MSG_FOUND).sendToTarget();
                    }
                    mPreScannedEPC = epcStr;
                    break;
            }
        } else {
            if (++mReadNoneCount >= MAX_READ_NONE_COUNT) {
                Message.obtain(mHandler, MSG_LOST).sendToTarget();
            }
        }
    }

    @Override
    protected void initFragment() {
        EventBus.getDefault().register(this);
        if (!MyVars.config.getReasons().isEmpty()) {
            mReasonSp.attachDataSource(MyVars.config.getReasons());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_delivery;
    }

    private static class InnerHandler extends Handler {

        private WeakReference<DeliveryFragment> mOuter;

        InnerHandler(DeliveryFragment fragment) {
            this.mOuter = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            DeliveryFragment outer = mOuter.get();
            switch (msg.what) {
                case MSG_FOUND:
                    outer.mProductTv.setVisibility(View.VISIBLE);
                    outer.mProductTv.setText(CommonUtils.getProduct(outer.mFoundBucket).getStr());
                    outer.mBodyCodeTv.setVisibility(View.VISIBLE);
                    outer.mBodyCodeTv.setText(CommonUtils.getBodyCode(outer.mFoundBucket));
                    break;
                case MSG_LOST:
                    outer.mReadCount = 0;
                    outer.mPreScannedEPC = outer.mFoundBucket = null;
                    outer.mProductTv.setVisibility(View.INVISIBLE);
                    outer.mBodyCodeTv.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }
}
