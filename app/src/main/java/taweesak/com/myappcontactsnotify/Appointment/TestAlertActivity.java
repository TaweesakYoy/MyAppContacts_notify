package taweesak.com.myappcontactsnotify.Appointment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.R;

public class TestAlertActivity extends Activity {

    MediaPlayer mp;
    int reso=R.raw.chec;

    SQLiteDatabase db;
    DbHelperApppoint mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_alert);

        mp=MediaPlayer.create(getApplicationContext(),reso);
        mp.start();

        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_content = findViewById(R.id.tv_content);
        ImageView alert_mv = findViewById(R.id.alert_mv);



        Bundle bundle = getIntent().getExtras();

        String Title = bundle.getString("title");
        String Content = bundle.getString("content");
        byte[] b = bundle.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        tv_title.setText(Title);
        tv_content.setText(Content);
        alert_mv.setImageBitmap(bmp);

    }

    @Override

    public void onDestroy() {
        //test---S
        Intent i = new Intent(TestAlertActivity.this, MainActivity.class);

        startActivity(i);
        finish();
        //test---F

        super.onDestroy();

        mp.release();

    }
}
