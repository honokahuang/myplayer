package com.honoka.player.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.honoka.player.Base.BaseActivity;

/**
 * Created by 41258 on 2017/2/7.
 */

public class MainHome extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(MainHome.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainHome.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
           finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    MainHomeActivity.ActivityStart(MainHome.this);
                }else {

                }
                break;
            default:
        }
    }
}
