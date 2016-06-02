package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhome.b.property.Constant;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ActionFragmentPresenter;

import java.util.Arrays;
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
    @Bind(R.id.lc_content)
    EditText lcContent;
    @Bind(R.id.lr_content)
    EditText lrContent;

    private int lcType = Constant.LC_PRA;

    private int lrType = Constant.LR_TYPE_COM;
    private int lrDevice = Constant.LR_DEVICE_LOCK;

    public int getLrType() {
        return lrType;
    }

    public void setLrType(int lrType) {
        this.lrType = lrType;
    }

    public int getLrDevice() {
        return lrDevice;
    }

    public void setLrDevice(int lrDevice) {
        this.lrDevice = lrDevice;
    }

    public EditText getLcContent() {
        return lcContent;
    }

    public EditText getLrContent() {
        return lrContent;
    }

    public void setLcType(int lcType) {
        this.lcType = lcType;
    }

    public int getLcType() {
        return lcType;
    }

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

        String[] strings = getResources().getStringArray(R.array.complaint_type);
        initWheelPicker(lcThemeWheelpicker, Arrays.asList(strings));

        strings = getResources().getStringArray(R.array.repair_type);
        initWheelPicker(lrTypeWheelpicker, Arrays.asList(strings));

        strings = getResources().getStringArray(R.array.repair_device);
        initWheelPicker(lrDeviceWheelpicker, Arrays.asList(strings));

        lcThemeWheelpicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                if (data.equals("表扬")) {
                    setLcType(Constant.LC_PRA);
                } else if (data.equals("投诉")) {
                    setLcType(Constant.LC_COM);
                } else {
                    setLcType(Constant.LC_SUG);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });

        lrTypeWheelpicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                if (data.equals("公共报修")) {
                    setLrType(Constant.LR_TYPE_COM);
                } else {
                    setLrType(Constant.LR_TYPE_PER);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });

        lrDeviceWheelpicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                if (data.equals("锁类")) {
                    setLrDevice(Constant.LR_DEVICE_LOCK);
                } else if (data.equals("水电类")) {
                    setLrDevice(Constant.LR_DEVICE_WATER);
                } else if (data.equals("门窗类")) {
                    setLrDevice(Constant.LR_DEVICE_DOOR);
                } else if (data.equals("墙类")) {
                    setLrDevice(Constant.LR_DEVICE_WALL);
                } else {
                    setLrDevice(Constant.LR_DEVICE_LIFT);
                }
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });

        lcContent.setImeOptions(EditorInfo.IME_ACTION_DONE);
        lrContent.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    private WheelCurvedPicker initWheelPicker(WheelCurvedPicker wheelCurvedPicker, List<String> names) {
        if (wheelCurvedPicker == null)
            return new WheelCurvedPicker(getContext());

        if (names == null || names.size() == 0)
            return new WheelCurvedPicker(getContext());

        wheelCurvedPicker.setData(names);
        wheelCurvedPicker.setTextColor(getResources().getColor(R.color.color_ffffff));
        wheelCurvedPicker.setCurrentTextColor(getResources().getColor(R.color.color_ffffff));
        wheelCurvedPicker.setItemIndex(0);
        wheelCurvedPicker.setItemSpace(RuleUtils.dp2Px(10));

        return wheelCurvedPicker;
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
