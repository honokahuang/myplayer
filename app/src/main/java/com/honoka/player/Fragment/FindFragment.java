package com.honoka.player.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.honoka.player.R;

public class FindFragment extends ListFragment {
    private View view;
    private ListView listView;
    public FindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_find_main, container, false);
        listView= (ListView) view.findViewById(android.R.id.list);

        return view;
    }



}
