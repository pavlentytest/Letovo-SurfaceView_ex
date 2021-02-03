package ru.samsung.itschool.mdev.myapplication;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    MyThread myThread;

    public MySurfaceView(Context context) {
        super(context);
        // необходимо для запуска surfaceCreated()
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        myThread = new MyThread(getHolder());
        myThread.setRunning(true);
        myThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // остановку
        boolean retry = true;
        myThread.setRunning(false);
        while(retry) {
            try {
                myThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                retry = false;
            }
        }
    }
}
