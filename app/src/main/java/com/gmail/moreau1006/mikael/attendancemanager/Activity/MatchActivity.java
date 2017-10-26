package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Adapter.InvitedPlayersAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.util.List;

public class MatchActivity extends AppCompatActivity {

    private ListView invitedPlayersListView;
    private List<Player> invitedPlayers;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        invitedPlayersListView = (ListView) findViewById(R.id.invitedPlayersListView);

        // get match from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);

        invitedPlayers = match.getInvitedPlayers();

        InvitedPlayersAdapter adapter = new InvitedPlayersAdapter(MatchActivity.this, invitedPlayers);
        invitedPlayersListView.setAdapter(adapter);

//        DrawingSquare drawingSquare = new DrawingSquare(this);
//        setContentView(drawingSquare);
    }

    public void editAttendance(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MatchActivity.this);
        alertDialog.setTitle("Editer la présence");

        final RadioButton attendantRadioButton = new RadioButton(MatchActivity.this);
        final RadioButton noAttendantRadioButton = new RadioButton(MatchActivity.this);
        final RadioButton unknownRadioButton = new RadioButton(MatchActivity.this);

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        attendantRadioButton.setLayoutParams(lp);
//        noAttendantRadioButton.setLayoutParams(lp);
//        unknownRadioButton.setLayoutParams(lp);
//
//        alertDialog.setView(attendantRadioButton);

        Context context = view.getContext();
        LinearLayout verticalLayout = new LinearLayout(context);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout attendantHorizontalLayout = new LinearLayout(context);
        attendantHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        attendantHorizontalLayout.addView(attendantRadioButton);
        TextView attendantTextView = new TextView(MatchActivity.this);
        attendantTextView.setText("Présent");
        attendantHorizontalLayout.addView(attendantTextView);

        LinearLayout noAttendantHorizontalLayout = new LinearLayout(context);
        noAttendantHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        noAttendantHorizontalLayout.addView(noAttendantRadioButton);
        TextView noAttendantTextView = new TextView(MatchActivity.this);
        noAttendantTextView.setText("Absent");
        noAttendantHorizontalLayout.addView(noAttendantTextView);

        LinearLayout unknownHorizontalLayout = new LinearLayout(context);
        unknownHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        unknownHorizontalLayout.addView(unknownRadioButton);
        TextView unknownTextView = new TextView(MatchActivity.this);
        unknownTextView.setText("Indéterminé");
        unknownHorizontalLayout.addView(unknownTextView);

        verticalLayout.addView(attendantHorizontalLayout);
        verticalLayout.addView(noAttendantHorizontalLayout);
        verticalLayout.addView(unknownHorizontalLayout);

        alertDialog.setView(verticalLayout);

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
}
