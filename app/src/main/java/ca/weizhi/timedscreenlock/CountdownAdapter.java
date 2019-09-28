package ca.weizhi.timedscreenlock;

import android.content.ComponentName;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

public class CountdownAdapter extends BaseAdapter {

    ComponentName mServieComponent;

    SuccessResponse successResponse;

    //CountDownTimer countDownTimer;

    public interface SuccessResponse{
        void sendContent(String info);
    }


    private ArrayList<CountdownTimer> listData;
    private LayoutInflater layoutInflater;
    public CountdownAdapter(Context aContext, ArrayList<CountdownTimer> listData,SuccessResponse successResponse) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        this.successResponse=successResponse;



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
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View v, ViewGroup vg) {
        final CountdownAdapter.ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.countdown_item, null);
            holder = new CountdownAdapter.ViewHolder();
            holder.start=v.findViewById(R.id.start);
            holder.timeText=v.findViewById(R.id.time_text);
            holder.countdownpicker=v.findViewById(R.id.countdown_picker);

            holder.delete=v.findViewById(R.id.delete);

            holder.textView1=v.findViewById(R.id.textView1);

            holder.textView2=v.findViewById(R.id.textView2);

            holder.hourPicker=v.findViewById(R.id.hourPicker);

            holder.minuPicker=v.findViewById(R.id.minuPicker);

            holder.seconPicker=v.findViewById(R.id.seconPicker);



            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listData.get(position).getMode()==1) {


                        successResponse.sendContent("2" + position);
                        listData.get(position).countDownTimer= new CountDownTimer(listData.get(position).getTime(), 1000) {

                            public void onTick(long millisUntilFinished) {


                                Log.i("countdown", "seconds_remaining: " + millisUntilFinished / 1000);

                                setTimeVIew((int) (millisUntilFinished / 1000), holder.timeText);

                                //holder.start.setClickable(false);


                                //holder.timeText.setText( (millisUntilFinished / 1000)+" ");

                            }

                            public void onFinish() {
                                Log.i("countdown", "done!");


                                setTimeVIew((int) (listData.get(position).getTime() / 1000), holder.timeText);

                                holder.start.setText("開始倒計時");
                                successResponse.sendContent("1"+position);
                            }
                        };
                        listData.get(position).countDownTimer.start();
                    }else if (listData.get(position).getMode()==2){

                        successResponse.sendContent("1" + position);
                        listData.get(position).countDownTimer.cancel();

                        //countDownTimer.cancel();

                    }else if (listData.get(position).getMode()==3){



                        successResponse.sendContent("4"+position);




                    }else if (listData.get(position).getMode()==4){

                        int time=holder.hourPicker.getValue()*60*60+holder.minuPicker.getValue()*60+holder.seconPicker.getValue();



                        successResponse.sendContent("5"+time);



                    }else if(listData.get(position).getMode()==0){

                        successResponse.sendContent("7");


                        //listData.add(new CountdownTimer(listData.size(),600));


                    }
                }
            });

            v.setTag(holder);
        } else {
            holder = (CountdownAdapter.ViewHolder) v.getTag();
        }
//        holder.uName.setText(listData.get(position).getName());
//        holder.uDesignation.setText(listData.get(position).getDesignation());
//        holder.uLocation.setText(listData.get(position).getLocation());
        //holder.editText.setText("1");

        if(listData.get(position).getMode()==0){

            holder.countdownpicker.setVisibility(View.GONE);

            holder.timeText.setVisibility(View.GONE);

            holder.delete.setVisibility(View.GONE);
            holder.start.setVisibility(View.VISIBLE);

            holder.start.setText("+");

            holder.textView1.setVisibility(View.GONE);
            holder.textView2.setVisibility(View.VISIBLE);

            //holder.start.setClickable(true);

            //holder.start.setVisibility(View.GONE);





        }else if (listData.get(position).getMode()==1){
            //holder.countdownpicker.setVisibility(View.GONE);

            holder.timeText.setVisibility(View.VISIBLE);

            holder.delete.setVisibility(View.GONE);
            holder.start.setVisibility(View.VISIBLE);


            holder.start.setText("開始倒計時");

            holder.textView1.setVisibility(View.VISIBLE);
            holder.textView2.setVisibility(View.GONE);

            setTimeVIew(listData.get(position).getTime()/1000,holder.timeText);
            holder.start.setClickable(true);
            holder.countdownpicker.setVisibility(View.GONE);



        }else if (listData.get(position).getMode()==2){

            holder.countdownpicker.setVisibility(View.GONE);

            holder.timeText.setVisibility(View.VISIBLE);

            holder.delete.setVisibility(View.GONE);

            holder.start.setVisibility(View.VISIBLE);

            holder.start.setText("正在倒計時");

            holder.start.setClickable(false);


            holder.textView1.setVisibility(View.VISIBLE);
            holder.textView2.setVisibility(View.GONE);

            setTimeVIew(listData.get(position).getTime()/1000,holder.timeText);

        }else  if (listData.get(position).getMode()==3){
            holder.delete.setVisibility(View.VISIBLE);
            holder.countdownpicker.setVisibility(View.GONE);
            setTimeVIew(listData.get(position).getTime()/1000,holder.timeText);

//
            holder.start.setClickable(true);
            holder.start.setText("更改時間");




        }else if (listData.get(position).getMode()==4){

            holder.start.setText("保存");

            holder.countdownpicker.setVisibility(View.VISIBLE);

            holder.hourPicker.setMaxValue(10);

            holder.minuPicker.setMaxValue(59);

            holder.seconPicker.setMaxValue(59);

            holder.delete.setVisibility(View.VISIBLE);

            holder.timeText.setVisibility(View.VISIBLE);


            setTimeVIew(listData.get(position).getTime()/1000,holder.timeText);


            setPickerVIew(listData.get(position).getTime()/1000,holder.hourPicker,holder.minuPicker,holder.seconPicker);



        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                successResponse.sendContent("6"+position);
            }
        });
        return v;
    }

     public void setTimeVIew(int time,TextView timeView){

        int hour;
        int minu;
        int second;

        second=time%60;
        minu=time/60%60;
        hour=time/60/60;

        if(hour==0&&minu==0&&second!=0){

            timeView.setText(second+"秒");

        }else if (hour==0&&minu!=0){
            timeView.setText(minu+"分鐘"+second+"秒");
        }else if(hour!=0){
            timeView.setText(hour+"小時"+minu+"分鐘"+second+"秒");

        }

         //timeView.setText(hour+"小時"+minu+"分鐘"+second+"秒");





     }



    public void setPickerVIew(int time,        NumberPicker hourPicker,NumberPicker minuPicker,NumberPicker sencPicker){

        int hour;
        int minu;
        int second;

        second=time%60;
        minu=time/60%60;
        hour=time/60/60;



            hourPicker.setValue(hour);
            minuPicker.setValue(minu);
            sencPicker.setValue(second);











    }

    static class ViewHolder {
        TextView timeText;
        Button start;
        LinearLayout countdownpicker;
        Button delete;

        TextView textView1;

        TextView textView2;

        NumberPicker hourPicker;

        NumberPicker minuPicker;

        NumberPicker seconPicker;


//        TextView uName;
//        TextView uDesignation;
//        TextView uLocation;
    }




}
