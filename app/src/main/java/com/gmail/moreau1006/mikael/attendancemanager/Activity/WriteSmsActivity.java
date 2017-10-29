package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.moreau1006.mikael.attendancemanager.DAO.MatchsDAO;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WriteSmsActivity extends AppCompatActivity {

    public static final int MY_PERMISSION_REQUEST_SEND_SMS = 1;
    private Match match;
    private List<Player> invitedPlayers;
    private String sms;
    private EditText smsEditText;
    private MatchsDAO matchsDAO;

    private String SENT = "SMS_SENT";
    private String DELIVERED= "SMS_DELIVERED";
    private PendingIntent sentPI, deliveredPI;
    private BroadcastReceiver smsSentReceiver, smsDeliveredReceveir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sms);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        smsEditText = (EditText) findViewById(R.id.sms_editText);

        // get match and players from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);
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

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsDeliveredReceveir);
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
                        Toast.makeText(WriteSmsActivity.this, "Sms sent !", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(WriteSmsActivity.this, "Generic Failure!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(WriteSmsActivity.this, "No service !", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(WriteSmsActivity.this, "Pull PDU !", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(WriteSmsActivity.this, "Radio off !", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        smsDeliveredReceveir = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(WriteSmsActivity.this, "Sms delivered !", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(WriteSmsActivity.this, "Sms not delivered !", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceveir, new IntentFilter(DELIVERED));
    }

    public void validateSms(View view){
        try {
            match = matchsDAO.createMatch(match);
            matchsDAO.close();

            // Send sms
            //TODO

            sms = "idMatch=" + match.getId() + "\n" + sms;

            for (int i = 0; i <= invitedPlayers.size(); i++){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSION_REQUEST_SEND_SMS);
                } else {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(invitedPlayers.get(i).getNumberPhone(), null, sms, sentPI, deliveredPI);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setResult(RESULT_OK);
        finish();
    }
}
