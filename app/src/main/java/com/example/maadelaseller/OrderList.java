package com.example.maadelaseller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class OrderList extends ArrayAdapter<OrderClass> {


    private Activity context;
    private List<OrderClass> orderList;

    public OrderList(Activity context , List<OrderClass> orderList){
        super(context ,R.layout.order_list,orderList );
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View orderListview = inflater.inflate(R.layout.order_list,null,true);


        TextView fishtype = orderListview.findViewById(R.id.type);
        TextView custname = orderListview.findViewById(R.id.cname);
        TextView amount = orderListview.findViewById(R.id.amount);
        TextView date = orderListview.findViewById(R.id.date);


        TextView contactt = orderListview.findViewById(R.id.contactc);

        OrderClass orderClass = orderList.get(position);





        fishtype.setText("  Fish Type : "+orderClass.getType());
        custname.setText("  Customer Name : "+orderClass.getCustomerName());
        amount.setText("  Amount : "+new Double(orderClass.getAmount()).toString()+ "Kg");
        date.setText("  Date Wanted : "+orderClass.getDate());
        contactt.setText("  Customer Contact : "+orderClass.getCustomerContact());



        return orderListview;
    }


}

