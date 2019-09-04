package com.example.maadelaseller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FishList extends ArrayAdapter<DailySelling> {
    private Activity context;
    private List<DailySelling> fishList;

    public FishList(Activity context, List<DailySelling>fishList){
        super(context, R.layout.fist_list_layout, fishList);
        this.context = context;
        this.fishList=fishList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate( R.layout.fist_list_layout,null,true );
        TextView name = (TextView) listViewItem.findViewById( R.id.fname );
        TextView rate = (TextView) listViewItem.findViewById( R.id.rate );

        DailySelling dailySelling = fishList.get( position );
        name.setText( dailySelling.getFishname() );
       rate.setText( "Rs "+new Double(dailySelling.getRate()).toString()+"/kg" );

        return listViewItem;
    }
}
