package com.gmail.moreau1006.mikael.attendancemanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.util.List;

/**
 * Created by mika on 16/09/17.
 */

public class PlayerAdapter extends ArrayAdapter<Player> {

    public PlayerAdapter(Context context, List<Player> players) {
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.player_listview,parent, false);
        }

        PlayerViewHolder viewHolder = (PlayerViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new PlayerViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.player_textview_name);
            viewHolder.numberPhone = (TextView) convertView.findViewById(R.id.player_textview_numberphone);
            viewHolder.delete_btn = (ImageButton) convertView.findViewById(R.id.player_button_delete);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Player player = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText(player.getName());
        viewHolder.numberPhone.setText(player.getNumberPhone());

        return convertView;
    }

    private class PlayerViewHolder{
        public TextView name;
        public TextView numberPhone;
        public ImageButton delete_btn;
    }
}
