package com.cs115.shceduledem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ScheduleDisplayAdapter extends ArrayAdapter<ScheduleElement> {

    int resource;
    Context context;

    public ScheduleDisplayAdapter(Context _context, int _resource, List<ScheduleElement> items){
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LinearLayout newView;
        ScheduleElement w = getItem(position);

        newView = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
        vi.inflate(resource, newView, true);

        TextView month = (TextView) newView.findViewById(R.id.monthItem);
        TextView day = (TextView) newView.findViewById(R.id.dayItem);
        TextView time = (TextView) newView.findViewById(R.id.timeItem);
        TextView people = (TextView) newView.findViewById(R.id.peopleItem);
        TextView count = (TextView) newView.findViewById(R.id.countItem);


        if(!w.month.equals("")){
            month.setText(w.month);
        }
        if(!w.day.equals("")){
            day.setText(w.day);
        }
        time.setText(w.time);
        people.setText(w.people);
        count.setText(w.count);


        return newView;
    }



}
