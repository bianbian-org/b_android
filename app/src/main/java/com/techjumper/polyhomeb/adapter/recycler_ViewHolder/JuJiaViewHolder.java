package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.JuJiaData;
import com.techjumper.polyhomeb.entity.JuJiaInfoEntity;
import com.techjumper.polyhomeb.mvp.v.activity.JujiaDetailWebActivity;
import com.techjumper.polyhomeb.widget.PolyModeView;

import java.util.LinkedList;
import java.util.List;

import static com.techjumper.polyhomeb.Constant.FIRST_PAGE_URL;
import static com.techjumper.polyhomeb.Constant.MORE_PAGE_URL;
import static com.techjumper.polyhomeb.Constant.SECOND_PAGE_URL;
import static com.techjumper.polyhomeb.Constant.THIRD_PAGE_URL;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "JuJiaDataBean", data = JuJiaData.class)
public class JuJiaViewHolder extends BaseRecyclerViewHolder<JuJiaData> {

    public static final int LAYOUT_ID = R.layout.item_home_page_jujia;

    private LinkedList<PolyModeView> mViews = new LinkedList<>();

    public JuJiaViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(JuJiaData data) {
        if (data == null || data.getEntity() == null || data.getEntity().getServers() == null
                || data.getEntity().getServers().size() == 0) {
            setVisibility(R.id.root_view, View.GONE);
            return;
        }
        initItems();
        setVisibility(R.id.root_view, View.VISIBLE);
        JuJiaInfoEntity.DataBean entity = data.getEntity();
        List<JuJiaInfoEntity.DataBean.ServersBean> servers = entity.getServers();
        int has_more = entity.getHas_more();
        String page_url = entity.getPage_url();

        //标题和更多的点击事件
        if (!TextUtils.isEmpty(page_url)) {
            setOnClickListener(R.id.tv_header, v -> jump2JujiaPage(page_url, MORE_PAGE_URL));
            setOnClickListener(R.id.more, v -> jump2JujiaPage(page_url, MORE_PAGE_URL));
        }

        if (mViews.size() == 0) return;
        for (int i = 0; i < servers.size(); i++) {
            if (TextUtils.isEmpty(servers.get(i).getCover_url()))
                mViews.get(i).setIcon(R.mipmap.icon_jujia_default);
            else
                mViews.get(i).setIcon(Config.sHost + servers.get(i).getCover_url());
            mViews.get(i).setText(TextUtils.isEmpty(servers.get(i).getServer_name())
                    ? getContext().getString(R.string.jujia_item_name) : servers.get(i).getServer_name());
        }
        setClickListener(servers);
    }

    private void initItems() {
        mViews.clear();
        mViews.add(getView(R.id.first));
        mViews.add(getView(R.id.second));
        mViews.add(getView(R.id.third));
    }

    private void setClickListener(List<JuJiaInfoEntity.DataBean.ServersBean> servers) {
        if (mViews.size() == 0) return;
        switch (servers.size()) {
            case 1:
                mViews.get(0).setOnClickListener(v -> jump2JujiaPage(servers.get(0).getPage_url(), FIRST_PAGE_URL));
                break;
            case 2:
                mViews.get(0).setOnClickListener(v -> jump2JujiaPage(servers.get(0).getPage_url(), FIRST_PAGE_URL));
                mViews.get(1).setOnClickListener(v -> jump2JujiaPage(servers.get(1).getPage_url(), SECOND_PAGE_URL));
                break;
            case 3:
                mViews.get(0).setOnClickListener(v -> jump2JujiaPage(servers.get(0).getPage_url(), FIRST_PAGE_URL));
                mViews.get(1).setOnClickListener(v -> jump2JujiaPage(servers.get(1).getPage_url(), SECOND_PAGE_URL));
                mViews.get(2).setOnClickListener(v -> jump2JujiaPage(servers.get(2).getPage_url(), THIRD_PAGE_URL));
                break;
        }
    }

    private void jump2JujiaPage(String url, String key) {
        Bundle bundle = new Bundle();
        bundle.putString(key, Config.sHost + url);
        new AcHelper.Builder((Activity) getContext()).target(JujiaDetailWebActivity.class).extra(bundle).start();
    }

}
