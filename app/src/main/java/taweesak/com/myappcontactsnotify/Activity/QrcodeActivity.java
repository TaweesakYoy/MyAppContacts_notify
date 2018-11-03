package taweesak.com.myappcontactsnotify.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import taweesak.com.myappcontactsnotify.Data.User;
import taweesak.com.myappcontactsnotify.Data.UserDbHelper;
import taweesak.com.myappcontactsnotify.R;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static taweesak.com.myappcontactsnotify.Activity.MainActivity.dbHelperUser;

public class QrcodeActivity extends AppCompatActivity {

    TextView tv_nameQrcode, tv_surnameQrcode, tv_phoneNumber, tv_lineId, tv_email;
    TextView tv_nameSurname;
    ImageView mv_qrcode, mv_image,qrcode_create;
    //Button btn_genBarcode;
    private String URL;
    private Bitmap qrImage;

    SQLiteDatabase dbUser;
    public static UserDbHelper dbHelperUser;
    public static ArrayList<User> UserData = new ArrayList<>();
    Cursor cursorUser;
    private QrcodeActivity self;

    String name, surname, phone, lineid, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        self = this;

        dbHelperUser = new UserDbHelper(this);
        dbUser = dbHelperUser.getWritableDatabase();

        tv_nameQrcode = findViewById(R.id.tv_nameQrcode);
        tv_surnameQrcode = findViewById(R.id.tv_surnameQrcode);
        tv_phoneNumber = findViewById(R.id.tv_phoneNumber);
        tv_lineId = findViewById(R.id.tv_lineId);
        tv_email = findViewById(R.id.tv_email);
        mv_qrcode = findViewById(R.id.mv_qrcode);
        mv_image = findViewById(R.id.mv_image);
        qrcode_create = findViewById(R.id.qrcode_create);

        tv_nameSurname = findViewById(R.id.tv_nameSurname);


        cursorUser = dbUser.rawQuery("select * from " + dbHelperUser.TABLE_NAME + " where " + dbHelperUser.COL_1, null);

        if (cursorUser != null) {
            if (cursorUser.moveToFirst()) {
                /*tv_name.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_2))+"  "+
                        cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_3))) ;*/

                tv_nameQrcode.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_2)));
                tv_surnameQrcode.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_3)));

                tv_nameSurname.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_2))+" "+
                        cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_3)));

                tv_phoneNumber.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_4)));
                tv_lineId.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_8)));
                tv_email.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_6)));

                byte[] bytes = cursorUser.getBlob(cursorUser.getColumnIndex(dbHelperUser.COL_7));
                mv_image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

            }
            cursorUser.close();
        }

        name = (String) tv_nameQrcode.getText();
        surname = (String) tv_surnameQrcode.getText();
        phone = (String) tv_phoneNumber.getText();
        lineid = (String) tv_lineId.getText();
        email = (String) tv_email.getText();

        qrcode_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.generateImage();
            }
        });

    }

    // gen barcode

    private void generateImage(){
        //final String text = tv_name.getText().toString();
        final String text = "{\"name\":\""+ name+"\""+ ",\""+ "surname\":\""+ surname+"\""+ ",\""+ "phone\":\""+ phone+"\""+ ",\""+ "email\":\""+ email+"\""+ ",\""+ "lineid\":\""+ lineid+"\"}";


        if(text.trim().isEmpty()){
            alert("No Data QR Code");
            return;
        }


        //endEditing();
        showLoadingVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = mv_qrcode.getMeasuredWidth();
                if (size > 1) {
                    //Log.e(tag, "size is set manually");
                    size = 260;
                }

                Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
                hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                hintMap.put(EncodeHintType.MARGIN, 1);
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BitMatrix byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size,
                            size, hintMap);
                    int height = byteMatrix.getHeight();
                    int width = byteMatrix.getWidth();
                    self.qrImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            qrImage.setPixel(x, y, byteMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }

                    self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            self.showImage(self.qrImage);
                            self.showLoadingVisible(false);
                            //self.snackbar("QRCode telah dibuat");
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                    alert(e.getMessage());
                }
            }
        }).start();
    }

    private void alert(String message) {
        AlertDialog dlg = new AlertDialog.Builder(self)
                .setTitle("QRCode Generator")
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dlg.show();
    }

    /*private void endEditing(){
        tv_name.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }*/

    private void showLoadingVisible(boolean visible) {
        if (visible) {
            showImage(null);
        }

        /*loader.setVisibility(
                (visible) ? View.VISIBLE : View.GONE
        );*/
    }

    private void showImage(Bitmap bitmap) {
        if (bitmap == null) {
            mv_qrcode.setImageResource(android.R.color.transparent);
            qrImage = null;
            //txtSaveHint.setVisibility(View.GONE);
        } else {
            mv_qrcode.setImageBitmap(bitmap);
            //txtSaveHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back_user:
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

            finish();
            return true;
        }
        return true;


    }
}
