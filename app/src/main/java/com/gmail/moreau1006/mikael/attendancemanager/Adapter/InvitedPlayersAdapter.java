package com.gmail.moreau1006.mikael.attendancemanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.util.List;

/**
 * Created by mika on 25/10/17.
 */

public class InvitedPlayersAdapter extends ArrayAdapter<Player> {

    public InvitedPlayersAdapter(Context context, List<Player> players) {
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.invited_player_listview,parent, false);
        }

        InvitedPlayerViewHolder viewHolder = (InvitedPlayerViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new InvitedPlayerViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.invited_player_textview_name);
            viewHolder.numberPhone = (TextView) convertView.findViewById(R.id.invited_player_textview_numberphone);
            viewHolder.attendance = (View) convertView.findViewById(R.id.invited_player_square);
            convertView.setTag(viewHolder);
        }

        Player player = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText(player.getName());
        viewHolder.numberPhone.setText(player.getNumberPhone());

        GradientDrawable bgShape = (GradientDrawable)viewHolder.attendance.getBackground();

        if(player.isAttendant() == null){
            bgShape.setColor(Color.GRAY);
        }else if(player.isAttendant()){
            bgShape.setColor(Color.GREEN);
        }else if(!player.isAttendant()){
            bgShape.setColor(Color.RED);
        }

        return convertView;
    }

    private class InvitedPlayerViewHolder {
        public TextView name;
        public TextView numberPhone;
        public View attendance;
    }
}

