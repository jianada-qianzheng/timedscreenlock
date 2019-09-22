package ca.weizhi.timedscreenlock;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CountdownAdapter.SuccessResponse {
    //private TextView mTextMessage;

    private SQLiteDatabase db;

    private SQLiteOpenHelper dbhelper;

    private int mode;

    ComponentName mAdminName;
    DevicePolicyManager mDPM;

    CountdownAdapter countdownAdapter;

    JobScheduler mJobScheduler;

    @Override
    public void sendContent(String info) {

        Log.i("sendContent",info+" ");
        Log.i("action",info.substring(0,1));

        mode=Integer.parseInt(info.substring(0,1));


        if(mode==1){
            int position=Integer.parseInt(info.substring(1));
//
            //mJobScheduler.cancelAll();


            countdownList.get(position).setMode(mode);

//

        } else if(mode==2){
            int position=Integer.parseInt(info.substring(1));

            //todo

            Toast.makeText(getApplicationContext(),getTimeString(countdownList.get(position).getTime()/1000)+"後鎖屏",Toast.LENGTH_LONG).show();


            countdownList.get(position).setMode(mode);


            countdownAdapter.notifyDataSetChanged();

            Log.i("time_value",countdownList.get(position).getTime()+" ");





            scheduleJob(position,countdownList.get(position).getTime(),false);




        }else if(mode==3){




//            int position=Integer.parseInt(info.substring(1));
//
//            countdownList.get(position).setMode(mode);
//
//            countdownAdapter.notifyDataSetChanged();



        }else if (mode==4){
            int position=Integer.parseInt(info.substring(1));

            for(int i=0;i<countdownList.size()-1;i++){
                countdownList.get(i).setMode(3);

            }

            countdownList.get(position).setMode(4);





        }else if (mode==5){
            int time=Integer.parseInt(info.substring(1));

            for(int i=0;i<countdownList.size()-1;i++){

                if(countdownList.get(i).getMode()==3){
                    countdownList.get(i).setMode(3);

                }else if(countdownList.get(i).getMode()==4){
                    countdownList.get(i).setTime(time*1000);


                    db.execSQL("UPDATE countdown SET time = "+time*1000+" WHERE ID = '"+countdownList.get(i).getId()+"'; ");
                    countdownList.get(i).setMode(3);



                }


            }
            mode=3;

        }else if (mode==6){



            int position=Integer.parseInt(info.substring(1));
            db.execSQL("delete from countdown where id="+countdownList.get(position).getId()+";");


            countdownList.remove(position);
            //database todo


            mode=3;

            for(int i=0;i<countdownList.size()-1;i++){

                countdownList.get(i).setMode(mode);

            }

        }else if(mode==7){


            countdownList.add(countdownList.size()-1,new CountdownTimer(countdownList.get(countdownList.size()-1).getId()+1,1800000));

            mode=3;

            ActionMenuItemView item=findViewById(R.id.mybutton);

            item.setTitle("確定");

            for(int i=0;i<countdownList.size()-1;i++){

                countdownList.get(i).setMode(3);

            }

            countdownList.get(countdownList.size()-2).setMode(4);
        }

        countdownAdapter.notifyDataSetChanged();

        //todo


    }
    ComponentName mServieComponent;

    ArrayList<CountdownTimer> countdownList;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        initCountDownList();



    }

    public String getTimeString(int time){

        String timeString="";
        int hour;
        int minu;
        int second;

        second=time%60;
        minu=time/60%60;
        hour=time/60/60;

        if(hour==0&&minu==0&&second!=0){

            timeString= (second+"秒");

        }else if (hour==0&&minu!=0){
            timeString= (minu+"分鐘"+second+"秒");
        }else if(hour!=0){
            timeString= (hour+"小時"+minu+"分鐘"+second+"秒");

        }

        return timeString;
    }

    public void initUI(){
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mode=1;

        //mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);


        mAdminName = new ComponentName(this, AdminManageReceiver.class);
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mServieComponent = new ComponentName(this, JobSchedulerService.class);// 获取到我们自己的jobservice，同时启动该service


        //如果设备管理器尚未激活，这里会启动一个激活设备管理器的Intent,具体的表现就是第一次打开程序时，手机会弹出激活设备管理器的提示，激活即可。
        if (!mDPM.isAdminActive(mAdminName)) {
            showAdminManagement(mAdminName);
        }

        dbhelper = new CustomSQLiteOpenHelper(this);

        db = dbhelper.getReadableDatabase();


    }

    public  void initCountDownList(){

        countdownList=new ArrayList<>();

        Cursor cursor=db.rawQuery("select * from countdown;",null);

        while (cursor.moveToNext()){

            int id=cursor.getInt(0);
            int time=cursor.getInt(1);

            Log.i("add","new Countdown");

            countdownList.add(0,new CountdownTimer(id,time));

        }




         countdownAdapter=new CountdownAdapter(MainActivity.this, countdownList,this);

        ListView countdownlistView=(ListView) findViewById(R.id.countdownlist);


//设置适配器

        countdownlistView.setAdapter(countdownAdapter);

    }

    public void lockScreen(){
        if (mDPM.isAdminActive(mAdminName)) {
            mDPM.lockNow();
        }
    }

    //激活设备管理器
    private void showAdminManagement(ComponentName mAdminName) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "activity device");
        startActivityForResult(intent,1);
    }


    public void scheduleJob(int mJobId,int delay,boolean repeat) {
        //开始配置JobInfo
        JobInfo.Builder builder = new JobInfo.Builder(mJobId, mServieComponent);

        //设置任务的延迟执行时间(单位是毫秒)
       // builder.setMinimumLatency(Long.valueOf(delay) * 1000);
            //builder.setPeriodic(3000);

        if(repeat) {
            builder.setPeriodic(1000 * 60 * 60*24);
        }else{
             builder.setMinimumLatency(0);

        }


        //设置任务最晚的延迟时间。如果到了规定的时间时其他条件还未满足，你的任务也会被启动。
            //builder.setOverrideDeadline(Long.valueOf(20) * 1000);




        // Extras, work duration.
        PersistableBundle extras = new PersistableBundle();

        extras.putLong("WORK_DURATION_KEY", delay );


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
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here

            if(mode==1) {

                mode = 3;//edit

                item.setTitle("確定");

                for (int i = 0; i < countdownList.size() - 1; i++) {

                    countdownList.get(i).setMode(mode);

                }

            }else if(mode==3){
                mode=1;
                item.setTitle("編輯");
                for (int i = 0; i < countdownList.size() - 1; i++) {

                    countdownList.get(i).setMode(mode);

                    //todo update

                }


            }else if(mode==2){
                mode = 3;//edit
                for(int i=0;i<countdownList.size()-1;i++) {

                    if(countdownList.get(i).countDownTimer!=null) {
                        countdownList.get(i).countDownTimer.cancel();

                    }
                    countdownList.get(i).setMode(mode);


                }



                item.setTitle("確定");



            }

            countdownAdapter.notifyDataSetChanged();


        }
        return super.onOptionsItemSelected(item);
    }




}


