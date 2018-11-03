package taweesak.com.myappcontactsnotify.Appointment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.R;


public class Edit_Note extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelperApppoint mDbHelper;
    EditText mTitleText;
    EditText mDescriptionText;
    Spinner mSpinner;
    DatePicker pickerDate;
    TimePicker pickerTime;
    TextView time;
    TextView date;
    TextView tv_name2;
    ImageView mv_edit;
    TextView check_box;

    //Button btn_cancelAlert;
    AlarmManager alarmMgr;
    PendingIntent pendingIntent;

    CheckBox cancel_checkBox;
    int checked = 0;

    int chk;
    Calendar calender;


    Random rand = new Random();
    int n = rand.nextInt(20); // Gives n such that 0 <= n < 20

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__note);
        mDbHelper = new DbHelperApppoint(this);
        db = mDbHelper.getWritableDatabase();

        mv_edit = findViewById(R.id.mv_edit);
        tv_name2 = (TextView) findViewById(R.id.tv_name2);
        mTitleText = (EditText) findViewById(R.id.txttitle);
        mDescriptionText = (EditText) findViewById(R.id.description);
        mSpinner = (Spinner) findViewById(R.id.spinnerNoteType);
        pickerDate = (DatePicker) findViewById(R.id.datePicker2);
        pickerTime = (TimePicker) findViewById(R.id.timePicker2);
        time = (TextView) findViewById(R.id.txt_selecttime);
        date = (TextView) findViewById(R.id.txt_selectdate);


        /*check_box = findViewById(R.id.check_box);
        cancel_checkBox = findViewById(R.id.cancel_checkBox);*/


        final long id = getIntent().getExtras().getLong(getString(R.string.row_id_log));

        // Get Data From RecyclerMain ***********
        Bundle bundle = getIntent().getExtras();
        String showName = bundle.getString("name");
        byte[] b = bundle.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        mv_edit.setImageBitmap(bmp);

        showToast(getString(R.string.added_alert));
        pickerDate.setVisibility(View.VISIBLE);
        pickerTime.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);


        Cursor cursor = db.rawQuery("select * from " + mDbHelper.TABLE_NAME + " where " + mDbHelper.C_ID + "=" + id, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mTitleText.setText(cursor.getString(cursor.getColumnIndex(mDbHelper.TITLE)));
                tv_name2.setText(cursor.getString(cursor.getColumnIndex(mDbHelper.NAME)));
                mDescriptionText.setText(cursor.getString(cursor.getColumnIndex(mDbHelper.DETAIL)));

//                check_box.setText(cursor.getString(cursor.getColumnIndex(mDbHelper.ALARM)));

                if (cursor.getString(cursor.getColumnIndex(mDbHelper.TIME)).toString().equals(getString(R.string.Not_Set_Alert))) {

                    pickerDate.setVisibility(View.INVISIBLE);
                    pickerTime.setVisibility(View.INVISIBLE);
                    time.setVisibility(View.INVISIBLE);
                    date.setVisibility(View.INVISIBLE);

                }

                /*chk = Integer.parseInt(check_box.getText().toString());

                if (chk == 1) {
                    cancel_checkBox.setChecked(true);
                    *//*alarmMgr.cancel(pendingIntent);
                    pendingIntent.cancel();*//*
                } else {
                    cancel_checkBox.setChecked(false);
                }*/


            }
            cursor.close();
        }


    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);

        Toast.makeText(this, "exit EditNote <--", Toast.LENGTH_LONG).show();
        finish();

    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_back:
                Intent openMainActivity = new Intent(this, MainActivity.class);
                startActivity(openMainActivity);

                Toast.makeText(this, "exit EditNote from back menu", Toast.LENGTH_LONG).show();

                finish();
                return true;
            case R.id.action_save:
                final long id = getIntent().getExtras().getLong(getString(R.string.row_id_long));
                String title = mTitleText.getText().toString();
                String name = tv_name2.getText().toString();
                String detail = mDescriptionText.getText().toString();

                //int num = cancel_checkBox.isChecked() ? 1 : 0; //**********test

                // set data Image to send
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mv_edit.getDrawable();
                byte[] imgContent = getBytes(bitmapDrawable.getBitmap());

                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.TITLE, title);
                cv.put(mDbHelper.NAME, name);
                cv.put(mDbHelper.DETAIL, detail);
                cv.put(mDbHelper.TIME, getString(R.string.Not_Set));
                cv.putNull(mDbHelper.DATE);
                cv.put(mDbHelper.IMAGE, imgContent);
                cv.put(mDbHelper.ALARM, 0); // num

                calender = Calendar.getInstance();
                calender.clear();
                calender.set(Calendar.MONTH, pickerDate.getMonth());
                calender.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());
                calender.set(Calendar.YEAR, pickerDate.getYear());
                calender.set(Calendar.HOUR, pickerTime.getCurrentHour());
                calender.set(Calendar.MINUTE, pickerTime.getCurrentMinute());
                calender.set(Calendar.SECOND, 00);

                SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                String timeString = formatter.format(new Date(calender.getTimeInMillis()));
                SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));


                alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);

                // ส่งข้อความไป + Image ไป AlarmReceiver **************************************
                String alertTitle = mTitleText.getText().toString();
                String alertContent = mDescriptionText.getText().toString();
                String contactName = tv_name2.getText().toString();
                BitmapDrawable im = (BitmapDrawable) mv_edit.getDrawable();
                byte[] im2 = getBytes(im.getBitmap());

                intent.putExtra(getString(R.string.alert_title), alertTitle);
                intent.putExtra(getString(R.string.alert_content), alertContent);
                intent.putExtra(getString(R.string.alert_contactname), contactName);
                intent.putExtra("image", im2);

                pendingIntent = PendingIntent.getBroadcast(this, n, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis() + 1 * 1000, pendingIntent);

                // Set repeating Alarm
                //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), 3*60*1000 , pendingIntent); //**เตือนซ้ำ 3 นาที ***format = 24*60*60*1000
                //alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calender.getTimeInMillis(), pendingIntent);

                /*// Cancel Alarm
                if (chk != 0) {

                    alarmMgr.cancel(pendingIntent);
                    pendingIntent.cancel();
                    cv.put(mDbHelper.TIME, "");
                    cv.put(mDbHelper.DATE, "");


                } else {

                    //alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis() + 1 * 1000, pendingIntent);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), 3 * 60 * 1000, pendingIntent); //**เตือนซ้ำ 3 นาที ***format = 24*60*60*1000
                    //alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                    cv.put(mDbHelper.TIME, timeString);
                    cv.put(mDbHelper.DATE, dateString);


                }*/

                cv.put(mDbHelper.TIME, timeString);
                cv.put(mDbHelper.DATE, dateString);

                db.update(mDbHelper.TABLE_NAME, cv, mDbHelper.C_ID + "=" + id, null);

                Intent openMainScreen = new Intent(Edit_Note.this, MainActivity.class);
                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openMainScreen);

                Toast.makeText(this, "exit EditNote from save menu", Toast.LENGTH_LONG).show();

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // Method send Image
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
