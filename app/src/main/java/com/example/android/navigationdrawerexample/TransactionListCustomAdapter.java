package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by gabdulla on 4/22/2015.
 */
public class TransactionListCustomAdapter extends ArrayAdapter<transacitonListItem>{

    Context mContext;
    int layoutResourceId;
    transacitonListItem data[] = null;


    public TransactionListCustomAdapter(Context mContext, int layoutResourceId, transacitonListItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

      //  ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
      //  TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        TextView sender = (TextView)listItem.findViewById(R.id.textView18);
        TextView receiver = (TextView)listItem.findViewById(R.id.textView19);
        TextView amount = (TextView)listItem.findViewById(R.id.textView21);

        transacitonListItem folder = data[position];


       // imageViewIcon.setImageResource(folder.icon);
       // textViewName.setText(folder.name);

        sender.setText(folder.sender);
        receiver.setText(folder.receiver);
        amount.setText(folder.amount);
        return listItem;
    }
}
