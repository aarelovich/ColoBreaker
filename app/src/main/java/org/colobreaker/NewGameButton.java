package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Paint;

public class NewGameButton extends UIElement {

    private boolean pressed;
    private float cornerR;

    public NewGameButton(float x, float y, float w, float h, UIElement.AnimationInterface ai) {
        super(x, y, w, h, ai);

        this.pressed = false;
        this.cornerR = Utils.GetUniversalCornerRadius();

    }

    @Override
    public int fingerDown(int x, int y) {
        if (!this.visible) return UIElement.TOUCH_CODE_NO_ACTION;
        if (this.isCoordinateInElement(x,y)) {
            this.pressed = true;
            return Utils.TOUCH_CODE_NEW_GAME;
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerUp(int x, int y) {
        this.pressed = false;
        if (!this.visible) return UIElement.TOUCH_CODE_NO_ACTION;
        if (this.isCoordinateInElement(x,y)) return Utils.TOUCH_CODE_NEW_GAME;
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerMove(int x, int y) {
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public void render(Canvas canvas) {

        if (!this.visible) return;

        Paint p = new Paint();

        int plusColor = Utils.COLOR_ACCENT_100;
        int btnColor   = Utils.COLOR_ACCENT_200;

        if (this.pressed){
            plusColor = Utils.COLOR_PRIMARY_200;
            btnColor   = Utils.COLOR_PRIMARY_100;
        }

        // Background
        p.setStyle(Paint.Style.FILL);
        p.setColor(btnColor);
        canvas.drawRoundRect(this.boundingBox,this.cornerR,this.cornerR,p);

        // The vertical line.
        p.setStyle(Paint.Style.STROKE);
        p.setColor(plusColor);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(this.boundingBox.width()*0.15f);
        float l  = this.boundingBox.height()*0.6f;
        float x1 = this.boundingBox.centerX();
        float y1 = this.boundingBox.centerY() - l/2;
        float y2 = this.boundingBox.centerY() + l/2;
        canvas.drawLine(x1,y1,x1,y2,p);

        // The horizontal line.
        l  = this.boundingBox.width()*0.6f;
        x1 = this.boundingBox.centerX() - l/2;
        float x2 = this.boundingBox.centerX() + l/2;
        y1 = this.boundingBox.centerY();
        canvas.drawLine(x1,y1,x2,y1,p);

        //canvas.drawRoundRect(this.verticalLine,);

    }


}
