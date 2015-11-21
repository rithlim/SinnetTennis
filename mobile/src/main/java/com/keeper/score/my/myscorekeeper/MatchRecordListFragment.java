package com.keeper.score.my.myscorekeeper;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.keeper.score.models.MatchRecord;
import com.keeper.score.utils.MatchRecordManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchRecordListFragment extends Fragment {
    private static final String TAG = MatchRecordListFragment.class.getSimpleName();
    private static ListView listView;
    private ListAdapter mAdapter;

    private static TextView tvEmptyList;

    public MatchRecordListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_match_record, container, false);

        listView = (ListView) view.findViewById(R.id.match_record_list);

         mAdapter = new MatchRecordListAdapter(getActivity(), MatchRecordManager.getMatchRecordList());

        listView.setAdapter(mAdapter);

        return view;
    }


}
