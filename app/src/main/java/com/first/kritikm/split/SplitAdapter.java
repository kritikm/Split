package com.first.kritikm.split;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kritik on 14-Nov-16.
 */

public class SplitAdapter extends RecyclerView.Adapter<SplitAdapter.ViewHolder>
{
    private SplitEntity[] splitsList;
    Context context;
    int nRows;

    public SplitAdapter(Context context, String username)
    {
        this.context = context;
        UserDataContract.UserEntry userDB = new UserDataContract.UserEntry(context);
        int userId = userDB.getUserId(username);
        UserDataContract.SplitEntry splitDB = new UserDataContract.SplitEntry(context);
        Cursor splits = splitDB.getRows(userId);
        nRows = splits.getCount();

        if(nRows == 0)
            splitsList = new SplitEntity[1];
        else
            splitsList = new SplitEntity[nRows];

        for(int i = 0; splits.moveToNext(); i++)
        {
            splitsList[i] = new SplitEntity(splits.getInt(0),
                            splits.getString(2),
                            splits.getString(3),
                            splits.getString(4),
                            splits.getString(5),
                            splits.getDouble(6));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView eventNameText, eventDateText, eventLocationText, totalAmountText;
        SplitEntity[] splitsList;
        Context ctx;
        public ViewHolder(View view, Context ctx, SplitEntity[] splits)
        {
            super(view);
            this.ctx = ctx;
            this.splitsList = splits;
            view.setOnClickListener(this);
            eventNameText = (TextView)view.findViewById(R.id.event_name_text);
            eventDateText = (TextView)view.findViewById(R.id.event_date_text);
            eventLocationText = (TextView)view.findViewById(R.id.event_location_text);
            totalAmountText = (TextView)view.findViewById(R.id.total_amount_text);
        }

        @Override
        public void onClick(View v) {
            SplitEntity clickedSplit = splitsList[getAdapterPosition()];
            Intent intent = new Intent(this.ctx, SplitDetailsActivity.class);
            intent.putExtra("EVENT_ID", clickedSplit.eventId);
            intent.putExtra("EVENT_NAME", clickedSplit.eventName);
            intent.putExtra("EVENT_LOCATION", clickedSplit.eventLocation);
            intent.putExtra("EVENT_DATE", clickedSplit.eventDate);
            intent.putExtra("CURRENCY", clickedSplit.currency);
            intent.putExtra("TOTAL_AMOUNT", clickedSplit.totalAmount);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.ctx.startActivity(intent);
        }
    }

    @Override
    public SplitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.split_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, context, splitsList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if(nRows != 0) {
            holder.eventNameText.setText(splitsList[position].eventName);
            holder.eventDateText.setText(splitsList[position].eventDate);
            holder.eventLocationText.setText(splitsList[position].eventLocation);
            holder.totalAmountText.setText(splitsList[position].currency);
            holder.totalAmountText.append(String.valueOf(splitsList[position].totalAmount));
        }
    }

    @Override
    public int getItemCount()
    {
        return splitsList.length;
    }


}
