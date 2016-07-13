package com.example.young.ohgamdiary.asynctask;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.young.ohgamdiary.DiaryListActivity;
import com.example.young.ohgamdiary.dao.TalkingQueueDao;

import java.util.ArrayList;

/**
 * Created by WonYoung on 2016. 5. 10..
 */
public class talkingAsyncTask extends AsyncTask<String, String, Void> {

    TextView talkingText;
    ArrayList<conversationList> talkingQueue = new ArrayList<>();
    int count;
    TalkingQueueDao talkingQueueDao = new TalkingQueueDao();
    String tempStr;
    int tempInt=0;
    boolean isPrint;

    //생성자로 talkingText 받아오기 START
    public talkingAsyncTask(TextView talkingText){

        this.talkingText = talkingText;

    }
    //생성자로 talkingText 받아오기 END


    @Override
    protected Void doInBackground(String... strings) {
        Log.d("talkingAsyncTask", "(MMMMMM)doInBackground() START");

        Log.d("talkingAsyncTask", "(VVVVVV)DiaryList.TALKING_TO_USER: " + DiaryListActivity.TALKING_TO_USER);
        while(DiaryListActivity.TALKING_TO_USER){   //DiaryList Activity가 종료되면, 루프를 멈춤

            //if(큐가 비어있으면) than DAO에 요청해서 큐를 받아옴 START
            Log.d("talkingAsyncTask", "(MMMMMM)while() count: " + count++);     //얼마나 돌았니?
            Log.d("talkingAsyncTask", "(VVVVVV)talkingQueue: " + talkingQueue); //큐에 뭐가 들었니?
            if(talkingQueue.isEmpty()){
                //talkingQueue = talkingQueueDao.getQueue();
            }
            //if(큐가 비어있으면) than DAO에 요청해서 큐를 받아옴 END

            //if(새로운 글을 썼다면) than DAO에 지금까지 썼던 큐를 보내고, 다시 받아옴 START
            Log.d("talkingAsyncTask", "(VVVVVV)DiaryList.IS_WRITE_NEW_DIARY: " + DiaryListActivity.IS_WRITE_NEW_DIARY);
            if(DiaryListActivity.IS_WRITE_NEW_DIARY){
                //talkingQueue = talkingQueueDao.updateQueue(talkingQueue);
            }
            //if(새로운 글을 썼다면) than DAO에 지금까지 썼던 큐를 보내고, 다시 받아옴 END

            //임시 글 생성 START
            tempStr = "잘 뜨고 있겠지?" + tempInt;
            tempInt++;
            //임시 글 생성 END

            //출력용 보내기 START
            isPrint = true;
            publishProgress(tempStr);      //우선 임시로 보내봄
            //출력용 보내기 END


            //1. Queue의 내용 중 첫 번째 내용을 꺼내서 텍스트를 publishProgress()로 보내고,
            //2. Queue의 publishCount를 1올린다.



            //6~8초 대기(Thread.sleep(7000);) 후 무한 반복시킬 것 START
            SystemClock.sleep(9000);    //출력하는 동안의 시간도 포함해서 생각할 것
            //6~8초 대기(Thread.sleep(7000);) 후 무한 반복시킬 것 END

            //없애기 용 보내기 START
            isPrint = false;
            publishProgress(tempStr);   //우선 임시로 보내봄
            SystemClock.sleep(2000);    //없애는 동안 기다려!!
            //없애기 용 보내기 END

        }

        talkingQueueDao.updateQueue(talkingQueue);  //올라간 publishCount를 반영해줌
        Log.d("talkingAsyncTask", "(MMMMMM)doInBackground() END");
        return null;
    }


    //UI 지속적으로 업데이트 START\
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("잘 뜨고 있니?", values[0]);

        if(!isPrint) {
            YoYo.with(Techniques.FadeOutDown)
                    .duration(800)
                    .playOn(talkingText);
        }

        if(isPrint) {
            talkingText.setText(values[0]);

            //나타나는 애니메이션 START
            YoYo.with(Techniques.FadeInDown)
                    .duration(800)
                    .playOn(talkingText);
            //나타나는 애니메이션 END
        }
    }
    //UI 지속적으로 업데이트 END


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e("talkingAsyncTask","talkingAsyncTask end..!!");
    }

}
