package com.example.young.ohgamdiary.dao;

import com.example.young.ohgamdiary.asynctask.conversationList;

import java.util.ArrayList;

/**
 * Created by WonYoung on 2016. 5. 16..
 */
public class TalkingQueueDao {

    ArrayList<conversationList> talkingQueueForSend = new ArrayList<>();
    conversationList conversationModel = new conversationList();

    public ArrayList<conversationList> getQueue() {

        //DB에서 내용을 받아와서 conversationModel에 넣은 뒤, for루프로 하나씩 talkingQueue에 넣을 것

        return talkingQueueForSend;
    }

    public ArrayList<conversationList> updateQueue(ArrayList<conversationList> talkingQueue) {

        //받은 talkingQueue를 DB로 보내고 DB를 갱신시킨 후

        //getQueue()로 보내버림
        return getQueue();
    }
}
