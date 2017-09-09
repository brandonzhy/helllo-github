package com.stone.app.Game.GameRecord;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stone.app.R;

import java.util.List;

/**
 * Created by Brandon Zhang on 2017/9/7.
 */

public class GameRecordAdapter extends ArrayAdapter<GameItem> {
    private int resourceID;
    private ViewHolder viewHolder;
    private View view;
    public GameRecordAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<GameItem> objects) {

        super(context, resource, objects);
        this.resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GameItem gameItem = getItem(resourceID);
        if(convertView==null){
             view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.gamedate = view.findViewById(R.id.tv_date);
            viewHolder.gameResult = view.findViewById(R.id.tv_result);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        gameItem.setDate(gameItem.getDate());
        gameItem.setResult(gameItem.getResult());

        return view;

    }

    class ViewHolder {
        TextView gamedate;
        TextView gameResult;
    }
}