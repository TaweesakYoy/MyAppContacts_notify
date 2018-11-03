package taweesak.com.myappcontactsnotify.reminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.R;


public class AlertReminder extends Activity {
    MediaPlayer mp;
    int reso = R.raw.piano;

    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);



        mp = MediaPlayer.create(getApplicationContext(), reso);
        mp.start();
        mp.setLooping(true); // setLoop


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);


        final TextView textViewTitle = (TextView) view.findViewById(R.id.tv_title);
        final TextView textViewContent = (TextView) view.findViewById(R.id.tv_content);
        final ImageView mv_content = view.findViewById(R.id.mv_content);
        final TextView tv_contactName = view.findViewById(R.id.tv_contactName);



        String msg = getString(R.string.alarmtextTitle) + getIntent().getExtras().getString(getString(R.string.title_msg));
        textViewTitle.setText(msg);

        String msg2 = getString(R.string.alarmtextContent) + getIntent().getExtras().getString(getString(R.string.content_msg));
        textViewContent.setText(msg2);

        String msg3 = getString(R.string.alarmtextName) + getIntent().getExtras().getString(getString(R.string.content_name));
        tv_contactName.setText(msg3);

        //get Image
        Bundle bundle = getIntent().getExtras();

        String Title = bundle.getString("title");
        String Content = bundle.getString("content");
        String contactName = bundle.getString("contactName");
        byte[] b = bundle.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        mv_content.setImageBitmap(bmp);

        builder.setMessage(null).setCancelable(
                false).setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        AlertReminder.this.finish();


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override

    public void onDestroy() {
        //test---S
        Intent i = new Intent(AlertReminder.this, MainActivity.class);

        startActivity(i);
        finish();
        //test---F

        super.onDestroy();

        mp.release();

    }

}
