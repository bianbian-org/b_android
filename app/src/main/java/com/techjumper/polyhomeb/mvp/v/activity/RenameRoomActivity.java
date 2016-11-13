package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.window.KeyboardUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.RenameRoomActivityPresenter;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(RenameRoomActivityPresenter.class)
public class RenameRoomActivity extends AppBaseActivity<RenameRoomActivityPresenter> {

    @Bind(R.id.et)
    EditText mEtRenameRoom;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_rename_room);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.rename_room);
    }

    private void initView() {
        String roomName = getPresenter().getRoomName();
        if (TextUtils.isEmpty(roomName)) return;
        mEtRenameRoom.setText(roomName);
        mEtRenameRoom.setSelection(roomName.length());
    }

    public EditText getEtNewRoom() {
        return mEtRenameRoom;
    }

    @Override
    protected void onDestroy() {
        KeyboardUtils.closeKeyboard(getEtNewRoom());
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        KeyboardUtils.closeKeyboard(getEtNewRoom());
        super.onBackPressed();
    }
}
