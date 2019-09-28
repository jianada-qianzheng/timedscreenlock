package ca.weizhi.timedscreenlock;

public class Timer {
    private int id;

    private int time;
    private int active;
    private int repeat;
    private int mode;



    public Timer(int id,int time,int repeat,int active,int mode){
        this.id=id;
        this.time=time;
        this.active=active;
        this.repeat=repeat;
        this.mode=mode;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public int isActive() {
        return active;
    }

    public int isRepeat() {
        return repeat;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
