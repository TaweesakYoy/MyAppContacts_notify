package taweesak.com.myappcontactsnotify.Appointment;


import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import taweesak.com.myappcontactsnotify.R;


public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        //boot sleep mode
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();


        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();


        String Title = intent.getStringExtra(context.getString(R.string.titttle));
        String Content = intent.getStringExtra(context.getString(R.string.contenttt));
        String contactName = intent.getStringExtra(context.getString(R.string.contactnamee));

        // get Image
        Bundle bundle = intent.getExtras();
        byte[] pic = bundle.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(pic, 0, pic.length);

        byte[] alertImg = getBytes(bmp);

        // Send Data
        Intent x = new Intent(context, Alert.class);

        x.putExtra(context.getString(R.string.titttle), Title);
        x.putExtra(context.getString(R.string.contenttt), Content);
        x.putExtra(context.getString(R.string.contactnamee), contactName);
        x.putExtra("image", alertImg);


        //test-F
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(x);


    }

    // Method send Image
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


}

