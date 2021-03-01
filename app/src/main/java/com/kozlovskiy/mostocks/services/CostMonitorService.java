package com.kozlovskiy.mostocks.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CostMonitorService extends Service {
    public CostMonitorService() {


    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}