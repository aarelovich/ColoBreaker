package org.colobreaker;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class SettingsScreen extends Screen {

    private MultiPurposeButton colorsAvailable;
    private MultiPurposeButton codeSize;
    private MultiPurposeButton soundState;

    public SettingsScreen(int W, int H, Context context ,ScreenControllerComm comm, UIElement.AnimationInterface ai) {
        super(W, H, context, comm, ai);

        float rMargin = 0.1f*W;
        float btnSide = Math.min(0.25f*W,0.2f*H);
        float tMargin = (H - 3*btnSide)/4;

        this.backGroundColor = Utils.COLOR_BG_300;

        float x = W - rMargin - btnSide;
        float y = tMargin/2.0f;
        this.colorsAvailable = new MultiPurposeButton(x,y,btnSide,btnSide,null);
        List<String> options = new ArrayList<>();
        options.add("6");options.add("7");options.add("8");
        this.colorsAvailable.setOptions(options);
        this.elements.add(this.colorsAvailable);

        y = y + tMargin + btnSide;
        this.codeSize = new MultiPurposeButton(x,y,btnSide,btnSide,null);
        options = new ArrayList<>();
        options.add("4");options.add("5");options.add("6");
        this.codeSize.setOptions(options);
        this.elements.add(this.codeSize);

        y = y + tMargin + btnSide;
        this.soundState = new MultiPurposeButton(x,y,btnSide,btnSide,null);
        options = new ArrayList<>();
        options.add("OFF"); options.add("ON");
        this.soundState.setOptions(options);
        this.elements.add(this.soundState);

        float textW = codeSize.getBoundingBox().left - rMargin;
        float textY = tMargin/2;
        float textH = btnSide;

        DisplayText dp = new DisplayText(rMargin,textY,textW,textH,null);
        dp.setUseOfSystemFont(false);
        float textSize = dp.getSizeFor("CODE SIZE");
        dp.setText("COLORS");
        dp.forceTextSize(textSize);
        this.elements.add(dp);

        textY = textY + tMargin + btnSide;
        dp = new DisplayText(rMargin,textY,textW,textH,null);
        dp.setText("CODE SIZE");
        dp.forceTextSize(textSize);
        dp.setUseOfSystemFont(false);
        this.elements.add(dp);

        textY = textY + tMargin + btnSide;
        dp = new DisplayText(rMargin,textY,textW,textH,null);
        dp.setText("SOUND");
        dp.forceTextSize(textSize);
        dp.setUseOfSystemFont(false);
        this.elements.add(dp);

        float btnH = tMargin*0.5f;
        float btnW = W*0.5f;
        float marginBottom = H*0.01f;
        float xSaveBtn = (W - btnW)/2;
        MultiPurposeButton mpb = new MultiPurposeButton(xSaveBtn,H-btnH - marginBottom,btnW,btnH,null);
        options = new ArrayList<>();
        options.add("SAVE");
        mpb.setUseOfSystemFont(false);
        mpb.setOptions(options);
        mpb.setBehaviourAsATextButton(Utils.TOUCH_CODE_SAVE_SETTINGS);
        this.elements.add(mpb);

    }

    public void setActive(boolean active){
        super.setActive(active);
        if (active){
            loadSettings();
        }
    }

    private void loadSettings(){
        this.soundState.setSelectedOption(Preferences.GetAsInt(this.context,Preferences.KEY_SOUND,0));
        this.codeSize.setOptionByValue(Integer.toString(Preferences.GetAsInt(this.context,Preferences.KEY_CODE_SIZE,4)));
        this.colorsAvailable.setOptionByValue(Integer.toString(Preferences.GetAsInt(this.context,Preferences.KEY_COLORS,6)));
    }

    public int fingerUp(int x, int y){
        int code = super.fingerUp(x,y);
        if (code == Utils.TOUCH_CODE_SAVE_SETTINGS){
            System.err.println("Saving Settings");
            // We save
            Preferences.SaveAsInt(this.context,Preferences.KEY_CODE_SIZE,Integer.parseInt(codeSize.getCurrentOptionText()));
            Preferences.SaveAsInt(this.context,Preferences.KEY_SOUND,soundState.getCurrentSelectedInt());
            Preferences.SaveAsInt(this.context,Preferences.KEY_COLORS,Integer.parseInt(colorsAvailable.getCurrentOptionText()));
            // And go back.
            this.commUp.sendMessageScreenController(Utils.SCREEN_CHANGE_SOUND);
            this.commUp.sendMessageScreenController(Utils.SCREEN_CHANGE);
        }
        return code;
    }


}
