package taweesak.com.myappcontactsnotify.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import static taweesak.com.myappcontactsnotify.Activity.MainActivity.dbHelperUser;


public class UserDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "user.db";
    public static final String TABLE_NAME = "tbluser";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FIRSTNAME";
    public static final String COL_3 = "LASTNAME";
    public static final String COL_4 = "PHONE";
    public static final String COL_5 = "FAVORITE";
    public static final String COL_6 = "EMAIL";
    public static final String COL_7 = "IMAGE";
    public static final String COL_8 = "LINE";



    public UserDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table IF NOT EXISTS " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME TEXT, LASTNAME TEXT, PHONE TEXT, FAVORITE INTEGER, EMAIL TEXT, IMAGE TEXT, LINE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String fname, String lname, String email, String phone, int favorite, Drawable dbDrawable, String line)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Bitmap bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if(VectorDrawableCompat.class == dbDrawable.getClass()) {
            Log.d("DB", " IT'S NOT A BITMAP!");

            bitmap = Bitmap.createBitmap(dbDrawable.getIntrinsicWidth(),
                    dbDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        else {
            bitmap = ((BitmapDrawable) dbDrawable).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }

        contentValues.put(COL_2, fname);
        contentValues.put(COL_3, lname);
        contentValues.put(COL_4, phone);
        contentValues.put(COL_5, favorite);
        contentValues.put(COL_6, email);
        contentValues.put(COL_7, stream.toByteArray());
        contentValues.put(COL_8, line);




        Log.d("INSERTED", contentValues.toString());

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getContact(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+ " WHERE 'ID = ?'", new String[] { id });
        return res;
    }

    public boolean updateFavorite(String id, String favorite)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_5, favorite);

        db.update(TABLE_NAME, contentValues, "ID ="+id, null);
        return true;
    }


    public boolean updateData(String id, String fname, String lname, String email, String phone, int favorite, Drawable dbDrawable, String line)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        Bitmap bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if(VectorDrawableCompat.class == dbDrawable.getClass()) {
            Log.d("DB", " IT'S NOT A BITMAP!");

            bitmap = Bitmap.createBitmap(dbDrawable.getIntrinsicWidth(),
                    dbDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        else {
            bitmap = ((BitmapDrawable) dbDrawable).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }


        contentValues.put(COL_1,id);
        contentValues.put(COL_2, fname);
        contentValues.put(COL_3, lname);
        contentValues.put(COL_4, phone);
        contentValues.put(COL_5, favorite);
        contentValues.put(COL_6, email);
        contentValues.put(COL_7, stream.toByteArray());
        contentValues.put(COL_8, line);



        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("delete from "+TABLE_NAME);
        onUpgrade(db,1,2);
    }

    public Integer deleteReset()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "1", null);
    }

    // Select Data /Check Data is null?
    public String[] SelectData(String ID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            Cursor cursor = db.query("tbluser", new String[] { "*" },
                    "FAVORITE=?",
                    new String[] { String.valueOf(ID) }, null, null, null, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     *  0 = MemberID
                     *  1 = Name
                     *  2 = Tel
                     */
                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

}
