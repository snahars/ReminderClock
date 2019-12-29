package com.reminder.pgdit.reminderclock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapterService extends ArrayAdapter<ReminderEventsServices> {

    Context context;
    ArrayList<ReminderEventsServices> eventList = new ArrayList<ReminderEventsServices>();


    public EventListAdapterService(@NonNull Context context, int resource, ArrayList<ReminderEventsServices> eventList) {
        super(context, R.layout.event_list_content, eventList);

        this.context = context;
        this.eventList = eventList;

    }


    @Override
    public int getCount() {
        return super.getCount();
    }


    @Nullable
    @Override
    public ReminderEventsServices getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        EventViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new EventViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_list_content, parent, false);

            viewHolder.eventTitle = convertView.findViewById(R.id.eventTitle);
            viewHolder.eventDescription = convertView.findViewById(R.id.eventDescription);
            viewHolder.eventDateTime = convertView.findViewById(R.id.eventDateTime);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (EventViewHolder) convertView.getTag();


        ReminderEventsServices eventVal = eventList.get(position);


        viewHolder.eventTitle.setText(eventVal.event_title);
        viewHolder.eventDescription.setText(eventVal.event_description);
        viewHolder.eventDateTime.setText(eventVal.event_datetime);


        return convertView;

    }

    private static class EventViewHolder {
        TextView eventTitle;
        TextView eventDescription;
        TextView eventDateTime;
    }

}
