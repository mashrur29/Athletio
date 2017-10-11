package com.blogspot.athletio;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tanvir on 10/12/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    List<Event> list;

    EventAdapter(List<Event> list){
        this.list=list;
    }


    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.event_card, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event=list.get(position);
        holder.title.setText(" "+event.title);
        holder.timeDate.setText(event.day.day+"/"+event.day.month+"/"+event.day.year+"  "+String.format("%02d", event.hour)+" : "+String.format("%02d", event.min));
        holder.description.setText(event.description);
        holder.hostName.setText(event.creatorName);
        if(event.type==0)
            holder.type.setText("Running");
        else if(event.type==1)
            holder.type.setText("Cycling");
        else if(event.type==2)
            holder.type.setText("Football");
        else if(event.type==3)
            holder.type.setText("Cricket");
        else if(event.type==4)
            holder.type.setText("Walking");
        else if(event.type==5)
            holder.type.setText("Other");
        holder.duration.setText(event.durationInSec/60+" min "+event.durationInSec%60+"sec");
        if(event.getStatus()==0){
            holder.status.setText("Active");
        }
        else if(event.getStatus()==1){
            holder.status.setText("Running");
        }
        else if(event.getStatus()==2){
            holder.status.setText("Cancelled");
        }
        else if(event.getStatus()==3){
            holder.status.setText("Finished");
        }
        if(event.type==0||event.type==1||event.type==4){
            holder.distance.setText(event.distanceInMeters+" m");
        }
        else {
            holder.distance.setText("Not Available");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView timeDate;
        protected TextView description;
        protected TextView hostName;
        protected TextView type;
        protected TextView distance;
        protected TextView duration;
        protected TextView status;
        protected TextView start;
        protected TextView stop;

        public EventViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.eventcardTitle);
            timeDate = (TextView)  v.findViewById(R.id.eventcardTimeDate);
            description = (TextView)  v.findViewById(R.id.eventcardDescription);
            hostName = (TextView) v.findViewById(R.id.eventcardHostName);
            type = (TextView) v.findViewById(R.id.eventcardType);
            distance= (TextView) v.findViewById(R.id.eventcardDistance);
            duration= (TextView) v.findViewById(R.id.eventcardDuration);
            status= (TextView) v.findViewById(R.id.eventcardStatus);
            start= (TextView) v.findViewById(R.id.eventcardstartloction);
            stop= (TextView) v.findViewById(R.id.eventcardEndloction);
        }
    }
}
