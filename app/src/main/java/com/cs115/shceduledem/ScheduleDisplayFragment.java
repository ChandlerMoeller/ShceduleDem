package com.cs115.shceduledem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.lang.Boolean;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;

public class ScheduleDisplayFragment extends Fragment {

    private ArrayList<ScheduleElement> aList;
    private ScheduleDisplayAdapter aa;
    String xlsfile = "";

    //This will be true if coming from Admin Options
    Boolean editalgorithm = false;
    int quota = -1;

    //This will be true if we are dealing with a modified XLS
    //This will be used as a check (i.e. if someone tries to switch to scheduleview with an UNMODIFIED schedule)
    Boolean ismodified = false;
    //This is will be true if the user wants to view the scheduleview, I will hand this in
    Boolean scheduledview = false;

    public static ScheduleDisplayFragment newInstance() {
        return new ScheduleDisplayFragment();
    }

    public ScheduleDisplayFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aList = new ArrayList<ScheduleElement>();
        //aa = new ScheduleDisplayAdapter(getContext(), R.layout.schedule_element, aList);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            xlsfile = bundle.getString("xlsfile");
            editalgorithm = bundle.getBoolean("editalgorithm", false);
            quota = bundle.getInt("newquota", -1);
            scheduledview = bundle.getBoolean("scheduleview", false);
        }
        Log.d("Test1", "SDF XLS IS: " + xlsfile);
        File xlsFilePath = new File((Environment.getExternalStorageDirectory().getPath() + xlsfile));

        try {
            Workbook wkb = Workbook.getWorkbook(xlsFilePath);

            Sheet sheet = wkb.getSheet(0);

            Log.d("ismodified????", sheet.getCell(0, 2).getContents());
            if(!sheet.getCell(0, 2).getContents().equals("")){
                ismodified = true;
            }

            if (sheet.getCell(0, 4).getContents().equals("")) {
                calendar(sheet);
            } else {
                freeText(sheet);
            }

            //If the XLS is modified AND we are scheduleview we will need to run the algorithm
            //If editaglorithm is true then we are coming from admin options
            Log.d("banana1: ", ""+ismodified);
            Log.d("banana2: ", ""+scheduledview);
            if((ismodified && scheduledview) || editalgorithm) {
                if(editalgorithm){
                    try {
                        // code adapted from http://stackoverflow.com/questions/11338383/writing-to-an-existing-excel-file
                        //Workbook oldXLS = Workbook.getWorkbook(xlsFilePath);
                        WritableWorkbook modXLS = Workbook.createWorkbook(xlsFilePath,wkb);
                        WritableSheet modSheet = modXLS.getSheet(0);
                        WritableCell modCell;
                        String newQuota = "" + quota;
                        Label mod = new Label(0,2,newQuota);
                        modCell = (WritableCell) mod;
                        modSheet.addCell(modCell);
                        modXLS.write();
                        modXLS.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }catch(WriteException e){
                        e.printStackTrace();
                    }
                }else if(ismodified) {
                    quota = Integer.parseInt(sheet.getCell(0, 2).getContents());
                }
                wkb.close();
                Log.d("ayyyyyy", ""+quota);
                NetworkMaker myNetwork = new NetworkMaker(aList, quota);
                Log.d("Network", myNetwork.toString());

                aList = myNetwork.getSolutionList();
            }



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

        aa = new ScheduleDisplayAdapter(getContext(), R.layout.schedule_element, aList);
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
                        while(sheet.getCell(col, 4).getContents().equals("")) {
                            --col;
                        }
                        dayString = sheet.getCell(col, 4).getContents();
                        col = timeStringCol;
                        while(sheet.getCell(col, 3).getContents().equals("")) {
                            --col;
                        }
                        monthString = sheet.getCell(col, 3).getContents();
                        col = timeStringCol;
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
