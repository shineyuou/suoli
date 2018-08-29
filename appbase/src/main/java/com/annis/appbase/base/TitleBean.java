package com.annis.appbase.base;

import android.view.View;

public class TitleBean {
    private String title;
    private String leftTitle;
    private boolean back;
    private View rightView;
    private int rightViewId;
    private int back_color;
    private int titleColor;

    public TitleBean(String title) {
        this.title = title;
    }

    public TitleBean(String title, int back_color) {
        this.title = title;
        this.back_color = back_color;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public TitleBean setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TitleBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isBack() {
        return back;
    }

    public TitleBean setBack(boolean back) {
        this.back = back;
        return this;
    }

    public View getRightView() {
        return rightView;
    }

    public TitleBean setRightView(View rightView) {
        this.rightView = rightView;
        return this;
    }

    public int getBackColor() {
        return back_color;
    }

    public TitleBean setBackColor(int back_color) {
        this.back_color = back_color;
        return this;
    }

    public String getLeftTitle() {
        return leftTitle;
    }

    public TitleBean setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
        return this;
    }

    public int getRightViewId() {
        return rightViewId;
    }

    public TitleBean setRightViewId(int rightViewId) {
        this.rightViewId = rightViewId;
        return this;
    }
}
