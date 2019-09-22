package ca.weizhi.timedscreenlock;

public class Timer {

    private int time;
    private boolean active;
    private boolean repeat;

    public Timer(int time,boolean active,boolean repeat){
        this.time=time;
        this.active=active;
        this.repeat=repeat;
    }

    public int getTime() {
        return time;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setTime(int time) {
        this.time = time;
    }


}
