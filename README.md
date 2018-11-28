

项目中需要做一个年月选择器的用来选择年和月，包括可以选择年月范围,秉承不重复造轮子的思想（偷懒）,我就在网上查了相关的开源项目，发现大部分都是日期选择器（可是这不是宝宝的想要的）...好吧，只能自己干啦,于是效果图如下：



先分析下需求：

    一 ：可以选择年，可以选择月，可以选择年月范围，如果不选择范围的话，结束时间默认是开始时间

    二：如果在时间选择范围之内，点击一下其他的年或月就结束范围选择，设置开始,结束时间为当前选择时间

    三：因为年和月在不同的页面维度，需要我们自己记录时间的选择，来做一些逻辑的控制~





 ok,下面来简单的说一下实现的关键点



好了，首先看ui，ui的话很简单，一个底部的弹框，和两个recycleview,一个recycleview用来放年份，一个recycleview用来放月份，是不是很easy,因为是recycleview, 所以自定义样式都会的吧？哈哈

接下来我们在MainActivity中放一个Button,点击一下就弹出我们的年月选择器,如下：



给底部弹窗顶部的年份（就是Textview）设置一个点击事件，通过点击来选择年或者月

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




当然，我们得需要一些变量来时间的选择：

    public static int startYear = 0;//开始年份
    public static int endYear = 0;//结束年份
    public static int startMonth = 0;//开始月份
    public static int endMonth = 0;//结束月份
    public static boolean isRange = false;//月份是否是选择范围
    boolean isShowYear = false;//是否展示年
    private String startTime;//开始时间
    private String endTime;//结束时间
我们给选择年的Adapter设置一个点击事件，很简单，记录当前选择年，改变选中年的演示，隐藏年选择，显示月选择，如下：

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
接下来就是重头戏，就是月选择的点击事件，这里面有很多逻辑的判断，先看代码：

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


private void setSameYearAndYear(int currentYear, int currentMonth) {
        //本来是有范围的选择，点击任何一个月份，就把范围取消，开始结束的年月都设置为当前年月
        isRange = false;
        startYear = currentYear;
        endYear = currentYear;
        startMonth = currentMonth;
        endMonth = currentMonth;
    }
  虽然代码有点长，但是就是有主要几个关键思路，首先判断当前选择的年份在啥位置？有以下几种情况：

  1.当前选择的年份等于开始年

  2.当前选择的年份等于结束年

  3.当前选择的年份等于开始年和结束年（在同一年）

 4.当前选择年份小于开始年

 5.当前选择年份大于结束年

 6.当前年份选择在开始年和结束年之间

  进入每种情况之后，首先判断当前选择是否是时间范围的选择，就是 isRange这个变量，如果是时间范围的选择，就取消范围选择，开始结束时间都设置为当前选择的时间，如果不是再根据具体的逻辑来判断。

确定按钮就很简单了，得到当前选中的年月，显示在页面上~~

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
   建议大家可以把项目clone到本地，然后自己点击一下，再看代码，应该很快就能看懂，并且能根据自己的业务逻辑来做出修改，试想一下，如果是日期选择器，其实也是这个逻辑，不就是再增加几个变量来控制业务嘛？
