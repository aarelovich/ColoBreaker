package org.colobreaker;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class MarbleRow extends UIElement{

    private List<Marble> marbles;
    private float borderWidthHorizontal;
    private float borderWidthVertical;
    private float innerSquareDim;
    private float outerSquareWidth;
    private float marbleD;
    private boolean marbleDropable;
    private boolean slotsSelectable;
    private int selectedSlot;
    private boolean renderBackground;

    public MarbleRow(float x, float y, float w, float h, AnimationInterface ai) {
        super(x,y,w,h,ai);
        this.marbles = new ArrayList<>();
        this.marbleDropable = false;
        this.slotsSelectable = false;
        this.selectedSlot = -1;
        this.renderBackground = true;
    }

    public void move(float dx, float dy){
        super.move(dx,dy);
        for (Marble m: this.marbles){
            m.move(dx,dy);
        }
    }

    public void setRenderBackground(boolean enable){
        this.renderBackground = enable;
    }

    public void setPosition(float x, float y){
        super.setPosition(x,y);
        // When we do this we need to recreate the marbles so. I need the color list
        List<Integer> colors = this.getColorCodeList();
        this.setRowSize(colors.size());
        for (int i = 0; i < colors.size(); i++){
            this.setColorCodeToSlot(i,colors.get(i));
        }
    }

    public void setMarblesDropable(boolean enable){
        this.marbleDropable = enable;
    }

    public void setSlotsSelectable(boolean selectable){
        this.slotsSelectable = selectable;
    }

    public void setMarbleColorCode(int index, int colorCode){
        if (index < 0) return;
        if (index >= this.marbles.size()) return;
        this.marbles.get(index).setColorCode(colorCode);
    }

    public void clearColorsFromRow(){
        for (int i = 0; i < this.marbles.size(); i++){
            this.marbles.get(i).clearColor();
        }
    }

    public void setColorCodeToSelectedSlot(int code){
        if (this.selectedSlot == -1) return;
        this.marbles.get(this.selectedSlot).setColorCode(code);
        this.selectedSlot = -1;
    }

    @Override
    public int fingerDown(int x, int y) {

        if (!this.slotsSelectable) return UIElement.TOUCH_CODE_NO_ACTION;

        for (int i = 0; i < this.marbles.size(); i++){
            int code = this.marbles.get(i).fingerDown(x,y);
            if (code != UIElement.TOUCH_CODE_NO_ACTION){
                this.selectedSlot = i;
                return Utils.TOUCH_CODE_SLOT_SELECTED;
            }
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    public boolean isRowComplete(){
        for (Marble m: this.marbles){
            if (m.getColorCode() == -1) return false;
        }
        return true;
    }

    public List<Integer> getColorCodeList(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < this.marbles.size(); i++){
            list.add(this.marbles.get(i).getColorCode());
        }
        return list;
    }

    @Override
    public int fingerUp(int x, int y) {
        if (!this.marbleDropable) return UIElement.TOUCH_CODE_NO_ACTION;
        for (int i = 0; i < this.marbles.size(); i++){
            if (this.marbles.get(i).isCoordinateInElement(x,y)){
                // A finger was lifter inside here. We just notify of it to the top.
                return Utils.TOUCH_CODE_MARBLE_DROP;
            }
        }
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    @Override
    public int fingerMove(int x, int y) {
        return UIElement.TOUCH_CODE_NO_ACTION;
    }

    public void setRowSize(int rowSize) {

        // Recomputing the rendering variables based on the new row size.
        float innerH = 0.9f*this.boundingBox.height();
        this.outerSquareWidth = this.boundingBox.width()/rowSize;

        float innerW = 0.9f*this.outerSquareWidth;

        this.innerSquareDim = Math.min(innerW,innerH);

        this.borderWidthHorizontal = (this.outerSquareWidth - this.innerSquareDim)/2;
        this.borderWidthVertical   = (this.boundingBox.height() - this.innerSquareDim)/2;

        this.marbleD = 0.9f*innerSquareDim;
        float r = this.marbleD/2;

        this.marbles.clear();

        for (int i = 0; i < rowSize; i++){

            float x = (i*this.outerSquareWidth + this.boundingBox.left) + this.outerSquareWidth/2 - r;
            float y = this.boundingBox.centerY() - r;

            Marble m = new Marble(x,y,this.marbleD,this.marbleD,null);
            m.setVisible(false);
            this.marbles.add(m);
        }

    }

    public float getMarbleDiameter() {
        return this.marbleD;
    }

    public void setColorCodeToSlot(int index, int code){
        if (index < 0) return;
        if (index >= this.marbles.size()) return;
        this.marbles.get(index).setColorCode(code);
    }

    public void render(Canvas canvas) {

        if (this.renderBackground){
            Paint p = new Paint();

            p.setStyle(Paint.Style.FILL);
            p.setColor(Utils.COLOR_BG_200);
            float R = Utils.GetUniversalCornerRadius();
            canvas.drawRoundRect(this.boundingBox,R,R,p);

            p.setStyle(Paint.Style.STROKE);
            p.setColor(Utils.COLOR_PRIMARY_100);
            p.setStrokeWidth(Utils.GetUniversalTraceWidth());
            canvas.drawRoundRect(this.boundingBox,R,R,p);

            for (int i = 0; i < this.marbles.size(); i++){

                float left = (float)(i)*this.outerSquareWidth + this.boundingBox.left;
                float right;

                // The inner square.
                p.setStyle(Paint.Style.FILL);
                p.setColor(Utils.COLOR_BG_100);
                left = left + this.borderWidthHorizontal;
                right = left + this.innerSquareDim;
                canvas.drawRect(left,this.boundingBox.top + this.borderWidthVertical,right,this.boundingBox.bottom - this.borderWidthVertical,p);

            }
        }


        // Then I render the marbles.
        for (Marble m: this.marbles){
            m.render(canvas);
        }

    }
}
