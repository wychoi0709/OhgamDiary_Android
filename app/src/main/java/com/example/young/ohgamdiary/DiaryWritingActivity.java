package com.example.young.ohgamdiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.young.ohgamdiary.adapter.DiaryListRecyclerAdapter;
import com.example.young.ohgamdiary.dao.DatabaseHandler;
import com.example.young.ohgamdiary.model.DiaryContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by WonYoung on 16. 5. 18..
 */
public class DiaryWritingActivity extends AppCompatActivity implements View.OnClickListener{

    DiaryContent diaryContent;
    EditText year;
    EditText month;
    EditText date;
    EditText content;
    EditText tags;
    String strCurYear;
    String strCurMonth;
    String strCurDate;
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Button confirmBtn = (Button) findViewById(R.id.confirmBtn);

        intent = getIntent();



        diaryContent = new DiaryContent();
        year = (EditText) findViewById(R.id.yearEditText);
        month = (EditText) findViewById(R.id.monthEditText);
        date = (EditText) findViewById(R.id.dateEditText);
        content = (EditText) findViewById(R.id.diaryEditText);
        tags = (EditText) findViewById(R.id.tagEditText);

        //오늘 날짜 입력해주기 START
        long now = System.currentTimeMillis();
        Date currentDate = new Date(now);

        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("dd");

        strCurYear = CurYearFormat.format(currentDate);
        strCurMonth = CurMonthFormat.format(currentDate);
        strCurDate = CurDateFormat.format(currentDate);
        //오늘 날짜 입력해주기 END

        if(intent.getExtras().getBoolean("isOpenDiary") == true){
            year.setText(intent.getExtras().getString("dateText").substring(0,4));
            month.setText(intent.getExtras().getString("dateText").substring(5,7));
            date.setText(intent.getExtras().getString("dateText").substring(8,10));
            content.setText(intent.getExtras().getString("diaryText"));
            tags.setText(intent.getExtras().getString("tags"));

        }else {
            year.setHint(strCurYear);
            month.setHint(strCurMonth);
            date.setHint(strCurDate);
        }

        confirmBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Log.d("DiaryWritingActivity", "onClick START");


        if(v.getId() == R.id.confirmBtn) {
            //현재 시간(시,분,초) 갱신 START
            long nowAgain = System.currentTimeMillis();
            Date currentDateAgain = new Date(nowAgain);

            SimpleDateFormat CurHourFormatAgain = new SimpleDateFormat("HH");
            SimpleDateFormat CurMinuteFormatAgain = new SimpleDateFormat("mm");
            SimpleDateFormat CurSecondFormatAgain = new SimpleDateFormat("ss");

            String strCurHourAgain = CurHourFormatAgain.format(currentDateAgain);
            String strCurMinuteAgain = CurMinuteFormatAgain.format(currentDateAgain);
            String strCurSecondAgain = CurSecondFormatAgain.format(currentDateAgain);
            //현재 시간 갱신 END
            if(TextUtils.isEmpty(year.getText())){
                diaryContent.year = strCurYear;
            }else {
                diaryContent.year = String.valueOf(year.getText());
            }

            if(TextUtils.isEmpty(month.getText())){
                diaryContent.month = strCurMonth;
            }else {
                diaryContent.month = String.valueOf(month.getText());
            }

            if(TextUtils.isEmpty(date.getText())){
                diaryContent.date = strCurDate;
            }else {
                diaryContent.date = String.valueOf(date.getText());
            }

            diaryContent.hour = String.valueOf(strCurHourAgain);
            diaryContent.minute = String.valueOf(strCurMinuteAgain);
            diaryContent.second = String.valueOf(strCurSecondAgain);

            diaryContent.content = String.valueOf(content.getText());
            diaryContent.tags = String.valueOf(tags.getText());



            //데이터 베이스 넣는 코드 START
            DatabaseHandler databaseHandler = DatabaseHandler.open(DiaryWritingActivity.this);
            long cnt = -1;

            if(intent.getExtras().getBoolean("isOpenDiary") == true){   //수정일 경우
                diaryContent.id = Integer.valueOf(intent.getExtras().getInt("id"));
                Log.d("다이어리 아이디 잘 들어감 : ", String.valueOf(diaryContent.id + "  " + Integer.valueOf(intent.getExtras().getInt("iddiary"))));
                databaseHandler.updateDiaryContent(diaryContent);
                DiaryListRecyclerAdapter.UPDATE_ITEM.id = diaryContent.id;
                DiaryListRecyclerAdapter.UPDATE_ITEM.diaryText = diaryContent.content;
                DiaryListRecyclerAdapter.UPDATE_ITEM.dateText =  diaryContent.year + "-" + diaryContent.month + "-" + diaryContent.date
                        + " "
                        + diaryContent.hour + ":" + diaryContent.minute + ":" + diaryContent.second;

                //태그는 나중에 해결하자(문제는 ArrayList로 담아놓는 걸로 선언해놔서 지금은 String으로만 되어 있음. 쪼개서 add해줘야 함)
                cnt = 1;
                SplashScreenActivity.IS_UPDATE_DIARY = true;
            }else {                                                     //생성일 경우
                cnt = databaseHandler.insertDiaryContent(diaryContent);
                SplashScreenActivity.IS_REAL_WRITE = true;
            }

            if (cnt == -1) {
                Toast.makeText(DiaryWritingActivity.this,
                        "입력에 실패했습니다.",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(DiaryWritingActivity.this,
                        "입력에 성공했습니다.",
                        Toast.LENGTH_SHORT)
                        .show();
            }

            databaseHandler.close();
            //데이터 베이스 넣는 코드 END

            finish();
        }
        Log.d("DiaryWritingActivity", "onClick END");
    }
}
