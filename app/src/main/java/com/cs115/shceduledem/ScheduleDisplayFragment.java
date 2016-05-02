package com.cs115.shceduledem;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ScheduleDisplayFragment extends Fragment {

    private ArrayList<ScheduleElement> aList;
    private ScheduleDisplayAdapter aa;

    public static ScheduleDisplayFragment newInstance() {
        return new ScheduleDisplayFragment();
    }

    public ScheduleDisplayFragment(){
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        aList = new ArrayList<ScheduleElement>();
        aa = new ScheduleDisplayAdapter(getContext(), R.layout.schedule_element, aList);


        try {
            Workbook wkb =  Workbook.getWorkbook(new File(Environment.getExternalStorageDirectory().getPath()+"/SHCEDULEDEM/Doodle.xls"));

            //Obtain the reference to the first sheet in the workbook
            Sheet sheet = wkb.getSheet(0);

            //Cell colArow1 = sheet.getCell(4, 4);
            //Cell colBrow1 = sheet.getCell(4, 2);
            //Cell colArow2 = sheet.getCell(4, 3);

            //String str_colArow1 = colArow1.getContents();
            //String str_colBrow1 = colBrow1.getContents();
            //String str_colArow2 = colArow2.getContents();
            //int x = sheet.getRows();

        //TextView ayy = (TextView) findViewById(R.id.textView);
        //ayy.setText(x+"");
        // getCell() uses col, row
        for(int i = 1;i < sheet.getColumns();++i){
            Cell dayCell = sheet.getCell(i, 4);
            String dayString = dayCell.getContents();
            if(!dayString.equals("")) {
                ScheduleElement se = new ScheduleElement();
                se.day = dayString;
                aList.add(se);
            }
        }

            //ayy.setText(aList.get(0).day);




        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        final ListView ScheduleList = (ListView) getActivity().findViewById(R.id.ScheduleDisplayList);
        if(ScheduleList != null) { // WHY IS SCHEDULELIST NULL HERE???????? IT SHOULDNT BE
            ScheduleList.setAdapter(aa);
        }
        aa.notifyDataSetChanged();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_schedule_display, container, false);
    }


}
