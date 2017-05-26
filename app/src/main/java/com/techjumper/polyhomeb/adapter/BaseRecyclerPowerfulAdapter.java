package com.techjumper.polyhomeb.adapter;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DataBean;
import com.steve.creact.library.display.DisplayBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/5/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public abstract class BaseRecyclerPowerfulAdapter extends CommonRecyclerAdapter {

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        DisplayBean bindBean = data.get(position);
        setListener(holder, bindBean, position);
        if (bindBean instanceof DataBean) {
            ((DataBean) bindBean).bindData(holder);
        }
    }

    public abstract void setListener(BaseRecyclerViewHolder holder, DisplayBean bindBean, int position);

}
