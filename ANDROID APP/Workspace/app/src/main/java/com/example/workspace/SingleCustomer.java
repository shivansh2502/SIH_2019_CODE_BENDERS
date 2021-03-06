package com.example.workspace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleCustomer extends AppCompatActivity {
    private String post_key=null;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView Title;
    private TextView Description;
    private TextView Printing;
    private TextView Frame;
    private TextView Pasting;
    private TextView Painting;
    private TextView Printingtime;
    private TextView Frametime;
    private TextView Pastingtime;
    private TextView Paintingtime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_customer);

        post_key=getIntent().getExtras().getString("PostId");

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Projects");

        Title=(TextView) findViewById(R.id.singleTitle1);
        Description=(TextView) findViewById(R.id.singleDescription1);
        Printing=(TextView) findViewById(R.id.singlePrinting1);
        Frame=(TextView) findViewById(R.id.singleFrame1);
        Pasting=(TextView) findViewById(R.id.singlePasting1);
        Painting=(TextView) findViewById(R.id.singlePainting1);
        Printingtime=(TextView) findViewById(R.id.singlePrintingtime1);
        Frametime=(TextView) findViewById(R.id.singleFrametime1);
        Pastingtime=(TextView) findViewById(R.id.singlePastingtime1);
        Paintingtime=(TextView) findViewById(R.id.singlePaintingtime1);



        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String post_Title=(String) dataSnapshot.child("title").getValue();
                String post_Desc=(String) dataSnapshot.child("description").getValue();
                String post_Printing=(String) dataSnapshot.child("printing").getValue();
                String post_frame=(String) dataSnapshot.child("frame").getValue();
                String post_Pasting=(String) dataSnapshot.child("pasting").getValue();
                String post_Painting=(String) dataSnapshot.child("painting").getValue();

                String post_Printing_time=(String) dataSnapshot.child("timeprinting").getValue();
                String post_frame_time=(String) dataSnapshot.child("timeframe").getValue();
                String post_Pasting_time=(String) dataSnapshot.child("timepasting").getValue();
                String post_Painting_time=(String) dataSnapshot.child("timepainting").getValue();

                Title.setText(post_Title);
                Description.setText(post_Desc);
                Printing.setText(post_Printing);
                Frame.setText(post_frame);
                Pasting.setText(post_Pasting);
                Painting.setText(post_Painting);
                Printingtime.setText(post_Printing_time);
                Frametime.setText(post_frame_time);
                Pastingtime.setText(post_Pasting_time);
                Paintingtime.setText(post_Painting_time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
