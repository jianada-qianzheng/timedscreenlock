package ca.weizhi.timedscreenlock;

import android.os.CountDownTimer;

public class CountdownTimer {
    private  int id;
    private  int time;
    private boolean editable;
    private boolean running;
    private int mode;

    CountDownTimer countDownTimer;


    public CountdownTimer(int id ,int time){
        this.id=id;
        this.time=time;
        editable=false;
        running=false;
        if(id==0){
            mode=0;
        }else{
            mode=1;
        }


    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public int getMode() {
        return mode;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isRunning() {
        return running;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setId(int id) {
        this.id = id;
    }
}
