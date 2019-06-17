package org.chromium;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.us.kinscape.R;

import org.chromium.chrome.browser.document.ChromeLauncherActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Handler handelr = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Intent intent = new Intent(LauncherActivity.this, ChromeLauncherActivity.class);
                LauncherActivity.this.startActivity(intent);
                finish();
                super.handleMessage(msg);
            }
        };
        handelr.sendEmptyMessageDelayed(0, 2500);
    }
}
