package com.techjumper.polyhomeb.adapter.recycler_Data;

import java.util.List;
import java.util.Map;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class CheckInDayDataSingle {

    //List的position是 日历表某行的全部7条数据,当然例如第一行和最后一行,虽然说不是7条都有数据,但是size是7,只不过某些天数是空的,所以List也是空的
    //Map中装的是这个天数,也就是X号这天,具体的X字符串,已经X号这天的签到状态.
    //综上,List的大小永远为7.
    private List<Map<String, Integer>> dayList;

    public List<Map<String, Integer>> getDayList() {
        return dayList;
    }

    public void setDayList(List<Map<String, Integer>> dayList) {
        this.dayList = dayList;
    }
}
