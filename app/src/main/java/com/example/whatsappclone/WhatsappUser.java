package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WhatsappUser extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private ListView listViewUsers;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<String> allUsers;
    private ArrayAdapter<String> mStringArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_user);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        listViewUsers = findViewById(R.id.listViewUser);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        gettingAllparseUser();
        listViewUsers.setOnItemClickListener(this);
    }

    private void gettingAllparseUser(){
        allUsers = new ArrayList<>();
        ParseQuery<ParseUser> queryAllUsers = ParseUser.getQuery();
        queryAllUsers.whereNotEqualTo("username",getIntent().getStringExtra("currentUser"));
        queryAllUsers.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size()>0 && e == null){
                    for (ParseUser user: objects){
                    allUsers.add(user.getUsername());
                    }
                }else{
                    Toast.makeText(WhatsappUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                mStringArrayAdapter = new ArrayAdapter<String>(getApplicationContext()
                        ,android.R.layout.simple_list_item_1,allUsers);
                listViewUsers.setAdapter(mStringArrayAdapter);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mntLogout:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(WhatsappUser.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        }else{
                            Toast.makeText(WhatsappUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

        ParseQuery<ParseUser>   query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.whereNotContainedIn("username",allUsers);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size()>0){
                    if (e == null){
                        for (ParseUser user : objects){
                            allUsers.add(user.getUsername());
                        }
                        mStringArrayAdapter.notifyDataSetChanged();
                        if (mSwipeRefreshLayout.isRefreshing()){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }else {
                    if (mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,WhatsappChat.class);
        intent.putExtra("user",allUsers.get(position));
        startActivity(intent);
    }
}
