package com.snumap.snumapversion1;

/**
 * Created by rukeon01 on 2015-08-29.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by rukeon01 on 2015-08-29.
 */
public class MyListAdapterForDR extends BaseAdapter {
    Context maincon;
    LayoutInflater inflater;
    ArrayList<MyItemForListViewDR> arSrc;
    int layout;
    int num;
    ArrayList<Integer> beDelete;

    public MyListAdapterForDR(Context context, int alayout, ArrayList<MyItemForListViewDR> aarSrc) {
        maincon = context;
        arSrc = aarSrc;
        layout = alayout;
        inflater = LayoutInflater.from(maincon);
        num = 0;
        beDelete = new ArrayList<Integer>();
    }

    public int getCount() {
        return arSrc.size();
    }

    public Object getItem(int position) {
        return arSrc.get(position).search;
    }

    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Integer> getBeDelete() {
        return beDelete;
    }

    public void setNum(int num){
        this.num = num;
    }

    public void setBeDelete(ArrayList<Integer> beDelete)
    {
        this.beDelete = beDelete;
    }

    // 각 항목의 뷰 생성
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView txtFrom = (TextView) convertView.findViewById(R.id.txtFromDR);
        txtFrom.setText(arSrc.get(position).search.getFrom());

        TextView txtTo = (TextView) convertView.findViewById(R.id.txtToDR);
        txtTo.setText(arSrc.get(position).search.getTo());

        ImageView imgArrow = (ImageView) convertView.findViewById(R.id.arrowDR);
        imgArrow.setImageResource(arSrc.get(position).iconArrow);

        ImageView button = (ImageView) convertView.findViewById(R.id.deleteBtn);
        button.setImageResource(arSrc.get(position).iconButton);

        final ImageView deleteBtn = (ImageView) convertView.findViewById(R.id.deleteBtn);

        final DeleteRecentSearchActivity deleteRouteActivity = (DeleteRecentSearchActivity) maincon;

//        img2.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                String str = arSrc.get(pos).name + "를 주문합니다.";
////                Toast.makeText(maincon, str, Toast.LENGTH_SHORT).show();
////                customImg.setBackgroundResource(R.drawable.select);
//
//
//            }
//        });
//
////        customImg.setImageResource(R.drawable.select);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (deleteBtn.isActivated()) {
                    --num;
                    if (num == 0) {
                        deleteRouteActivity.deleteTxtDR.setText("삭제");
                    } else {
                        deleteRouteActivity.deleteTxtDR.setText("삭제 " + String.valueOf(num));
                    }
                    beDelete.remove(new Integer(pos));

                    for (int i : beDelete) {
                        Log.e("현재 beDelete의 상태true:  ", String.valueOf(i));
                    }

                } else {
                    ++num;
                    deleteRouteActivity.deleteTxtDR.setText("삭제 " + String.valueOf(num));
                    beDelete.add(new Integer(pos));

                    for (int i : beDelete) {
                        Log.e("현재 beDelete의 상태false:  ", String.valueOf(i));
                    }
                }
                deleteBtn.setActivated(!deleteBtn.isActivated());
            }
        });

        return convertView;
    }
}
