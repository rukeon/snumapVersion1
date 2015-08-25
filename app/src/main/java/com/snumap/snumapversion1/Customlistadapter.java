package com.snumap.snumapversion1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rukeon01 on 2015-07-29.
 */
public class Customlistadapter extends BaseAdapter {
    private Context context;
    private ArrayList<SlidingListData> mSlidingListData = new ArrayList<>();

    public Customlistadapter(Context context){
        super();
        this.context = context;
    }

    public ArrayList<SlidingListData> getmSlidingListData() {
        return mSlidingListData;
    }

    @Override
    public int getCount() {
        return mSlidingListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mSlidingListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem (int icon, String activity) {
        SlidingListData addInfo = new SlidingListData(icon, activity);
        mSlidingListData.add(addInfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row = inflater.inflate(R.layout.list_for_drawer_row, null,
                true);
        TextView textView = (TextView) single_row.findViewById(R.id.listtxtView);
        ImageView imageView = (ImageView) single_row.findViewById(R.id.listImgView);

        SlidingListData data = mSlidingListData.get(position);

        textView.setText(data.getmActivity());
        imageView.setImageResource(data.getmIcon());
        return single_row;
    }
}
