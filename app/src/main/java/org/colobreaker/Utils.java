package org.colobreaker;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final int COLOR_PRIMARY_100          = Color.parseColor("#B33A3A");
    public static final int COLOR_PRIMARY_200          = Color.parseColor("#ea6a64");
    public static final int COLOR_PRIMARY_300          = Color.parseColor("#ffcec3");
    public static final int COLOR_ACCENT_100           = Color.parseColor("#F2BAC9");
    public static final int COLOR_ACCENT_200           = Color.parseColor("#8e5c6a");
    public static final int COLOR_BG_100               = Color.parseColor("#4A4E4D");
    public static final int COLOR_BG_200               = Color.parseColor("#5b5f5e");
    public static final int COLOR_BG_300               = Color.parseColor("#767a79");

    public static final int MAX_ATTEMPTS               = 10;
    public static final int LARGEST_CODE               = 6;

    public static final int TOUCH_CODE_MARBLE_DROP     = 100;
    public static final int TOUCH_CODE_MARBLE_MOVE     = 101;
    public static final int TOUCH_CODE_MARBLE_SELECTED = 102;
    public static final int TOUCH_CODE_SLOT_SELECTED   = 103;
    public static final int TOUCH_CODE_CHECK_BUTTON    = 104;
    public static final int TOUCH_CODE_NEW_GAME        = 105;
    public static final int TOUCH_CODE_SETTINGS        = 106;
    public static final int TOUCH_CODE_SWITCH_BUTTON   = 107;
    public static final int TOUCH_CODE_SAVE_SETTINGS   = 108;

    public static final int ANIMATION_TICK_LENGTH= 20;
    public static final int ANIMATION_SLIDE_ATTEMPT = 200;
    public static final int ANIMATION_SHOW_SOLUTION = 201;

    public static final int SCREEN_COM_PLAY_PLOP   = 300;
    public static final int SCREEN_COM_PLAY_GOT_IT = 301;
    public static final int SCREEN_COM_PLAY_CHECK  = 302;
    public static final int SCREEN_REFRESH         = 303;
    public static final int SCREEN_CHANGE_SOUND    = 304;
    public static final int SCREEN_CHANGE          = 305;

    public static final String MESSAGE_SUCCESS = "CONGRATULATIONS!";
    public static final String MESSAGE_FAILURE = "Bummer. Sorry, better luck next time";

    private static List<Integer> COLOR_CODES;
    private static float UNIVERSAL_BORDER_HIGHLIGHT_WIDTH;
    private static float UNIVERSAL_CORNER_R;
    private static Typeface SETTINGS_FONT;

    public static void InitColorList(){
        COLOR_CODES = new ArrayList<>();
        COLOR_CODES.add(Color.parseColor("#800000"));
        COLOR_CODES.add(Color.parseColor("#000075"));
        COLOR_CODES.add(Color.parseColor("#ffe119"));
        COLOR_CODES.add(Color.parseColor("#3cb44b"));
        COLOR_CODES.add(Color.parseColor("#42d4f4"));
        COLOR_CODES.add(Color.parseColor("#f58231"));
        COLOR_CODES.add(Color.parseColor("#911eb4"));
        COLOR_CODES.add(Color.parseColor("#fabed4"));
    }

    public static void InitGlobalDimension(float W, float H){
        UNIVERSAL_BORDER_HIGHLIGHT_WIDTH = W*0.003f;
        UNIVERSAL_CORNER_R = W*0.01f;
    }

    public static void LoadTypeFace(AssetManager asm) {
        SETTINGS_FONT = Typeface.createFromAsset(asm,"coolfont.otf");
    }

    public static Typeface GetFont() {
       return SETTINGS_FONT;
    }

    public static float GetUniversalTraceWidth(){
        return UNIVERSAL_BORDER_HIGHLIGHT_WIDTH;
    }

    public static float GetUniversalCornerRadius(){
        return UNIVERSAL_CORNER_R;
    }

    public static int GetColorFromCode(int code){
        if (code < 0) return -1;
        if (code >= COLOR_CODES.size()) return -1;
        return COLOR_CODES.get(code);
    }

    public static int GetCodeForColor(int color){
        return COLOR_CODES.indexOf(color);
    }

    public static List<Integer> GenerateCode(int codeSize, int numberOfColors){
        List<Integer> code = new ArrayList<>();
        for (int i = 0; i < codeSize; i++){
            int index = (int)Math.floor(Math.random()*numberOfColors);
            code.add(index);
        }
        return code;
    }

}
