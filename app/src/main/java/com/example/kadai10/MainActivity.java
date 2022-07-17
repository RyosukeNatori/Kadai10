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
    int count;

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
        Block[] br=new Block[20];
        public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
            screen_height=height;
            screen_width=width;
            for(int i=0;i<2;i++){
                for(int j=0;j<10;j++){
                    br[count]=new Block(i,j);
                    count++;
                }
            }
        }
        public void surfaceCreated(SurfaceHolder holder){
            ba=new Ball();
            ra=new Racket();
        }
        public void surfaceDestroyed(SurfaceHolder holder){}
        public boolean onTouch(View v, MotionEvent mv){
            switch (mv.getAction()){
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    ra.x=(int)mv.getX()-100;
                    break;
            }
            return true;
        }
        class Racket{
            int x=0;
            void draw(Canvas ca){
                Paint paint=new Paint();
                paint.setColor(Color.WHITE);
                ca.drawRect(x,screen_height-20,x+200,screen_height-10,paint);
            }
        }

        class Block{
            float x,y,w,h;
            int count2=count;
            Paint paint;
            Block(int i,int j){
                x=(float) screen_width/10*j;
                y=(float) screen_width/10*i;
                w=(float) screen_width/10+x;
                h=(float) screen_width/10+y;
                paint=new Paint();
                if(count2%3==0){
                    paint.setColor(Color.BLUE);
                }
                else if(count2%3==1){
                    paint.setColor(Color.RED);
                }
                else{
                    paint.setColor(Color.GREEN);
                }
            }
            void draw(Canvas ca){
                ca.drawRect(x,y,w,h,paint);
                System.out.println(paint.getColor());
            }
        }
        class Ball{
            float x=0,y=200,r=10,dx=2.5f,dy=5;
            void move(){
                x=x+dx;
                y=y+dy;
                if(y>screen_height){
                    dx=0;
                    dy=0;
                }
                else if(x<0||x>screen_width){
                    dx=dx*-1;
                }
                else if(y<=0){
                    dy=dy*-1;
                }
                else if(y>screen_height-20&&ra.x<=x&&ra.x+25>x){
                    dy=dy*-1;
                    dx=dx-4;
                    if(dx<-2.5f){
                        dx=-2.5f;
                    }
                }
                else if(y>screen_height-20&&ra.x+25<=x&&ra.x+50>x){
                    dy=dy*-1;
                    dx=dx-3;
                    if(dx<-2.5f){
                        dx=-2.5f;
                    }
                }
                else if(y>screen_height-20&&ra.x+50<=x&&ra.x+75>x){
                    dy=dy*-1;
                    dx=dx-2;
                    if(dx<-2.5f){
                        dx=-2.5f;
                    }
                }
                else if(y>screen_height-20&&ra.x+75<=x&&ra.x+100>x){
                    dy=dy*-1;
                    dx=dx-1;
                    if(dx<-2.5f){
                        dx=-2.5f;
                    }
                }
                else if(y>screen_height-20&&ra.x+100<=x&&ra.x+125>x){
                    dy=dy*-1;
                    dx=dx+1;
                    if(dx>2.5f){
                        dx=2.5f;
                    }
                }
                else if(y>screen_height-20&&ra.x+125<=x&&ra.x+150>x){
                    dy=dy*-1;
                    dx=dx+2;
                    if(dx>2.5f){
                        dx=2.5f;
                    }
                }
                else if(y>screen_height-20&&ra.x+150<=x&&ra.x+175>=x){
                    dy=dy*-1;
                    dx=dx+3;
                    if(dx>2.5f){
                        dx=2.5f;
                    }
                }
                else if(y>screen_height-20&&ra.x+175<=x&&ra.x+200>=x){
                    dy=dy*-1;
                    dx=dx+4;
                    if(dx>2.5f){
                        dx=2.5f;
                    }
                }
                int count3=0;
                for(int i=0;i<20;i++){
                    if(br[i].paint.getColor()==0){
                        count3++;
                    }
                    if(((y<=br[i].h&&br[i].h-5<=y)||(br[i].y<=y&&y<=br[i].y+5))&&br[i].x<=x&&x<=br[i].w&&br[i].paint.getColor()!=0) {
                        br[i].paint.setColor(Color.argb(0,0,0,0));
                        dy=dy*-1;
                    }
                    else if(y<=br[i].h&&br[i].y<=y&&((br[i].x<=x&&x<=br[i].x+3)||(x<=br[i].w&&br[i].w-3<=x))&&br[i].paint.getColor()!=0) {
                        br[i].paint.setColor(Color.argb(0, 0, 0, 0));
                        dx = dx * -1;
                    }
                }
                if(count3>=20){
                    th=null;
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
                    for(int i=0;i<20;i++){
                        br[i].draw(canvas);
                    }
                    holder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(1);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}