package com.okayfan.yearmonthselect;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;



/**
 * author: FYx
 * date:   On 2018/9/20
 */
public class ImageUtil {


            public static void setBackground(View view,int resId){
                Drawable drawable = view.getContext().getResources().getDrawable(resId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                } else {
                    view.setBackgroundDrawable(drawable);
                }
            }






}
