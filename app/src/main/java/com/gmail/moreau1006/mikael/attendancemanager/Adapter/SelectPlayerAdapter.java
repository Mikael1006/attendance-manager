package com.gmail.moreau1006.mikael.attendancemanager.Adapter;

import android.content.Context;
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
 * Created by mika on 04/10/17.
 */

public class SelectPlayerAdapter extends ArrayAdapter<Player> {

    public SelectPlayerAdapter(Context context, List<Player> players) {
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_player_listview,parent, false);
        }

        SelectPlayerAdapter.PlayerViewHolder viewHolder = (SelectPlayerAdapter.PlayerViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new SelectPlayerAdapter.PlayerViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.select_player_textview_name);
            viewHolder.numberPhone = (TextView) convertView.findViewById(R.id.select_player_textview_numberphone);
            viewHolder.player_checkbox = (CheckBox) convertView.findViewById(R.id.select_player_checkBox);
            convertView.setTag(viewHolder);
        }

        Player player = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText(player.getName());
        viewHolder.numberPhone.setText(player.getNumberPhone());

        return convertView;
    }

    private class PlayerViewHolder{
        public TextView name;
        public TextView numberPhone;
        public CheckBox player_checkbox;
    }
}
