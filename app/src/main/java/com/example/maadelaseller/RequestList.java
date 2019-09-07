package com.example.maadelaseller;


import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RequestList extends ArrayAdapter<Requests> {

    private Activity context;
    private List<Requests> requestsList;

    public RequestList(Activity context, List<Requests> requestsList){
        super(context, R.layout.requestlist_layout, requestsList);
        this.context=context;
        this.requestsList=requestsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listviweItem = inflater.inflate( R.layout.requestlist_layout,null,true );

        TextView cusn = (TextView)listviweItem.findViewById( R.id.cname );
       TextView fname= (TextView)listviweItem.findViewById( R.id.fname );
       TextView amount= (TextView)listviweItem.findViewById( R.id.amount );
       TextView rtimed = (TextView)listviweItem.findViewById( R.id.rtime);
        TextView atimed = (TextView)listviweItem.findViewById( R.id.at);

        Requests requests = requestsList.get( position );

       if(requests.getStatus().equals( "Pending" ))
           listviweItem.setBackgroundColor( Color.BLUE );
       if(requests.getStatus().equals( "Confirmed" ))
           listviweItem.setBackgroundColor( Color.GREEN );
        fname.setText( requests.getFishname() );
        amount.setText( requests.getAmount() );
        cusn.setText( requests.getCusname() );
        rtimed.setText( requests.getTime() );
        atimed.setText( requests.getAcctime() );
        return listviweItem;
    }
}
