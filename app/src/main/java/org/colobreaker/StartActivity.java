package org.colobreaker;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

public class StartActivity extends Activity {

    ScreenController screenController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // I use the device resolution for everything, so I get it first.
        // Then I use it for the game screen.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // We need to do this first.
        Utils.InitColorList();
        Utils.InitGlobalDimension(width,height);
        Utils.LoadTypeFace(getAssets());

        screenController = new ScreenController(this,width,height);
        setContentView(screenController);

    }

}
