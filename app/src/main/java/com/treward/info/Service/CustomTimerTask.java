package com.treward.info.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;


import com.treward.info.GameAllActivity;
import com.treward.info.utils.Const;
import com.treward.info.utils.Utils;

import java.util.TimerTask;

public class CustomTimerTask extends TimerTask {


    private Context context;
    private Handler mHandler = new Handler();

    public CustomTimerTask(Context con) {
        this.context = con;

    }



    @Override
    public void run() {
        new Thread(new Runnable() {

            public void run() {

                mHandler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(context, "Wait " + GameAllActivity.value, Toast.LENGTH_SHORT).show();
                        GameAllActivity.txtview(String.valueOf(GameAllActivity.value));
                        if(GameAllActivity.value == 0){
                            String val = Utils.getFrmShared(context, Const.btnVal,"");
                            Utils.saveInShared(context, val, "true");
                            context.stopService(new Intent(context, Sample_service.class));
                        }else{
                            GameAllActivity.value = GameAllActivity.value - 1;
                        }


                    }
                });
            }
        }).start();

    }

}