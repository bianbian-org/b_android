package com.techjumper.polyhomeb.adapter;

import com.steve.creact.library.display.DisplayBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.PolyHomeViewHolder;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomePageAdapter extends BaseRecyclerPowerfulAdapter {

    private IListener iListener;

    @Override
    public void setListener(BaseRecyclerViewHolder holder, DisplayBean bindBean, int position) {
        if (!(holder instanceof PolyHomeViewHolder))
            return;
        holder.setOnClickListener(R.id.layout_poly_home, v -> {
            if (iListener != null)
                iListener.onSmartHomeClick();
        });
    }

    public void setClickListener(IListener iListener) {
        this.iListener = iListener;
    }

    public interface IListener {
        void onSmartHomeClick();
    }

}
