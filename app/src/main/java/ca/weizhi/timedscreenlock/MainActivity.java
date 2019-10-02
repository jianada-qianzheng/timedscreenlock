package ca.weizhi.timedscreenlock;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.sql.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CountdownAdapter.SuccessResponse,TimerAdapter.Response {
    //private TextView mTextMessage;

    private SQLiteDatabase db;

    private SQLiteOpenHelper dbhelper;

    private int mode;

    ComponentName mAdminName;

    DevicePolicyManager mDPM;

    CountdownAdapter countdownAdapter;

    TimerAdapter timerAdapter;

    JobScheduler mJobScheduler;

    ConstraintLayout timedLayout;

    ConstraintLayout countdownLayout;

    ComponentName mServieComponent;

    ArrayList<CountdownTimer> countdownList;

    ArrayList<Timer> timerArrayList;

    private IncomingMessageHandler mHandler;


    public static void cancelJob(Context mContext, int jobID) {
        JobScheduler scheduler = (JobScheduler)
                mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == jobID) {
                scheduler.cancel(jobID);
                //Log.i(TAG,"Cancelled Job with ID:" + jobID);
            }
        }
    }


    @Override
    public void sendTimerContent(String info) {

        mode = Integer.parseInt(info.substring(0, 1));


        if (mode == 1) {
//            int position=Integer.parseInt(info.substring(1));
////
//            //mJobScheduler.cancelAll();
//
//
//            countdownList.get(position).setMode(mode);

//

        } else if (mode == 2) {
//            int position=Integer.parseInt(info.substring(1));
//
//            //todo
//
//            Toast.makeText(getApplicationContext(),getTimeString(countdownList.get(position).getTime()/1000)+"後鎖屏",Toast.LENGTH_LONG).show();
//
//
//            countdownList.get(position).setMode(mode);
//
//
//            countdownAdapter.notifyDataSetChanged();
//
//            Log.i("time_value",countdownList.get(position).getTime()+" ");
//
//
//
//
//
//            scheduleJob(position,countdownList.get(position).getTime(),false);


        } else if (mode == 3) {


            int position = Integer.parseInt(info.substring(1));
//
            timerArrayList.get(position).setMode(3);
//
//            countdownAdapter.notifyDataSetChanged();


        } else if (mode == 4) {
            int position = Integer.parseInt(info.substring(1));
            timerArrayList.get(position).setMode(mode);

//
            for (int i = 0; i < timerArrayList.size() - 1; i++) {
                timerArrayList.get(i).setMode(3);

            }

            timerArrayList.get(position).setMode(4);


        } else if (mode == 5) {
            int time = Integer.parseInt(info.substring(1));

            for (int i = 0; i < timerArrayList.size() - 1; i++) {

                Log.i("id_" + i, " " + timerArrayList.get(i).getId());

                if (timerArrayList.get(i).getMode() == 3) {
                    timerArrayList.get(i).setMode(3);

                } else if (timerArrayList.get(i).getMode() == 4) {
                    timerArrayList.get(i).setTime(time * 1000);
                    Log.i("update", mode + " ");


                    db.execSQL("UPDATE timer SET time = " + time * 1000 + " WHERE id = " + timerArrayList.get(i).getId() + "; ");

                    timerArrayList.get(i).setMode(3);


                }


            }
            mode = 3;

        } else if (mode == 6) {


            int position = Integer.parseInt(info.substring(1));
            db.execSQL("delete from timer where id=" + timerArrayList.get(position).getId() + ";");


            timerArrayList.remove(position);
            //database todo


            mode = 3;

            for (int i = 0; i < timerArrayList.size() - 1; i++) {

                timerArrayList.get(i).setMode(mode);
//
            }

        } else if (mode == 7) {
            for (int i = 0; i < timerArrayList.size() - 1; i++) {

                timerArrayList.get(i).setMode(3);

            }

            timerArrayList.add(0, new Timer(timerArrayList.get(0).getId() + 1, 0, 0, 0, 4));

            db.execSQL("insert into timer values(" + timerArrayList.get(0).getId() + "," + timerArrayList.get(timerArrayList.size() - 1).getTime() * 1000 + ",0,0);");


//            countdownList.add(countdownList.size()-1,new CountdownTimer(countdownList.get(countdownList.size()-1).getId()+1,1800000));
//
            mode = 3;
//
            ActionMenuItemView item = findViewById(R.id.mybutton);

            item.setTitle("確定");
//

//
            //timerArrayList.get(timerArrayList.size()-1).setMode(4);
        } else if (mode == 8) {

            int repeat = Integer.parseInt(info.substring(1, 2));

            int position = Integer.parseInt(info.substring(2));

            Log.i("repeat", " " + repeat + "" + position);

            timerArrayList.get(position).setRepeat(repeat);

            db.execSQL("UPDATE timer SET repeat = " + repeat + " WHERE id = " + timerArrayList.get(position).getId() + "; ");

            if (timerArrayList.get(position).isActive() == 1) {

                cancelJob(this, timerArrayList.get(position).getId() * (-1));
                mJobScheduler.cancel(timerArrayList.get(position).getId() * (-1));

                if (repeat == 1) {
                    scheduleJob(timerArrayList.get(position).getId() * (-1), getLockTime(timerArrayList.get(position).getTime()), true);
                } else {
                    scheduleJob(timerArrayList.get(position).getId() * (-1), getLockTime(timerArrayList.get(position).getTime()), false);

                }
            }


        } else if (mode == 9) {

            int active = Integer.parseInt(info.substring(1, 2));

            int position = Integer.parseInt(info.substring(2));

            timerArrayList.get(position).setActive(active);

            Log.i("switch", " " + active + "" + position);

            db.execSQL("UPDATE timer SET ative = " + active + " WHERE id = " + timerArrayList.get(position).getId() + "; ");

            boolean repeat = false;

            if (timerArrayList.get(position).isRepeat() == 1) {

                repeat = true;

            }

            if (active == 1) {

                scheduleJob(timerArrayList.get(position).getId() * (-1), getLockTime(timerArrayList.get(position).getTime()), repeat);

                Log.i("turn_on", " " + timerArrayList.get(position).getId() * (-1));
            } else {

                mJobScheduler.cancel(timerArrayList.get(position).getId() * (-1));

                cancelJob(this, timerArrayList.get(position).getId() * (-1));
                Log.i("turn_off", " " + timerArrayList.get(position).getId() * (-1));


            }


        }

        timerAdapter.notifyDataSetChanged();


    }

    public int getLockTime(int time) {


//        Calendar c = Calendar.getInstance();
//        System.out.println("Current time => "+c.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = df.format(c.getTime());
//        // formattedDate have current date/time
//        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();

        Date currentTime = Calendar.getInstance().getTime();


//        Calendar c = Calendar.getInstance();
//        int now_int = c.get(Calendar.MILLISECOND);


        //Date currentTime = Calendar.getInstance().getTime();

        //long now_long=currentTime.getTime();


        long now_long = (currentTime.getHours() * 60 * 60 + currentTime.getMinutes() * 60 + currentTime.getSeconds()) * 1000;

        int now_int = Integer.parseInt("" + now_long);

        Log.i("lock_time", now_int + "/" + time);

        if (now_int < time) {
            return time - now_int;

        } else {

            return time + 24 * 60 * 60 * 1000 - now_int;

        }


    }

    @Override
    public void sendContent(String info) {

        Log.i("sendContent", info + " ");
        Log.i("action", info.substring(0, 1));

        mode = Integer.parseInt(info.substring(0, 1));


        if (mode == 1) {
            int position = Integer.parseInt(info.substring(1));
//
            //mJobScheduler.cancelAll();


            countdownList.get(position).setMode(mode);

//

        } else if (mode == 2) {
            int position = Integer.parseInt(info.substring(1));

            //todo

            Toast.makeText(getApplicationContext(), getTimeString(countdownList.get(position).getTime() / 1000) + "後鎖屏", Toast.LENGTH_LONG).show();


            countdownList.get(position).setMode(mode);


            countdownAdapter.notifyDataSetChanged();

            Log.i("time_value", countdownList.get(position).getTime() + " ");


            scheduleJob(position, countdownList.get(position).getTime(), false);


        } else if (mode == 3) {


//            int position=Integer.parseInt(info.substring(1));
//
//            countdownList.get(position).setMode(mode);
//
//            countdownAdapter.notifyDataSetChanged();


        } else if (mode == 4) {
            int position = Integer.parseInt(info.substring(1));

            for (int i = 0; i < countdownList.size() - 1; i++) {
                countdownList.get(i).setMode(3);

            }

            countdownList.get(position).setMode(4);


        } else if (mode == 5) {
            int time = Integer.parseInt(info.substring(1));

            for (int i = 0; i < countdownList.size() - 1; i++) {

                if (countdownList.get(i).getMode() == 3) {
                    countdownList.get(i).setMode(3);

                } else if (countdownList.get(i).getMode() == 4) {
                    countdownList.get(i).setTime(time * 1000);


                    db.execSQL("UPDATE countdown SET time = " + time * 1000 + " WHERE ID = '" + countdownList.get(i).getId() + "'; ");
                    countdownList.get(i).setMode(3);


                }


            }
            mode = 3;

        } else if (mode == 6) {


            int position = Integer.parseInt(info.substring(1));
            db.execSQL("delete from countdown where id=" + countdownList.get(position).getId() + ";");


            countdownList.remove(position);
            //database todo


            mode = 3;

            for (int i = 0; i < countdownList.size() - 1; i++) {

                countdownList.get(i).setMode(mode);

            }

        } else if (mode == 7) {


            countdownList.add(countdownList.size() - 1, new CountdownTimer(countdownList.get(countdownList.size() - 1).getId() + 1, 1800000));

            mode = 3;

            ActionMenuItemView item = findViewById(R.id.mybutton);

            item.setTitle("確定");

            for (int i = 0; i < countdownList.size() - 1; i++) {

                countdownList.get(i).setMode(3);

            }

            countdownList.get(countdownList.size() - 2).setMode(4);
        }

        countdownAdapter.notifyDataSetChanged();

        //todo


    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:

                    if (countdownLayout.getVisibility() == View.VISIBLE) {
                        mode = 1;

                        ActionMenuItemView item1 = findViewById(R.id.mybutton);


                        item1.setTitle("編輯");

                        for (int i = 0; i < countdownList.size() - 1; i++) {

                            countdownList.get(i).setMode(mode);

                            //todo update

                        }

                        countdownAdapter.notifyDataSetChanged();
                    }

                    timedLayout.setVisibility(View.VISIBLE);

                    countdownLayout.setVisibility(View.GONE);


                    return true;
                case R.id.navigation_dashboard:
                    if (countdownLayout.getVisibility() == View.GONE) {
                        mode = 1;

                        ActionMenuItemView item1 = findViewById(R.id.mybutton);
                        item1.setTitle("編輯");

                        for (int i = 0; i < timerArrayList.size() - 1; i++) {

                            timerArrayList.get(i).setMode(mode);

                            //todo update

                        }
                        timerAdapter.notifyDataSetChanged();

                    }

                    timedLayout.setVisibility(View.GONE);

                    countdownLayout.setVisibility(View.VISIBLE);


                    return true;
//                case R.id.navigation_notifications:
//                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // 启动服务并提供一种与此类通信的方法。
        Intent startServiceIntent = new Intent(this, JobSchedulerService.class);
        Messenger messengerIncoming = new Messenger(mHandler);
        startServiceIntent.putExtra("MESSENGER_INTENT_KEY", messengerIncoming);
        startService(startServiceIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        initCountDownList();

        initTimerList();


    }

    public String getTimeString(int time) {

        String timeString = "";
        int hour;
        int minu;
        int second;

        second = time % 60;
        minu = time / 60 % 60;
        hour = time / 60 / 60;

        if (hour == 0 && minu == 0 && second != 0) {

            timeString = (second + "秒");

        } else if (hour == 0 && minu != 0) {
            timeString = (minu + "分鐘" + second + "秒");
        } else if (hour != 0) {
            timeString = (hour + "小時" + minu + "分鐘" + second + "秒");

        }

        return timeString;
    }

    public void initUI() {

        timedLayout = findViewById(R.id.timed);

        countdownLayout = findViewById(R.id.countdown);

        timedLayout.setVisibility(View.VISIBLE);

        countdownLayout.setVisibility(View.GONE);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mode = 1;

        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);


        mAdminName = new ComponentName(this, AdminManageReceiver.class);
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mServieComponent = new ComponentName(this, JobSchedulerService.class);// 获取到我们自己的jobservice，同时启动该service


        //如果设备管理器尚未激活，这里会启动一个激活设备管理器的Intent,具体的表现就是第一次打开程序时，手机会弹出激活设备管理器的提示，激活即可。
        if (!mDPM.isAdminActive(mAdminName)) {
            showAdminManagement(mAdminName);
        }

        dbhelper = new CustomSQLiteOpenHelper(this);

        db = dbhelper.getWritableDatabase();

        mHandler = new IncomingMessageHandler(this);

    }

    public void initCountDownList() {

        countdownList = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from countdown;", null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            int time = cursor.getInt(1);

            Log.i("add", "new Countdown");

            countdownList.add(0, new CountdownTimer(id, time));

        }


        countdownAdapter = new CountdownAdapter(MainActivity.this, countdownList, this);

        ListView countdownlistView = (ListView) findViewById(R.id.countdownlist);


//设置适配器

        countdownlistView.setAdapter(countdownAdapter);

    }

    public void initTimerList() {

        timerArrayList = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from timer;", null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            int time = cursor.getInt(1);
            int repeat = cursor.getInt(2);
            int active = cursor.getInt(3);

            Log.i("id", "new timer" + id);

            timerArrayList.add(0, new Timer(id, time, repeat, active, 1));

        }

        timerArrayList.get(timerArrayList.size() - 1).setMode(0);


        timerAdapter = new TimerAdapter(MainActivity.this, timerArrayList, this);

        ListView timerListView = (ListView) findViewById(R.id.timerlist);


//设置适配器

        timerListView.setAdapter(timerAdapter);

    }

    public void lockScreen() {
        if (mDPM.isAdminActive(mAdminName)) {
            mDPM.lockNow();
        }
    }

    @Override
    protected void onStop() {
        // 服务可以是“开始”和/或“绑定”。 在这种情况下，它由此Activity“启动”
        // 和“绑定”到JobScheduler（也被JobScheduler称为“Scheduled”）。
        // 对stopService（）的调用不会阻止处理预定作业。
        // 然而，调用stopService（）失败将使它一直存活。
        stopService(new Intent(this, JobSchedulerService.class));
        super.onStop();
    }

    //激活设备管理器
    private void showAdminManagement(ComponentName mAdminName) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "activity device");
        startActivityForResult(intent, 1);
    }


    public void scheduleJob(int mJobId, int delay, boolean repeat) {
        //开始配置JobInfo
        JobInfo.Builder builder = new JobInfo.Builder(mJobId, mServieComponent);

        //设置任务的延迟执行时间(单位是毫秒)
        // builder.setMinimumLatency(Long.valueOf(delay) * 1000);
        //builder.setPeriodic(3000);

        if (repeat) {
            //builder.setPeriodic(1000 * 60 * 60 * 24);
            builder.setPeriodic(1000 * 60 * 16);

        } else {

            //builder.setPeriodic(0);

            builder.setMinimumLatency(0);

        }


        //设置任务最晚的延迟时间。如果到了规定的时间时其他条件还未满足，你的任务也会被启动。
        //builder.setOverrideDeadline(Long.valueOf(20) * 1000);


        // Extras, work duration.
        PersistableBundle extras = new PersistableBundle();

        extras.putLong("WORK_DURATION_KEY", delay);


        //extras.putLong("WORK_DURATION_KEY", delay * 1000);
        //extras.putLong("WORK_DURATION_KEY", 30 * 1000);

        builder.setExtras(extras);

        // Schedule job
        //Log.d(TAG, "Scheduling job");
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        // 这里就将开始在service里边处理我们配置好的job
        mJobScheduler.schedule(builder.build());


    }




    public void cancelAllJobs() {
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancelAll();

        //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

        //Toast.makeText(MainActivity.this, R.string.all_jobs_cancelled, Toast.LENGTH_SHORT).show();
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("set3", "mode=3");

        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here

            if (mode == 1) {

                mode = 3;//edit

                item.setTitle("確定");

                if (countdownLayout.getVisibility() == View.VISIBLE) {

                    for (int i = 0; i < countdownList.size() - 1; i++) {
                        Log.i("set1", "mode=3");


                        countdownList.get(i).setMode(mode);

                    }
                } else {
                    for (int i = 0; i < timerArrayList.size() - 1; i++) {
                        Log.i("set2", "mode=3");


                        timerArrayList.get(i).setMode(mode);

                    }

                }

            } else if (mode == 3) {
                mode = 1;
                item.setTitle("編輯");
                if (countdownLayout.getVisibility() == View.VISIBLE) {
                    for (int i = 0; i < countdownList.size() - 1; i++) {

                        countdownList.get(i).setMode(mode);

                        //todo update

                    }
                } else {
                    for (int i = 0; i < timerArrayList.size() - 1; i++) {

                        timerArrayList.get(i).setMode(mode);

                        //todo update

                    }

                }


                //}else if(mode==2){

            } else {
                mode = 3;//edit

                if (countdownLayout.getVisibility() == View.VISIBLE) {
                    for (int i = 0; i < countdownList.size() - 1; i++) {


                        if (countdownList.get(i).countDownTimer != null) {
                            countdownList.get(i).countDownTimer.cancel();

                        }
                        countdownList.get(i).setMode(mode);


                    }
                } else {
                    for (int i = 0; i < timerArrayList.size() - 1; i++) {

                        Log.i("set", "mode=3");


                        timerArrayList.get(i).setMode(mode);


                    }

                }


                item.setTitle("確定");


            }

            countdownAdapter.notifyDataSetChanged();
            timerAdapter.notifyDataSetChanged();


        }
        return super.onOptionsItemSelected(item);
    }

    private  class IncomingMessageHandler extends Handler {

        // 使用弱引用防止内存泄露
        private WeakReference<MainActivity> mActivity;

        IncomingMessageHandler(MainActivity activity) {
            super(/* default looper */);
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mSchedulerAcitvity = mActivity.get();
            if (mSchedulerAcitvity == null) {
                // 活动不再可用，退出。
                return;
            }

            // 获取到两个View，用于之后根据Job运行状态显示不同的运行状态（颜色变化）
//            View showStartView = mSchedulerAcitvity.findViewById(R.id.onstart_textview);
//            View showStopView = mSchedulerAcitvity.findViewById(R.id.onstop_textview);

            Message m;
            Log.i("message"," "+msg.what+"/"+msg.obj);

            int position=(Integer.parseInt(""+msg.obj))*(-1);

            if(msg.what==0){
                //todo
                if(timerArrayList.get(position).isRepeat()==0){
                    timerArrayList.get(position).setActive(0);

                    //timerArrayList.get(3).setActive(1);

                    //timerArrayList.get(position).setMode(1);

                    Log.i("changed"," "+msg.what+"/"+position);

                    //db.execSQL("");todo
                }



                timerAdapter.notifyDataSetChanged();

                //timedLayout.setVisibility(View.GONE);





            }




//            switch (msg.what) {
//                // 当作业登录到应用程序时，从服务接收回调。 打开指示灯（上方View闪烁）并发送一条消息，在一秒钟后将其关闭。
//                case MSG_JOB_START:
//                    // Start received, turn on the indicator and show text.
//                    // 开始接收，打开指示灯（上方View闪烁）并显示文字。
//                    showStartView.setBackgroundColor(getColor(R.color.start_received));
//                    updateParamsTextView(msg.obj, "started");
//
//                    // Send message to turn it off after a second.
//                    // 发送消息，一秒钟后关闭它。
//                    m = Message.obtain(this, MSG_ONJOB_START);
//                    sendMessageDelayed(m, 1000L);
//                    break;
//
//                // 当先前执行在应用程序中的作业必须停止执行时，
//                // 从服务接收回调。 打开指示灯并发送一条消息，
//                // 在两秒钟后将其关闭。
//                case MSG_JOB_STOP:
//                    // Stop received, turn on the indicator and show text.
//                    // 停止接收，打开指示灯并显示文本。
//                    showStopView.setBackgroundColor(getColor(R.color.stop_received));
//                    updateParamsTextView(msg.obj, "stopped");
//
//                    // Send message to turn it off after a second.
//                    // 发送消息，一秒钟后关闭它。
//                    m = obtainMessage(MSG_ONJOB_STOP);
//                    sendMessageDelayed(m, 2000L);
//                    break;
//                case MSG_ONJOB_START:
//                    showStartView.setBackgroundColor(getColor(R.color.none_received));
//                    updateParamsTextView(null, "job had started");
//                    break;
//                case MSG_ONJOB_STOP:
//                    showStopView.setBackgroundColor(getColor(R.color.none_received));
//                    updateParamsTextView(null, "job had stoped");
//                    break;
//            }
        }


    }


}