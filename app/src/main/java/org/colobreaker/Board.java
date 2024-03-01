package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class Board extends UIElement {

    private List<MarbleRow> rows;
    private float rowHeight;
    private int animationSteps;
    private float finalYAnimationPosition;
    private float deltaYAnimation;
    private static final int SLIDE_ANIMATION_LENGTH = 600; // ms

    public Board(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);
        this.rows = new ArrayList<>();
        this.rowHeight = (int)(this.boundingBox.height()/Utils.MAX_ATTEMPTS);
        this.animationSteps = -2;
    }

    public void clearBoard(){
        this.rows.clear();
    }

    public float getRowHeight(){
        return this.rowHeight;
    }

    public List<Integer> getLastAttempt(){
        if (this.rows.size() < 1) return new ArrayList<>();
        return this.rows.get(this.rows.size()-1).getColorCodeList();
    }

    public RectF getLastRowRect(){
        if (this.rows.size() < 1) return new RectF(0,0,0,0);
        return this.rows.get(this.rows.size()-1).getBoundingBox();
    }
    public void setNewRow(List<Integer> colors, float starting_y_position){

        if (this.rows.size() >= Utils.MAX_ATTEMPTS) return;

        this.finalYAnimationPosition = this.rows.size()*(int)this.rowHeight + (int)this.boundingBox.top;
        MarbleRow mr = new MarbleRow(this.boundingBox.left,starting_y_position,this.boundingBox.width(),this.rowHeight,null);
        mr.setRowSize(colors.size());
        mr.setRenderBackground(false);

        // Setting the colors.
        for (int i = 0; i < colors.size(); i++){
            mr.setColorCodeToSlot(i,colors.get(i));
        }

        this.rows.add(mr);

        // Now we computer the number of animation steps, based on the animation duration.
        this.animationSteps = SLIDE_ANIMATION_LENGTH/Utils.ANIMATION_TICK_LENGTH;

        // How much to displace in each step.
        this.deltaYAnimation = (this.finalYAnimationPosition - starting_y_position)/(this.animationSteps);

        // And we start the animation.
        this.animationInterface.startAnimation(Utils.ANIMATION_SLIDE_ATTEMPT);

    }

    public float getMarbleDiameter(){
        MarbleRow mr = new MarbleRow(this.boundingBox.left,this.boundingBox.top,this.boundingBox.width(),this.boundingBox.height(),null);
        return mr.getMarbleDiameter();
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

        float R = Utils.GetUniversalCornerRadius();

        Paint p = new Paint();
        p.setColor(Utils.COLOR_BG_200);
        canvas.drawRoundRect(this.boundingBox,R,R,p);

        p.setColor(Utils.COLOR_PRIMARY_100);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(Utils.GetUniversalTraceWidth());
        canvas.drawRoundRect(this.boundingBox,R,R,p);


        if (this.animationSteps >= 0){
            this.animationSteps--;
            // And we displace the last row.
            int last = this.rows.size()-1;
            this.rows.get(last).move(0,this.deltaYAnimation);
        }

        else if (this.animationSteps == -1){
            // We stop the animation and set the final value
            this.animationSteps--;
            int last = this.rows.size()-1;
            float x = this.rows.get(last).getBoundingBox().left;
            this.rows.get(last).setPosition(x,this.finalYAnimationPosition);

            // And here we stop the animation.
            this.animationInterface.stopAnimation(Utils.ANIMATION_SLIDE_ATTEMPT);

        }

        for (int i = 0; i < this.rows.size(); i++){
            this.rows.get(i).render(canvas);
        }

    }
}
