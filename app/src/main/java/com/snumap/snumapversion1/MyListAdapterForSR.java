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
 * Created by rukeon01 on 2015-08-29.
 */
public class MyListAdapterForSR extends BaseAdapter {
    Context maincon;
    LayoutInflater inflater;
    ArrayList<MyItemForListViewSR> arSrc;
    int layout;

    public MyListAdapterForSR(Context context, int alayout, ArrayList<MyItemForListViewSR> aarSrc) {
        maincon = context;
        arSrc = aarSrc;
        layout = alayout;
        // ListView에서 사용한 View를 정의한 xml 를 읽어오기 위해
        // LayoutInfalater 객체를 생성
        inflater = LayoutInflater.from(maincon);
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

    // 각 항목의 뷰 생성
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        ImageView imgWalk = (ImageView) convertView.findViewById(R.id.walkMan);
        imgWalk.setImageResource(arSrc.get(position).iconWalk);

        TextView txtFrom = (TextView) convertView.findViewById(R.id.txtFrom);
        txtFrom.setText(arSrc.get(position).search.getFrom());

        TextView txtTo = (TextView) convertView.findViewById(R.id.txtTo);
        txtTo.setText(arSrc.get(position).search.getTo());

        ImageView imgArrow = (ImageView) convertView.findViewById(R.id.arrow);
        imgArrow.setImageResource(arSrc.get(position).iconArrow);

        final SearchRouteActivity searchRouteActivity = (SearchRouteActivity) maincon;

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
//        customImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (customImg.isActivated())
//                {
//                    --num;
//                    if (num == 0) {
//                        favActivity.deleteTxt.setText("삭제");
//                    } else {
//                        favActivity.deleteTxt.setText("삭제 " + String.valueOf(num));
//                    }
//                    beDelete.remove(new Integer(pos));
//
//                    for (int i: beDelete)
//                    {
//                        Log.e("현재 beDelete의 상태:  ", String.valueOf(i));
//                    }
//
//                } else {
//                    ++num;
//                    favActivity.deleteTxt.setText("삭제 " + String.valueOf(num));
//                    beDelete.add(new Integer(pos));
//
//                    for (int i: beDelete)
//                    {
//                        Log.e("현재 beDelete의 상태:  ", String.valueOf(i));
//                    }
//                }
//                Log.e("선택된 것의 숫자:  ", String.valueOf(num));
////                String str = arSrc.get(pos).name + "를 주문합니다.";
//                customImg.setActivated(!customImg.isActivated());
//            }
//        });
        return convertView;
    }
}
