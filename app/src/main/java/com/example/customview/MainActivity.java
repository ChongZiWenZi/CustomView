package com.example.customview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private CustomCircleProgress circleProgress;
    private int progress;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PROGRESS_CIRCLE_STARTING:
                    progress = circleProgress.getProgress();
                    circleProgress.setProgress(++progress);
                    if(progress >= 100){
                        handler.removeMessages(PROGRESS_CIRCLE_STARTING);
                        progress = 0;
                        circleProgress.setProgress(0);
                        circleProgress.setStatus(CustomCircleProgress.Status.End);//修改显示状态为完成
                    }else{
                        //延迟100ms后继续发消息，实现循环，直到progress=100
                        handler.sendEmptyMessageDelayed(PROGRESS_CIRCLE_STARTING, 100);
                    }
                    break;
            }
        }
    };
    public static final int PROGRESS_CIRCLE_STARTING = 0x110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleProgress = (CustomCircleProgress) findViewById(R.id.circleProgress);

        circleProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(circleProgress.getStatus() == CustomCircleProgress.Status.start){//如果是开始状态
                    //点击则开始
                     circleProgress.setStatus(CustomCircleProgress.Status.starting);
                    //注意，当我们暂停时，同时还要移除消息，不然的话进度不会被停止
                    Message message = Message.obtain();
                    message.what = PROGRESS_CIRCLE_STARTING;
                    handler.sendMessage(message);
                } else if (circleProgress.getStatus() == CustomCircleProgress.Status.starting){
                    //点击则变成开启状态
                    circleProgress.setStatus(CustomCircleProgress.Status.Stop);
                    handler.removeMessages(PROGRESS_CIRCLE_STARTING);
                }else if (circleProgress.getStatus() == CustomCircleProgress.Status.Stop){
                    //点击则变成开启状态
                    circleProgress.setStatus(CustomCircleProgress.Status.starting);
                    Message message = Message.obtain();
                    message.what = PROGRESS_CIRCLE_STARTING;
                    handler.sendMessage(message);
                }else {
                    circleProgress.setStatus(CustomCircleProgress.Status.starting);
                    //注意，当我们暂停时，同时还要移除消息，不然的话进度不会被停止
                    Message message = Message.obtain();
                    message.what = PROGRESS_CIRCLE_STARTING;
                    handler.sendMessage(message);
                }
            }
        });

        /*Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(progress >= 100){
                    progress = 0;
                    circleProgress.setProgress(0);
                }else{
                    progress = circleProgress.getProgress();
                    circleProgress.setProgress(++progress);
                }
            }
        };
        timer.schedule(task,0,100);*/

    }
}
