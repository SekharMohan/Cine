package com.cine.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cine.R;

/**
 * Created by sekhar on 23/03/17.
 */

public class ToastUtil {

    public static void showErrorUpdate(Context context,String msg){

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast. setGravity(Gravity.TOP|Gravity.LEFT|Gravity.FILL_HORIZONTAL, 0, 0);
        View view = toast.getView();

        //To change the Background of Toast)
        view.setBackgroundResource(R.color.colorErro);
        TextView text = (TextView) view.findViewById(android.R.id.message);

        //Shadow of the Of the Text Color
        text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
        text.setTextColor(Color.WHITE);
        text.setTextSize(14);
        toast.show();

    }

}
