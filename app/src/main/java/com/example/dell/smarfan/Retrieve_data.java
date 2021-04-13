package com.example.dell.smarfan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Retrieve_data extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView mtime;
    private TextView mtemp;
    private TextView t;
    private ToggleButton afanstate;
    SQLiteDatabase db;
    String time, temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        mtime = (TextView) findViewById(R.id.rtime);
        mtemp = (TextView) findViewById(R.id.rtemp);
        t = (TextView) findViewById(R.id.textView1);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db = openOrCreateDatabase("DB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS details(Time TEXT unique, Temp TEXT);");
        mDatabase.child("realtimedata").child("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                time = dataSnapshot.getValue().toString();
                mtime.setText("Time: "+time);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("realtimedata").child("temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                temp = dataSnapshot.getValue().toString();
                mtemp.setText("Temperature: "+temp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        try
        {
            db.execSQL("INSERT INTO details VALUES('"+time+"','"+temp+"');");
        }
        catch(Exception e){
        }
        afanstate = (ToggleButton) findViewById(R.id.genstate);

        final String[] state = new String[1];
        mDatabase.child("fanstate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                state[0] = dataSnapshot.getValue().toString();
                if(state[0].equals("on"))
                {
                    t.setText("on");
                    afanstate.setChecked(true);
                }
                else if(state[0].equals("off"))
                {
                    t.setText("off");
                    afanstate.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        final String[] stat = new String[1];
        mDatabase.child("manual_mode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stat[0] = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        afanstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b && (stat[0].equals("on")))
                {
                    //mDatabase.child("manual_mode").setValue("on");
                    mDatabase.child("fanstate").setValue("on");
                }
                else if(stat[0].equals("on"))
                {
                    //mDatabase.child("manual_mode").setValue("off");
                    mDatabase.child("fanstate").setValue("off");
                }
                else if(stat[0].equals("off"))
                {
                    mDatabase.child("fanstate").setValue(state[0]);
                }

            }
        });
    }
}