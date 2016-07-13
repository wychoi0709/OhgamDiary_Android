package com.example.young.ohgamdiary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.young.ohgamdiary.adapter.DiaryListRecyclerAdapter;
import com.example.young.ohgamdiary.adapter.ViewItem;
import com.example.young.ohgamdiary.asynctask.talkingAsyncTask;
import com.example.young.ohgamdiary.dao.DatabaseHandler;
import com.example.young.ohgamdiary.listener.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by young on 2016-04-17.
 */
public class DiaryListActivity extends AppCompatActivity {

    Context context;
    TextView talkingText;
    List<ViewItem> allDiaryList = null;
    public static boolean TALKING_TO_USER = true;  //이 엑티비티가 onPause() 상태가 되면 false로 바꿔준다
    public static boolean IS_WRITE_NEW_DIARY = false;
    DiaryListRecyclerAdapter adapter;
    RecyclerView recyclerView;
    int listCount = 0;
    LinearLayoutManager linearLayoutManager;
    Button editDiaryBtn;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diarylist);
        Log.d("DiaryList", "onCreate() START");

    //RecylerView 이슈 START
        recyclerView = (RecyclerView)findViewById(R.id.diaryListRecyclerView);

        //LayoutManager 생성 START
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //LayoutManager 생성 END

        listCount = 0;
        recyclerView.setLayoutManager(linearLayoutManager);

        listRecall();   //adapter 생성


        //ItemAnimation / Item Decoration 두 개도 적용해볼 것
    //RecylerView 이슈 END

        //xml 요소들 붙이기 START
        Button onlyBtn = (Button) findViewById(R.id.onlyBtn);
        Button writeDiaryBtn = (Button)findViewById(R.id.writeDiaryBtn);
        editDiaryBtn = (Button) findViewById(R.id.editDiaryBtn);
        talkingText = (TextView)findViewById(R.id.talkingText);
        //xml 요소들 붙이기 END

    //talkingText를 위한 AsyncTask 실행하기 START
        new talkingAsyncTask(talkingText).execute();
    //talkingText를 위한 AsyncTask 실행하기 END

        onlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiaryListActivity.this, "서비스 준비중입니다", Toast.LENGTH_SHORT).show();
            }
        });

        writeDiaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryListActivity.this, DiaryWritingActivity.class);
                intent.putExtra("isOpenDiary", false);
                startActivity(intent);
            }
        });

//
//        editDiaryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getItemCount();
//                ViewItem forEditDiaryItem = (allDiaryList.get(recyclerView.getChildAdapterPosition(editDiaryBtn)));
//
//                Intent intent = new Intent(DiaryListActivity.this, DiaryWritingActivity.class);
//                intent.putExtra("dateText", forEditDiaryItem.dateText);
//                intent.putExtra("diaryText", forEditDiaryItem.diaryText);
//                intent.putExtra("tags", forEditDiaryItem.tags);
//                intent.putExtra("isOpenDiary", forEditDiaryItem.isOpenDiary);
//                startActivity(intent);
//
//            }
//        });

        Log.d("DiaryList", "onCreate() END");
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("DiaryList", "onResume() START");

        if(SplashScreenActivity.IS_UPDATE_DIARY) {
            adapter.modifyData();
            SplashScreenActivity.IS_UPDATE_DIARY = false;
        }
        TALKING_TO_USER = true;
        new talkingAsyncTask(talkingText).execute();
        Log.d("DiaryList", "TALKING_TO_USER: "+ TALKING_TO_USER);

        //사용자가 글쓰기를 눌러서 글을 추가 했다면, adapter를 다시 만들고 리스너도 다시 달아줄 것 START
        if(SplashScreenActivity.IS_REAL_WRITE){
            listRecall();
            SplashScreenActivity.IS_REAL_WRITE = false;
        }

        Log.d("DiaryList", "onResume() END");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("DiaryList", "onPause() START");
        TALKING_TO_USER = false;    //엑티비티 종료 false로 변경
        Log.d("DiaryList", "TALKING_TO_USER: "+ TALKING_TO_USER);
        Log.d("DiaryList", "onPause() END");
    }

    public List<ViewItem> receiveItems() {
        Log.d("DiaryList", "createItem() START");
        List<ViewItem> items = new ArrayList<>();


        //테스트용 for문 START
        ViewItem viewItem = null;

        DatabaseHandler databaseHandler = DatabaseHandler.open(this);

        Cursor cursor = databaseHandler.selectDiaryList(listCount);

        Log.d("cursorDDDDDD", String.valueOf(cursor.getCount()));

        if(cursor.getCount() == 0){

        }else{
            do{
                viewItem = new ViewItem();
                //ArrayList<String> tagContents = new ArrayList<>();

                //아. viewItem이랑 DiaryContent이 나눠져있으니까 개고생이구나... 이걸 합쳤어야 하는데!!
                viewItem.id = cursor.getInt(cursor.getColumnIndex("iddiary"));
                Log.d("리스트 생성할 떄 아이디 값: ", String.valueOf(viewItem.id));
                viewItem.dateText = cursor.getString(cursor.getColumnIndex("writeDate"))
                        + " "
                        + cursor.getString(cursor.getColumnIndex("writeTime"));
                viewItem.diaryText = cursor.getString(cursor.getColumnIndex("contents"));
//                viewItem.tags = tagContents;

                items.add(viewItem);
            }while(cursor.moveToNext());
        }

        listCount += 10;

        //테스트용 for문 END

        databaseHandler.close();

        Log.d("DiaryList", "items: "+ items);
        Log.d("DiaryList", "createItem() END");
        return items;
    }

    public void listRecall(){

        listCount = 0;
        allDiaryList = receiveItems();    //일기 리스트 갱신
        adapter = new DiaryListRecyclerAdapter(this, allDiaryList, R.layout.diaryitem);
        recyclerView.setAdapter(adapter);//adapter 다시 만들어서 연결

//        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
//
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                if(totalItemsCount < 10) {  //limit 10으로 했으니깐..
//                    List<ViewItem> moreContacts = receiveItems();
//                    int curSize = adapter.getItemCount();
//                    allDiaryList.addAll(moreContacts);
//                    adapter.notifyItemRangeInserted(curSize, allDiaryList.size() - 1);
//                }
//            }
//        });

    }



}
