package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Adapter.MessageAdapter;
import com.gmail.moreau1006.mikael.attendancemanager.DAO.SmsDAO;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Sms;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private Match match;
    private Player player;
    private SmsDAO smsDAO;
    private List<Sms> smses;
    private ListView messageListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageListview = (ListView) findViewById(R.id.message_listview);

        // empty view
        messageListview.setEmptyView((TextView) findViewById(android.R.id.empty));

        // get match from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);
        player = (Player) getIntent().getSerializableExtra(MatchActivity.EXTRA_PLAYER);

        smsDAO = new SmsDAO(getContentResolver());
        smses = smsDAO.getAllSmsBySmsCodeAndNumber(match.getSmsCode(), player.getNumberPhone());
        MessageAdapter adapter = new MessageAdapter(MessageActivity.this, smses);
        messageListview.setAdapter(adapter);
    }

    public void onClickOk(View view){
        setResult(Activity.RESULT_OK);
        finish();
    }
}
