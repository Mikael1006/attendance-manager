package com.gmail.moreau1006.mikael.attendancemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by mika on 25/09/17.
 */

public class MatchAdapter extends ArrayAdapter<Match> {

    public MatchAdapter(Context context, List<Match> matchs) {
        super(context, 0, matchs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_listview,parent, false);
        }

        MatchAdapter.MatchViewHolder viewHolder = (MatchAdapter.MatchViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MatchAdapter.MatchViewHolder();
            viewHolder.match_textview_date = (TextView) convertView.findViewById(R.id.match_textview_date);
            viewHolder.match_textview_dateRdv = (TextView) convertView.findViewById(R.id.match_textview_dateRdv);
            viewHolder.match_textview_opponent = (TextView) convertView.findViewById(R.id.match_textview_opponent);
            viewHolder.match_textview_home = (TextView) convertView.findViewById(R.id.match_textview_home);
            viewHolder.match_textview_team = (TextView) convertView.findViewById(R.id.match_textview_team);
            viewHolder.match_button_edit = (Button) convertView.findViewById(R.id.match_button_edit);
            viewHolder.match_button_delete = (Button) convertView.findViewById(R.id.match_button_delete);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Match match = getItem(position);

        DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm", Locale.FRENCH);

        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String dateMatch = dateFormat.format(match.getDateMatch());
        String dateRdv = dateFormat.format(match.getDateRdv());

        //il ne reste plus qu'à remplir notre vue
        viewHolder.match_textview_date.setText(dateMatch);
        viewHolder.match_textview_dateRdv.setText(dateRdv);
        viewHolder.match_textview_opponent.setText(match.getOpponent());

        String home;
        if(match.isHome()){
            home = "Domicile";
        }else{
            home = "Exterieur";
        }

        viewHolder.match_textview_home.setText(home);
        viewHolder.match_textview_team.setText(match.getTeam().getName());

        return convertView;
    }

    private class MatchViewHolder{
        public TextView match_textview_date;
        public TextView match_textview_dateRdv;
        public TextView match_textview_opponent;
        public TextView match_textview_home;
        public TextView match_textview_team;
        public Button match_button_edit;
        public Button match_button_delete;
    }

}
