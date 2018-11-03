package taweesak.com.myappcontactsnotify.Appointment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.R;

public class CreateNote extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelperApppoint mDbHelper;
    EditText mTitleText;
    EditText mDescriptionText;
    Spinner mSpinner;
    DatePicker pickerDate;
    TimePicker pickerTime;
    TextView time;
    TextView date;
    CheckBox checkBoxAlarm, checkboxnotify;
    ImageView mv1;
    TextView Name;
    private Bitmap bytes;
    Drawable drawable;
    Button btn_setAlarm;

    // random RequestCode Number To pending
    Random rand = new Random();
    int n = rand.nextInt(500);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mDbHelper = new DbHelperApppoint(this);
        db = mDbHelper.getWritableDatabase();


        mTitleText = (EditText) findViewById(R.id.txttitle);
        mDescriptionText = (EditText) findViewById(R.id.description);
        mSpinner = (Spinner) findViewById(R.id.spinnerNoteType);
        pickerDate = (DatePicker) findViewById(R.id.datePicker);
        pickerTime = (TimePicker) findViewById(R.id.timePicker);
        time = (TextView) findViewById(R.id.txtTime);
        date = (TextView) findViewById(R.id.txtDate);


        mv1 = findViewById(R.id.mv1);
        Name = (TextView) findViewById(R.id.tv_name);


        // Get Data From RecyclerMain ***********
        Bundle bundle = getIntent().getExtras();
        String showName = bundle.getString("name");
        byte[] b = bundle.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        Name.setText(showName);
        mv1.setImageBitmap(bmp);


        pickerDate.setVisibility(View.INVISIBLE);
        pickerTime.setVisibility(View.INVISIBLE);
        time.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);

        btn_setAlarm = findViewById(R.id.btn_setAlarm);
        btn_setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showToast(getString(R.string.added_alert));

                pickerDate.setVisibility(View.VISIBLE);
                pickerTime.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
        //test
        Toast.makeText(this, "exit CreateNote <--", Toast.LENGTH_LONG).show();
        finish();
    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save: // Save Data*******************************
                String title = mTitleText.getText().toString();
                String detail = mDescriptionText.getText().toString();
                String name = Name.getText().toString();

                // set data Image to send
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mv1.getDrawable();
                byte[] imgContent = getBytes(bitmapDrawable.getBitmap());

                // Save to Database***************************
                ContentValues cv = new ContentValues();
                cv.put(mDbHelper.TITLE, title);
                cv.put(mDbHelper.DETAIL, detail);
                cv.put(mDbHelper.TIME, getString(R.string.Not_Set));
                cv.put(mDbHelper.NAME, name);
                cv.put(mDbHelper.IMAGE, imgContent);
                cv.put(mDbHelper.ALARM,0); // Set defult value = 0

                Calendar calender = Calendar.getInstance();
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

                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);

                // ส่งข้อความไป + Image ไป AlarmReceiver **************************************
                String alertTitle = mTitleText.getText().toString();
                String alertContent = mDescriptionText.getText().toString();
                String contactName = Name.getText().toString();
                BitmapDrawable im = (BitmapDrawable) mv1.getDrawable();
                byte[] im2 = getBytes(im.getBitmap());

                intent.putExtra(getString(R.string.alert_title), alertTitle);
                intent.putExtra(getString(R.string.alert_content), alertContent);
                intent.putExtra(getString(R.string.alert_contactname), contactName);
                intent.putExtra("image", im2);


                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, n, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis() + 1 * 1000, pendingIntent);
                //alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), 3*60*1000 , pendingIntent); //  ****เตือนซ้ำ 3 นาที ***format = 24*60*60*1000
                //alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis() + 1 * 1000,3*1000, pendingIntent);

                cv.put(mDbHelper.TIME, timeString);
                cv.put(mDbHelper.DATE, dateString);

                db.insert(mDbHelper.TABLE_NAME, null, cv);

                Intent openMainScreen = new Intent(this, MainActivity.class);
                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openMainScreen);

                Toast.makeText(this, "exit CreateNote from save menu", Toast.LENGTH_LONG).show();

                finish();
                return true;

            case R.id.action_back:
                Intent openMainActivity = new Intent(this, MainActivity.class);
                startActivity(openMainActivity);

                Toast.makeText(this, "exit CreateNote from back menu", Toast.LENGTH_LONG).show();

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
