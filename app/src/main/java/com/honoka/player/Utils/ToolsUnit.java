package com.honoka.player.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 41258 on 2017/2/14.
 */

public class ToolsUnit {
    public static void toast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
