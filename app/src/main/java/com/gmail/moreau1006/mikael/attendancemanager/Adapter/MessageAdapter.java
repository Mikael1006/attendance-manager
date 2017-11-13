package com.gmail.moreau1006.mikael.attendancemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Sms;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by mika on 13/11/17.
 */

public class MessageAdapter extends ArrayAdapter<Sms> {

    public MessageAdapter(Context context, List<Sms> smses) {
        super(context, 0, smses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_listview,parent, false);
        }

        MessageAdapter.SmsViewHolder viewHolder = (MessageAdapter.SmsViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MessageAdapter.SmsViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.date_message_textview);
            viewHolder.body = (TextView) convertView.findViewById(R.id.body_message_textview);
            convertView.setTag(viewHolder);
        }

        Sms sms = getItem(position);

        DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yyyy à HH:mm", Locale.FRENCH);

        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String dateSms = dateFormat.format(sms.getDate());

        //il ne reste plus qu'à remplir notre vue
        viewHolder.date.setText(dateSms);
        viewHolder.body.setText(sms.getBody());

        return convertView;
    }

    private class SmsViewHolder{
        public TextView date;
        public TextView body;
    }
}
