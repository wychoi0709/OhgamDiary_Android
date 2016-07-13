package com.example.young.ohgamdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

    private Timer timer;
    private TimerTask task;
    Context context;
    public static boolean IS_REAL_WRITE = false;
    public static boolean IS_UPDATE_DIARY = false;

    public static boolean NECESSITY_OF_GUIDE_PAGE ;   //'가이드로 가야하는가?'에 대한 대답

    public static String fileName = "configOfOhGamDiary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SplashScreen", "onCreate() START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = this;

        //처음 접속인지 확인하고, 처음이면 기본값 넣어놓기 START
        SharedPreferences flagOfGuide = getSharedPreferences(fileName, 0);
        NECESSITY_OF_GUIDE_PAGE = flagOfGuide.getBoolean("isFirst", true);
        Log.d("SplashScreen", "NECESSITY_OF_GUIDE_PAGE: " + NECESSITY_OF_GUIDE_PAGE);
        //처음 접속인지 확인하고, 처음이면 기본값 넣어놓기 END


        //TimerTask 생성 및 Intent 생성 START
        task = new TimerTask() {
            @Override
            public void run() {

                Intent intent;

                //처음 접속이면 가이드 페이지로, 아니면 일기 리스트 페이지로 가는 인텐트 생성 START
                if(NECESSITY_OF_GUIDE_PAGE) {
                    intent = new Intent(context, IntroductionActivity.class);
                }else{
                    intent = new Intent(context, DiaryListActivity.class);
                }
                Log.d("SplashScreen", "Next intent: " + intent);
                //처음 접속이면 가이드 페이지로, 아니면 일기 리스트 페이지로 가는 인텐트 생성 END

                startActivity(intent);
                finish();
            }
        };
        //TimerTask 생성 및 Intent 생성 END
        timer = new Timer();
        timer.schedule(task, 1000);
        Log.d("SplashScreen", "onCreate() END");
    }

    @Override
    protected void onPause() {
        Log.d("SplashScreen", "onPause() START");
        timer.cancel();
        super.onPause();
        Log.d("SplashScreen", "onPause() END");
    }
}
