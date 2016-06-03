package com.cs115.shceduledem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;

public class AdminActivity extends AppCompatActivity {

    String xlsfile = "";
    int newquota = -1;
    EditText Edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent current_intent = getIntent();
        xlsfile = current_intent.getStringExtra("xlsfile");

        Edittext = (EditText)findViewById(R.id.editText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Edittext.getText().toString() != null) {
                    newquota = Integer.parseInt(Edittext.getText().toString());

                    Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                    intent.putExtra("editalgorithm", true);
                    intent.putExtra("xlsfile", xlsfile);
                    intent.putExtra("scheduleview", true);
                    intent.putExtra("newquota", newquota);
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share_email) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("vnd.android.cursor.dir/email");
            //intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "[ScheduleDem] Here is the Schedule");
            intent.putExtra(Intent.EXTRA_TEXT, "Hi,\nHere is the schedule!");
            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + xlsfile));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Send Email..."));

            return true;
        }

        if (id == R.id.action_switchuser) {

            if (xlsfile != null) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor e = settings.edit();
                e.putBoolean("pollview", true);
                e.commit();

                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                intent.putExtra("xlsfile", xlsfile);
                startActivity(intent);
            }
            return true;
        }

        if (id == R.id.action_switchscheduleduser) {

            if (xlsfile != null) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor e = settings.edit();
                e.putBoolean("pollview", false);
                e.commit();

                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                intent.putExtra("xlsfile", xlsfile);
                intent.putExtra("scheduleview", true);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
