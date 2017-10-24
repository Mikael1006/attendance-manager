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
import java.util.List;
import java.util.Locale;

public class WriteSmsActivity extends AppCompatActivity {

    private Match match;
    private List<Player> invitedPlayers;
    private String sms;
    private EditText smsEditText;
    private MatchsDAO matchsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sms);

        smsEditText = (EditText) findViewById(R.id.sms_editText);

        // get match and players from previous activity
        match = (Match) getIntent().getSerializableExtra(SelectDateMatchActivity.EXTRA_MATCH);
        invitedPlayers = match.getInvitedPlayers();

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
        match = matchsDAO.createMatch(match);
        matchsDAO.close();
        setResult(RESULT_OK);
        finish();
    }
}
