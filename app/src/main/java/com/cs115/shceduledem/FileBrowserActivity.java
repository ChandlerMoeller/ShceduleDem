package com.cs115.shceduledem;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileBrowserActivity extends AppCompatActivity {

    ListView filebrowserlistview;
    ArrayAdapter<String> adapter;
    String[] listitems;
    String xlsfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filebrowser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(xlsfile!=null) {
                    Intent intent = new Intent(FileBrowserActivity.this, MainActivity.class);
                    intent.putExtra("xlsfile", xlsfile);
                    startActivity(intent);
                }
            }
        });
        //Hide Fab until xls is found
        fab.hide();

        //Gets the SHCEDULEDEM File of creates it if it doesnt exist
        File dir2 = new File(Environment.getExternalStorageDirectory(), "SHCEDULEDEM");
        if (!dir2.exists()) {
            dir2.mkdirs();
        }
        //Refreshes dir
        MediaScannerConnection.scanFile(this, new String[] {dir2.toString()}, null, null);
        //Gets all the files in the dir
        File listfiles[] = dir2.listFiles();
        //Puts all the file names in dir into a string array
        if(listfiles != null) {
            listitems = new String[listfiles.length];
            for (int i = 0; i < listfiles.length; i++) {
                listitems[i] = listfiles[i].getName();
            }
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listitems);
        filebrowserlistview = (ListView) findViewById(R.id.filebrowserlistview);
        filebrowserlistview.setAdapter(adapter);


        filebrowserlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

                //Splits filenames to get extension
                String[] ext = ((String)parent.getItemAtPosition(position)).split("\\.");
                if(ext.length>1) {
                    Log.d("FileExtension", "" + ext[ext.length - 1]);
                    //If the filename is xls then fab is displayed
                    if(ext[ext.length - 1].equals("xls")) {
                        xlsfile = Environment.getExternalStorageDirectory().toString() + "/SHCEDULEDEM/" +((String)parent.getItemAtPosition(position));
                        Log.d("filexls", ""+xlsfile);
                        fab.show();
                    } else {
                        fab.hide();
                    }
                } else {
                    fab.hide();
                }

            }
        });

        /*File dir3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File listfiles2[] = dir3.listFiles();
        if(listfiles2 != null) {
            Log.d("status", "length: " + listfiles2.length);
            for (int i = 0; i < listfiles2.length; i++) {
                Log.d("status", "FileName:" + listfiles2[i].getName());
            }
        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(FileBrowserActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
