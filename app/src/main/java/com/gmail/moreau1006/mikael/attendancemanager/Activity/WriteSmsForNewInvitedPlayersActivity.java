package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gmail.moreau1006.mikael.attendancemanager.DAO.MatchsDAO;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WriteSmsForNewInvitedPlayersActivity extends AppCompatActivity {

    private EditText smsEditText;

    private Match match;
    private List<Player> playersToInvite;
    private String sms;
    private MatchsDAO matchsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_write_sms_for_new_invited_players);
        setContentView(R.layout.activity_write_sms);

        // get match and players from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);
        playersToInvite = (ArrayList<Player>) getIntent().getSerializableExtra(AddInvitedPlayerActivity.EXTRA_INVITED_PLAYERS);

        smsEditText = (EditText) findViewById(R.id.sms_editText);

        DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm", Locale.FRENCH);
        String dateMatch = dateFormat.format(match.getDateMatch());
        String dateRdv = dateFormat.format(match.getDateRdv());
        String home;
        if(match.isHome()){
            home = "Domicile";
        }else{
            home = "Exterieur";
        }

        sms =   "Date Match : " + dateMatch + "\n"
                + "Date Rdv : " + dateRdv + "\n"
                + "Adversaire : " + match.getOpponent() + "\n"
                + "Lieu : " + home + "\n";

        smsEditText.setText(sms);

        matchsDAO = new MatchsDAO(this);
        matchsDAO.open();
    }

    public void validateSms(View view){

        for (int i=0; i < playersToInvite.size(); i++){
            try {
                matchsDAO.invitePlayerToMatch(playersToInvite.get(i), match);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        setResult(RESULT_OK);
        finish();
    }
}
