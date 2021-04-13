package com.example.dell.smarfan;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //SQLiteDatabase db;
    private TextView t;
    private DatabaseReference mDatabase;
    private ToggleButton statechange;
    private Button gendata, analyt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        statechange = (ToggleButton) findViewById(R.id.statechange);
        //t = (TextView) findViewById(R.id.textView);
        mDatabase.child("manual_mode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String state = dataSnapshot.getValue().toString();
                if(state.equals("on"))
                {
          //          t.setText("on");
                    statechange.setChecked(true);
                }
                else
                {
            //        t.setText("off");
                    statechange.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        statechange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mDatabase.child("manual_mode").setValue("on");
                    //mDatabase.child("fanstate").setValue("on");
                }
                else
                {
                    mDatabase.child("manual_mode").setValue("off");
                    //mDatabase.child("fanstate").setValue("off");
                }
            }
        });

        gendata = (Button) findViewById(R.id.genstate);
        gendata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a =  new Intent(MainActivity.this, Retrieve_data.class);
                startActivity(a);
            }
        });
        analyt = (Button) findViewById(R.id.analytics);
        analyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a =  new Intent(MainActivity.this, Analytics.class);
                startActivity(a);
            }
        });
    }
}
