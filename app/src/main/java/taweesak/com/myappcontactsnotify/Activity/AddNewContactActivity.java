package taweesak.com.myappcontactsnotify.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import taweesak.com.myappcontactsnotify.Data.DBHelper;
import taweesak.com.myappcontactsnotify.R;


/**
 * Created by Strahinja on 4/9/2018.
 */

public class AddNewContactActivity extends AppCompatActivity {

    DBHelper myDb;
    EditText addFirstName, addLastName, addPhone, addEmail, addLine;
    Button createContact;
    ImageView addImage;
    CheckBox favorite;
    int flag = 0;
    public static final int PICK_IMAGE = 1;
    public static final int QRIMAGE = 2;

    Uri selectedImage = null;
    int checked = 0;

    ImageView scanQrcode;
    //qr code scanner object

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        myDb = new DBHelper(this);

        addFirstName = findViewById(R.id.add_first_name);
        addLastName = findViewById(R.id.add_last_name);
        addPhone = findViewById(R.id.add_phone);
        addEmail = findViewById(R.id.add_email);
        addImage = findViewById(R.id.add_image);
        addImage.setImageResource(R.drawable.ic_person);
        addImage.setTag(R.drawable.ic_person);
        favorite = findViewById(R.id.add_favorite);
        addLine = findViewById(R.id.add_line);


        createContact = findViewById(R.id.create_contact);

        //Start Qrcode
        //scanQrcode = findViewById(R.id.scanQrcode); ============= hold
        //intializing scan object
        qrScan = new IntentIntegrator(this);


        /*scanQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiating the qr code scan
                qrScan.initiateScan();


            }
        });*/

        //*****End Qrcode

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked = 1;
                } else {
                    checked = 0;
                }
            }
        });


        //Image integer? or string?
        createContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int num = favorite.isChecked() ? 1 : 0;

                if (flag == 0) {

                    Log.d("FLAG", "IS " + flag);
                    //addImage.setImageResource(R.drawable.ic_person_white);
                    addImage.setImageResource(R.drawable.ic_person_green);
                }

                boolean isInserted = myDb.insertData(
                        addFirstName.getText().toString(),
                        addLastName.getText().toString(),
                        addEmail.getText().toString(),
                        addPhone.getText().toString(),
                        num,
                        addImage.getDrawable(),
                        addLine.getText().toString());
                Log.d("Contact inserted: ", addFirstName.getText().toString() + " " + num);

                if (isInserted == true) {
                    Toast.makeText(AddNewContactActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(AddNewContactActivity.this, MainActivity.class);
                    AddNewContactActivity.this.startActivity(myIntent);
                    MainActivity.getThemAll();
                    finish();
                } else
                    Toast.makeText(AddNewContactActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
            }
        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        // start Qrcode

        IntentResult resultQrcode = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultQrcode != null) {
            //if qrcode has nothing in it
            if (resultQrcode.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(resultQrcode.getContents());
                    //setting values to textviews
                    addFirstName.setText(obj.getString("name"));
                    addLastName.setText(obj.getString("surname"));
                    addPhone.setText(obj.getString("phone"));
                    addEmail.setText(obj.getString("email"));
                    addLine.setText(obj.getString("lineid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, resultQrcode.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


        // end Qrcode

        if (requestCode != PICK_IMAGE) {
            //addImage.setImageResource(R.drawable.ic_person_white);
            addImage.setImageResource(R.drawable.ic_person_green);
            Log.d("PICK_IMAGE", " IS NOT TRIGGERED");
        }

        if (requestCode == PICK_IMAGE && null != data) {
            selectedImage = data.getData();

            CropImage.activity(selectedImage)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4, 4)
                    .start(this);

        }


        //if (requestCode == PICK_IMAGE && null != data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            flag = 1;
            //Uri selectedImage = data.getData();
            selectedImage = data.getData();
            ParcelFileDescriptor parcelFD = null;

            Uri resultUri = result.getUri();
            //addImage.setImageURI(resultUri);
            selectedImage = resultUri;


            try {
                parcelFD = getContentResolver().openFileDescriptor(selectedImage, "r");
                FileDescriptor imageSource = parcelFD.getFileDescriptor();

                // Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(imageSource, null, o);

                // the new size we want to scale to
                //final int REQUIRED_SIZE = 1024;
                final int REQUIRED_SIZE = 512; // Test Size Image ******************

                // Find the correct scale value. It should be the power of 2.
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1;
                while (true) {
                    if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                        break;
                    }
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }

                // decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

                addImage.setImageBitmap(bitmap);
                addImage.setTag(bitmap);
                Log.d("Added image:", addImage.toString());


            } catch (FileNotFoundException e) {
                // handle errors
                e.printStackTrace();
            } catch (IOException e) {
                // handle errors
                e.printStackTrace();
            } finally {
                if (parcelFD != null)
                    try {
                        parcelFD.close();
                    } catch (IOException e) {
                        // ignored
                    }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_main_read_qrcode, menu);
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

            case R.id.action_read_qrcode:

                        qrScan.initiateScan();

                return true;
        }
        return true;


    }






}
