package com.example.young.ohgamdiary;

import android.app.Application;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by young on 2016-04-26.
 */
public class OhgamApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
