package io.yatin.datatracker;

import android.app.ActivityManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);
        dataTracker();
    }

    public void startService(View view) {
        updateUI("Service is running");
        MyIntentService.startPingAction(this, "PING");
    }


    private void updateUI(String msg) {
        textView.setText(msg);
    }

    public void cancelService(View view) {
        updateUI("Hello World");
        MyIntentService.startPingAction(this, "CANCEL");
    }

    public void dataTracker() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {

            if (runningApp.processName.equals("io.yatin.datatracker")) {
                // Get UID of the selected process
                int uid = runningApp.uid;
                // Get traffic data
                long received = TrafficStats.getUidRxBytes(uid);
                long send = TrafficStats.getUidTxBytes(uid);
                Timber.d("Time > "+new Date());
                Timber.d("UID > "+uid +" Data in bytes");
                Timber.d("Data Sent > "+send);
                Timber.d("Data Received > "+received);
            }
        }
    }

    public void getNetworkStat(View view) {
        dataTracker();
    }
}
