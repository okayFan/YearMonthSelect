package com.okayfan.yearmonthselect;

import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * author: FYx
 * date:   On 2018/10/25
 * 年份选择
 */
public class YearAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {


    public int selectPosition = -1;

    public YearAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_year, item+"");
        LinearLayout ll_year = helper.getView(R.id.ll_year);
        TextView tv_year = helper.getView(R.id.tv_year);
        if (helper.getAdapterPosition() == selectPosition){
            ImageUtil.setBackground(ll_year,R.drawable.shape_corners_select);
            tv_year.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }else {
            ImageUtil.setBackground(ll_year,R.color.colorWhite);
            tv_year.setTextColor(mContext.getResources().getColor(R.color.color_666666));
        }
    }



}

