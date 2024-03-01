package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

public class MultiPurposeButton extends UIElement {

    private boolean pressed;
    private List<Path> glyphs;
    private List<String> displayText;
    private int currentOption;
    private RectF innerFrame;
    private int signal;

    public MultiPurposeButton(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);
        this.pressed = false;
        this.currentOption = -1;
        this.displayText = new ArrayList<>();
        this.glyphs = new ArrayList<>();

        float iw = w*0.9f;
        float ih = h*0.9f;
        float ix = x + (w - iw)/2;
        float iy = y + (h - ih)/2;
        this.innerFrame = new RectF(ix,iy,ix+iw,iy+ih);

        this.signal = -1;
    }

    public void setBehaviourAsATextButton(int signal){
        this.signal = signal;
    }

    public String getCurrentOptionText(){
        if (this.currentOption < 0 ) return "";
        if (this.currentOption >= this.displayText.size()) return "";
        return this.displayText.get(this.currentOption);
    }

    public void setOptions(List<String> options){
       this.displayText = options;
       this.glyphs.clear();
       this.currentOption = 0;
    }

    public void setSelectedOption(int index){
        if (index < 0) return;
        if (this.glyphs.size() > 0){
            if (index >= this.glyphs.size()) return;
            this.currentOption = index;
        }
        else {
            if (index >= this.displayText.size()) return;
            this.currentOption = index;
        }
    }

    public void setOptionByValue(String value){
        int index = this.displayText.indexOf(value);
        if (index != -1){
            this.currentOption = index;
        }
    }

    public void setGlyphs(List<Path> glyphs){
        this.glyphs = glyphs;
        this.displayText.clear();
        this.currentOption = 0;
    }


    public int getCurrentSelectedInt(){
        return this.currentOption;
    }

    @Override
    public int fingerDown(int x, int y) {
        if (this.isCoordinateInElement(x,y)){
            this.pressed = true;
            return Utils.TOUCH_CODE_SWITCH_BUTTON;
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerUp(int x, int y) {

        if (!this.pressed) return UIElement.TOUCH_CODE_NO_ACTION;
        this.pressed = false;

        if (this.isCoordinateInElement(x,y)){

            if (this.signal == -1) {
                this.currentOption++;
                if (this.glyphs.size() > 0){
                    if (this.currentOption ==  this.glyphs.size()){
                        this.currentOption = 0;
                    }
                }
                else {
                    if (this.currentOption ==  this.displayText.size()){
                        this.currentOption = 0;
                    }
                }
                return Utils.TOUCH_CODE_SWITCH_BUTTON;
            }
            else {
                // The push button behaviour requires to return a signal now.
                return signal;
            }

        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerMove(int x, int y) {
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public void render(Canvas canvas) {

        int colorBKG = Utils.COLOR_PRIMARY_100;
        int colorGlyph = Utils.COLOR_PRIMARY_300;
        if (this.pressed){
            colorBKG = Utils.COLOR_PRIMARY_200;
            colorGlyph = Utils.COLOR_ACCENT_200;
        }

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(colorBKG);
        float R = Utils.GetUniversalCornerRadius();
        canvas.drawRoundRect(this.boundingBox,R,R,p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(colorGlyph);
        p.setStrokeWidth(Utils.GetUniversalTraceWidth());
        canvas.drawRoundRect(this.innerFrame,R,R,p);

        p.setStyle(Paint.Style.FILL);

        if (this.currentOption == -1) return; // Nothing to render.

        // Now what we render depends on whether or not there are glyphs defined.
        if (glyphs.size() > 0){
            p.setColor(colorGlyph);
            canvas.drawPath(this.glyphs.get(this.currentOption),p);
        }
        else {

            String text = this.displayText.get(this.currentOption);

            p.setTextAlign(Paint.Align.CENTER);
            p.setColor(colorGlyph);
            p.setTypeface(this.getTypeface(true));
            p.setTextSize(this.getTextSizeToFit(text,p,this.boundingBox)*0.8f);
            float bline = this.getTextBaseLine(text,p,this.boundingBox);
            canvas.drawText(text,this.boundingBox.centerX(),bline,p);

        }

    }
}
