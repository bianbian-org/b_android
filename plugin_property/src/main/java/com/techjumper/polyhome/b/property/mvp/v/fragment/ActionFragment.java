package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
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
    @Bind(R.id.lc_theme_wheelpicker)
    WheelCurvedPicker lcThemeWheelpicker;
    @Bind(R.id.lr_layout)
    LinearLayout lrLayout;
    @Bind(R.id.lc_layout)
    LinearLayout lcLayout;
    @Bind(R.id.lr_type_wheelpicker)
    WheelCurvedPicker lrTypeWheelpicker;
    @Bind(R.id.lr_device_wheelpicker)
    WheelCurvedPicker lrDeviceWheelpicker;

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

        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_ACTION));

        // TODO: 16/5/13 到时候看是本地的还是网络层的
        List<String> strings = new ArrayList<String>();
        strings.add("表扬");
        strings.add("投诉");
        strings.add("建议");
        lcThemeWheelpicker.setData(strings);
        lcThemeWheelpicker.setTextColor(getResources().getColor(R.color.color_ffffff));
        lcThemeWheelpicker.setCurrentTextColor(getResources().getColor(R.color.color_ffffff));
        lcThemeWheelpicker.setItemIndex(1);
        lcThemeWheelpicker.setItemSpace(RuleUtils.dp2Px(10));

        strings = new ArrayList<String>();
        strings.add("公共报修");
        strings.add("个人报修");
        lrTypeWheelpicker.setData(strings);
        lrTypeWheelpicker.setTextColor(getResources().getColor(R.color.color_ffffff));
        lrTypeWheelpicker.setCurrentTextColor(getResources().getColor(R.color.color_ffffff));
        lrTypeWheelpicker.setItemIndex(1);
        lrTypeWheelpicker.setItemSpace(RuleUtils.dp2Px(10));

        strings = new ArrayList<String>();
        strings.add("门窗类");
        strings.add("水电类");
        strings.add("电梯类");
        strings.add("墙类");
        strings.add("锁类");
        lrDeviceWheelpicker.setData(strings);
        lrDeviceWheelpicker.setTextColor(getResources().getColor(R.color.color_ffffff));
        lrDeviceWheelpicker.setCurrentTextColor(getResources().getColor(R.color.color_ffffff));
        lrDeviceWheelpicker.setItemIndex(2);
        lrDeviceWheelpicker.setItemSpace(RuleUtils.dp2Px(10));

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
