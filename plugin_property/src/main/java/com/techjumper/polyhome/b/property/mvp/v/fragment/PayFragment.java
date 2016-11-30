package com.techjumper.polyhome.b.property.mvp.v.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;
import com.steve.creact.library.display.DisplayBean;
import com.techjumper.commonres.entity.PayEntity;
import com.techjumper.commonres.entity.event.BackEvent;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.mvp.p.fragment.PayFragmentPresenter;
import com.techjumper.polyhome.b.property.mvp.v.activity.MainActivity;
import com.techjumper.polyhome.b.property.utils.AdapterUtil;
import com.techjumper.polyhome.b.property.viewholder.databean.PayEntityBean;
import com.techjumper.polyhome.b.property.widget.PayCheckView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@Presenter(PayFragmentPresenter.class)
public class PayFragment extends AppBaseFragment<PayFragmentPresenter> {

    @Bind(R.id.rb_no)
    RadioButton rbNo;
    @Bind(R.id.rb_yes)
    RadioButton rbYes;
    @Bind(R.id.total_price)
    TextView totalPrice;
    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.layout_pay_list)
    LinearLayout layoutPayList;
    @Bind(R.id.layout_pay_detail)
    RelativeLayout layoutPayDetail;
    @Bind(R.id.layout_pay_result)
    LinearLayout layoutPayResult;
    @Bind(R.id.detail_name)
    TextView detailName;
    @Bind(R.id.detail_type)
    TextView detailType;
    @Bind(R.id.detail_object)
    TextView detailObject;
    @Bind(R.id.detail_deadline)
    TextView detailDeadline;
    @Bind(R.id.detail_normal)
    TextView detailNormal;
    @Bind(R.id.detail_putoff)
    TextView detailPutoff;
    @Bind(R.id.detail_wechat)
    PayCheckView detailWechat;
    @Bind(R.id.detail_alipay)
    PayCheckView detailAlipay;
    @Bind(R.id.detail_total_price)
    TextView detailTotalPrice;
    @Bind(R.id.detail_submit)
    TextView detailSubmit;
    @Bind(R.id.result_name)
    TextView resultName;
    @Bind(R.id.result_qrcode)
    ImageView resultQrcode;
    @Bind(R.id.result_total_price)
    TextView resultTotalPrice;

    private PayFragmentPresenter presenter;
    private int type;
    private String totalPriceString;

    private CommonRecyclerAdapter adapter;
    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TextView getTotalPrice() {
        return totalPrice;
    }

    public PayCheckView getDetailWechat() {
        return detailWechat;
    }

    public PayCheckView getDetailAlipay() {
        return detailAlipay;
    }

    public ImageView getResultQrcode() {
        return resultQrcode;
    }

    public static PayFragment getInstance(int type) {
        PayFragment payFragment = new PayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.TYPE, type);
        payFragment.setArguments(bundle);
        return payFragment;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        adapter = new CommonRecyclerAdapter();
        list.setLayoutManager(manager);
        type = getArguments().getInt(MainActivity.TYPE);

        presenter = new PayFragmentPresenter();
    }

    public void getOrders(List<PayEntity.PayItemEntity> entities, int page) {
        if (entities.size() == 0 && page == 1) {
            AdapterUtil.clear(adapter);
            return;
        }

        List<DisplayBean> displayBeans = new ArrayList<>();
        if (entities == null || entities.size() == 0)
            return;

        showList();

        for (int i = 0; i < entities.size(); i++) {
            displayBeans.add(new PayEntityBean(entities.get(i)));
        }

        if (page == 1) {
            adapter.loadData(displayBeans);
            list.setAdapter(adapter);
        } else {
            adapter.insertData(adapter.getItemCount(), displayBeans);
        }
    }

    public void showPayDetail(PayEntity.PayItemEntity entity) {
        if (entity == null)
            return;

        showDetail();

        detailName.setText(getString(R.string.property_pay_name) + entity.getPay_name());
        detailType.setText(getString(R.string.property_pay_type) + entity.getPay_type());
        detailObject.setText(getString(R.string.property_pay_object) + entity.getObject());
        detailDeadline.setText(getString(R.string.property_pay_deadline) + entity.getExpiry_date());
        detailNormal.setText(Html.fromHtml(getString(R.string.property_pay_normal) + "<font color='#ff6700'>" + "￥" + entity.getPrice() + "</font>"));
        detailPutoff.setText(Html.fromHtml(getString(R.string.property_pay_putoff) + "<font color='#ff6700'>" + "￥" + entity.getExpiry_price() + "</font>"));

        totalPriceString = String.valueOf(Double.valueOf(entity.getPrice()) + Double.valueOf(entity.getExpiry_price()));

        detailTotalPrice.setText("￥" + totalPriceString);

        RxBus.INSTANCE.send(new BackEvent(BackEvent.PAY_DETAIL));
    }

    public void showPayResult(PayEntity.PayItemEntity entity) {
        showResult();
        RxBus.INSTANCE.send(new BackEvent(BackEvent.PAY_RESULT));

        resultName.setText(getString(R.string.property_pay_name) + " " + entity.getPay_name());
        resultTotalPrice.setText(" ￥" + totalPriceString);
    }

    public void showList() {
        layoutPayList.setVisibility(View.VISIBLE);
        layoutPayDetail.setVisibility(View.GONE);
        layoutPayResult.setVisibility(View.GONE);
    }

    public void showDetail() {
        layoutPayList.setVisibility(View.GONE);
        layoutPayDetail.setVisibility(View.VISIBLE);
        layoutPayResult.setVisibility(View.GONE);
    }

    public void showResult() {
        layoutPayList.setVisibility(View.GONE);
        layoutPayDetail.setVisibility(View.GONE);
        layoutPayResult.setVisibility(View.VISIBLE);
    }
}
