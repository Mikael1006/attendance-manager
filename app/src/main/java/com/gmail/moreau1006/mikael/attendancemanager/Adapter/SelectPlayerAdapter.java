package com.gmail.moreau1006.mikael.attendancemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.util.List;

/**
 * Created by mika on 04/10/17.
 */

public class SelectPlayerAdapter extends ArrayAdapter<Player> {

    private boolean checked[];

    public SelectPlayerAdapter(Context context, List<Player> players) {
        super(context, 0, players);
        checked = new boolean[players.size()];
        for (int i = 0; i < checked.length; i++){
            checked[i] = false;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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

        // https://stackoverflow.com/questions/5444355/android-listview-with-checkbox-problem
        viewHolder.player_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked[position] = isChecked;
            }
        });
        viewHolder.player_checkbox.setChecked(checked[position]);

        return convertView;
    }

    private class PlayerViewHolder{
        public TextView name;
        public TextView numberPhone;
        public CheckBox player_checkbox;
    }
}
