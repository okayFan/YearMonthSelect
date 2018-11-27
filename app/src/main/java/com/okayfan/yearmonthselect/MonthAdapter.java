package com.okayfan.yearmonthselect;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

/**
 * author: FYx
 * date:   On 2018/10/25
 * 月份选择
 */
public class MonthAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {


    public int currentYear = 0;//当前选择的年份

    public MonthAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_month, item+"月");
        LinearLayout ll_month = helper.getView(R.id.ll_month);
        if ((MainActivity.startYear == currentYear) && (MainActivity.endYear == currentYear)){
            //开始年和结束年一样
            if ((item >= MainActivity.startMonth) && (item <= MainActivity.endMonth)){
                ImageUtil.setBackground(ll_month,R.color.color_E4F2FD);
            }else {
                ImageUtil.setBackground(ll_month,R.color.colorWhite);
            }
        }else if (currentYear < MainActivity.startYear){
            //当前页面小于开始页面（不在页面选择范围内）
            ImageUtil.setBackground(ll_month,R.color.colorWhite);
        }else if (currentYear > MainActivity.endYear){
            //当前页面大于开始页面（不在页面选择范围内）
            ImageUtil.setBackground(ll_month,R.color.colorWhite);
        }else if ((currentYear > MainActivity.startYear) && (currentYear <MainActivity.endYear)){
            //当前页面在开始年和结束年之间（在选择范围内）
            ImageUtil.setBackground(ll_month,R.color.color_E4F2FD);
        }else if (MainActivity.startYear == currentYear){
            //当前页面是当前年（判断月份）
            if (item >= MainActivity.startMonth){
                ImageUtil.setBackground(ll_month,R.color.color_E4F2FD);
            }else {
                ImageUtil.setBackground(ll_month,R.color.colorWhite);
            }
        }else if (MainActivity.endYear == currentYear){
            //当前页面是结束年（判断月份）
            if (item <= MainActivity.endMonth){
                ImageUtil.setBackground(ll_month,R.color.color_E4F2FD);
            }else {
                ImageUtil.setBackground(ll_month,R.color.colorWhite);
            }
        }

    }



}
