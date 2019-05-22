package com.casc.pgkg.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.casc.pgkg.MyVars;
import com.casc.pgkg.R;
import com.casc.pgkg.activity.ActivityCollector;
import com.casc.pgkg.activity.MainActivity;
import com.casc.pgkg.adapter.BucketAdapter;
import com.casc.pgkg.bean.Bucket;
import com.casc.pgkg.helper.NetHelper;
import com.casc.pgkg.helper.net.NetAdapter;
import com.casc.pgkg.helper.net.param.MsgStack;
import com.casc.pgkg.helper.net.param.Reply;
import com.casc.pgkg.message.PollingResultMessage;
import com.casc.pgkg.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class StackFragment extends BaseFragment {

    private static final String TAG = StackFragment.class.getSimpleName();

    private static final int MSG_UPDATE = 0;
    private static final int MSG_CLEAR = 1;

    private int mStackID = -1;
    private List<Bucket> mStacks = new ArrayList<>();
    private BucketAdapter mStackAdapter = new BucketAdapter(mStacks);

    private List<Bucket> mBulks = new ArrayList<>();
    private BucketAdapter mBulkAdapter = new BucketAdapter(mBulks);

    private final Object mLock = new Object();
    private Queue<Bucket> mQueue = new ConcurrentLinkedQueue<>();
    private List<Bucket> mOtherStacks = new ArrayList<>();
    private List<Bucket> mRemovedBuckets = new ArrayList<>();

    private boolean mIsRemoveMode = false;

    private InnerHandler mHandler = new InnerHandler(this);

    @BindView(R.id.rv_stack_area) RecyclerView mStackRv;
    @BindView(R.id.rv_bulk_area) RecyclerView mBulkRv;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PollingResultMessage msg) {
        if (!isVisible() && ActivityCollector.topNotOf(MainActivity.class)) return;
        if (msg.isRead) {
            String epcStr = CommonUtils.bytesToHex(msg.epc);
            Bucket bucket = new Bucket(epcStr);
            switch (CommonUtils.validEPC(msg.epc)) {
                case BUCKET:
                    synchronized (mLock) {
                        if (mIsRemoveMode) {
                            if (mStacks.contains(bucket)) {
                                mStacks.remove(bucket);
                                mRemovedBuckets.add(bucket);
                            } else {
                                mBulks.remove(bucket);
                            }
                        } else {
                            if (!mQueue.contains(bucket)
                                    && !mStacks.contains(bucket)
                                    && !mBulks.contains(bucket)
                                    && !mOtherStacks.contains(bucket)
                                    && !mRemovedBuckets.contains(bucket)) {
                                mQueue.offer(bucket);
                            }
                            if (mRemovedBuckets.contains(bucket)) {
                                mStacks.add(bucket);
                                mRemovedBuckets.remove(bucket);
                            }
                        }
                    }
                    Message.obtain(mHandler, MSG_UPDATE).sendToTarget();
                    break;
            }
        } else {
        }
    }

    @OnClick(R.id.btn_mode)
    void onModeButtonClicked(Button btn) {
        if (mIsRemoveMode) {
            mIsRemoveMode = false;
            btn.setText("正常模式");
            btn.getBackground().setTint(mContext.getColor(R.color.light_gray));
        } else {
            mIsRemoveMode = true;
            btn.setText("移除模式");
            btn.getBackground().setTint(mContext.getColor(R.color.red));
        }
    }

    @OnClick(R.id.btn_clear)
    void onClearButtonClicked() {
        showDialog("确认清空区域内容吗？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Message.obtain(mHandler, MSG_CLEAR).sendToTarget();
            }
        });
    }

    @OnClick(R.id.btn_commit)
    void onCommitButtonClicked() {
        new MaterialDialog.Builder(mContext)
                .title("请选择要提交垛的类型")
                .items(new String[]{"整垛", "散货"})
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        return false;
                    }
                })
                .positiveText("确认")
                .positiveColorRes(R.color.white)
                .btnSelector(R.drawable.md_btn_postive, DialogAction.POSITIVE)
                .negativeText("取消")
                .negativeColorRes(R.color.gray)
                .alwaysCallInputCallback()
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        MsgStack msg = new MsgStack(mStackID, (dialog.getSelectedIndex() + 1) % 2);
                        for (Bucket bucket : mBulks) {
                            msg.addBucket(bucket.getEPC(), bucket.getScannedTime(), 1);
                        }
                        for (Bucket bucket : mRemovedBuckets) {
                            msg.addBucket(bucket.getEPC(), bucket.getScannedTime(), 0);
                        }
                        NetHelper.getInstance().modifyStack(msg).enqueue(new NetAdapter() {
                            @Override
                            public void onSuccess(Reply reply) {
                                showToast("提交成功");
                                Message.obtain(mHandler, MSG_CLEAR).sendToTarget();
                            }

                            @Override
                            public void onFail(String msg) {
                                showToast(msg);
                            }
                        });
                    }
                })
                .show();
//        showDialog("确认提交该垛信息吗？", new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                synchronized (mLock) {
//                    mStacks.clear();
//                    mBulks.clear();
//                    mOtherStacks.clear();
//                    Message.obtain(mHandler, MSG_UPDATE).sendToTarget();
//                }
//            }
//        });
    }

    @Override
    protected void initFragment() {
        EventBus.getDefault().register(this);
        mStackRv.setLayoutManager(new GridLayoutManager(mContext, 3));
        mStackRv.setAdapter(mStackAdapter);
        mBulkRv.setLayoutManager(new GridLayoutManager(mContext, 3));
        mBulkRv.setAdapter(mBulkAdapter);
        MyVars.executor.schedule(new IdentifyStackTask(), 0, TimeUnit.SECONDS);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_stack;
    }

    private static class InnerHandler extends Handler {

        private WeakReference<StackFragment> mOuter;

        InnerHandler(StackFragment fragment) {
            this.mOuter = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            StackFragment outer = mOuter.get();
            switch (msg.what) {
                case MSG_CLEAR:
                    outer.mStackID = -1;
                    synchronized (outer.mLock) {
                        outer.mStacks.clear();
                        outer.mBulks.clear();
                        outer.mOtherStacks.clear();
                        outer.mRemovedBuckets.clear();
                    }
                case MSG_UPDATE:
                    outer.mStackAdapter.notifyDataSetChanged();
                    outer.mBulkAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private class IdentifyStackTask implements Runnable {

        private class StackInfo {

            @SerializedName("id")
            private int id;

            @SerializedName("flag")
            private String flag;

            @SerializedName("bucket_list")
            private List<String> buckets;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (mLock) {
                        if (!mQueue.isEmpty()) {
                            final Bucket bucket = mQueue.peek();
                            Reply reply = NetHelper.getInstance()
                                    .checkStackOrSingle(bucket.getEPC()).execute().body();
                            if (reply != null && reply.getCode() == 200) {
                                StackInfo info =
                                        new Gson().fromJson(reply.getContent(), StackInfo.class);
                                if ("1".equals(info.flag)) {
                                    if (mStacks.isEmpty()) {
                                        mStackID = info.id;
                                        for (String epc : info.buckets) {
                                            mStacks.add(new Bucket(epc));
                                        }
                                    } else {
                                        for (String epc : info.buckets) {
                                            mOtherStacks.add(new Bucket(epc));
                                        }
                                    }
                                } else {
                                    mBulks.add(bucket);
                                }
                                mQueue.poll();
                                Message.obtain(mHandler, MSG_UPDATE).sendToTarget();
                            }
                        }
                    }
                    Thread.sleep(100);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
