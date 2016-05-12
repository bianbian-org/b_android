package com.techjumper.polyhome.b.setting.mvp.v.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.setting.R;
import com.techjumper.polyhome.b.setting.mvp.p.fragment.LoginFragmentPresenter;

import butterknife.Bind;

/**
 * Created by kevin on 16/5/6.
 */
@Presenter(LoginFragmentPresenter.class)
public class LoginFragment extends AppBaseFragment<LoginFragmentPresenter> {

    @Bind(R.id.login_mobile_input)
    EditText loginMobileInput;
    @Bind(R.id.login_password_input)
    EditText loginPasswordInput;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.forgerpassword)
    TextView forgerpassword;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_login, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public EditText getLoginMobileInput() {
        return loginMobileInput;
    }

    public EditText getLoginPasswordInput() {
        return loginPasswordInput;
    }

    public void showError(EditText editText, CharSequence message) {
        editText.setError(message);
    }

    public void setText(EditText et, Editable text) {
        et.setText(text);
    }
}
