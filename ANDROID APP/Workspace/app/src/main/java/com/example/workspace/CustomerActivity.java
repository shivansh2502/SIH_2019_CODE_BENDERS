package com.example.workspace;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerActivity extends AppCompatActivity {

    private RecyclerView mProjectView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProjectView = (RecyclerView) findViewById(R.id.viewprojectslist);
        mProjectView.setHasFixedSize(true);
        mProjectView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("CustomerProjects");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(CustomerActivity.this,SignupActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }

    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Projects, CustomerActivity.ProjectViewHolder> FBRA = new FirebaseRecyclerAdapter<Projects, CustomerActivity.ProjectViewHolder>(
                Projects.class,
                R.layout.viewprojects_row,
                CustomerActivity.ProjectViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(CustomerActivity.ProjectViewHolder viewHolder, Projects model, int position) {
                final String post_key = getRef(position).getKey().toString();

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDescription(model.getDescription());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent singleInstaActivity = new Intent(CustomerActivity.this, SingleCustomer.class);
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
        getMenuInflater().inflate(R.menu.menu_customer, menu);
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
            Intent intent= new Intent(CustomerActivity.this,SignupActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.PlaceOrder){
            Intent intent= new Intent(CustomerActivity.this,PlaceOrder.class);
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
