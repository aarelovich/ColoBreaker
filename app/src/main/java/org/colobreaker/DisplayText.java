package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class DisplayText extends UIElement {
    private String text;
    private float textSize;
    public DisplayText(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);
        this.text = "";
        this.textSize = 0;
    }

    public void setText(String text){
        this.text = text;
    }

    public void forceTextSize(float size){
        this.textSize = size;
    }

    public float getSizeFor(String testText){
        Paint p = new Paint();
        p.setTextAlign(Paint.Align.LEFT);
        p.setTypeface(this.getTypeface(true));
        p.setColor(Utils.COLOR_BG_100);
        return this.getTextSizeToFit(testText,p,this.boundingBox)*0.8f;
    }

    @Override
    public int fingerDown(int x, int y) {
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
        p.setTextAlign(Paint.Align.LEFT);
        p.setColor(Utils.COLOR_BG_100);
        p.setStyle(Paint.Style.FILL);
        p.setTypeface(this.getTypeface(true));

        if (this.textSize >= 0) p.setTextSize(this.textSize);
        else p.setTextSize(this.getTextSizeToFit(text,p,this.boundingBox)*0.8f);

        float bline = this.getTextBaseLine(text,p,this.boundingBox);
        canvas.drawText(text,this.boundingBox.left,bline,p);

        // Outlining the text
        p.setColor(Utils.COLOR_PRIMARY_100);
        p.setStrokeWidth(Utils.GetUniversalTraceWidth());
        p.setStyle(Paint.Style.STROKE);
        canvas.drawText(text,this.boundingBox.left,bline,p);

    }
}
