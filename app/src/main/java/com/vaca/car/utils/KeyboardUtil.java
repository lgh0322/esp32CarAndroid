package com.vaca.car.utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.vaca.car.MainApplication;
import com.vaca.car.R;

public class KeyboardUtil {
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private Keyboard k3;// 符号键盘
    private boolean isNum = false;// 是否数据键盘
    private boolean isUpper = false;// 是否大写
    private boolean isSymbol = false;// 是否符号

    private static final int SYMBOL_CODE = -7;//符号键盘
    private static final int ELLIPSES_CODE = -8;//省略号
    private static final int CHINESE_HORIZONTAL_LINE_CODE = -9;//中文横线
    private static final int SMILING_FACE_CODE = -10;//笑脸
    private static final int LEFT_CODE = -11;//中文横线
    private static final int RIGHT_CODE = -12;//中文横线
    private static final int HEE_CODE = -13;//哈哈
    private static final int AWKWARD_CODE = -14;//尴尬

    private ConstraintLayout rootView;
    private EditText ed;


    private KeyboardUtil(Context activity, EditText edit,ConstraintLayout yes) {
        this.ed = edit;
        k1 = new Keyboard(activity, R.xml.letter);
        k2 = new Keyboard(activity, R.xml.number);
        k3 = new Keyboard(activity, R.xml.symbol);


        keyboardView = new KeyboardView(activity, null);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);

        rootView = yes;

    }

    OnKeyboardActionListener onKeyboardActionListener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                isUpper = !isUpper;
                k1.setShifted(isUpper);
                keyboardView.invalidateAllKeys();
            } else if (primaryCode == SYMBOL_CODE) {// 符号键盘
                if (isSymbol) {
                    isSymbol = false;
                    keyboardView.setKeyboard(k2);
                } else {
                    isSymbol = true;
                    keyboardView.setKeyboard(k3);
                }
            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                if (isNum) {
                    isNum = false;
                    keyboardView.setKeyboard(k1);
                } else {
                    isNum = true;
                    keyboardView.setKeyboard(k2);
                }
            } else if (primaryCode == LEFT_CODE) { //向左
                if (start > 0) {
                    ed.setSelection(start - 1);
                }
            } else if (primaryCode == RIGHT_CODE) { // 向右
                if (start < ed.length()) {
                    ed.setSelection(start + 1);
                }
            } else if (primaryCode == ELLIPSES_CODE) { //省略号
                editable.insert(start, "...");
            } else if (primaryCode == CHINESE_HORIZONTAL_LINE_CODE) {
                editable.insert(start, "——");
            } else if (primaryCode == SMILING_FACE_CODE) {
                editable.insert(start, "^_^");
            } else if (primaryCode == HEE_CODE) {
                editable.insert(start, "^o^");
            } else if (primaryCode == AWKWARD_CODE) {
                editable.insert(start, ">_<");
            } else {
                String str = Character.toString((char) primaryCode);
                if (isWord(str)) {
                    if (isUpper) {
                        str = str.toUpperCase();
                    } else {
                        str = str.toLowerCase();
                    }
                }
                editable.insert(start, str);

            }
        }
    };

    private boolean isShow = false;

    private int dpUtils(int yes){
        return (int) new TypedValue().applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, yes, MainApplication.application.getResources().getDisplayMetrics()
        );
    }

    public void showKeyboard() {
        if (!isShow) {
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);

            layoutParams.endToEnd=R.id.view_group;
            layoutParams.topToTop=R.id.view_group;
            layoutParams.setMarginEnd(dpUtils(50));
            layoutParams.bottomToBottom=R.id.view_group;
            rootView.addView(keyboardView, layoutParams);
            isShow = true;
        }
    }

    private void hideKeyboard() {
        if (rootView != null && keyboardView != null && isShow) {
            isShow = false;
            rootView.removeView(keyboardView);
        }
        mInstance = null;
    }

    private boolean isWord(String str) {
        return str.matches("[a-zA-Z]");
    }

    private static KeyboardUtil mInstance;

    public static KeyboardUtil shared(Context activity, EditText edit, ConstraintLayout yes) {
        if (mInstance == null) {
            mInstance = new KeyboardUtil(activity, edit,yes);
        }
        mInstance.ed = edit;
        return mInstance;
    }

}
