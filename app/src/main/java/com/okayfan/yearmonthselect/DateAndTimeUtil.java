package com.okayfan.yearmonthselect;

import java.util.Calendar;

/**
 * author: FYx
 * date:   On 2018/11/27
 */
public class DateAndTimeUtil {


    /**
     * 得到当前年份
     * @return
     */
    public static int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }


    /**
     * 得到当前月份
     * @return
     */
    public static int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }





}
