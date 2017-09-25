package com.gmail.moreau1006.mikael.attendancemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mika on 25/09/17.
 */

public class MatchAdapter extends ArrayAdapter<Team> {

    public MatchAdapter(Context context, List<Team> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_listview,parent, false);
        }

        TeamAdapter.TeamViewHolder viewHolder = (TeamAdapter.TeamViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TeamAdapter.TeamViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.team_textview_name);
            viewHolder.edit_btn = (Button) convertView.findViewById(R.id.team_button_edit);
            viewHolder.delete_btn = (Button) convertView.findViewById(R.id.team_button_delete);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Team team = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText(team.getName());

        return convertView;
    }

    private class TeamViewHolder{
        public TextView name;
        public Button edit_btn;
        public Button delete_btn;
    }

}
