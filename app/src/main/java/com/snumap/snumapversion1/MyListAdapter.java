package com.snumap.snumapversion1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// 어댑터 클래스
class MyListAdapter extends BaseAdapter {
    Context maincon;
    LayoutInflater inflater;
    ArrayList<MyItemForListView> arSrc;
    int layout;
    int num;
    ArrayList<Integer> beDelete;

    public MyListAdapter(Context context, int alayout, ArrayList<MyItemForListView> aarSrc) {
        maincon = context;
        arSrc = aarSrc;
        layout = alayout;
        // ListView에서 사용한 View를 정의한 xml 를 읽어오기 위해
        // LayoutInfalater 객체를 생성
        inflater = LayoutInflater.from(maincon);
        num = 0;
        beDelete = new ArrayList<Integer>();
    }

    public int getCount() {
        return arSrc.size();
    }

    public Object getItem(int position) {
        return arSrc.get(position).name;
    }

    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Integer> getBeDelete() {
        return beDelete;
    }

    public void setBeDelete(ArrayList<Integer> beDelete) {
        this.beDelete = beDelete;
    }

    // 각 항목의 뷰 생성
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        final ImageView customImg = (ImageView) convertView.findViewById(R.id.customImg);;

        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        img.setImageResource(arSrc.get(position).iconPin);

        TextView txt = (TextView) convertView.findViewById(R.id.text);
        txt.setText(arSrc.get(position).name.getName());

        ImageView img2 = (ImageView) convertView.findViewById(R.id.btn);
        img2.setImageResource(arSrc.get(position).iconArrow);

        final FavoriteActivity favActivity = (FavoriteActivity) maincon;

        img2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String str = arSrc.get(pos).name + "를 주문합니다.";
//                Toast.makeText(maincon, str, Toast.LENGTH_SHORT).show();
//                customImg.setBackgroundResource(R.drawable.select);


            }
        });

//        customImg.setImageResource(R.drawable.select);
        customImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (customImg.isActivated())
                {
                    --num;
                    if (num == 0) {
                        favActivity.deleteTxt.setText("삭제");
                    } else {
                        favActivity.deleteTxt.setText("삭제 " + String.valueOf(num));
                    }
                    beDelete.remove(new Integer(pos));

                    for (int i: beDelete)
                    {
                        Log.e("현재 beDelete의 상태:  ", String.valueOf(i));
                    }

                } else {
                    ++num;
                    favActivity.deleteTxt.setText("삭제 " + String.valueOf(num));
                    beDelete.add(new Integer(pos));

                    for (int i: beDelete)
                    {
                        Log.e("현재 beDelete의 상태:  ", String.valueOf(i));
                    }
                }
                Log.e("선택된 것의 숫자:  ", String.valueOf(num));
//                String str = arSrc.get(pos).name + "를 주문합니다.";
                customImg.setActivated(!customImg.isActivated());
            }
        });

        return convertView;
    }
}