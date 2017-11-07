package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    public static final int MY_PERMISSION_REQUEST_SEND_SMS = 1;
    private EditText smsEditText;
    private Match match;
    private List<Player> playersToInvite;
    private String sms;
    private MatchsDAO matchsDAO;
    private Button validateButton;
    private ProgressBar progressBar;

    private String SENT = "SMS_SENT";
    private PendingIntent sentPI;
    private BroadcastReceiver smsSentReceiver;
    private int nbSentSmsResponseOK;
    private int nbSentSmsResponseError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sms);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        nbSentSmsResponseOK = 0;
        nbSentSmsResponseError = 0;


        // get match and players from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);
        playersToInvite = (ArrayList<Player>) getIntent().getSerializableExtra(AddInvitedPlayerActivity.EXTRA_INVITED_PLAYERS);

        smsEditText = (EditText) findViewById(R.id.sms_editText);
        validateButton = (Button) findViewById(R.id.validate_sms_button);
        progressBar = (ProgressBar) findViewById(R.id.write_sms_progressbar);

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

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        nbSentSmsResponseOK++;
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        nbSentSmsResponseError++;
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        nbSentSmsResponseError++;
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        nbSentSmsResponseError++;
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        nbSentSmsResponseError++;
                        break;
                }

                // If all the response were returned
                if(nbSentSmsResponseOK + nbSentSmsResponseError == playersToInvite.size()){

                    if(nbSentSmsResponseError == 0){
                        Toast.makeText(WriteSmsForNewInvitedPlayersActivity.this, nbSentSmsResponseOK + " sms envoyés", Toast.LENGTH_SHORT).show();

                        WriteSmsForNewInvitedPlayersActivity.this.setResult(RESULT_OK);
                        finish();
                    }else{
                        // Re-try ?
                        AlertDialog alertDialog = new AlertDialog.Builder(WriteSmsForNewInvitedPlayersActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Erreur réseau : " + nbSentSmsResponseError
                                + " sms n'ont pas été envoyés");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Réessayer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        nbSentSmsResponseError = 0;
                                        nbSentSmsResponseOK = 0;
                                        sendSMS(match);
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Abandonner",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        WriteSmsForNewInvitedPlayersActivity.this.setResult(RESULT_OK);
                                        WriteSmsForNewInvitedPlayersActivity.this.finish();
                                    }
                                });
                        // on back button
                        alertDialog.setOnKeyListener(new AlertDialog.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode,
                                                 KeyEvent event) {

                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    dialog.dismiss();
                                    WriteSmsForNewInvitedPlayersActivity.this.setResult(RESULT_OK);
                                    WriteSmsForNewInvitedPlayersActivity.this.finish();
                                }
                                return true;
                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
    }

    public void validateSms(View view){

        for (int i=0; i < playersToInvite.size(); i++){
            try {
                matchsDAO.invitePlayerToMatch(playersToInvite.get(i), match);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        try{
            sms = "idMatch=" + match.getId() + "\n" + sms;

            progressBar.setVisibility(View.VISIBLE);
            validateButton.setVisibility(View.GONE);

            sendSMS(match);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendSMS(Match match){

        for (int i = 0; i < playersToInvite.size(); i++){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS);
            } else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(playersToInvite.get(i).getNumberPhone(), null, sms, sentPI, null);
            }
        }
    }
}
