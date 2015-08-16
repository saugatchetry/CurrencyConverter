package saugat.chetry.com.currencyconverter;

import android.app.Application;
import android.content.Context;

/**
 * Created by a511863 on 14/08/15.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }


    public static MyApplication getInstance()
    {
        return sInstance;
    }

    public static Context getAppContext()
    {
        return sInstance.getApplicationContext();
    }
}
