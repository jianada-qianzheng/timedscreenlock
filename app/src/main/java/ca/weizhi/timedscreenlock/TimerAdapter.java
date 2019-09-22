package ca.weizhi.timedscreenlock;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class TimerAdapter extends BaseAdapter {
    private ArrayList<Timer> listData;
    private LayoutInflater layoutInflater;
    public TimerAdapter(Context aContext, ArrayList<Timer> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
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
    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.timer_item, null);
            holder = new ViewHolder();
            //holder.editText=v.findViewById(R.id.editText);
//            holder.uName = (TextView) v.findViewById(R.id.name);
//            holder.uDesignation = (TextView) v.findViewById(R.id.designation);
//            holder.uLocation = (TextView) v.findViewById(R.id.location);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
//        holder.uName.setText(listData.get(position).getName());
//        holder.uDesignation.setText(listData.get(position).getDesignation());
//        holder.uLocation.setText(listData.get(position).getLocation());
        //holder.editText.setText("1");
        return v;
    }
    static class ViewHolder {
        EditText editText;
//        TextView uName;
//        TextView uDesignation;
//        TextView uLocation;
    }
}
