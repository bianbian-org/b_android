package com.techjumper.polyhome.mvp.v.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.techjumper.commonres.entity.LoginEntity;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.R;
import com.techjumper.polyhome.UserManager;
import com.techjumper.polyhome.mvp.p.fragment.SettingFragmentPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/5/10.
 */
@Presenter(SettingFragmentPresenter.class)
public class SettingFragment extends AppBaseFragment<SettingFragmentPresenter> {

    @Bind(R.id.title_rg)
    RadioGroup titleRg;
    @Bind(R.id.su_layout)
    LinearLayout suLayout;
    @Bind(R.id.su_title_rg)
    RadioGroup suTitleRg;
    @Bind(R.id.sup_layout)
    LinearLayout supLayout;
    @Bind(R.id.sui_layout)
    LinearLayout suiLayout;
    @Bind(R.id.sui_nickname_input)
    EditText suiNicknameInput;
    @Bind(R.id.sui_birthday_input)
    EditText suiBirthdayInput;
    @Bind(R.id.sui_email_input)
    EditText suiEmailInput;
    @Bind(R.id.sup_oldpassword_input)
    EditText supOldpasswordInput;
    @Bind(R.id.sup_newpassword_input)
    EditText supNewpasswordInput;
    @Bind(R.id.sup_confirmpassword_input)
    EditText supConfirmpasswordInput;
    @Bind(R.id.sui_male)
    RadioButton suiMale;
    @Bind(R.id.sui_female)
    RadioButton suiFemale;

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_setting, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.title_user) {
                suLayout.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.title_project) {
                suLayout.setVisibility(View.GONE);
            }
        });

        suTitleRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.su_title_user_info) {
                supLayout.setVisibility(View.GONE);
                suiLayout.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.su_title_user_password) {
                supLayout.setVisibility(View.VISIBLE);
                suiLayout.setVisibility(View.GONE);
            }
        });

        suiNicknameInput.setText(UserManager.INSTANCE.getUserNickName());
        suiBirthdayInput.setText(UserManager.INSTANCE.getUserInfo(UserManager.KEY_BIRTHDAY));
        if (UserManager.INSTANCE.getSexId() == LoginEntity.MALE) {
            suiMale.setChecked(true);
        } else {
            suiFemale.setChecked(true);
        }
        suiEmailInput.setText(UserManager.INSTANCE.getUserInfo(UserManager.KEY_EMAIL));
    }

    public EditText getSuiBirthdayInput() {
        return suiBirthdayInput;
    }

    public EditText getSuiNicknameInput() {
        return suiNicknameInput;
    }

    public EditText getSuiEmailInput() {
        return suiEmailInput;
    }


    public EditText getSupOldpasswordInput() {
        return supOldpasswordInput;
    }

    public EditText getSupNewpasswordInput() {
        return supNewpasswordInput;
    }

    public EditText getSupConfirmpasswordInput() {
        return supConfirmpasswordInput;
    }

    public void showError(EditText editText, CharSequence message) {
        editText.setError(message);
    }

    public void setText(EditText et, Editable text) {
        et.setText(text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
