package ru.samsung.itschool.mdev.myapplication;


import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class MyThread extends Thread {

    //  частота обновления экрана
    private final int REDRAW_TIME = 5000;

    //  константа для интерполяции
    private final int FRACTION_TIME = 1500;

    // флажок запущен ли поток
    private boolean flag;

    // время начала анимации
    private long startTime;

    // время с пред. перерисовки
    private long prevRedrawTime;

    // маркер для отрисовки
    private Paint paint;

    // переменная для интерполяции
    private ArgbEvaluator evaluator;

    // указатель для получения canvas
    private SurfaceHolder surfaceh;

    MyThread(SurfaceHolder h) {
        this.flag = false;
        this.paint = new Paint();
        // сглаживание
        paint.setAntiAlias(true);
        // заливаем
        paint.setStyle(Paint.Style.FILL);
        this.evaluator = new ArgbEvaluator();
        this.surfaceh = h;
    }

    public long getTime() {
        return System.nanoTime()/1000;
    }

    public void doDraw(Canvas canvas) {
        long currentTime = getTime() - startTime;
        // фон
        canvas.drawColor(Color.BLACK);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int centerX = width/2;
        int centerY = height/2;
        float maxradius = Math.min(width,height)/2;
        // шаг интерполирования
        // получаю число от 0 до 1
        float step = (float) (currentTime % FRACTION_TIME) / FRACTION_TIME;
        Log.d("RRRR","step:"+step);
        int color = (int)evaluator.evaluate(step, Color.RED, Color.BLACK);
        paint.setColor(color);
        canvas.drawCircle(centerX, centerY,maxradius*step, paint);
    }

    public void setRunning(boolean f) {
        this.flag = f;
        this.prevRedrawTime = getTime();
    }

    @Override
    public void run() {
        Canvas canvas;
        startTime = getTime();
        while(flag) {
            long currTime = getTime();
            long elapsedTime = currTime - prevRedrawTime;
            if(elapsedTime < REDRAW_TIME) continue;
            // блокировка canvas
            canvas = surfaceh.lockCanvas();
            // отрисовка
            doDraw(canvas);
            // очищаем canvas
            surfaceh.unlockCanvasAndPost(canvas);
            prevRedrawTime = getTime();
        }
    }

}
