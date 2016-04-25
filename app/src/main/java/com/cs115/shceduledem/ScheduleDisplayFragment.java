package com.cs115.shceduledem;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScheduleDisplayFragment extends Fragment {

    public static ScheduleDisplayFragment newInstance() {
        return new ScheduleDisplayFragment();
    }

    public ScheduleDisplayFragment(){
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_schedule_display, container, false);
    }


}
