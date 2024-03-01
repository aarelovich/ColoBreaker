package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public abstract class UIElement {

    public interface AnimationInterface {
        public void startAnimation(int code);
        public void stopAnimation(int code);
    }

    public static final int TOUCH_CODE_NO_ACTION = -1;

    public static int COLOR_TRANSPARENT = Color.parseColor("#00000000");

    protected RectF boundingBox;
    protected AnimationInterface animationInterface;
    protected boolean visible;
    protected boolean useSystemFont;

    public UIElement(float x, float y, float w, float h, AnimationInterface ai){
        this.boundingBox = new RectF(x,y,x + w, y+h);
        this.animationInterface = ai;
        this.visible = true;
        this.useSystemFont = true;
    }

    public void setUseOfSystemFont(boolean enable) {
        this.useSystemFont = enable;
    }

    protected Typeface getTypeface(boolean bold){
        if (this.useSystemFont){
            if (bold) return Typeface.create("",Typeface.BOLD);
            else return Typeface.create("",Typeface.NORMAL);
        }
        else {
            return Utils.GetFont();
        }
    }

    public boolean isCoordinateInElement(int x, int y){
        return this.boundingBox.contains(x,y);
    }

    public void setVisible(boolean v){
        this.visible = v;
    }

    public RectF getBoundingBox() {
        return this.boundingBox;
    }

    public PointF getCenter(){
        return new PointF(this.boundingBox.centerX(),this.boundingBox.centerY());
    }

    public float getTextSizeToFit(String text,  Paint paint, RectF textFitter) {

        if (text == null) text = "A";

        // Start with an initial guess for text size
        float textSize = 100f;

        // Set the initial text size
        paint.setTextSize(textSize);

        // Get the bounds of the text
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textWidth = paint.measureText(text);
        float textHeight = fm.descent - fm.ascent;

        // Calculate the scaling factors for width and height
        float widthScale = textFitter.width() / textWidth;
        float heightScale = textFitter.height() / textHeight;

        // Use the smaller of the two scales to fit the text inside the rectangle
        float scale = Math.min(widthScale, heightScale);

        // Set the text size based on the scale
        textSize *= scale;

        return textSize;
    }

    public float getTextBaseLine(String text, Paint p, RectF textFitter) {
        Rect textBounds = new Rect();
        p.getTextBounds(text, 0, text.length(), textBounds);
        float textHeight = textBounds.height();
        return textFitter.centerY() + textHeight/2f;
    }

    public void setPosition(float x, float y){
        float w = this.boundingBox.width();
        float h = this.boundingBox.height();
        this.boundingBox.top = y;
        this.boundingBox.left = x;
        this.boundingBox.right = x + w;
        this.boundingBox.bottom = y + h;

    }

    public void move(float dx, float dy){
        this.boundingBox.top = this.boundingBox.top + dy;
        this.boundingBox.left = this.boundingBox.left + dx;
        this.boundingBox.bottom = this.boundingBox.bottom + dy;
        this.boundingBox.right = this.boundingBox.right + dx;
    }

    public void scaleFromCenter(float k){

        // We do the scaling.
        float w = this.boundingBox.width();
        float h = this.boundingBox.height();
        w = w * k;
        h = h * k;

        // WE compute the new top left.
        float top = this.boundingBox.centerY() - h/2.0f;
        float left = this.boundingBox.centerX() - w/2.0f;

        // Set the new boudning box.
        this.boundingBox.set(new RectF(left,top,left+w,top+h));

    }

    public abstract int fingerDown(int x, int y);
    public abstract int fingerUp(int x, int y);
    public abstract int fingerMove(int x, int y);
    public abstract void render(Canvas canvas);

}
