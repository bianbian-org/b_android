package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ActionFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(ActionFragmentPresenter.class)
public class ActionFragment extends AppBaseFragment<ActionFragmentPresenter> {
    //两种类型，保修和投诉
    public static final int REPAIR = 0;
    public static final int COMPLAINT = 1;

    public static final String TYPE = "type";

    @Bind(R.id.fa_title)
    TextView faTitle;
    @Bind(R.id.lr_theme_wheelpicker)
    WheelCurvedPicker lrThemeWheelpicker;
    @Bind(R.id.lr_layout)
    LinearLayout lrLayout;
    @Bind(R.id.lc_layout)
    LinearLayout lcLayout;
    @Bind(R.id.lc_type_wheelpicker)
    WheelCurvedPicker lcTypeWheelpicker;
    @Bind(R.id.lc_device_wheelpicker)
    WheelCurvedPicker lcDeviceWheelpicker;

    public static ActionFragment getInstance(int type) {
        ActionFragment actionFragment = new ActionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        actionFragment.setArguments(bundle);
        return actionFragment;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        int type = getArguments().getInt(TYPE);
        if (type == REPAIR) {
            faTitle.setText(R.string.property_new_repair);
            lrLayout.setVisibility(View.VISIBLE);
            lcLayout.setVisibility(View.GONE);
        } else {
            faTitle.setText(R.string.property_new_complaint);
            lrLayout.setVisibility(View.GONE);
            lcLayout.setVisibility(View.VISIBLE);
        }

        // TODO: 16/5/13 到时候看是本地的还是网络层的
        List<String> strings = new ArrayList<String>();
        strings.add("表扬");
        strings.add("投诉");
        strings.add("建议");
        lrThemeWheelpicker.setData(strings);
        lrThemeWheelpicker.setTextColor(getResources().getColor(R.color.color_ffffff));
        lrThemeWheelpicker.setCurrentTextColor(getResources().getColor(R.color.color_ffffff));
        lrThemeWheelpicker.setItemIndex(1);
        lrThemeWheelpicker.setItemSpace(RuleUtils.dp2Px(10));

        strings = new ArrayList<String>();
        strings.add("公共报修");
        strings.add("个人报修");
        lcTypeWheelpicker.setData(strings);
        lcTypeWheelpicker.setTextColor(getResources().getColor(R.color.color_ffffff));
        lcTypeWheelpicker.setCurrentTextColor(getResources().getColor(R.color.color_ffffff));
        lcTypeWheelpicker.setItemIndex(1);
        lcTypeWheelpicker.setItemSpace(RuleUtils.dp2Px(10));

        strings = new ArrayList<String>();
        strings.add("门窗类");
        strings.add("水电类");
        strings.add("电梯类");
        strings.add("墙类");
        strings.add("锁类");
        lcDeviceWheelpicker.setData(strings);
        lcDeviceWheelpicker.setTextColor(getResources().getColor(R.color.color_ffffff));
        lcDeviceWheelpicker.setCurrentTextColor(getResources().getColor(R.color.color_ffffff));
        lcDeviceWheelpicker.setItemIndex(2);
        lcDeviceWheelpicker.setItemSpace(RuleUtils.dp2Px(10));

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
