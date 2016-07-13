package com.example.young.ohgamdiary.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by WonYoung on 16. 5. 22..
 */
public class DatabaseManager extends SQLiteOpenHelper {


    public DatabaseManager(Context context) {
        super(context, "ohgamdb.db", null, 10);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("DatabaseManager", "onCreate() START");

        String userTableCreateSQL = "CREATE TABLE IF NOT EXISTS `user` (\n" +
                "  `iduser` INT NOT NULL DEFAULT 0,\n" +
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  `lastOnFunction` DATETIME NULL,\n" +
                "  `age` INT NULL,\n" +
                "  `nickname` VARCHAR(45) NULL,\n" +
                "  `password` BINARY(64) NULL,\n" +
                "  `sex` CHAR(1) NULL,\n" +
                "  PRIMARY KEY (`iduser`))\n;";

        String diaryTableCreateSQL = "CREATE TABLE IF NOT EXISTS `diary` (\n" +
                "  `iddiary` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  `contents` BLOB NOT NULL,\n" +
                "  `writeDate` DATE NOT NULL,\n" +
                "  `writeTime` TIME NOT NULL,\n" +
                "  `user_iduser` INT NOT NULL,\n" +
                "  CONSTRAINT `fk_diary_user`\n" +
                "    FOREIGN KEY (`user_iduser`)\n" +
                "    REFERENCES `user` (`iduser`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION)";

        String conversationTableCreateSQL = "CREATE TABLE IF NOT EXISTS `conversation` (\n" +
                "  `idconversation` INTEGER NOT NULL,\n" +
                "  `talkingContent` VARCHAR(200) NOT NULL,\n" +
                "  `conversationCount` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idconversation`));";

        String tagTableCreateSQL = "CREATE TABLE IF NOT EXISTS `tags` (\n" +
                "  `idtags` INT NOT NULL,\n" +
                "  `tagName` VARCHAR(200) NOT NULL,\n" +
                "  PRIMARY KEY (`idtags`));";

        String tagInDiaryTableCreateSQL = "CREATE TABLE IF NOT EXISTS `tagInDiary` (\n" +
                "  `diary_iddiary` INT NOT NULL,\n" +
                "  `tags_idtags` INT NOT NULL,\n" +
                "  CONSTRAINT `fk_tag_diary1`\n" +
                "    FOREIGN KEY (`diary_iddiary`)\n" +
                "    REFERENCES `diary` (`iddiary`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `fk_tagInDiary_tags1`\n" +
                "    FOREIGN KEY (`tags_idtags`)\n" +
                "    REFERENCES `tags` (`idtags`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);";

        String tagForConverationTableCreateSQL = "CREATE TABLE IF NOT EXISTS `tagForConversation` (\n" +
                "  `idtagForConversation` INT NOT NULL,\n" +
                "  `conversation_idconversation` INT NOT NULL,\n" +
                "  `tagName` VARCHAR(200) NOT NULL,\n" +
                "  PRIMARY KEY (`idtagForConversation`, `conversation_idconversation`),\n" +
                "  CONSTRAINT `fk_tagForConversation_conversation1`\n" +
                "    FOREIGN KEY (`conversation_idconversation`)\n" +
                "    REFERENCES `conversation` (`idconversation`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);";

        String tagInfoForConversationTableCreateSQL = "CREATE TABLE IF NOT EXISTS `tagInfoForConversation` (\n" +
                "  `tags_idtags` INT NOT NULL,\n" +
                "  `conversation_idconversation` INT NOT NULL,\n" +
                "  CONSTRAINT `fk_tagInfoForConversation_tags1`\n" +
                "    FOREIGN KEY (`tags_idtags`)\n" +
                "    REFERENCES `tags` (`idtags`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `fk_tagInfoForConversation_conversation1`\n" +
                "    FOREIGN KEY (`conversation_idconversation`)\n" +
                "    REFERENCES `conversation` (`idconversation`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION);";

        Log.d("DatabaseManager", "userTableCreateSQL: " + userTableCreateSQL);
        Log.d("DatabaseManager", "diaryTableCreateSQL: " + diaryTableCreateSQL);
        Log.d("DatabaseManager", "conversationTableCreateSQL: " + conversationTableCreateSQL);
        Log.d("DatabaseManager", "tagTableCreateSQL: " + tagTableCreateSQL);
        Log.d("DatabaseManager", "tagInDiaryTableCreateSQL: " + tagInDiaryTableCreateSQL);
        Log.d("DatabaseManager", "tagForConverationTableCreateSQL: " + tagForConverationTableCreateSQL);
        Log.d("DatabaseManager", "tagInfoForConversationTableCreateSQL: " + tagInfoForConversationTableCreateSQL);

        db.execSQL(userTableCreateSQL);
        db.execSQL(diaryTableCreateSQL);
        db.execSQL(conversationTableCreateSQL);
        db.execSQL(tagTableCreateSQL);
        db.execSQL(tagInDiaryTableCreateSQL);
        db.execSQL(tagForConverationTableCreateSQL);
        db.execSQL(tagInfoForConversationTableCreateSQL);

        Log.d("DatabaseManager", "db: " + db);
        Log.d("DatabaseManager", "onCreate() END");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("DatabaseManager", "onUpgrade() START");

        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS diary");
        db.execSQL("DROP TABLE IF EXISTS conversation");
        db.execSQL("DROP TABLE IF EXISTS tag");
        db.execSQL("DROP TABLE IF EXISTS tagInDiary");
        db.execSQL("DROP TABLE IF EXISTS tagForConversation");
        db.execSQL("DROP TABLE IF EXISTS tagInfoForConversation");

        onCreate(db);

        Log.d("DatabaseManager", "onUpgrade() END");
    }
}
