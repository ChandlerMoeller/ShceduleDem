package com.cs115.shceduledem;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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

    public ScheduleDisplayFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aList = new ArrayList<ScheduleElement>();
        aa = new ScheduleDisplayAdapter(getContext(), R.layout.schedule_element, aList);

        String xlsfile = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            xlsfile = bundle.getString("xlsfile");
        }
        Log.d("Test1", "SDF XLS IS: " + xlsfile);

        try {
            //Workbook wkb = Workbook.getWorkbook(new File(Environment.getExternalStorageDirectory().getPath() + "/SHCEDULEDEM/Doodle.xls"));
            Workbook wkb = Workbook.getWorkbook(new File(Environment.getExternalStorageDirectory().getPath() + xlsfile));

            Sheet sheet = wkb.getSheet(0);


            if (sheet.getCell(0, 4).getContents().equals("")) {
                calendar(sheet);
            } else {
                freeText(sheet);
            }

            NetworkMaker myNetwork = new NetworkMaker(aList);

        /*for(int i = 1;i < sheet.getColumns();++i){
            Cell dayCell = sheet.getCell(i, 4);
            String dayString = dayCell.getContents();
            if(!dayString.equals("")) {
                ScheduleElement se = new ScheduleElement();
                se.day = dayString;
                aList.add(se);
            }
        }*/

        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_display, container, false);
    }

    public void onStart() {
        super.onStart();
        final ListView ScheduleList = (ListView) getActivity().findViewById(R.id.ScheduleDisplayList);
        ScheduleList.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    private void freeText(Sheet sheet) {
        for (int i = 1; i < sheet.getColumns(); ++i) { // iterate over times
            Cell timeCell = sheet.getCell(i, 3);
            String timeString = timeCell.getContents();
            if (!timeString.equals("")) {
                ScheduleElement se = new ScheduleElement();
                se.time = timeString;
                String peopleString = "";
                String countString = "0";
                String colon = "";
                int timeStringCol = 1;
                for (int col = 1; col < sheet.getColumns(); ++col) {
                    if (sheet.getCell(col, 3).getContents().equals(timeString)) {
                        timeStringCol = col;
                    }
                }
                for (int j = 4; j < sheet.getRows(); ++j) { // iterate over people
                    if (!sheet.getCell(0, j).getContents().equals("Count")) {
                        colon = ":";
                        peopleString += sheet.getCell(0, j).getContents();
                        peopleString += colon;
                        if (sheet.getCell(timeStringCol, j).getContents().equals("OK")) {
                            peopleString += "OK,";
                        } else {
                            peopleString += "NO,";
                        }
                    } else {
                        countString = sheet.getCell(timeStringCol, j).getContents();
                    }
                }
                se.people = peopleString;
                se.count = countString;
                aList.add(se);
            }
        }
    }

    private void calendar(Sheet sheet) {
        for (int i = 1; i < sheet.getColumns(); ++i) { // iterate over times
            Cell timeCell = sheet.getCell(i, 5);
            String timeString = timeCell.getContents();
            if (!timeString.equals("")) {
                ScheduleElement se = new ScheduleElement();
                se.time = timeString;
                String peopleString = "";
                String monthString = "";
                String dayString = "";
                String countString = "0";
                String colon = "";
                int timeStringCol = 1;
                for (int col = 1; col < sheet.getColumns(); ++col) {
                    if (sheet.getCell(col, 5).getContents().equals(timeString)) {
                        timeStringCol = col;
                        //while(sheet.getCell(col, 4).getContents().equals("")) {
                        //    --col;
                        //}
                        dayString = sheet.getCell(col, 4).getContents();
                       // col = timeStringCol;
                        //while(sheet.getCell(col, 3).getContents().equals("")) {
                        //    --col;
                        //}
                        monthString = sheet.getCell(col, 3).getContents();
                    }
                }
                for (int j = 6; j < sheet.getRows(); ++j) { // iterate over people
                    if (!sheet.getCell(0, j).getContents().equals("Count")) {
                        colon = ":";
                        peopleString += sheet.getCell(0, j).getContents();
                        peopleString += colon;
                        if (sheet.getCell(timeStringCol, j).getContents().equals("OK")) {
                            peopleString += "OK,";
                        } else {
                            peopleString += "NO,";
                        }
                    } else {
                        countString = sheet.getCell(timeStringCol, j).getContents();
                    }
                }
                se.people = peopleString;
                se.count = countString;
                se.day = dayString;
                se.month = monthString;
                aList.add(se);
            }
        }
    }


}
