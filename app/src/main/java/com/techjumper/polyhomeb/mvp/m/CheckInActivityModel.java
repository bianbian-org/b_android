package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.adapter.recycler_Data.CheckInDayData;
import com.techjumper.polyhomeb.adapter.recycler_Data.CheckInDayDataSingle;
import com.techjumper.polyhomeb.adapter.recycler_Data.CheckInTitleData;
import com.techjumper.polyhomeb.adapter.recycler_Data.CheckInWeekData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.CheckInDayBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.CheckInDayDataSingleBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.CheckInTitleBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.CheckInWeekBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.CheckInEntity;
import com.techjumper.polyhomeb.mvp.p.activity.CheckInActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/9
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class CheckInActivityModel extends BaseModel<CheckInActivityPresenter> {

    public static final int sDrawableBottomStatusChecked = 1;  //已签到  显示实心圈圈
    public static final int sDrawableBottomStatusUnChecked = 2;  //未签到,漏签   显示空心圈圈
    public static final int sDrawableBottomStatusUnCheckedNotDay = 0;  //未签到,不包含漏签!!.,时间未到那天或者那天不在日历表中.比如本月1号是日历表第一行的星期四,那么上个月的31号就是日历中的星期三,而星期三肯定不能显示出来,所以...  什么都不显示

    public CheckInActivityModel(CheckInActivityPresenter presenter) {
        super(presenter);
    }

    //点击签到  post
    public Observable<CheckInEntity> checkIn() {
        KeyValuePair keyValuePair = KeyValueCreator
                .checkIn(UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                        , UserManager.INSTANCE.getTicket());
        BaseArgumentsEntity baseArguments = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .checkIn(baseArguments)
                .compose(CommonWrap.wrap());
    }

    //获取签到数据  get
    public Observable<CheckInEntity> getCheckInData() {
        KeyValuePair checkInData = KeyValueCreator
                .getCheckInData(UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                        , UserManager.INSTANCE.getTicket());
        Map<String, String> map = NetHelper.createBaseArgumentsMap(checkInData);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .getCheckInData(map)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> getData(CheckInEntity checkInEntity) {

        List<DisplayBean> displayBeen = new ArrayList<>();
        int total;
        if (checkInEntity == null
                || checkInEntity.getData() == null
                || checkInEntity.getData().getSign_days() == null
                || checkInEntity.getData().getSign_days().size() == 0) {
            total = 0;
        } else {
            total = checkInEntity.getData().getSign_days().size();
        }

        List<Integer> signDays = checkInEntity.getData().getSign_days();

        Calendar instance = Calendar.getInstance();
        int date = instance.get(Calendar.DATE);  //今天是几号
        int week = instance.get(Calendar.DAY_OF_WEEK) - 1;  //今天是星期几  要减去1
        int month = instance.get(Calendar.MONTH) + 1;  //这个月是几月份   要加1
        int year = instance.get(Calendar.YEAR);         //今年是哪年
        int firstDayWeek = getMonthFirstDayWeek(year, month);   //当月1号是周几,1是周一,2是周二,0是周日
        int lastDayWeek = getMonthLastDayWeek();                //当月最后一天是星期几,1是周一,2是周二,0是周日
        boolean isCheck = false;  //当日是否签到了
        if (Constant.TRUE_ENTITY_RESULT.equals(checkInEntity.getData().getResult())) {
            isCheck = true;
        }
        for (Integer i : signDays) {
            if (i == date) {
                isCheck = true;
                break;
            }
        }

        //大标题  现在是几月,本月签到数
        CheckInTitleData checkInTitleData = new CheckInTitleData();
        checkInTitleData.setMonth(month);
        checkInTitleData.setTotal(total);
        CheckInTitleBean checkInTitleBean = new CheckInTitleBean(checkInTitleData);
        displayBeen.add(checkInTitleBean);

        //长分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);
        displayBeen.add(propertyPlacardDividerLongBean);

        //星期
        CheckInWeekData checkInWeekData = new CheckInWeekData();
        CheckInWeekBean checkInWeekBean = new CheckInWeekBean(checkInWeekData);
        displayBeen.add(checkInWeekBean);

        if (isCheck) {  //显示当月全部
            int lines = calculateLines(year, month);                //日历表行数
            int day = 1; //用++的方式,在设置给CheckInDayData的时候++一次.作为X月的所有30天的数字---只要是被put进map,就++

            for (int i = 0; i < lines; i++) {  //循环创建  lines个  横着的天数
                if (i == 0) {   //第一行较为特别,因为前面可能没有数字
                    CheckInDayData checkInDayData = new CheckInDayData();
                    List<Map<String, Integer>> list = new ArrayList<>();
                    for (int j = 0; j < 7; j++) {
                        //不管某行有几天,都是7条数据,只不过有些天数没有数据,是上个月的月末或者下个月月初,这部分数据不会显示在当月日历表中
                        if (0 == firstDayWeek) {//证明周日是本月第一天,那么意味着这一行7天全都有数据
                            Map<String, Integer> map = new HashMap<>();
                            map.put(day + "", isChecked(date, day, checkInEntity));
                            day++;
                            list.add(map);
                        } else {  //如果周日不是本月第一天,那就说明前面有X天是没有数据的,是上个月的月末几天.
                            if (j < firstDayWeek) {  //前面J天,都是上月月初
                                Map<String, Integer> map = new HashMap<>();
                                list.add(map);
                            } else {  //if之后的天数才是本月的从1号开始的号数
                                Map<String, Integer> map = new HashMap<>();
                                map.put(day + "", isChecked(date, day, checkInEntity));
                                day++;
                                list.add(map);
                            }
                        }
                    }
                    checkInDayData.setDayList(list);
                    CheckInDayBean checkInDayBean = new CheckInDayBean(checkInDayData);
                    displayBeen.add(checkInDayBean);
                } else if (i == lines - 1) {  //最后一行较为特别,因为前面可能没有数字
                    CheckInDayData checkInDayData = new CheckInDayData();
                    List<Map<String, Integer>> list = new ArrayList<>();
                    for (int j = 0; j < 7; j++) {
                        if (6 == lastDayWeek) {  //证明本月最后一天是周六,那就说明这最后一行数据是满的
                            Map<String, Integer> map = new HashMap<>();
                            map.put(day + "", isChecked(date, day, checkInEntity));
                            day++;
                            list.add(map);
                        } else {   //证明本月最后一天不是周六,那就说明最后一行数据不是满的,虽然size是7,但是后面有X个空数据.
                            if (j > lastDayWeek) {  //后面的J天都没数据,是下月的月初几天
                                Map<String, Integer> map = new HashMap<>();
                                list.add(map);
                            } else {   //前面J天是有数据的,是本月的月末那几天
                                Map<String, Integer> map = new HashMap<>();
                                map.put(day + "", isChecked(date, day, checkInEntity));
                                day++;
                                list.add(map);
                            }
                        }
                    }
                    checkInDayData.setDayList(list);
                    CheckInDayBean checkInDayBean = new CheckInDayBean(checkInDayData);
                    displayBeen.add(checkInDayBean);

                } else {  //中间的所有行数
                    CheckInDayData checkInDayData = new CheckInDayData();
                    List<Map<String, Integer>> list = new ArrayList<>();
                    for (int j = 0; j < 7; j++) {
                        Map<String, Integer> map = new HashMap<>();
                        map.put(day + "", isChecked(date, day, checkInEntity));
                        day++;
                        list.add(map);
                    }
                    checkInDayData.setDayList(list);
                    CheckInDayBean checkInDayBean = new CheckInDayBean(checkInDayData);
                    displayBeen.add(checkInDayBean);
                }
            }

        } else {      //显示当天所在的那周,只有一个Data

            Calendar calendar = Calendar.getInstance();
            int day_of_week_in_month = calendar.get(Calendar.WEEK_OF_MONTH); //今天是当前月的第几周
            int actualMaximum = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);  //指定年和月,月份中共有多少周. 2016-9-9,9月共有多少周

            if (day_of_week_in_month == 1) {  //当天在本月的第一周里面

                CheckInDayDataSingle checkInDataSingle = new CheckInDayDataSingle();
                List<Map<String, Integer>> list = new ArrayList<>();

                int day = 1;
                for (int i = 0; i < 7; i++) {
                    Map<String, Integer> map = new HashMap<>();
                    if (i < firstDayWeek) {
                        list.add(map);
                    } else {
                        map.put(day + "", isChecked(date, day, checkInEntity));
                        day++;
                        list.add(map);
                    }
                }

                checkInDataSingle.setDayList(list);
                CheckInDayDataSingleBean checkInDayDataSingleBean = new CheckInDayDataSingleBean(checkInDataSingle);
                displayBeen.add(checkInDayDataSingleBean);
            } else if (day_of_week_in_month == actualMaximum) { // 当天在本月的最后一周内

                CheckInDayDataSingle checkInDataSingle = new CheckInDayDataSingle();
                List<Map<String, Integer>> list = new ArrayList<>();

                int lastWeekDay = getCurrentMonthDay() - lastDayWeek;//当月总共N天,N-lastDayWeek = 最后一周的周日的号数.所以号数++,直到本月最后一天即可
                ToastUtils.show(lastWeekDay + "");
                for (int i = 0; i < 7; i++) {
                    Map<String, Integer> map = new HashMap<>();
                    if (i <= lastDayWeek) {
                        map.put(lastWeekDay + "", isChecked(date, lastWeekDay, checkInEntity));
                        lastWeekDay++;
                        list.add(map);
                    } else {
                        list.add(map);
                    }
                }

                checkInDataSingle.setDayList(list);
                CheckInDayDataSingleBean checkInDayDataSingleBean = new CheckInDayDataSingleBean(checkInDataSingle);
                displayBeen.add(checkInDayDataSingleBean);
            } else {  //是当月的中间部分

                CheckInDayDataSingle checkInDataSingle = new CheckInDayDataSingle();
                List<Map<String, Integer>> list = new ArrayList<>();

                Calendar calendar1 = Calendar.getInstance();
                int currentDay = calendar1.get(Calendar.DATE);  //今天是几号
                //今天是周几  int week
                int tempDay = currentDay - week;
                for (int i = 0; i < 7; i++) {
                    if (i <= week) { //今天之前的那几天,从周日开始,包括今天(理论上来讲,今天和今天之前的这几天,圈圈都是要显示的)
                        Map<String, Integer> map = new HashMap<>();
                        map.put(tempDay + "", isChecked(currentDay, tempDay, checkInEntity));
                        tempDay++;
                        list.add(map);
                    } else { //今天之后的那几天,一直到周六(理论上来讲,else里面的数据,圈圈都是不显示的)
                        Map<String, Integer> map = new HashMap<>();
                        map.put(tempDay + "", isChecked(currentDay, tempDay, checkInEntity));
                        tempDay++;
                        list.add(map);
                    }
                }

                checkInDataSingle.setDayList(list);
                CheckInDayDataSingleBean checkInDayDataSingleBean = new CheckInDayDataSingleBean(checkInDataSingle);
                displayBeen.add(checkInDayDataSingleBean);
            }
        }
        return displayBeen;
    }

    /**
     * 计算日历的行数
     */
    private int calculateLines(int year, int month) {
        int days = getCurrentMonthDays();            //得到X月的天数
        String date = year + "-" + month + "-" + 1;  //得到字符串xxxx-xx-1,即2016-9-9;
        String week = "";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            week = formatter.format(myDate);   //得到具体的 "周四" 或者可能是Tuesday或者是Tue
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int weekPlace = getWeekInteger(week);  //得到周X在日历表中的位置.0是周日(从左往右第一个,position为0),1是周一从(左往右第2个,position为1),2是周二,...

        if (days == 28 && weekPlace == 0) { //当某年的2月只有28天,并且2月1号是周日,那么就意味着,,,这个日历表只有4行
            return 4;
        }
        if (days == 31 && (weekPlace == 5 || weekPlace == 6)) {//当某年的X月有31天,并且X月1号是周五或者周六,那么就意味着,,,这个日历表有6行
            return 6;
        }
        if (days == 31 && weekPlace == 6) {//当某年的X月有30天,并且X月1号是周六,那么就意味着,,,这个日历表有6行
            return 6;
        }
        return 5; //除此之外,日历表都是5行
    }

    /**
     * 获取当月1号是周几
     */
    private int getMonthFirstDayWeek(int year, int month) {
        String date = year + "-" + month + "-" + 1;  //得到字符串xxxx-xx-1,即2016-9-9;
        String week = "";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            week = formatter.format(myDate);   //得到具体的 "周四" 或者可能是Tuesday或者是Tue
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int weekPlace = getWeekInteger(week);  //得到周X在日历表中的位置.0是周日(从左往右第一个,position为0),1是周一从(左往右第2个,position为1),2是周二,...
        return weekPlace;
    }

    /**
     * 获取当月最后一天是周几
     */
    private int getMonthLastDayWeek() {
        Calendar cal = Calendar.getInstance();//获取当前日期
        cal.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        cal.add(Calendar.MONTH, 1);//月增加1天
        cal.add(Calendar.DAY_OF_MONTH, -1);//日期倒数一日,既得到本月最后一天
        Date time = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        String format = formatter.format(time);  //得到具体的 "周四" 或者可能是Tuesday或者是Tue
        return getWeekInteger(format);  //得到周X在日历表中的位置.0是周日(从左往右第一个,position为0),1是周一从(左往右第2个,position为1),2是周二,...
    }

    /**
     * 获取当月天数
     */
    private int getCurrentMonthDays() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 转换星期为当前这个日历表的行的位置
     * <>此处不能用String替换,因为一旦替换,将无法正确匹配到星期</>
     */
    private int getWeekInteger(String string) {

        switch (string) {
            case "星期一":
            case "周一":
            case "Monday":
            case "Mon":
            case "mon":
                return 1;
            case "星期二":
            case "周二":
            case "Tuesday":
            case "Tue":
            case "tue":
                return 2;
            case "星期三":
            case "周三":
            case "Wednesday":
            case "Wed":
            case "wed":
                return 3;
            case "星期四":
            case "周四":
            case "Thursday":
            case "Thurs":
                return 4;
            case "星期五":
            case "周五":
            case "Friday":
            case "Fri":
            case "fri":
                return 5;
            case "星期六":
            case "周六":
            case "Saturday":
            case "Sat.":
            case "St.":
                return 6;
            case "星期天":
            case "周日":
            case "周天":
            case "Sun.":
                return 0;
        }
        return -1;

    }

    /**
     * 判断某天是否已经签到
     */
    private int isChecked(int currentDay, int day, CheckInEntity checkInEntity) {
        for (Integer entity : checkInEntity.getData().getSign_days()) {
            if (day == entity) {
                return sDrawableBottomStatusChecked;  //已经签到
            }
        }
        if (currentDay < day) {  //如果当天号数小于需要显示的号数,说明不需要显示任何圈圈,因为那天还没到
            return sDrawableBottomStatusUnCheckedNotDay;
        } else {  //否则就显示未签到的圈圈
            return sDrawableBottomStatusUnChecked;
        }
    }

    /**
     * 获取当月的 天数
     */
    private int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
