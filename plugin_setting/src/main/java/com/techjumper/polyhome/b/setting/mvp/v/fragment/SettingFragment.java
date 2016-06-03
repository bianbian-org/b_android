package com.techjumper.polyhome.b.setting.mvp.v.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.techjumper.commonres.entity.LoginEntity;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.b.setting.R;
import com.techjumper.polyhome.b.setting.UserManager;
import com.techjumper.polyhome.b.setting.mvp.p.fragment.SettingFragmentPresenter;
import com.techjumper.polyhome.b.setting.utils.StringUtil;

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
    @Bind(R.id.spm_title_rg)
    RadioGroup spmTitleRg;
    @Bind(R.id.spr_layout)
    LinearLayout sprLayout;
    @Bind(R.id.sps_layout)
    LinearLayout spsLayout;
    @Bind(R.id.spn_layout)
    LinearLayout spnLayout;
    @Bind(R.id.spp_layout)
    LinearLayout sppLayout;
    @Bind(R.id.sp_layout)
    FrameLayout spLayout;
    @Bind(R.id.spl_layout)
    LinearLayout splLayout;
    @Bind(R.id.spm_layout)
    LinearLayout spmLayout;
    @Bind(R.id.spn_ip_tx)
    TextView spnIpTx;

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
                spLayout.setVisibility(View.GONE);
            } else if (checkedId == R.id.title_project) {
                suLayout.setVisibility(View.GONE);
                spLayout.setVisibility(View.VISIBLE);
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

        spmTitleRg.setOnCheckedChangeListener(((group, checkedId) -> {
            if (checkedId == R.id.spm_title_project_roomnum) {
                sprLayout.setVisibility(View.VISIBLE);
                spsLayout.setVisibility(View.GONE);
                spnLayout.setVisibility(View.GONE);
                sppLayout.setVisibility(View.GONE);
            } else if (checkedId == R.id.spm_title_project_sip) {
                sprLayout.setVisibility(View.GONE);
                spsLayout.setVisibility(View.VISIBLE);
                spnLayout.setVisibility(View.GONE);
                sppLayout.setVisibility(View.GONE);
            } else if (checkedId == R.id.spm_title_project_network) {
                sprLayout.setVisibility(View.GONE);
                spsLayout.setVisibility(View.GONE);
                spnLayout.setVisibility(View.VISIBLE);
                sppLayout.setVisibility(View.GONE);
            } else {
                sprLayout.setVisibility(View.GONE);
                spsLayout.setVisibility(View.GONE);
                spnLayout.setVisibility(View.GONE);
                sppLayout.setVisibility(View.VISIBLE);
            }
        }));

        if (UserManager.INSTANCE.getSexId() == LoginEntity.MALE) {
            suiMale.setChecked(true);
        } else {
            suiFemale.setChecked(true);
        }

        setTextAndSelection(suiNicknameInput, UserManager.INSTANCE.getUserNickName());
        setTextAndSelection(suiBirthdayInput, UserManager.INSTANCE.getUserInfo(UserManager.KEY_BIRTHDAY));
        setTextAndSelection(suiEmailInput, UserManager.INSTANCE.getUserInfo(UserManager.KEY_EMAIL));

        spnIpTx.setText(String.format(getString(R.string.setting_project_network_ip_address), StringUtil.getIPAddress()));
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

    private void setTextAndSelection(EditText editText, String content) {
        if (editText == null || TextUtils.isEmpty(content))
            return;

        editText.setText(content);
        editText.setSelection(content.length());
    }

    public void showSettingMain() {
        splLayout.setVisibility(View.GONE);
        spmLayout.setVisibility(View.VISIBLE);
    }
}
