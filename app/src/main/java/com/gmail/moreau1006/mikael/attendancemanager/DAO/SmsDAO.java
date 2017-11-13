package com.gmail.moreau1006.mikael.attendancemanager.DAO;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mika on 07/11/17.
 */

public class SmsDAO {

    private String[] allColumns = new String[] {"address", "body", "date"};
    private final String SMS_URI_ALL = "content://sms/";
    private final String SMS_URI_INBOX = "content://sms/inbox";
    private final String SMS_URI_SENT = "content://sms/sent";

    private ContentResolver contentResolver;

    public SmsDAO(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }

    public List<Sms> getAllSmsBySmsCodeAndNumber(String smsCode, String number){
        List<Sms> smss = new ArrayList<Sms>();

        Uri uri = Uri.parse(SMS_URI_INBOX);
        Cursor cursor = contentResolver.query(uri, allColumns ,
                "body LIKE '%"+smsCode+"%' AND address = ?",
                new String[] {number}, "date desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sms sms = cursorToSms(cursor);
            smss.add(sms);
            cursor.moveToNext();
        }

        cursor.close();

        return smss;
    }

    public Sms getLastSmsBySmsCodeAndNumber(String smsCode, String number){
        Uri uri = Uri.parse(SMS_URI_SENT);
        Cursor cursor = contentResolver.query(uri, allColumns ,
                "body LIKE '%"+smsCode+"%' AND address = ?",
                new String[] {number}, "date desc limit 1");

        Sms smsInbox = null;
        if(cursor.moveToFirst()){
            smsInbox = cursorToSms(cursor);
        }
        cursor.close();

        return smsInbox;
    }

    public Sms getSmsAfterDateByNumberAndSmsCode(String smsCode, Date date, String number){
        Uri uri = Uri.parse(SMS_URI_INBOX);
        Cursor cursor = contentResolver.query(uri, allColumns ,
                "date>=" + date.getTime() + " AND body LIKE '%"+smsCode+"%' AND address = ?",
                new String[] {number}, "date desc limit 1");

        Sms smsInbox = null;
        if(cursor.moveToFirst()){
            smsInbox = cursorToSms(cursor);
        }
        cursor.close();

        return smsInbox;
    }

    private Sms cursorToSms(Cursor cursor){
        Sms sms = new Sms();
        sms.setNumber(cursor.getString(0));
        sms.setBody(cursor.getString(1));
        sms.setDate(new Date(cursor.getLong(2)));
        return sms;
    }
}
