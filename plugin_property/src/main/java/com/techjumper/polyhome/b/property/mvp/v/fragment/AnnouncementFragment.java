package com.techjumper.polyhome.b.property.mvp.v.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.commonres.ComConstant;
import com.techjumper.commonres.entity.AnnouncementEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.commonres.entity.event.PropertyNormalDetailEvent;
import com.techjumper.commonres.entity.event.loadmoreevent.LoadmorePresenterEvent;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.p.fragment.AnnouncementPresenter;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.utils.AdapterUtil;
import com.techjumper.polyhome.b.property.viewholder.databean.InfoAnnouncementEntityBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/10/31.
 */
@Presenter(AnnouncementPresenter.class)
public class AnnouncementFragment extends AppBaseFragment<AnnouncementPresenter> {

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.lnd_title)
    TextView lndTitle;
    @Bind(R.id.lnd_date)
    TextView lndDate;
    @Bind(R.id.lnd_content)
    WebView lndContent;
    @Bind(R.id.lnd_layout)
    LinearLayout lndLayout;

    private CommonRecyclerAdapter adapter;
    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static AnnouncementFragment getInstance(int type) {
        AnnouncementFragment announcementFragment = new AnnouncementFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.TYPE, type);
        announcementFragment.setArguments(bundle);
        return announcementFragment;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcement, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getArguments().getInt(MainActivity.TYPE);

        adapter = new CommonRecyclerAdapter();
        list.setLayoutManager(manager);
    }

    public void showLndLayout(PropertyNormalDetailEvent event) {
        if (event == null)
            return;

        lndLayout.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);

        lndTitle.setText(event.getTitle());
        lndDate.setText(event.getDate());

        lndContent.loadDataWithBaseURL(null, event.getContent(), "text/html", "utf-8", null);
        lndContent.getSettings().setJavaScriptEnabled(true);
        lndContent.setWebChromeClient(new WebChromeClient());

        RxBus.INSTANCE.send(new BackEvent(BackEvent.PROPERTY_LIST));

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (adapter != null && String.valueOf(adapter.getItemCount()).equals(ComConstant.PAGESIZE)) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int lastVisiblePosition = manager.findLastVisibleItemPosition();
                        if (lastVisiblePosition >= manager.getItemCount() - 1) {
                            RxBus.INSTANCE.send(new LoadmorePresenterEvent(type));
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void getAnnouncements(List<AnnouncementEntity.AnnouncementDataEntity> announcementDataEntities, int page) {
        lndLayout.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);


        if (announcementDataEntities.size() == 0 && page == 1) {
            AdapterUtil.clear(adapter);
            return;
        }

        List<DisplayBean> displayBeans = new ArrayList<>();

        if (announcementDataEntities == null || announcementDataEntities.size() == 0)
            return;

        for (int i = 0; i < announcementDataEntities.size(); i++) {
            displayBeans.add(new InfoAnnouncementEntityBean(announcementDataEntities.get(i)));
        }

        if (page == 1) {
            adapter.loadData(displayBeans);
            list.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }
    }
}
