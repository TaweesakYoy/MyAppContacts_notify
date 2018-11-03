package taweesak.com.myappcontactsnotify.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import taweesak.com.myappcontactsnotify.Adapter.ViewPagerAdapter;
import taweesak.com.myappcontactsnotify.Data.Contact;
import taweesak.com.myappcontactsnotify.Data.DBHelper;
import taweesak.com.myappcontactsnotify.Data.User;
import taweesak.com.myappcontactsnotify.Data.UserDbHelper;
import taweesak.com.myappcontactsnotify.Fragment.FragmentFavorite;
import taweesak.com.myappcontactsnotify.Fragment.FragmentMain;
import taweesak.com.myappcontactsnotify.Fragment.FragmentAppointment;
import taweesak.com.myappcontactsnotify.Fragment.FragmentReminder;
import taweesak.com.myappcontactsnotify.R;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DBHelper myDb;
    public static ArrayList<Contact> allContacts = new ArrayList<>();

    private TabLayout tabLayout;
    private ViewPager viewpager;
    private ViewPagerAdapter adapter;

    public String title;

    // User Data
    SQLiteDatabase dbUser;
    public static UserDbHelper dbHelperUser;
    public static ArrayList<User> UserData = new ArrayList<>();
    Cursor cursorUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Add Navigation drawer ---Start

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //--------------Cancle /////////drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        // Add Navigation drawer ---Finish

        //Add image to navigation Drawable-----------Start

        //final long id = getIntent().getExtras().getLong(getString(R.string.row_id));
        dbHelperUser = new UserDbHelper(this);
        dbUser = dbHelperUser.getWritableDatabase();

        // cursorUser = dbUser.rawQuery("select * from " + dbHelperUser.TABLE_NAME + " where " + dbHelperUser.COL_1 + "=" + id, null);
        cursorUser = dbUser.rawQuery("select * from " + dbHelperUser.TABLE_NAME + " where " + dbHelperUser.COL_1, null);


        View headerView = navigationView.getHeaderView(0);
        ImageView imgProfile = headerView.findViewById(R.id.header_imageView);
        TextView tv_profile_name = headerView.findViewById(R.id.tv_profile_name);
       // TextView tv_profile_surname = headerView.findViewById(R.id.tv_profile_surname);
        /*TextView tv_profile_phone = headerView.findViewById(R.id.tv_profile_phone);
        TextView tv_profile_lineid = headerView.findViewById(R.id.tv_profile_lineid);
        TextView tv_profile_email = headerView.findViewById(R.id.tv_profile_email);*/
        TextView tv_info = headerView.findViewById(R.id.tv_info);

        imgProfile.setOnClickListener(new View.OnClickListener() {

            final UserDbHelper myDb = new UserDbHelper(getApplicationContext());

            @Override
            public void onClick(View v) {
                // Check data is null ?
                String arrData[] = myDb.SelectData("0");
                if(arrData == null)
                {
                    Toast.makeText(MainActivity.this,"Not found Data!",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(),AddUserDataActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"ID = " + arrData[0]
                                    + ","  + arrData[1] + ","  + arrData[2],
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(),EditUserDataActivity.class);
                    i.putExtra("FAVORITE",dbHelperUser.COL_5);
                    startActivity(i);
                    finish();
                }
            }
        });



        if (cursorUser != null) {
            if (cursorUser.moveToFirst()) {
                tv_profile_name.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_2))+"  "+
                        cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_3)));
                //tv_profile_surname.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_3)));
                /*tv_profile_phone.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_4)));
                tv_profile_lineid.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_8)));
                tv_profile_email.setText(cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_6)));*/

                tv_info.setText("Phone : "+cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_4))+"\n"+
                "Line id : "+cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_8))+"\n"+
                "Email : "+cursorUser.getString(cursorUser.getColumnIndex(dbHelperUser.COL_6)));


                byte[] bytes = cursorUser.getBlob(cursorUser.getColumnIndex(dbHelperUser.COL_7));
                imgProfile.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

            }
            cursorUser.close();
        }


        //imgProfile.setImageResource(R.drawable.vevi);
        //Add image to navigation Drawable-----------Start


        myDb = new DBHelper(this);
        allContacts = new ArrayList<Contact>();
        getThemAll(); //allContacts = getThemAll();

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewpager = (ViewPager) findViewById(R.id.viewpager_id);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentMain(), "");
        adapter.addFragment(new FragmentFavorite(), "");
        adapter.addFragment(new FragmentReminder(), "");
        //adapter.addFragment(new FragmentAppointment(), ""); // Add

        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_import_contacts_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_star);
        //tabLayout.getTabAt(2).setIcon(R.drawable.ic_person1);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_notification);

        //Disable Swipe ViewPager
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }


    // User Data
    public static ArrayList<User> getUserData() {
        Cursor res2 = dbHelperUser.getAllData();
        UserData.removeAll(UserData);
        while (res2.moveToNext()) {

            User c2 = new User(

                    res2.getString(0),
                    res2.getString(1),
                    res2.getString(2),
                    res2.getString(3),
                    res2.getInt(4),
                    res2.getString(5),
                    res2.getBlob(6),
                    res2.getString(7)
            );

            if (c2.getLastName() != null || c2.getFirstName() != null)
                UserData.add(c2);
        }
        return UserData;
    }

    // Contact Data
    public static ArrayList<Contact> getThemAll() {
        Cursor res = myDb.getAllData();
        allContacts.removeAll(allContacts);
        while (res.moveToNext()) {

            Contact c = new Contact(

                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getInt(4),
                    res.getString(5),
                    res.getBlob(6),
                    res.getString(7)
            );

            if (c.getLastName() != null || c.getFirstName() != null)
                allContacts.add(c);
        }
        return allContacts;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Navigation Menu

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.nav_camera:
                title = "ICALL"; // Set title name
                viewpager.setCurrentItem(0);
                break;

            case R.id.nav_gallery:
                title = "ICALL";
                viewpager.setCurrentItem(1);
                break;

            case R.id.nav_slideshow:
                title = "ICALL";
                viewpager.setCurrentItem(2);
                break;

            case R.id.nav_qrcode:

                Toast.makeText(this, "Preview my Qrcode Data", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, QrcodeActivity.class);
                startActivity(intent);
                break;



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        item.setChecked(true);
        getSupportActionBar().setTitle(title);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







}
