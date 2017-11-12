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

public class WriteSmsActivity extends AppCompatActivity {

    public static final int MY_PERMISSION_REQUEST_SEND_SMS = 1;
    private Match match;
    private List<Player> invitedPlayers;
    private String sms;
    private EditText smsEditText;
    private MatchsDAO matchsDAO;
    private Button validateButton;
    private ProgressBar progressBar;
    private int progressStatus;
    private int step;

    private String SENT = "SMS_SENT";
    private PendingIntent sentPI;
    private BroadcastReceiver smsSentReceiver;
    private int mMessageSentTotalParts;
    private int mMessageSentCount;
    private int mMessageSentParts;
    private ArrayList<String> multipartSmsText;
    private ArrayList<PendingIntent> sentPiList;
    private SmsManager smsManager;

    private ArrayList<String> numberPhoneToSendList;
    private int numberPhoneToSendListCount;
    private boolean error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sms);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        smsEditText = (EditText) findViewById(R.id.sms_editText);
        validateButton = (Button) findViewById(R.id.validate_sms_button);
        progressBar = (ProgressBar) findViewById(R.id.write_sms_progressbar);

        // get match and players from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);
        invitedPlayers = match.getInvitedPlayers();

        sms = match.getDefaultSms();

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
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        error = true;
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        error = true;
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        error = true;
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        error = true;
                        break;
                }

                // update progressBar
                progressStatus += step;
                progressBar.setProgress(progressStatus);

                mMessageSentParts++;
                if ( mMessageSentParts == mMessageSentTotalParts ) {
                    if(error){
                        numberPhoneToSendListCount++;
                    }else {
                        numberPhoneToSendList.remove(numberPhoneToSendListCount);
                        mMessageSentCount++;
                    }
                    error = false;
                    sendNextMessage();
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
    }

    public void validateSms(View view){
        try{
            match = matchsDAO.createMatch(match);
            matchsDAO.close();

            sms = match.prepareSms(sms);

            progressBar.setVisibility(View.VISIBLE);
            validateButton.setVisibility(View.GONE);

            initializeListNumberPhone();
            startSendMessages();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startSendMessages(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSION_REQUEST_SEND_SMS);
        } else {
            mMessageSentCount = 0;
            numberPhoneToSendListCount = 0;
            error=false;
            if(numberPhoneToSendList.size()>0){
                smsManager = SmsManager.getDefault();
                multipartSmsText = smsManager.divideMessage(sms);
                mMessageSentTotalParts = multipartSmsText.size();
                sentPiList = new ArrayList<PendingIntent>(mMessageSentTotalParts);
                for (int j = 0; j< mMessageSentTotalParts; j++) {
                    sentPiList.add(sentPI);
                }
                progressStatus = 0;
                step=(progressBar.getMax()/numberPhoneToSendList.size())/mMessageSentTotalParts;
                progressBar.setProgress(progressStatus);
                sendSMS();
            }
        }
    }

    private void sendSMS() {
        mMessageSentParts = 0;
        smsManager.sendMultipartTextMessage(numberPhoneToSendList.get(numberPhoneToSendListCount), null, multipartSmsText, sentPiList, null);
    }

    private void sendNextMessage(){
        if(thereAreSmsToSend()){
            sendSMS();
        }else{
            if(!numberPhoneToSendList.isEmpty()){
                showAlertDialog();
            }else{
                Toast.makeText(WriteSmsActivity.this, mMessageSentCount + " sms envoyés", Toast.LENGTH_LONG).show();
                WriteSmsActivity.this.setResult(RESULT_OK);
                finish();
            }
        }
    }

    private boolean thereAreSmsToSend(){
        return numberPhoneToSendListCount + mMessageSentCount < invitedPlayers.size();
    }

    private void showAlertDialog(){
//         Re-try ?
        AlertDialog alertDialog = new AlertDialog.Builder(WriteSmsActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Erreur réseau : " + numberPhoneToSendList.size()
                + " sms n'ont pas été envoyés");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Réessayer",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startSendMessages();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Abandonner",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        WriteSmsActivity.this.setResult(RESULT_OK);
                        WriteSmsActivity.this.finish();
                    }
                });
        // on back button
        alertDialog.setOnKeyListener(new AlertDialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    WriteSmsActivity.this.setResult(RESULT_OK);
                    WriteSmsActivity.this.finish();
                }
                return true;
            }
        });
        alertDialog.show();
    }

    private void initializeListNumberPhone(){
        numberPhoneToSendList = new ArrayList<>(invitedPlayers.size());
        for (int i = 0; i < invitedPlayers.size(); i++){
            numberPhoneToSendList.add(invitedPlayers.get(i).getNumberPhone());
        }
    }
}
