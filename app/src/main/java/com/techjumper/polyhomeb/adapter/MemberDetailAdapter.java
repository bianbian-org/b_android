package com.techjumper.polyhomeb.adapter;

import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.steve.creact.library.display.DisplayBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.MemberDetailData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.MemberDetailBean;

import java.util.HashMap;
import java.util.Map;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MemberDetailAdapter extends BaseRecyclerPowerfulAdapter {

    private Map<String, Boolean> mCheckedMap = new HashMap<>();
    private IItemCheckedChange sItemCheckedChange;

    @Override
    public void setListener(BaseRecyclerViewHolder holder, DisplayBean bindBean, int position) {
        if (!(bindBean instanceof MemberDetailBean) || sItemCheckedChange == null) return;
        SwitchCompat sc = (SwitchCompat) holder.getView(R.id.switch_admin);

        sc.setOnCheckedChangeListener(null);
        MemberDetailBean dataBean = (MemberDetailBean) bindBean;
        boolean checked = getChecked(dataBean);
        sc.setChecked(checked);

        sc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            changeCheck(dataBean, isChecked);
            if (sItemCheckedChange != null) {
                sItemCheckedChange.itemCheckedChange(isChecked, dataBean,buttonView);
            }
        });
    }

    private void changeCheck(MemberDetailBean dataBean, boolean isChecked) {
        if (dataBean == null || dataBean.getData() == null) return;
        MemberDetailData data = dataBean.getData();
        changeCheck(data.getRoomName() + data.getRoomId(), isChecked);
    }

    private void changeCheck(String name, boolean checked) {
        mCheckedMap.put(name, checked);
    }

    private boolean getChecked(MemberDetailBean dataBean) {
        if (dataBean == null || dataBean.getData() == null) return false;
        Boolean aBoolean = mCheckedMap.get(dataBean.getData().getRoomName() + dataBean.getData().getRoomId());
        if (aBoolean == null) {
            boolean manageable = dataBean.getData().getManageable();
            if (manageable) return true;
        }
        return aBoolean == null ? false : aBoolean;
    }

    public interface IItemCheckedChange {
        void itemCheckedChange(boolean isChecked, MemberDetailBean dataBean, CompoundButton buttonView);
    }

    public void setOnCheckedListener(IItemCheckedChange sItemCheckedChange) {
        this.sItemCheckedChange = sItemCheckedChange;
    }
}
