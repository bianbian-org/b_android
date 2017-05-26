package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.CheckInDayData;
import com.techjumper.polyhomeb.mvp.m.CheckInActivityModel;

import java.util.Map;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(data = CheckInDayData.class, beanName = "CheckInDayBean")
public class CheckInDayViewHolder extends BaseRecyclerViewHolder<CheckInDayData> {

    public static final int LAYOUT_ID = R.layout.item_check_in_day;

    public CheckInDayViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(CheckInDayData data) {
        if (data == null) return;
        if (data.getDayList() == null || data.getDayList().size() == 0) return;

        for (int i = 0; i < data.getDayList().size(); i++) {  //只要有数据,size肯定是7,只不过可能其中真正有数据的没有7个
            Map<String, Integer> map = data.getDayList().get(i);

            if (map == null || map.size() == 0) {  //证明日历表中第i个位置不显示天数,例如整个大的日历表中的第一行和最后一行,他们的前面和后面可能没有数据,毕竟是上个月和下个月的月末和月初
                //nothing to do
            } else {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String dayString = entry.getKey();
                    Integer drawableStatus = entry.getValue();
                    setTextAndDrawable(dayString, drawableStatus, i);
                }
            }
        }
    }

    private void setTextAndDrawable(String dayString, Integer status, Integer position) {

        switch (position) {
            case 0:
                //如果字符串是空的,代表这个位置不显示日期,实际上这里在遍历data的时候就已经做了判断了.
                if (!TextUtils.isEmpty(dayString)) {
                    TextView tv1 = getView(R.id.tv1);
                    tv1.setText(dayString);
                    PicassoHelper.getDefault().load(showDrawable(status)).into((ImageView) getView(R.id.iv1));
                }
                break;
            case 1:
                if (!TextUtils.isEmpty(dayString)) {
                    TextView tv2 = getView(R.id.tv2);
                    tv2.setText(dayString);
                    PicassoHelper.getDefault().load(showDrawable(status)).into((ImageView) getView(R.id.iv2));
                }
                break;
            case 2:
                if (!TextUtils.isEmpty(dayString)) {
                    TextView tv3 = getView(R.id.tv3);
                    tv3.setText(dayString);
                    PicassoHelper.getDefault().load(showDrawable(status)).into((ImageView) getView(R.id.iv3));
                }
                break;
            case 3:
                if (!TextUtils.isEmpty(dayString)) {
                    TextView tv4 = getView(R.id.tv4);
                    tv4.setText(dayString);
                    PicassoHelper.getDefault().load(showDrawable(status)).into((ImageView) getView(R.id.iv4));
                }
                break;
            case 4:
                if (!TextUtils.isEmpty(dayString)) {
                    TextView tv5 = getView(R.id.tv5);
                    tv5.setText(dayString);
                    PicassoHelper.getDefault().load(showDrawable(status)).into((ImageView) getView(R.id.iv5));
                }
                break;
            case 5:
                if (!TextUtils.isEmpty(dayString)) {
                    TextView tv6 = getView(R.id.tv6);
                    tv6.setText(dayString);
                    PicassoHelper.getDefault().load(showDrawable(status)).into((ImageView) getView(R.id.iv6));
                }

                break;
            case 6:
                if (!TextUtils.isEmpty(dayString)) {
                    TextView tv7 = getView(R.id.tv7);
                    tv7.setText(dayString);
                    PicassoHelper.getDefault().load(showDrawable(status)).into((ImageView) getView(R.id.iv7));
                }
                break;
        }
    }

    //设置天数下面的圈圈.先判断是不是0.是0的话,直接显示null,证明时间未到那天或者那天不在日历表中
    //如果不是0,再判断是不是1,是1的话就显示已签到,不是1的话就是2了,也就是未签到.所以不需要单独判断2.
    private int showDrawable(int status) {
        return status == CheckInActivityModel.sDrawableBottomStatusUnCheckedNotDay ? -1
                : (status == CheckInActivityModel.sDrawableBottomStatusChecked ? R.mipmap.icon_checked : R.mipmap.icon_uncheck);
    }
}
