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
 * Created by mika on 04/10/17.
 */

public class SelectTeamAdapter extends ArrayAdapter<Team> {

    public SelectTeamAdapter(Context context, List<Team> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_team_listview,parent, false);
        }

        SelectTeamAdapter.TeamViewHolder viewHolder = (SelectTeamAdapter.TeamViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new SelectTeamAdapter.TeamViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.select_team_textview_name);
            viewHolder.select_btn = (Button) convertView.findViewById(R.id.select_team_button_select);
            convertView.setTag(viewHolder);
        }

        Team team = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText(team.getName());

        return convertView;
    }

    private class TeamViewHolder{
        public TextView name;
        public Button select_btn;
    }
}
