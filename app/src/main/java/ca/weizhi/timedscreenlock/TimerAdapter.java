package ca.weizhi.timedscreenlock;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class TimerAdapter extends BaseAdapter {
    private ArrayList<Timer> listData;
    private LayoutInflater layoutInflater;

    Response response;





    public interface Response{
        void sendTimerContent(String info);
    }

    public TimerAdapter(Context aContext, ArrayList<Timer> listData,Response response) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);

        this.response=response;

    }
    @Override
    public int getCount() {
        return listData.size();
        //return 2;
    }
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
    @Override
    public long getItemId( int position) {
        return position;
    }
    public View getView(final int position, View v, ViewGroup vg) {
        final ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.timer_item, null);
            holder = new ViewHolder();
            holder.aSwitch=v.findViewById(R.id.switch1);
            holder.checkBox=v.findViewById(R.id.checkBox);
            holder.deleteButton=v.findViewById(R.id.delete);
            holder.timeView=v.findViewById(R.id.time_text);
            holder.timePicker=v.findViewById(R.id.timePicker1);

            holder.changeButton=v.findViewById(R.id.change);


            holder.deleteButton.setVisibility(View.GONE);

            holder.timePicker.setVisibility(View.GONE);

//            if(listData.get(position).isActive()==1){
//
//                holder.aSwitch.setChecked(true);
//
//            }else {
//                holder.aSwitch.setChecked(false);
//            }
//
//            if(listData.get(position).isRepeat()==1){
//
//                holder.checkBox.setChecked(true);
//
//            }else {
//                holder.checkBox.setChecked(false);
//            }

            holder.deleteButton.setVisibility(View.GONE);
            holder.changeButton.setVisibility(View.GONE);

            holder.aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((Switch)view).isChecked()){
                        response.sendTimerContent("91"+position);

                    }else{

                        response.sendTimerContent("90"+position);


                    }
                }
            });



            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((CheckBox)view).isChecked()){
                        response.sendTimerContent("81"+position);

                    }else{

                        response.sendTimerContent("80"+position);


                    }
                }
            });

            holder.changeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listData.get(position).getMode()==1) {


//                        successResponse.sendContent("2" + position);
//                        listData.get(position).countDownTimer= new CountDownTimer(listData.get(position).getTime(), 1000) {
//
//                            public void onTick(long millisUntilFinished) {
//
//
//                                Log.i("countdown", "seconds_remaining: " + millisUntilFinished / 1000);
//
//                                setTimeVIew((int) (millisUntilFinished / 1000), holder.timeText);
//
//                                //holder.start.setClickable(false);
//
//
//                                //holder.timeText.setText( (millisUntilFinished / 1000)+" ");
//
//                            }
//
//                            public void onFinish() {
//                                Log.i("countdown", "done!");
//
//
//                                setTimeVIew((int) (listData.get(position).getTime() / 1000), holder.timeText);
//
//                                holder.start.setText("開始倒計時");
//                                successResponse.sendContent("1"+position);
//                            }
//                        };
//                        listData.get(position).countDownTimer.start();
                    }else if (listData.get(position).getMode()==2){

//                        successResponse.sendContent("1" + position);
//                        listData.get(position).countDownTimer.cancel();

                        //countDownTimer.cancel();

                    }else if (listData.get(position).getMode()==3){

                        response.sendTimerContent("4"+position);



                        //successResponse.sendContent("4"+position);




                    }else if (listData.get(position).getMode()==4){





                        int time=(holder.timePicker.getCurrentHour()*60*60+holder.timePicker.getCurrentMinute()*60);

                        //time=holder.timePicker.getHour()*60*60;

                        Log.i("time"," "+time);


//
//
//
                       response.sendTimerContent("5"+time);



                    }else if(listData.get(position).getMode()==0){

                        response.sendTimerContent("7");


                        //listData.add(0, new Timer(listData.get(listData.size()-1).getId()+1,0, 0,0,4));


                    }
                }
            });





            //holder.editText=v.findViewById(R.id.editText);
//            holder.uName = (TextView) v.findViewById(R.id.name);
//            holder.uDesignation = (TextView) v.findViewById(R.id.designation);
//            holder.uLocation = (TextView) v.findViewById(R.id.location);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.timeView.setText   (getTimeString(listData.get(position).getTime()/1000)  );

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response.sendTimerContent("6"+position);
            }
        });


        if(listData.get(position).getMode()==0){
            holder.timeView.setVisibility(View.GONE);
            holder.changeButton.setText("+");
            holder.checkBox.setVisibility(View.GONE);
            holder.aSwitch.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
            holder.changeButton.setVisibility(View.VISIBLE);

        }else if(listData.get(position).getMode()==1){
            //holder.timeView.setText   (getTimeString(listData.get(position).getTime()/1000)  );

            //holder.changeButton.setText("更改時間");
            holder.timePicker.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.aSwitch.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.GONE);
            holder.changeButton.setVisibility(View.GONE);


        }else if(listData.get(position).getMode()==2){


        }else if(listData.get(position).getMode()==3){
            Log.i("change_mode","mode=3");

            holder.timeView.setVisibility(View.VISIBLE);
            holder.changeButton.setText("更改時間");
            holder.checkBox.setVisibility(View.GONE);
            holder.aSwitch.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.changeButton.setVisibility(View.VISIBLE);
            holder.timePicker.setVisibility(View.GONE);


        }else if(listData.get(position).getMode()==4){
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.changeButton.setVisibility(View.VISIBLE);
            holder.aSwitch.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.GONE);

            holder.timePicker.setVisibility(View.VISIBLE);
            holder.changeButton.setText("保存");

            Log.i("change_mode","mode=3");
            //holder.timeView.setVisibility(View.GONE);
//            holder.changeButton.setText("更改時間");
//            holder.checkBox.setVisibility(View.GONE);
//            holder.aSwitch.setVisibility(View.GONE);
//            holder.deleteButton.setVisibility(View.VISIBLE);
//            holder.changeButton.setVisibility(View.VISIBLE);

        }

        if(listData.get(position).isActive()==1){

            holder.aSwitch.setChecked(true);

        }else {
            holder.aSwitch.setChecked(false);
        }

        if(listData.get(position).isRepeat()==1){

            holder.checkBox.setChecked(true);

        }else {
            holder.checkBox.setChecked(false);
        }

        holder.timePicker.setIs24HourView(true);



//        holder.uName.setText(listData.get(position).getName());
//        holder.uDesignation.setText(listData.get(position).getDesignation());
//        holder.uLocation.setText(listData.get(position).getLocation());
        //holder.editText.setText("1");
        return v;
    }
    static class ViewHolder {
        TextView timeView;
        CheckBox checkBox;
        Button deleteButton;

        Button changeButton;

        Switch aSwitch;

        TimePicker timePicker;




//        TextView uName;
//        TextView uDesignation;
//        TextView uLocation;
    }


    String getTimeString(int time){
        //long timeSec= 84561;// Json output
        int hours = (int) time/ 3600;


        int temp = (int) time- hours * 3600;
        int mins = temp / 60;
        temp = temp - mins * 60;
        int secs = temp;

        String hourString="";
        if(hours>9){
            hourString=""+hours;

        }else{
            hourString="0"+hours;
        }


        String minuString="";
        if(mins>9){
            minuString=""+mins;

        }else{
            minuString="0"+mins;
        }


        String seconString="";
        if(secs>9){
            seconString=""+secs;

        }else{
            seconString="0"+secs;
        }


        return  hourString+ ": "+minuString+": "+seconString;//hh:mm:ss formatted string


    }
}
