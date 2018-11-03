package taweesak.com.myappcontactsnotify.Appointment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.R;

public class View_Note extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelperApppoint dbHelper;
    byte[] blob;
    Bitmap bitmap;
    ImageView mv_ViewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__note);

        final long id = getIntent().getExtras().getLong(getString(R.string.row_id));

        dbHelper = new DbHelperApppoint(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + dbHelper.TABLE_NAME + " where " + dbHelper.C_ID + "=" + id, null);
        TextView name = (TextView) findViewById(R.id.tv_name1);
        TextView title = (TextView) findViewById(R.id.title);
        TextView detail = (TextView) findViewById(R.id.detail);
        TextView notetype = (TextView) findViewById(R.id.note_type_ans);
        TextView time = (TextView) findViewById(R.id.alertvalue);
        TextView date = (TextView) findViewById(R.id.datevalue);
        mv_ViewNote = findViewById(R.id.mv_ViewNote);



        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name.setText(cursor.getString(cursor.getColumnIndex(dbHelper.NAME)));
                title.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TITLE)));
                detail.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DETAIL)));
                notetype.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)));
                time.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TIME)));
                date.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DATE)));

                // Cursor getImage*************
                mv_ViewNote.setImageBitmap(getImageFromBLOB(cursor.getBlob(cursor.getColumnIndex("image"))));

            }
            cursor.close();
        }
    }

    // getImage****************
    public static Bitmap getImageFromBLOB(byte[] mBlob)
    {
        byte[] bb = mBlob;
        return BitmapFactory.decodeByteArray(bb, 0, bb.length);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, MainActivity.class);
        startActivity(setIntent);
        //Test
        Toast.makeText(this, "exit ViewNote from <---", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final long id = getIntent().getExtras().getLong(getString(R.string.rowID));

        switch (item.getItemId()) {
            case R.id.action_back:
                Intent openMainActivity = new Intent(this, MainActivity.class);
                startActivity(openMainActivity);
                //test
                Toast.makeText(this, "exit ViewNote from back menu", Toast.LENGTH_LONG).show();
                //--
                //test
                finish();
                return true;
            case R.id.action_edit:

                Intent openEditNote = new Intent(View_Note.this, Edit_Note.class);


                //test send image -----Start
                Drawable drawable=mv_ViewNote.getDrawable();
                Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                //test send image -----End

                openEditNote.putExtra(getString(R.string.intent_row_id), id);
                openEditNote.putExtra("image", b);


                startActivity(openEditNote);
                //test
                Toast.makeText(this, "exit ViewNote from Edit menu", Toast.LENGTH_LONG).show();
                //test
                finish();
                return true;

            case R.id.action_discard:
                AlertDialog.Builder builder = new AlertDialog.Builder(View_Note.this);
                builder
                        .setTitle(getString(R.string.delete_title))
                        .setMessage(getString(R.string.delete_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Long id = getIntent().getExtras().getLong(getString(R.string.rodID));
                                db.delete(DbHelperApppoint.TABLE_NAME, DbHelperApppoint.C_ID + "=" + id, null);
                                db.close();
                                Intent openMainActivity = new Intent(View_Note.this, MainActivity.class);
                                startActivity(openMainActivity);
                                //test
                                Toast.makeText(View_Note.this, "exit ViewNote from Discard menu", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)                        //Do nothing on no
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
