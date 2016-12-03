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

import static com.techjumper.polyhomeb.Constant.KEY_JUJIA_JUMP_URL;

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
    private LinkedList<JuJiaInfoEntity.DataBean.ServersBean> mDatas = new LinkedList<>();

    private View mParentView;

    public JuJiaViewHolder(View itemView) {
        super(itemView);
        mParentView = getView(R.id.parent_view);
    }

    @Override
    public void setData(JuJiaData data) {
        if (data == null || data.getEntity() == null || data.getEntity().getServers() == null
                || data.getEntity().getServers().size() == 0) {
            if (mParentView == null) return;
            mParentView.setVisibility(View.GONE);
            return;
        }
        initItems();
        if (mParentView == null) return;
        mParentView.setVisibility(View.VISIBLE);
        if (mViews.size() == 0) return;
        clearViewsData();
        JuJiaInfoEntity.DataBean entity = data.getEntity();
        mDatas.clear();
        mDatas.addAll(entity.getServers());
        JuJiaInfoEntity.DataBean.ServersBean more = new JuJiaInfoEntity.DataBean.ServersBean();
        more.setPage_url(entity.getPage_url());
        more.setCover_url(null);
        more.setServer_name(getContext().getString(R.string.more));
        mDatas.add(more);

        if (!TextUtils.isEmpty(entity.getPage_url())) {
            setOnClickListener(R.id.tv_header, v -> jump2JujiaPage(entity.getPage_url()));
        }

        //只设置服务器返回的原始数据
        for (int i = 0; i <= mDatas.size() - 2; i++) {
            if (TextUtils.isEmpty(mDatas.get(i).getCover_url()))
                mViews.get(i).setIcon(R.mipmap.icon_jujia_default);
            else
                mViews.get(i).setIcon(Config.sHost + mDatas.get(i).getCover_url());
            mViews.get(i).setText(TextUtils.isEmpty(mDatas.get(i).getServer_name())
                    ? getContext().getString(R.string.jujia_item_name) : mDatas.get(i).getServer_name());

            int finalI = i;
            mViews.get(i).setOnClickListener(v -> jump2JujiaPage(mDatas.get(finalI).getPage_url()));
        }

        //设置手动加入的数据
        mViews.get(mDatas.size() - 1).setIcon(R.mipmap.icon_more);
        mViews.get(mDatas.size() - 1).setText(getContext().getString(R.string.more));
        mViews.get(mDatas.size() - 1).setOnClickListener(v -> jump2JujiaPage(mDatas.get(mDatas.size() - 1).getPage_url()));
    }

    private void clearViewsData() {
        for (PolyModeView view : mViews) {
            view.setIcon(0);
            view.setText("");
            view.setOnClickListener(null);
        }
    }

    private void initItems() {
        mViews.clear();
        mViews.add(getView(R.id.first));
        mViews.add(getView(R.id.second));
        mViews.add(getView(R.id.third));
        mViews.add(getView(R.id.fourth));
    }

    private void jump2JujiaPage(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_JUJIA_JUMP_URL, Config.sHost + url);
        new AcHelper.Builder((Activity) getContext())
                .target(JujiaDetailWebActivity.class)
                .extra(bundle)
                .start();
    }

}
