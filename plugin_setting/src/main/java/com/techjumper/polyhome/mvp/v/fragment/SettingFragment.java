package com.techjumper.polyhome.mvp.v.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhome.R;
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
