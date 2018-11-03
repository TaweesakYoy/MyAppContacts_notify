package taweesak.com.myappcontactsnotify.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.Adapter.RecyclerFavoriteAdapter;
import taweesak.com.myappcontactsnotify.R;


public class FragmentFavorite extends Fragment {

    View view;
    RecyclerView recyclerView;

    public FragmentFavorite() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

/*        gv = (GridView) view.findViewById(R.id.gv);

        adapter = new AdapterFavorite(getActivity(), MainActivity.allContacts);
        gv.setAdapter(adapter);*/

        recyclerView = view.findViewById(R.id.contact_recyclerview_fav);
        RecyclerFavoriteAdapter recyclerFavoriteAdapter = new RecyclerFavoriteAdapter(getActivity(),MainActivity.allContacts);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerFavoriteAdapter);

        return view;
    }

}
