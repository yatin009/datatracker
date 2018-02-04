package io.yatin.datatracker;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MyIntentService extends IntentService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startPingAction(Context context, String action) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.equals("PING")) {
                handlePingAction();
            } else {
                cancelAlarm();
            }
        }
    }

    private void handlePingAction() {
        API service = retrofit.create(API.class);
        Call<List<ResponseBody>> postsCallback = service.getPost();
        postsCallback.enqueue(new Callback<List<ResponseBody>>() {
            @Override
            public void onResponse(Call<List<ResponseBody>> call, Response<List<ResponseBody>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                response.headers().size();
                Timber.d("SERVICE RUN SUCCESSFULL > >" + response.headers().size());
                setAlarm();
            }

            @Override
            public void onFailure(Call<List<ResponseBody>> call, Throwable t) {
                Timber.d("failure for hitting API service");
                Timber.d(t.getMessage());
                setAlarm();
            }
        });

    }

    private void setAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, MyReceiver.class);
        i.setAction("io.yatin.datatracker.DATATRACKER");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 2000, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm() {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction("io.yatin.datatracker.DATATRACKER");
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
