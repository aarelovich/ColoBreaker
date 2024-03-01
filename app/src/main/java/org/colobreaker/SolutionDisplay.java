package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

public class SolutionDisplay extends UIElement {

    private List<Marble> code;
    private List<Integer> codeColors;
    private RectF innerFrame;
    private RectF textFitter;
    private String message;
    private static final int ANIMATION_DURATION = 800;
    private int animationSteps;
    private float deltaYAnimation;

    public SolutionDisplay(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);
        this.code = new ArrayList<>();
        this.innerFrame = new RectF(0,0,0,0);
        this.textFitter = new RectF(0,0,0,0);
        this.codeColors = new ArrayList<>();
        this.animationSteps = -2;
        this.message = "";
    }

    public void setCode(List<Integer> colorCodes){
        this.codeColors = colorCodes;
        // Now we hide it.
        this.move(0,-this.boundingBox.height());
        this.renderComputations();
    }

    private void renderComputations(){

        // First we need to computer the inner rect.
        float xc = this.boundingBox.centerX();
        float yc = this.boundingBox.centerY();

        float margin = this.boundingBox.height()*0.05f;

        float w  = this.boundingBox.width() - margin*2;
        float h  = this.boundingBox.height() - margin*2;
        this.innerFrame.set(xc-w/2,yc-h/2,xc+w/2,yc+h/2);

        // The marbles go in the lower half. And occupy only part of the width.
        float hMargin = this.innerFrame.width()*0.05f;
        float bottomMargin = this.innerFrame.height()*0.05f;
        float k = 0.5f; // We compute the horizontal diameter, so that the space between marbles is half the diameter of the marbles.
        float n = this.codeColors.size();
        float diameter = (this.innerFrame.width() - 2*hMargin)/(n + k*(n-1));
        float rowH = this.innerFrame.height()/2 - bottomMargin;

        diameter = Math.min(rowH,diameter);

        // Now we need to recompute the spacing between the marbles.
        float spaceBetweenMarbles = ((this.innerFrame.width() - 2*hMargin) - n*diameter)/(n-1);
        float x = this.innerFrame.left + hMargin;
        //float y = this.innerFrame.top  + this.innerFrame.height()/2;
        float y = this.innerFrame.bottom - diameter - margin;

        this.code.clear();
        for (int i = 0; i < this.codeColors.size(); i++){
            //System.err.println("Setting marble for code to : " + x + " " + y + " " + diameter);
            Marble m = new Marble(x,y,diameter,diameter,null);
            x = x + spaceBetweenMarbles + diameter;
            m.setColorCode(this.codeColors.get(i));
            this.code.add(m);
        }

        // The rect where the text should fit.
        w = this.innerFrame.width()*0.95f;
        h = this.innerFrame.height()*0.95f/2;
        float top = this.innerFrame.top + margin;
        float left = this.innerFrame.centerX() - w/2;
        this.textFitter.set(left,top,left+w,top+h);

    }

    public void showMessage(String text){
        this.message = text;

        // This should start the animation.
        this.animationSteps = ANIMATION_DURATION/Utils.ANIMATION_TICK_LENGTH;

        // This animation dY;
        this.deltaYAnimation = this.boundingBox.height()/this.animationSteps;

        // And we start the animation.
        this.animationInterface.startAnimation(Utils.ANIMATION_SHOW_SOLUTION);
    }

    public void hideMessage(){
        this.setPosition(this.boundingBox.left,-this.boundingBox.height());
        this.renderComputations();
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

        if (animationSteps >= 0){
            this.move(0,this.deltaYAnimation);
            this.renderComputations();
            this.animationSteps--;
        }
        else if (animationSteps == -1){
            this.animationSteps--;
            this.setPosition(this.boundingBox.left,0);
            this.renderComputations();
            this.animationInterface.stopAnimation(Utils.ANIMATION_SHOW_SOLUTION);
        }

        // Rendering the background.
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Utils.COLOR_BG_100);
        float R = this.boundingBox.height()*0.1f;
        canvas.drawRoundRect(this.boundingBox,R,R,p);

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(this.boundingBox.width()*0.01f);
        p.setColor(Utils.COLOR_PRIMARY_200);

        canvas.drawRoundRect(this.innerFrame,R,R,p);

        for (Marble m: this.code){
            m.render(canvas);
        }

        // Now we render the text.
        p.setStyle(Paint.Style.FILL);
        p.setColor(Utils.COLOR_ACCENT_100);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(this.getTextSizeToFit(this.message,p,this.textFitter)*0.8f);
        p.setTypeface(Typeface.create("",Typeface.BOLD));
        float bline = this.getTextBaseLine(this.message,p,this.textFitter);
        canvas.drawText(this.message,this.textFitter.centerX(),bline,p);

    }
}
