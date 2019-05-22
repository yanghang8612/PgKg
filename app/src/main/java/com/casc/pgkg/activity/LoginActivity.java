package com.casc.pgkg.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.casc.pgkg.MyParams;
import com.casc.pgkg.MyVars;
import com.casc.pgkg.R;
import com.casc.pgkg.bean.User;
import com.casc.pgkg.helper.NetHelper;
import com.casc.pgkg.helper.SpHelper;
import com.casc.pgkg.helper.net.NetAdapter;
import com.casc.pgkg.helper.net.param.Reply;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.et_username) EditText mUsernameEt;
    @BindView(R.id.et_password) EditText mPasswordEt;

    @OnClick(R.id.btn_login) void onLoginButtonClicked() {
        final String username = mUsernameEt.getText().toString();
        final String password = mPasswordEt.getText().toString();

        if (TextUtils.isEmpty(username)) {
            showToast("用户名不能为空");
        } else if (TextUtils.isEmpty(password)) {
            showToast("密码不能为空");
        } else {
            NetHelper.getInstance().getAuthorization(username, password).enqueue(new NetAdapter() {
                @Override
                public void onSuccess(Reply reply) {
                    MyVars.user = new Gson().fromJson(reply.getContent(), User.class);
                    if (MyVars.user.getRoleId() == 6 || MyVars.user.getRoleId() == 7) {
                        SpHelper.setParam(MyParams.S_USERNAME, username);
                        SpHelper.setParam(MyParams.S_PASSWORD, password);
                        MainActivity.actionStart(LoginActivity.this);
                        finish();
                    } else {
                        showToast("非品管/库管用户");
                    }
                }

                @Override
                public void onFail(String msg) {
                    showToast(msg);
                }
            });
        }
    }

    @OnClick(R.id.tv_setting_link) void onSettingLinkTextViewClicked() {
        SettingActivity.actionStart(this);
    }

    @Override
    protected void initActivity() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

}
