package org.colobreaker;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for any game screens.
 */
public class Screen {

    protected int backGroundColor;
    protected boolean isActive;
    protected int screenWidth;
    protected int screenHeight;
    protected int activeElement;
    protected List<UIElement> elements;
    protected ScreenControllerComm commUp;
    protected UIElement.AnimationInterface animationInterface;
    public interface ScreenControllerComm {
        void sendMessageScreenController (int messageCode);

    }

    public Screen(int W, int H, ScreenControllerComm comm, UIElement.AnimationInterface ai){
        this.isActive = false;
        this.screenWidth = W;
        this.screenHeight = H;
        this.activeElement = -1;
        this.elements = new ArrayList<>();
        this.backGroundColor = Color.BLACK;
        this.commUp = comm;
        this.animationInterface = ai;
    }

    public void setActive(boolean active){
        this.isActive = active;
    }

    public int fingerDown(int x, int y){
        this.activeElement = -1;
        if (!this.isActive) return UIElement.TOUCH_CODE_NO_ACTION;

        for (int i = 0; i < this.elements.size(); i++){
            int code = this.elements.get(i).fingerDown(x,y);
            if (code != UIElement.TOUCH_CODE_NO_ACTION) {
                this.activeElement = i;
                return code;
            }
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    public int fingerUp(int x, int y){
        this.activeElement = -1;
        if (!this.isActive) return UIElement.TOUCH_CODE_NO_ACTION;

        for (int i = 0; i < this.elements.size(); i++){
            int code = this.elements.get(i).fingerUp(x,y);
            if (code != UIElement.TOUCH_CODE_NO_ACTION) {
                this.activeElement = i;
                return code;
            }
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    public int fingerMove(int x, int y){
        this.activeElement = -1;
        if (!this.isActive) return UIElement.TOUCH_CODE_NO_ACTION;

        for (int i = 0; i < this.elements.size(); i++){
            int code = this.elements.get(i).fingerMove(x,y);
            if (code != UIElement.TOUCH_CODE_NO_ACTION) {
                this.activeElement = i;
                return code;
            }
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    public void notifyAnimationStop(int animationCode){
    }


    public void render(Canvas canvas){

        // We render the background color first.
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.backGroundColor);
        canvas.drawPaint(paint);

        for (UIElement ui: this.elements){
            ui.render(canvas);
        }
    }

}
