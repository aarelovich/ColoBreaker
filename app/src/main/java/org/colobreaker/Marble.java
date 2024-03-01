package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

import javax.xml.xpath.XPath;

public class Marble extends UIElement {

    private float D;
    private float R;
    private RadialGradient aura;
    private float RAura;
    private boolean isMoving;
    private boolean isMarvelMovable;
    private int marbleColor;
    private int marbleColorCode;
    private boolean isSelected;
    public static final float AURA_R_MULT = 0.5f;
    private String name;

    public Marble(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);
        this.D = Math.min(w,h);
        this.R = this.D/2.0f;
        this.marbleColor = -1;
        this.marbleColorCode = -1;
        this.isMoving = false;
        this.isMarvelMovable = false;
        this.visible = false;

        this.name = "";

        this.RAura = this.R*AURA_R_MULT;
        this.aura = new RadialGradient(this.boundingBox.centerX(), this.boundingBox.centerY(), this.R,
                new int[]{Utils.COLOR_PRIMARY_200, Color.TRANSPARENT},
                new float[]{0.8f, 1.0f},
                Shader.TileMode.CLAMP);

    }

    public void clearColor(){
        this.setColorCode(-1);
    }

    public void setColorCode(int code){
        if (code == -1){
            this.visible = false;
        }
        else {
            this.visible = true;
            this.marbleColor = Utils.GetColorFromCode(code);
        }
        this.marbleColorCode = code;
    }

    public int getColorCode(){
        return this.marbleColorCode;
    }

    public void setSelected(boolean selected){
        this.isSelected = selected;
    }

    @Override
    public int fingerDown(int x, int y) {
        if (this.isMarvelMovable){
            if (isMoving) return UIElement.TOUCH_CODE_NO_ACTION;
            if (!this.isCoordinateInElement(x,y)) return UIElement.TOUCH_CODE_NO_ACTION;
            this.isMoving = true;
            return Utils.TOUCH_CODE_MARBLE_MOVE;
        }
        else {
            // This is a selectable marble. so finger down selects it.
            if (this.isCoordinateInElement(x,y)) return Utils.TOUCH_CODE_MARBLE_SELECTED;
            return UIElement.TOUCH_CODE_NO_ACTION;
        }
    }

    @Override
    public int fingerUp(int x, int y) {
        this.isMoving = false;
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerMove(int x, int y) {
        if (!isMoving) return UIElement.TOUCH_CODE_NO_ACTION;
        // The position to ser must be the center of the marble so, we substract the diameter
        x = x - (int)this.R;
        y = y - (int)this.R;
        this.setPosition(x,y);
        return Utils.TOUCH_CODE_MARBLE_MOVE;
    }

    @Override
    public void render(Canvas canvas) {

        if (!visible) return;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        float rToUse = this.R;

        if (this.isSelected){
            // Initialize Paint for the aura
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(this.aura);
            canvas.drawCircle(this.boundingBox.centerX(),this.boundingBox.centerY(),this.R,paint);
            rToUse =  this.R*0.7f;
            //System.err.println("SELECTED MARBLE " + this.name + ": " + this.boundingBox + ". Drawn R: " + rToUse);
        }
        else {
            //System.err.println("REGULAR MARBLE " + this.name + ": " + this.boundingBox + ". Drawn R: " + rToUse);
        }

        // Create a radial gradient for the sphere's shading
        float light = rToUse*0.2f;
        RadialGradient radialGradient = new RadialGradient(this.boundingBox.centerX()+light, this.boundingBox.centerY()-light, rToUse, Color.WHITE, marbleColor, Shader.TileMode.CLAMP);

        paint.setColor(this.marbleColor);
        paint.setShader(radialGradient);
        canvas.drawCircle(this.boundingBox.centerX(),this.boundingBox.centerY(),rToUse,paint);

    }
}
