package com.first.kritikm.split;

import android.app.Application;

/**
 * Created by Kritik on 13-Nov-16.
 */

public class GlobalData extends Application {

    private int userId;

    public void setUserId(int toUserId)
    {
        userId = toUserId;
    }

    public int getUserId()
    {
        return userId;
    }
}
