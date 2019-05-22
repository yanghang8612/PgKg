package com.casc.pgkg.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.casc.pgkg.MyParams;
import com.casc.pgkg.MyVars;
import com.casc.pgkg.R;
import com.casc.pgkg.bean.User;
import com.casc.pgkg.helper.NetHelper;
import com.casc.pgkg.helper.SpHelper;
import com.casc.pgkg.helper.net.NetAdapter;
import com.casc.pgkg.helper.net.param.Reply;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.skv_splash) SpinKitView mProgressBar;

    @Override
    protected void initActivity() {
        mProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username = SpHelper.getString(MyParams.S_USERNAME);
                String password = SpHelper.getString(MyParams.S_PASSWORD);
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    LoginActivity.actionStart(SplashActivity.this);
                    finish();
                } else {
                    NetHelper.getInstance().getAuthorization(username, password).enqueue(new NetAdapter() {
                        @Override
                        public void onSuccess(Reply reply) {
                            MyVars.user = new Gson().fromJson(reply.getContent(), User.class);
                            MainActivity.actionStart(SplashActivity.this);
                            finish();
                        }

                        @Override
                        public void onFail(String msg) {
                            showToast(msg);
                            LoginActivity.actionStart(SplashActivity.this);
                            finish();
                        }
                    });
                }

            }
        }, 500);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

}
