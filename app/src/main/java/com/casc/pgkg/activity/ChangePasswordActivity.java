package com.casc.pgkg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.casc.pgkg.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {

    private static final String TAG = ChangePasswordActivity.class.getSimpleName();

    public static void actionStart(Context context) {
        if (ActivityCollector.topNotOf(ChangePasswordActivity.class)) {
            Intent intent = new Intent(context, ChangePasswordActivity.class);
            context.startActivity(intent);
        }
    }

    @BindView(R.id.tv_title) TextView mTitleTv;

    @OnClick(R.id.btn_back) void onBackButtonClicked() {
        finish();
    }

    @OnClick(R.id.btn_save) void onSaveButtonClicked() {
        finish();
    }

    @Override
    protected void initActivity() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_change_password;
    }
}
