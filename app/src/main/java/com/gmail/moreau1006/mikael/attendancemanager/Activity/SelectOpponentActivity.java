package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.R;

public class SelectOpponentActivity extends AppCompatActivity {

    private Match match;
    private RadioButton homeTrueRadioButton;
    private RadioButton homeFalseRadioButton;
    private EditText opponentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_opponent);

        // get the match
        match = (Match) getIntent().getSerializableExtra(SelectDateMatchActivity.EXTRA_MATCH);

        homeTrueRadioButton = (RadioButton) findViewById(R.id.home_true_radioButton);
        homeFalseRadioButton = (RadioButton) findViewById(R.id.home_false_radioButton);
        opponentEditText = (EditText) findViewById(R.id.opponent_editText);

        homeTrueRadioButton.setChecked(true);
        homeFalseRadioButton.setChecked(false);
        match.setHome(true);


    }

    public void checkHomeTrue(View view){
        match.setHome(true);
        homeFalseRadioButton.setChecked(false);
    }

    public void checkHomeFalse(View view){
        match.setHome(false);
        homeTrueRadioButton.setChecked(false);
    }

    public void validateOpponent(View view){
        String opponent = opponentEditText.getText().toString();
        if(!opponent.isEmpty()){
            match.setOpponent(opponent);

            Intent intent = new Intent(this, SelectTeamActivity.class);
            intent.putExtra(SelectDateMatchActivity.EXTRA_MATCH,match);
            startActivityForResult(intent, SelectDateMatchActivity.RESQUEST_CODE);

        }else {
            AlertDialog alertDialog = new AlertDialog.Builder(SelectOpponentActivity.this).create();
            alertDialog.setTitle("Attention");
            alertDialog.setMessage("Veuillez entrer un adversaire");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode==SelectDateMatchActivity.RESQUEST_CODE){
            if(resultCode==RESULT_OK){
                setResult(SelectDateMatchActivity.RESQUEST_CODE);
                finish();
            }
        }
        super.onActivityResult (requestCode, resultCode, data);
    }
}
