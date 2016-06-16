package com.techjumper.polyhome.b.info.widget;

import com.steve.creact.library.adapter.CommonRecyclerAdapter;

/**
 * 对于CommonRecyclerAdapter 操作的一些工具类
 * Created by kevin on 16/5/17.
 */
public class AdapterUtil {

    /**
     * 清空所有数据
     *
     * @param adapter
     */
    public static void clear(CommonRecyclerAdapter adapter) {
        if (adapter == null)
            return;

        int size = adapter.getData().size();

        if (size == 0)
            return;

        adapter.removeData(new int[size]);
        adapter.notifyDataSetChanged();
    }
}
