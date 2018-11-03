package taweesak.com.myappcontactsnotify.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.Adapter.RecyclerMainViewAdapter;
import taweesak.com.myappcontactsnotify.Activity.AddNewContactActivity;
import taweesak.com.myappcontactsnotify.Data.Contact;
import taweesak.com.myappcontactsnotify.R;


public class FragmentMain extends Fragment {



    private ArrayList<Contact> allContacts = new ArrayList<>();
    private View view;
    private RecyclerView myRecyclerView;
    private RecyclerMainViewAdapter recyclerViewAdapter;
    private FloatingActionButton fab;
    private boolean flag = false;

    public FragmentMain() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allContacts = new ArrayList<Contact>();

        allContacts = MainActivity.getThemAll();

        if (allContacts.isEmpty()) {
            flag = true;
        }
        else {
            flag = false;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        Log.d("FragmentMain:", "Content view set");
        myRecyclerView = (RecyclerView) view.findViewById(R.id.contact_recyclerview);

        recyclerViewAdapter = new RecyclerMainViewAdapter(getActivity(), MainActivity.allContacts);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerViewAdapter);

        if(flag) {
            showMessage("Welcome to ICALL.","Enjoy my app.");
        }

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getActivity(), "click fab", Toast.LENGTH_SHORT).show();
                Intent myIntent;
                myIntent = new Intent(getActivity(), AddNewContactActivity.class);
                startActivity(myIntent);
                getActivity().finish(); // test

            }
        });


        // hide floating button when scroll recyclerview
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        return view;
    }


    public void showMessage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
