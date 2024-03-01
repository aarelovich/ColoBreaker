package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class CheckSolutionButton extends UIElement {

    private boolean pressed;
    private Path arrowPath;
    private float cornerR;
    private float diameter;

    public CheckSolutionButton(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);

        this.pressed = false;
        this.cornerR = Utils.GetUniversalCornerRadius();
        this.diameter = Math.min(w,h);

        float marginV = 0.1f*h;
        float indentBottom = 0.7f*h;
        float arrowH = h - 2*marginV;
        float arrowW = Math.min(0.9f*w,1.5f*arrowH);
        float leftSideX = this.boundingBox.centerX() - arrowW/2.0f;

        this.arrowPath = new Path();
        this.arrowPath.moveTo(this.boundingBox.centerX(),y + marginV);
        this.arrowPath.lineTo(leftSideX,y+h-marginV);
        this.arrowPath.lineTo(this.boundingBox.centerX(),y+indentBottom);
        this.arrowPath.lineTo(leftSideX+arrowW,y+h-marginV);
        this.arrowPath.close();

    }

    @Override
    public int fingerDown(int x, int y) {
        if (!this.visible) return UIElement.TOUCH_CODE_NO_ACTION;
        if (this.isCoordinateInElement(x,y)) {
            this.pressed = true;
            return Utils.TOUCH_CODE_CHECK_BUTTON;
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerUp(int x, int y) {
        this.pressed = false;
        if (!this.visible) return UIElement.TOUCH_CODE_NO_ACTION;
        if (this.isCoordinateInElement(x,y)) return Utils.TOUCH_CODE_CHECK_BUTTON;
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

        int arrowColor = Utils.COLOR_ACCENT_100;
        int btnColor   = Utils.COLOR_ACCENT_200;

        if (this.pressed){
            arrowColor = Utils.COLOR_PRIMARY_200;
            btnColor   = Utils.COLOR_PRIMARY_100;
        }

        // Background
        p.setStyle(Paint.Style.FILL);
        p.setColor(btnColor);
        canvas.drawRoundRect(this.boundingBox,this.cornerR,this.cornerR,p);
        //canvas.drawCircle(this.boundingBox.centerX(),this.boundingBox.centerY(),this.diameter/2,p);

        // The up arrow.
        p.setColor(arrowColor);
        canvas.drawPath(this.arrowPath,p);

    }
}
