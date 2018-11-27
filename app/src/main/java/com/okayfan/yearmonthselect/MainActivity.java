package com.okayfan.yearmonthselect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pedaily.yc.ycdialoglib.dialogFragment.BottomDialogFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private BottomDialogFragment yearPickerDialogFragment;
    public static int startYear = 0;//开始年份
    public static int endYear = 0;//结束年份
    public static int startMonth = 0;//开始月份
    public static int endMonth = 0;//结束月份
    public static boolean isRange = false;//月份是否是选择范围
    boolean isShowYear = false;//是否展示年
    private String startTime;//开始时间
    private String endTime;//结束时间
    private Button b_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b_year = findViewById(R.id.b_year);
        b_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearPicker();
            }
        });
    }



    private void showYearPicker() {
        yearPickerDialogFragment = BottomDialogFragment.create(getSupportFragmentManager())
                .setViewListener(v -> initYearPick(v))
                .setLayoutRes(R.layout.dialog_bottom_year_picker)
                .setDimAmount(0.5f)
                .setTag("yearDialog")
                .setCancelOutside(true)
                .setHeight(DensityUtil.dip2px(this, 285));
        yearPickerDialogFragment.show();
    }





    private void initYearPick(View v) {
        TextView tv_year = v.findViewById(R.id.tv_year);
        LinearLayout ll_sure = v.findViewById(R.id.ll_sure);
        RecyclerView rv_month = v.findViewById(R.id.rv_month);
        RecyclerView rv_year = v.findViewById(R.id.rv_year);
        //初始化开始结束年月日
        startYear = DateAndTimeUtil.getCurrentYear();//默认为当前年
        startMonth = DateAndTimeUtil.getCurrentMonth();//默认为当前月
        endYear = startYear;
        endMonth = startMonth;
        isRange = false;
        //初始化年份数据
        int defaultYearPosition = 0;
        ArrayList<Integer> years = new ArrayList<>();
        for (int i = 2000; i < 2101; i++) {
            years.add(i);
            if (i == DateAndTimeUtil.getCurrentYear()) {
                defaultYearPosition = years.size() - 1;
            }
        }
        //初始化月份数据
        ArrayList<Integer> months = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            months.add(i);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rv_month.setLayoutManager(gridLayoutManager);
        MonthAdapter monthAdapter = new MonthAdapter(R.layout.item_month, months);
        monthAdapter.currentYear = startYear;
        rv_month.setAdapter(monthAdapter);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 5);
        rv_year.setLayoutManager(gridLayoutManager1);
        YearAdapter yearAdapter = new YearAdapter(R.layout.item_year, years);
        yearAdapter.selectPosition = defaultYearPosition;
        rv_year.setAdapter(yearAdapter);
        rv_year.setVisibility(View.GONE);

        tv_year.setOnClickListener(v1 -> {
            if (isShowYear) {
                isShowYear = false;
                rv_year.setVisibility(View.GONE);
                rv_month.setVisibility(View.VISIBLE);
            } else {
                isShowYear = true;
                rv_year.setVisibility(View.VISIBLE);
                rv_month.setVisibility(View.GONE);
            }
        });

        yearAdapter.setOnItemClickListener((adapter, view, position) -> {
            int year = (Integer) adapter.getData().get(position);
            tv_year.setText(year + "");
            yearAdapter.selectPosition = position;
            yearAdapter.notifyDataSetChanged();
            rv_year.setVisibility(View.GONE);
            rv_month.setVisibility(View.VISIBLE);
            monthAdapter.currentYear = year;
            monthAdapter.notifyDataSetChanged();
        });

        monthAdapter.setOnItemClickListener((adapter, view, position) -> {
            int currentMonth = (int) adapter.getData().get(position);
            int currentYear = Integer.parseInt(tv_year.getText().toString());
            if ((currentYear == startYear) && (currentYear == endYear)) {
                //当前选中年份和开始结束年相同
                if (isRange) {
                    isRange = false;
                    startMonth = currentMonth;
                    endMonth = currentMonth;
                } else {
                    isRange = true;
                    if (currentMonth < startMonth) {
                        startMonth = currentMonth;
                    } else if (currentMonth > endMonth) {
                        endMonth = currentMonth;
                    }
                }
            } else if (currentYear < startYear) {
                //当前选中年小于开始年
                if (isRange) {
                    setSameYearAndYear(currentYear, currentMonth);
                } else {
                    isRange = true;
                    startYear = currentYear;
                    startMonth = currentMonth;
                }
            } else if (currentYear > endYear) {
                //当前选中年大于开始年
                if (isRange) {
                    setSameYearAndYear(currentYear, currentMonth);
                } else {
                    isRange = true;
                    endYear = currentYear;
                    endMonth = currentMonth;
                }
            } else if ((currentYear > startYear) && (currentYear < endYear)) {
                //当前选中年在开始年和结束年之间
                if (isRange) {
                    setSameYearAndYear(currentYear, currentMonth);
                } else {
                    isRange = true;
                    endYear = currentYear;
                    endMonth = currentMonth;
                }
            } else if (currentYear == startYear) {
                //当前选中年等于开始年
                if (isRange) {
                    setSameYearAndYear(currentYear, currentMonth);
                } else {
                    isRange = false;
                    startMonth = currentMonth;
                }
            } else if (currentYear == endYear) {
                //当前选中年等于结束年
                if (isRange) {
                    setSameYearAndYear(currentYear, currentMonth);
                } else {
                    isRange = false;
                    endMonth = currentMonth;
                }
            }
            monthAdapter.currentYear = currentYear;
            monthAdapter.notifyDataSetChanged();
        });

        ll_sure.setOnClickListener(v12 -> {
            if (startMonth < 10) {
                startTime = String.format("%s-%s%s", startYear, 0, startMonth);
            } else {
                startTime = String.format("%s-%s", startYear, startMonth);
            }
            if (endMonth < 10) {
                endTime = String.format("%s-%s%s", endYear, 0, endMonth);
            } else {
                endTime = String.format("%s-%s", endYear, endMonth);
            }
            b_year.setText(startTime+" 至 "+endTime);
            yearPickerDialogFragment.dismiss();
        });
    }


    private void setSameYearAndYear(int currentYear, int currentMonth) {
        //本来是有范围的选择，点击任何一个月份，就把范围取消，开始结束的年月都设置为当前年月
        isRange = false;
        startYear = currentYear;
        endYear = currentYear;
        startMonth = currentMonth;
        endMonth = currentMonth;
    }







}
