package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class ColorResults extends UIElement {

    private List<Integer> positionColors;
    private float largeD;
    private float smallD;
    private float verticalSpace;
    private float horizontalSpace;
    private List<PointF> slotCenters;

    private final int CODE_BLACK = 0;
    private final int CODE_WHITE = 1;
    private final int CODE_NONE  = 2;


    public ColorResults(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);
        this.positionColors = new ArrayList<>();
        this.slotCenters = new ArrayList<>();

        // Computing this requires two iterations. First. We set vertical and horizontal spaces.
        this.verticalSpace = h*0.05f;
        this.horizontalSpace = w*0.1f;

        float DdueToWidth = (w - 3*this.verticalSpace)/2;
        float DdueToHeight = (h - 4*this.verticalSpace)/3;

        this.largeD = Math.min(DdueToHeight,DdueToWidth);
        float R = this.largeD/2;
        this.smallD = this.largeD*0.8f;
        this.verticalSpace = (h - 3*this.largeD)/4.0f;
        this.horizontalSpace = (w - 2*this.largeD)/3.0f;

        float xc = x + this.horizontalSpace + R;
        float yc = this.verticalSpace + R + y;
        slotCenters.add(new PointF(xc,yc));
        yc = yc + this.verticalSpace + this.largeD;
        slotCenters.add(new PointF(xc,yc));
        yc = yc + this.verticalSpace + this.largeD;
        slotCenters.add(new PointF(xc,yc));

        xc = xc + this.horizontalSpace + this.largeD;
        yc = this.verticalSpace + R + y;
        slotCenters.add(new PointF(xc,yc));
        yc = yc + this.verticalSpace + this.largeD;
        slotCenters.add(new PointF(xc,yc));
        yc = yc + this.verticalSpace + this.largeD;
        slotCenters.add(new PointF(xc,yc));

    }

    public void setEvaluation(int correct_colors, int correct_colors_in_correct_places){
        positionColors.clear();
        int rem = Utils.LARGEST_CODE - correct_colors - correct_colors_in_correct_places;
        List<Integer> baseList = new ArrayList<>();

        for (int i = 0; i < correct_colors_in_correct_places; i++){
            baseList.add(CODE_BLACK);
        }

        for (int i = 0; i < correct_colors; i++){
            baseList.add(CODE_WHITE);
        }

        for (int i = 0; i < rem; i++){
            baseList.add(CODE_NONE);
        }

        // System.err.println("Original Color List. " + baseList + ". Randomized: " + this.positionColors);

        // Now we randomize it.
        this.positionColors.clear();
        while (baseList.size() > 0){
            int index = (int)(Math.random()*baseList.size());
            this.positionColors.add(baseList.remove(index));
        }

        // System.err.println("Original Color List. " + baseList + ". Randomized: " + this.positionColors);

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
        p.setColor(Utils.COLOR_PRIMARY_100);
        p.setStyle(Paint.Style.STROKE);
        float R = Utils.GetUniversalCornerRadius();
        p.setStrokeWidth(Utils.GetUniversalTraceWidth());
        canvas.drawRoundRect(this.boundingBox,R,R,p);

        p.setStyle(Paint.Style.FILL);
        for (int i = 0; i < this.slotCenters.size(); i++){
            p.setColor(Utils.COLOR_BG_100);
            PointF center = this.slotCenters.get(i);
            int color = this.positionColors.get(i);
            canvas.drawCircle(center.x,center.y,this.largeD/2,p);
            if (color == CODE_BLACK){
                p.setColor(Color.BLACK);
                canvas.drawCircle(center.x,center.y,this.smallD/2,p);
            }
            else if (color == CODE_WHITE) {
                p.setColor(Color.WHITE);
                canvas.drawCircle(center.x,center.y,this.smallD/2,p);
            }

        }

    }
}
