package com.snumap.snumapversion1;

// 리스트 뷰에 출력할 항목
public class MyItemForListView {
    int iconPin;
    User name;
    int iconArrow;

    MyItemForListView(int icon1, User name, int icon2) {
        this.iconPin = icon1;
        this.name = name;
        this.iconArrow = icon2;
    }
}
