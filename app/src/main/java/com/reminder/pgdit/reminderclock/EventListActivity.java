package com.reminder.pgdit.reminderclock;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class EventListActivity extends AppCompatActivity {

    private ListView eventlistView;
    private FloatingActionButton eventFbutton;
    private SqliteHelper sqliteHelper;
    private Cursor cursor;
    private EventListAdapterService foodAdapterService;
    ArrayList<ReminderEventsServices> eventList = new ArrayList<ReminderEventsServices>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        eventlistView = (ListView) findViewById(R.id.eventList);
        eventFbutton = (FloatingActionButton) findViewById(R.id.floatingAddEvent);

        sqliteHelper = new SqliteHelper(this);
        eventList = getEventList();
        foodAdapterService = new EventListAdapterService(EventListActivity.this, R.layout.event_list_content, eventList);
        eventlistView.setAdapter(foodAdapterService);


        eventlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View arg1,int pos, long id) {

                sqliteHelper.deleteEventItem(String.valueOf(id));

                eventList = getEventList();
                foodAdapterService = new EventListAdapterService(EventListActivity.this, R.layout.event_list_content, eventList);
                eventlistView.setAdapter(foodAdapterService);

                eventlistView.invalidateViews();

                return true;
            }
        });


        eventFbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = getIntent().getExtras().getString("userId");
                Intent callingIntent = new Intent(EventListActivity.this, EventsCreateActivity.class);
                callingIntent.putExtra("userId", userId);
                startActivity(callingIntent);
            }
        });
    }


    protected ArrayList<ReminderEventsServices> getEventList() {

        String userId = getIntent().getExtras().getString("userId");

        ArrayList<ReminderEventsServices> eventList = sqliteHelper.retreiveEventByUser(userId);

        return eventList;
    }



}
