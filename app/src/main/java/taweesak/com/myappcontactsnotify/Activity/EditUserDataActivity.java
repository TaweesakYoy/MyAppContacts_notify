package taweesak.com.myappcontactsnotify.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import taweesak.com.myappcontactsnotify.Appointment.DbHelperApppoint;
import taweesak.com.myappcontactsnotify.Data.Contact;
import taweesak.com.myappcontactsnotify.Data.User;
import taweesak.com.myappcontactsnotify.Data.UserDbHelper;
import taweesak.com.myappcontactsnotify.Fragment.FragmentEdit;
import taweesak.com.myappcontactsnotify.R;

import static taweesak.com.myappcontactsnotify.Activity.MainActivity.dbHelperUser;

public class EditUserDataActivity extends AppCompatActivity {

    private EditText editFirstName, editLastName, editPhone, editEmail, editLineid;
    private ImageView imageView;
    private Button btnSaveEdit;
    int flag = 0;
    public static final int PICK_IMAGE = 1;

    Uri selectedImage = null;
    CheckBox editFavorite;
    SQLiteDatabase db;
    UserDbHelper dbUserHelper;

    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);



        dbUserHelper = new UserDbHelper(this);
        //db = userDbHelper.getWritableDatabase();
        db = dbUserHelper.getReadableDatabase();
        String id = getIntent().getStringExtra("FAVORITE");
        Cursor cursor = db.rawQuery("select * from " + dbUserHelper.TABLE_NAME + " where " + dbUserHelper.COL_5 + "=" + id, null);
        // เช็ค Cursor จาก Column Faverite แทน

        imageView = (ImageView) findViewById(R.id.editimageUserEdit);
        /*byte[] bytes2 = EditActivity.currentContact.getImage();
        Log.d("FRAGMENT-EDIT: Bytes: ", "" + bytes2);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.length);
        imageView.setImageBitmap(bitmap);*/

        editFirstName = (EditText)findViewById(R.id.edit_first_nameUserEdit);
        editLastName = (EditText)findViewById(R.id.edit_last_nameUserEdit);
        editPhone = (EditText)findViewById(R.id.edit_phoneUserEdit);
        editEmail = (EditText)findViewById(R.id.edit_emailUserEdit);
        editLineid = (EditText)findViewById(R.id.edit_lineUserEdit);
        btnSaveEdit = (Button)findViewById(R.id.edit_contactUserEdit);
        editFavorite = (CheckBox) findViewById(R.id.edit_favoriteUserEdit);



        if (cursor != null) {
            if (cursor.moveToFirst()) {

                editFirstName.setText(cursor.getString(cursor.getColumnIndex(dbUserHelper.COL_2)));
                editLastName.setText(cursor.getString(cursor.getColumnIndex(dbUserHelper.COL_3)));
                editPhone.setText(cursor.getString(cursor.getColumnIndex(dbUserHelper.COL_4)));
                editEmail.setText(cursor.getString(cursor.getColumnIndex(dbUserHelper.COL_6)));
                editLineid.setText(cursor.getString(cursor.getColumnIndex(dbUserHelper.COL_8)));

                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(dbUserHelper.COL_7));
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
            cursor.close();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
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

        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelperUser.updateData("1",editFirstName.getText().toString(),editLastName.getText().toString(),
                        editEmail.getText().toString(),editPhone.getText().toString(),0,imageView.getDrawable(),editLineid.getText().toString());

                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                byte[] imgContent = getBytes(bitmapDrawable.getBitmap());

                // Image Save
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();

                String fName = editFirstName.getText().toString();
                String lName = editLastName.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                String line = editLineid.getText().toString();

                ContentValues cv = new ContentValues();
                cv.put(dbHelperUser.COL_2, fName);
                cv.put(dbHelperUser.COL_3, lName);
                cv.put(dbHelperUser.COL_4, phone);
                cv.put(dbHelperUser.COL_6, email);
                cv.put(dbHelperUser.COL_8, line);
                cv.put(dbHelperUser.COL_7, imgContent);


                MainActivity.getUserData();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    // Method send Image
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PICK_IMAGE) {
            //addImage.setImageResource(R.drawable.ic_person_white);
            imageView.setImageResource(R.drawable.ic_person_green);
            Log.d("PICK_IMAGE", " IS NOT TRIGGERED");
        }

        if(requestCode == PICK_IMAGE && null != data){
            selectedImage = data.getData();

            CropImage.activity(selectedImage)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4,4)
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
                final int REQUIRED_SIZE = 1024;

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

                imageView.setImageBitmap(bitmap);
                imageView.setTag(bitmap);
                Log.d("Added image:", imageView.toString());


            } catch (FileNotFoundException e) {
                // handle errors
                e.printStackTrace();
            } /*catch (IOException e) {
                // handle errors
                e.printStackTrace();
            } */finally {
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
