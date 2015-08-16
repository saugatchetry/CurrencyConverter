package saugat.chetry.com.currencyconverter;

import android.app.DownloadManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by a511863 on 14/08/15.
 */
public class VolleySingleton {

    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton()
    {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static VolleySingleton getInstance()
    {
        if(sInstance == null)
        {
            sInstance = new VolleySingleton();
        }

        return sInstance;
    }

    public RequestQueue getRequestQueue()
    {
        return mRequestQueue;
    }
}
