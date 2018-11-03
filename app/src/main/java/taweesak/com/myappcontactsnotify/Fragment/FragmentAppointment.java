package taweesak.com.myappcontactsnotify.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;

import taweesak.com.myappcontactsnotify.Appointment.CreateNote;
import taweesak.com.myappcontactsnotify.Appointment.DbHelperApppoint;
import taweesak.com.myappcontactsnotify.Appointment.View_Note;
import taweesak.com.myappcontactsnotify.R;


public class FragmentAppointment extends Fragment {
    SQLiteDatabase db;
    DbHelperApppoint mDbHelper;
    ListView list;
    FloatingActionButton fab;

    public FragmentAppointment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.APPNAME);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        list = (ListView)view.findViewById(R.id.commentlist);

        /*fab=(FloatingActionButton)view.findViewById(R.id.fabAppPoint);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "click :", Toast.LENGTH_SHORT).show();
                Intent openCreateNote = new Intent(getActivity(), CreateNote.class);
                startActivity(openCreateNote);
                //test
                getActivity().finish();
            }
        });*/

        mDbHelper = new DbHelperApppoint(getActivity());
        db= mDbHelper.getWritableDatabase();
        final ImageView alarmImage = (ImageView) view.findViewById(R.id.alarmImage);
        ImageView mv = view.findViewById(R.id.mv);

        String[] from = {mDbHelper.TITLE, mDbHelper.DETAIL, mDbHelper.TYPE, mDbHelper.TIME, mDbHelper.DATE, mDbHelper.NAME, mDbHelper.IMAGE};

        final String[] column = {mDbHelper.C_ID, mDbHelper.TITLE, mDbHelper.DETAIL, mDbHelper.TYPE, mDbHelper.TIME, mDbHelper.DATE, mDbHelper.NAME, mDbHelper.IMAGE};
        int[] to = {R.id.title, R.id.Detail, R.id.type, R.id.time, R.id.date, R.id.name, R.id.mv};

        final Cursor cursor = db.query(mDbHelper.TABLE_NAME, column, null, null ,null, null, null);

        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_entry, cursor, from, to, 0);

        adapter.setViewBinder(new MyViewBinder());

        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> listView, View view, int position,
                                    long id){
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), View_Note.class);
                intent.putExtra(getString(R.string.rodId), id);
                startActivity(intent);

                //test
                //getActivity().moveTaskToBack(true);
                getActivity().finish();
            }

        });


        return view;
    }


}
