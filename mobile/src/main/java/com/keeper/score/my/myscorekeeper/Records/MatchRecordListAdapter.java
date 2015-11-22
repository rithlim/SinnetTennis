package com.keeper.score.my.myscorekeeper.Records;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keeper.score.models.MatchRecord;
import com.keeper.score.my.myscorekeeper.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Rith on 11/20/2015.
 */
public class MatchRecordListAdapter extends ArrayAdapter<MatchRecord> {
    private static final String TAG = MatchRecordListAdapter.class.getSimpleName();

    private List<MatchRecord> matchRecordList;

    public MatchRecordListAdapter(Context context, List<MatchRecord> matchRecordList) {
        super(context, R.layout.match_record_layout, matchRecordList);
        this.matchRecordList = matchRecordList;
        Collections.reverse(this.matchRecordList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolderItem;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.match_record_layout, parent, false);
            //Setup ViewHolder
            viewHolderItem = new ViewHolderItem();

            viewHolderItem.homePlayerName = (TextView) convertView.findViewById(R.id.record_home_player_name_label);
            viewHolderItem.homeFirstSetScore = (TextView) convertView.findViewById(R.id.record_home_first_set_score);
            viewHolderItem.homeSecondSetScore = (TextView) convertView.findViewById(R.id.record_home_second_set_score);
            viewHolderItem.homeThirdSetScore = (TextView) convertView.findViewById(R.id.record_home_third_set_score);

            viewHolderItem.awayPlayerName = (TextView) convertView.findViewById(R.id.record_away_player_name_label);
            viewHolderItem.awayFirstSetScore = (TextView) convertView.findViewById(R.id.record_away_first_set_score);
            viewHolderItem.awaySecondSetScore = (TextView) convertView.findViewById(R.id.record_away_second_set_score);
            viewHolderItem.awayThirdSetScore = (TextView) convertView.findViewById(R.id.record_away_third_set_score);

            viewHolderItem.matchDate = (TextView) convertView.findViewById(R.id.record_match_date);
            viewHolderItem.matchBeginTime = (TextView) convertView.findViewById(R.id.record_match_begin_time);
            viewHolderItem.matchEndTime = (TextView) convertView.findViewById(R.id.record_match_end_time);

            //Set tag so it can be referenced later
            convertView.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        viewHolderItem.homePlayerName.setText(matchRecordList.get(position).getHomePlayerName());
        viewHolderItem.homeFirstSetScore.setText(matchRecordList.get(position).getHomePlayerFirstSetScore());
        viewHolderItem.homeSecondSetScore.setText(matchRecordList.get(position).getHomePlayerSecondSetScore());
        viewHolderItem.homeThirdSetScore.setText(matchRecordList.get(position).getHomePlayerThirdSetScore());

        viewHolderItem.awayPlayerName.setText(matchRecordList.get(position).getAwayPlayerName());
        viewHolderItem.awayFirstSetScore.setText(matchRecordList.get(position).getAwayPlayerFirstSetScore());
        viewHolderItem.awaySecondSetScore.setText(matchRecordList.get(position).getAwayPlayerSecondSetScore());
        viewHolderItem.awayThirdSetScore.setText(matchRecordList.get(position).getAwayPlayerThirdSetScore());

        viewHolderItem.matchDate.setText(matchRecordList.get(position).getMatchDate());
        viewHolderItem.matchBeginTime.setText(matchRecordList.get(position).getMatchBeginTime());
        viewHolderItem.matchEndTime.setText(matchRecordList.get(position).getMatchEndTime());

        return convertView;
    }

    static class ViewHolderItem {
        TextView homePlayerName;
        TextView homeFirstSetScore;
        TextView homeSecondSetScore;
        TextView homeThirdSetScore;
        TextView awayPlayerName;
        TextView awayFirstSetScore;
        TextView awaySecondSetScore;
        TextView awayThirdSetScore;
        TextView matchDate;
        TextView matchBeginTime;
        TextView matchEndTime;
    }
}
