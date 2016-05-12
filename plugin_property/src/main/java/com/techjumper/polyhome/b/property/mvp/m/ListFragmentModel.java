package com.techjumper.polyhome.b.property.mvp.m;

import com.techjumper.corelib.mvp.model.BaseModel;
import com.techjumper.polyhome.b.property.hehe.AnnounHehe;
import com.techjumper.polyhome.b.property.hehe.ComplaintHehe;
import com.techjumper.polyhome.b.property.hehe.RepairHehe;
import com.techjumper.polyhome.b.property.mvp.p.fragment.ListFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 16/5/12.
 */
public class ListFragmentModel extends BaseModel<ListFragmentPresenter> {

    public ListFragmentModel(ListFragmentPresenter presenter) {
        super(presenter);
    }

    public List<AnnounHehe> getAnnounHehes() {
        return new ArrayList<AnnounHehe>();
    }

    public List<ComplaintHehe> getComplaintHehes() {
        return new ArrayList<ComplaintHehe>();
    }

    public List<RepairHehe> getRepairHehes() {
        return new ArrayList<RepairHehe>();
    }
}
