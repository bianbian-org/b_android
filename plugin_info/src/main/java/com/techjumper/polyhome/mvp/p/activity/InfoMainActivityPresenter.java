package com.techjumper.polyhome.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.commonres.entity.InfoEntity;
import com.techjumper.polyhome.InfoEntityTemporary;
import com.techjumper.polyhome.mvp.m.InfoMainActivityModel;
import com.techjumper.polyhome.mvp.v.activity.InfoMainActivity;

import java.util.List;

/**
 * Created by kevin on 16/4/29.
 */
public class InfoMainActivityPresenter extends AppBaseActivityPresenter<InfoMainActivity> {
    InfoMainActivityModel infoMainActivityModel = new InfoMainActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getList();
    }

    public void getList(){
        List<InfoEntity.infoDataEntity> list = infoMainActivityModel.getList();

        InfoEntity.infoDataEntity infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(0);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(1);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(0);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(1);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(2);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(2);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(0);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(2);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(2);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(3);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(2);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(1);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(0);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(3);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(0);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(2);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(0);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(1);
        list.add(infoEntityTemporary);

        infoEntityTemporary = new InfoEntity.infoDataEntity();
        infoEntityTemporary.setHas_read(0);
        infoEntityTemporary.setCreated_at("10月23日");
        infoEntityTemporary.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您 联系，工作人员将赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的打打大的萨达萨达大大大大");
        infoEntityTemporary.setTitle("您的洗衣订单已激活，请等待工作人员上门");
        infoEntityTemporary.setTypes(2);
        list.add(infoEntityTemporary);

        getView().getList(list);
    }
}
