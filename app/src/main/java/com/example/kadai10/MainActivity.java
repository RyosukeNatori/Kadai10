package com.example.kadai10;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Thread th;
    SurfaceHolder holder;
    BallSurfaceView bsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView sv=(SurfaceView) findViewById(R.id.surfaceView);
        bsv=new BallSurfaceView();
        holder= sv.getHolder();
        holder.addCallback(bsv);
        sv.setOnTouchListener(bsv);
    }
    @Override
    public void onStart(){
        super.onStart();
        th=new Thread(bsv);
        th.start();
    }
    @Override
    public void onStop(){
        super.onStop();
        th=null;
    }
    class BallSurfaceView implements View.OnTouchListener,SurfaceHolder.Callback,Runnable{
        int screen_width;
        int screen_height;
        Ball ba;
        Racket ra;
        public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
            screen_height=height;
            screen_width=width;
        }
        public void surfaceCreated(SurfaceHolder holder){
            ba=new Ball();
            ra=new Racket();
        }
        public void surfaceDestroyed(SurfaceHolder holder){}
        public boolean onTouch(View v, MotionEvent mv){
            switch (mv.getAction()){
                case MotionEvent.ACTION_DOWN:
                    ra.x=(int)mv.getX();
                    break;
            }
            return true;
        }
        class Racket{
            int x=0;
            void draw(Canvas ca){
                Paint paint=new Paint();
                paint.setColor(Color.WHITE);
                ca.drawRect(x,screen_height-20,x+100,screen_height-10,paint);
            }
        }
        class Ball{
            int x=0,y=0,r=10,dx=5,dy=5;
            void move(){
                x=x+dx;
                y=y+dy;
                if(x<0||x>screen_width){
                    dx=dx*-1;
                }
                if(y<0||y>screen_height){
                    dy=dy*-1;
                }
            }
            void draw(Canvas ca){
                Paint paint=new Paint();
                paint.setColor(Color.WHITE);
                ca.drawCircle(x,y,r,paint);
            }
        }
        public void run(){
            while (th!=null){
                Canvas canvas=holder.lockCanvas();
                if(canvas!=null){
                    canvas.drawColor(Color.BLACK);
                    ba.move();
                    ba.draw(canvas);
                    ra.draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}