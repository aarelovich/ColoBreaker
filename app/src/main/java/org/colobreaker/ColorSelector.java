package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class ColorSelector extends UIElement {

    private List<Marble> colorsToSelect;
    private int currentlySelectedColor;
    private boolean enabled;

    public ColorSelector(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);
        this.colorsToSelect = new ArrayList<>();
        this.currentlySelectedColor = -1;
        this.enabled = true;
    }

    public void setMarbleDimension(float dimension, int nColors){

        if (nColors >= 8) nColors = 8;
        else if (nColors < 6) nColors = 6;

        float vMargin = this.boundingBox.height()*0.05f;
        float x = this.boundingBox.left + (this.boundingBox.width() - dimension)/2.0f;
        float n = nColors;
        float verticalSeparation = ((this.boundingBox.height()-2*vMargin) - n*dimension)/(n-1);

        float y = this.boundingBox.top + vMargin;

        this.colorsToSelect.clear();

        for (int i = 0; i < nColors; i++){
            Marble marble = new Marble(x,y,dimension,dimension,null);
            marble.setColorCode(i);
            this.colorsToSelect.add(marble);
            y = y + dimension + verticalSeparation;
        }

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        // Changing the enabling should reset the selection.
        this.currentlySelectedColor = -1;
    }

    public int getCurrentlySelectedColor(){
        if (this.currentlySelectedColor < 0) return -1;
        if (this.currentlySelectedColor >= this.colorsToSelect.size()) return -1;
        int color = this.colorsToSelect.get(this.currentlySelectedColor).getColorCode();
        // These two lines below deselect the color after one use. Better to just let the user
        // Deselect it by selecting another color.
        // this.colorsToSelect.get(this.currentlySelectedColor).setSelected(false);
        // this.currentlySelectedColor = -1;
        return color;
    }

    @Override
    public int fingerDown(int x, int y) {

        if (!this.enabled) return UIElement.TOUCH_CODE_NO_ACTION;

        for (int i = 0; i < this.colorsToSelect.size(); i++){
            int code = this.colorsToSelect.get(i).fingerDown(x,y);
            if (code == Utils.TOUCH_CODE_MARBLE_SELECTED) {

                //System.err.println("SELECTED MARBLE: " + this.colorsToSelect.get(i).name);

                // If the currently selected color is not -1, another color has been selected.
                if (this.currentlySelectedColor != -1) {
                    //this.colorsToSelect.get(this.currentlySelectedColor)System.err.println("DE SELECTING MARBLE: " + this.colorsToSelect.get(this.currentlySelectedColor).name);
                    this.colorsToSelect.get(this.currentlySelectedColor).setSelected(false);
                }

                this.colorsToSelect.get(i).setSelected(true);
                this.currentlySelectedColor = i;
                return code;
            }
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerUp(int x, int y) {
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerMove(int x, int y) {
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public void render(Canvas canvas) {

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Utils.COLOR_PRIMARY_100);
        p.setStrokeWidth(Utils.GetUniversalTraceWidth());
        float R = Utils.GetUniversalCornerRadius();
        canvas.drawRoundRect(this.boundingBox,R,R,p);

        for (Marble m: this.colorsToSelect){
            m.render(canvas);
        }
    }
}

