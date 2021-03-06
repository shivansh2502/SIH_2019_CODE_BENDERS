package com.example.workspace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PastingActivity extends AppCompatActivity {
    private RecyclerView mProjectView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasting);

        mProjectView = (RecyclerView) findViewById(R.id.viewprojectslist);
        mProjectView.setHasFixedSize(true);
        mProjectView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(PastingActivity.this,LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }

    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Projects, ProjectViewHolder> FBRA = new FirebaseRecyclerAdapter<Projects, PastingActivity.ProjectViewHolder>(
                Projects.class,
                R.layout.viewprojects_row,
                PastingActivity.ProjectViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(PastingActivity.ProjectViewHolder viewHolder, Projects model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleInstaActivity = new Intent(PastingActivity.this, SingleProjectPasting.class);
                        singleInstaActivity.putExtra("PostId", post_key);
                        startActivity(singleInstaActivity);

                    }
                });
            }

        };
        mProjectView.setAdapter(FBRA);
    }

    public static class ProjectViewHolder extends  RecyclerView.ViewHolder{
        View mView;
        public  ProjectViewHolder(View itemView){
            super(itemView);
            mView= itemView;
        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }
        public  void setDescription(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.textDescription);
            post_desc.setText(desc);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.Logout){
            mAuth.signOut();
            Intent intent= new Intent(PastingActivity.this,SignupActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
