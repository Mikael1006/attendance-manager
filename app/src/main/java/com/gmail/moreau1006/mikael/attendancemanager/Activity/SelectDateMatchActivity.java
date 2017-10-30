package com.gmail.moreau1006.mikael.attendancemanager.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectDateMatchActivity extends AppCompatActivity {

    private static final String FRAG_TAG_DATE_PICKER = "datePickerMatch";
    private static final String FRAG_TAG_TIME_PICKER = "timePickerMatch";
    private TextView dateTextView;
    private TextView timeTextView;
    private Match match;
    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_match);

        // initiate TextViews
        dateTextView = (TextView) findViewById(R.id.date_match_textView);
        timeTextView = (TextView) findViewById(R.id.time_match_textView);

        date = null;
        time = null;

        // get match from previous activity
        match = (Match) getIntent().getSerializableExtra(ListMatchsActivity.EXTRA_MATCH);
    }

    public void onClickDate(View view){

        // https://stackoverflow.com/questions/27362971/android-material-design-datepicker-with-appcompat

        // calender class's instance and get current dateTextView , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        //MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay();

        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {

                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                        // set day of month , month and year value in the edit text
                        dateTextView.setText(date);

                    }
                })
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(mYear, mMonth, mDay)
                .setDateRange(null, null)
                .setDoneText("Valider")
                .setCancelText("Annuler");
        cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);

    }

    public void onClickTime(View view){

        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment view, int selectedHour, int selectedMinute) {
                        String selectedMinuteString = String.format("%02d", selectedMinute);
                        time = selectedHour + ":" + selectedMinuteString;
                        // set time in text view
                        timeTextView.setText(time);
                    }

                })
                .setStartTime(20, 0)
                .setDoneText("Valider")
                .setCancelText("Annuler");
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    public void validateDateMatch(View view){

        if (date != null && time != null){
            Date dateMatch = null;
            try {
                dateMatch = new SimpleDateFormat("dd/MM/yyyy, HH:mm").parse(date + ", " + time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            match.setDateMatch(dateMatch);

            Intent intent = new Intent(this, SelectDateRdvActivity.class);
            intent.putExtra(ListMatchsActivity.EXTRA_MATCH,match);
            startActivityForResult(intent, ListMatchsActivity.CREATE_MATCH_RESQUEST_CODE);


        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(SelectDateMatchActivity.this).create();
            alertDialog.setTitle("Attention");
            alertDialog.setMessage("Veuillez selectionner une date et une heure de match");
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
        if(requestCode==ListMatchsActivity.CREATE_MATCH_RESQUEST_CODE){
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }
        super.onActivityResult (requestCode, resultCode, data);
    }
}
