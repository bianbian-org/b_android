package com.techjumper.polyhome.b.property.mvp.p.fragment;

import android.os.Bundle;

import com.techjumper.polyhome.b.property.R;
import com.techjumper.polyhome.b.property.hehe.AnnounHehe;
import com.techjumper.polyhome.b.property.hehe.ComplaintHehe;
import com.techjumper.polyhome.b.property.hehe.RepairHehe;
import com.techjumper.polyhome.b.property.mvp.m.ListFragmentModel;
import com.techjumper.polyhome.b.property.mvp.v.fragment.ListFragment;

import java.util.List;

import butterknife.OnCheckedChanged;

/**
 * Created by kevin on 16/5/12.
 */
public class ListFragmentPresenter extends AppBaseFragmentPresenter<ListFragment> {
    private ListFragmentModel model = new ListFragmentModel(this);

    @OnCheckedChanged(R.id.fl_title_announcement)
    void checkAnnouncement(boolean check) {
        if (check) {
            getView().getAnnounHehes(getAnnounHehes());
        }
    }

    @OnCheckedChanged(R.id.fl_title_repair)
    void checkRepair(boolean check) {
        if (check) {
            getView().getComplaintHehes(getComplaintHehes());
        }
    }

    @OnCheckedChanged(R.id.fl_title_complaint)
    void checkComplaint(boolean check) {
        if (check) {
            getView().getRepairHehes(getRepairHehes());
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        getView().getAnnounHehes(getAnnounHehes());
    }


    public List<AnnounHehe> getAnnounHehes() {
        List<AnnounHehe> announHehes = model.getAnnounHehes();
        AnnounHehe announHehe = new AnnounHehe();
        announHehe.setTitle("本周六八点开始实行宵禁");
        announHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        announHehe.setDate("10月23日");
        announHehe.setHasRead(AnnounHehe.HASREAD_TURE);
        announHehes.add(announHehe);

        announHehe = new AnnounHehe();
        announHehe.setTitle("本周六八点开始实行宵禁");
        announHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        announHehe.setDate("10月23日");
        announHehe.setHasRead(AnnounHehe.HASREAD_FALSE);
        announHehes.add(announHehe);

        announHehe = new AnnounHehe();
        announHehe.setTitle("本周六八点开始实行宵禁");
        announHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        announHehe.setDate("10月23日");
        announHehe.setHasRead(AnnounHehe.HASREAD_FALSE);
        announHehes.add(announHehe);

        announHehe = new AnnounHehe();
        announHehe.setTitle("本周六八点开始实行宵禁");
        announHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        announHehe.setDate("10月23日");
        announHehe.setHasRead(AnnounHehe.HASREAD_FALSE);
        announHehes.add(announHehe);

        announHehe = new AnnounHehe();
        announHehe.setTitle("本周六八点开始实行宵禁");
        announHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        announHehe.setDate("10月23日");
        announHehe.setHasRead(AnnounHehe.HASREAD_TURE);
        announHehes.add(announHehe);

        getView().getAnnounHehes(announHehes);

        return announHehes;
    }

    public List<ComplaintHehe> getComplaintHehes() {
        List<ComplaintHehe> complaintHehes = model.getComplaintHehes();
        ComplaintHehe complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new ComplaintHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehes.add(complaintHehe);

        return complaintHehes;
    }

    public List<RepairHehe> getRepairHehes() {
        List<RepairHehe> complaintHehes = model.getRepairHehes();
        RepairHehe complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_FINISH);
        complaintHehe.setContent("尊敬的用户，您在8:24时提交的洗衣订单申请，已经被我公司接到，我们将在最快的时间内与您联系，工赶到您的家里，您在8:24时提交的洗衣订单申请，已经被我公司接到我司的");
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_RESPONSE);
        complaintHehes.add(complaintHehe);

        complaintHehe = new RepairHehe();
        complaintHehe.setTitle("个人保修-门窗: 请及时的所得税费");
        complaintHehe.setDate("10月23日");
        complaintHehe.setHasRead(ComplaintHehe.HASREAD_TURE);
        complaintHehe.setType(ComplaintHehe.TYPE_SUBMIT);
        complaintHehes.add(complaintHehe);

        return complaintHehes;
    }
}
