package com.first.kritikm.split;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kritik on 16-Nov-16.
 */

public class EaterAdapter extends RecyclerView.Adapter<EaterAdapter.ViewHolder>
{
    private EaterEntity eatersList[];
    int nRows;
    String currency;

    public EaterAdapter(Context context, int eventId, String currency)
    {
        this.currency = currency;
        UserDataContract.EaterEntry eaterDB = new UserDataContract.EaterEntry(context);
        Cursor rows = eaterDB.getRows(eventId);
        nRows = rows.getCount();

        if(nRows == 0)
            eatersList = new EaterEntity[1];
        else
            eatersList = new EaterEntity[nRows];

        for(int i = 0; rows.moveToNext(); i++)
        {
            eatersList[i] = new EaterEntity(eventId,
                            rows.getString(rows.getColumnIndex("eater")),
                            rows.getDouble(rows.getColumnIndex("eater_ate")),
                            rows.getDouble(rows.getColumnIndex("eater_paid")));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView eaterName, eaterAte, eaterPaid, eaterOwes;

        public ViewHolder(View view)
        {
            super(view);
            eaterName = (TextView)view.findViewById(R.id.eater_name);
            eaterAte = (TextView)view.findViewById(R.id.eater_ate);
            eaterPaid = (TextView)view.findViewById(R.id.eater_paid);
            eaterOwes = (TextView)view.findViewById(R.id.eater_owes);
        }
    }

    @Override
    public EaterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eater_card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if(nRows != 0)
        {
            holder.eaterName.setText(eatersList[position].eaterName);
            holder.eaterAte.append(currency + String.valueOf(eatersList[position].eaterAte));
            holder.eaterPaid.append(currency + String.valueOf(eatersList[position].eaterPaid));
            holder.eaterOwes.append(currency + String.valueOf(eatersList[position].eaterAte - eatersList[position].eaterPaid));
        }
    }

    @Override
    public int getItemCount()
    {
        return eatersList.length;
    }
}