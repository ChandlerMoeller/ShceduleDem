package com.cs115.shceduledem;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.*;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

   // private ArrayList<ScheduleElement> aList;
    //private ScheduleDisplayAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //aList = new ArrayList<ScheduleElement>();
        //aa = new ScheduleDisplayAdapter(this, R.layout.schedule_element, aList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





//----------------------------------------------------------------------------------------------

        /*try {
            Workbook wkb =  Workbook.getWorkbook(new File(Environment.getExternalStorageDirectory().getPath()+"/SHCEDULEDEM/Doodle.xls"));

            //Obtain the reference to the first sheet in the workbook
            Sheet sheet = wkb.getSheet(0);

            Cell colArow1 = sheet.getCell(4, 4);
            Cell colBrow1 = sheet.getCell(4, 2);
            Cell colArow2 = sheet.getCell(4, 3);

            String str_colArow1 = colArow1.getContents();
            String str_colBrow1 = colBrow1.getContents();
            String str_colArow2 = colArow2.getContents();
            int x = sheet.getRows();

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
        }*/
//----------------------------------------------------------------------------------------------









        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ScheduleDisplayFragment sdf = new ScheduleDisplayFragment();
            ft.add(R.id.fragment_container, sdf);
            ft.commit();
        }


        //final ListView ScheduleList = (ListView) findViewById(R.id.ScheduleDisplayList);
        //ScheduleList.setAdapter(aa);
        //aa.notifyDataSetChanged();
    }

}
