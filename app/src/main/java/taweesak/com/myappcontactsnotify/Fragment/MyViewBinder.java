package taweesak.com.myappcontactsnotify.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.Blob;

import taweesak.com.myappcontactsnotify.Appointment.DbHelperApppoint;
import taweesak.com.myappcontactsnotify.R;

class MyViewBinder implements SimpleCursorAdapter.ViewBinder {
   DbHelperApppoint mDbHelper;
    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        int viewID = view.getId();
        switch(viewID){

            case R.id.mv :
                ImageView contactProfile = (ImageView) view;
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(mDbHelper.IMAGE));
                if(imageBytes != null ){
                    // Pic image from database
                    contactProfile.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                }else {
                    // If image not found in database , assign a default image
                    contactProfile.setBackgroundResource(R.drawable.ic_camera);
                }
                break;

            case R.id.name :
                TextView name = (TextView) view;
                String myName;
                myName = cursor.getString(cursor.getColumnIndex(mDbHelper.NAME));
                name.setText(myName);
                break;

            case R.id.title :
                TextView title = (TextView) view;
                String myTitle;
                myTitle = cursor.getString(cursor.getColumnIndex(mDbHelper.TITLE));
                title.setText(myTitle);
                break;

            case R.id.Detail :
                TextView detail = (TextView) view;
                String myDetail;
                myDetail = cursor.getString(cursor.getColumnIndex(mDbHelper.DETAIL));
                detail.setText(myDetail);
                break;

            case R.id.type :
                TextView type = (TextView) view;
                String myType;
                myType = cursor.getString(cursor.getColumnIndex(mDbHelper.TYPE));
                type.setText(myType);
                break;

            case R.id.time :
                TextView time = (TextView) view;
                String myTime;
                myTime = cursor.getString(cursor.getColumnIndex(mDbHelper.TIME));
                time.setText(myTime);
                break;

            case R.id.date :
                TextView date = (TextView) view;
                String myDate;
                myDate = cursor.getString(cursor.getColumnIndex(mDbHelper.DATE));
                date.setText(myDate);
                break;
        }
        return true;
    }



}
