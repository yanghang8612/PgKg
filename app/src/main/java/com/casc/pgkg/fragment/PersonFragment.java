package com.casc.pgkg.fragment;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.casc.pgkg.MyParams;
import com.casc.pgkg.R;
import com.casc.pgkg.activity.ChangePasswordActivity;
import com.casc.pgkg.activity.ChangeUsernameActivity;
import com.casc.pgkg.activity.ReaderSettingsActivity;
import com.casc.pgkg.activity.ActivityCollector;
import com.casc.pgkg.helper.SpHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonFragment extends BaseFragment {

    private static final String TAG = PersonFragment.class.getSimpleName();

    @BindView(R.id.tv_username) TextView mUsernameTv;

    @OnClick(R.id.cl_reader_settings)
    void onReaderSettingsClicked() {
        ReaderSettingsActivity.actionStart(mContext);
    }

    @OnClick(R.id.cl_modify_username)
    void onChangeUsernameClicked() {
        ChangeUsernameActivity.actionStart(mContext);
    }

    @OnClick(R.id.cl_modify_password)
    void onChangePasswordClicked() {
        ChangePasswordActivity.actionStart(mContext);
    }

    @OnClick(R.id.cl_exit_app)
    void onExitAppClicked() {
        showDialog("确认退出软件吗？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                ActivityCollector.backToLogin();
            }
        });
    }

    @Override
    protected void initFragment() {

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_person;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUsernameTv.setText(SpHelper.getString(MyParams.S_USERNAME));
    }
}
