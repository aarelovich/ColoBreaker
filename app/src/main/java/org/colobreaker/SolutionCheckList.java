package org.colobreaker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class SolutionCheckList extends UIElement {

    private List<ColorResults> listOfResults;
    private float blockHeight;
    private List<Integer> code;

    public static final int SOLUTION_CODE_NONE = 0;
    public static final int SOLUTION_CODE_WIN  = 1;
    public static final int SOLUTION_CODE_LOSS = 2;

    public SolutionCheckList(float x, float y, float w, float h, AnimationInterface ai) {
        super(x, y, w, h, ai);
        this.listOfResults = new ArrayList<>();
        this.blockHeight = h/Utils.MAX_ATTEMPTS;
        this.code = new ArrayList<>();
    }

    public void setCode(List<Integer> code){
        this.code = code;
        this.listOfResults.clear();
    }

    public int checkSolution(List<Integer> solution, RectF boundingBoxCorrespondingRow){

        int correct = 0;
        int only_color = 0;
        List<Integer> colorOnly = new ArrayList<>();
        List<Integer> colorSolution = new ArrayList<>();

        for (int i = 0; i < solution.size(); i++){
            if (solution.get(i) == this.code.get(i)){
                correct++;
            }
            else {
                colorOnly.add(this.code.get(i));
                colorSolution.add(solution.get(i));
            }
        }

        for (int i = 0; i < colorSolution.size(); i++){
            int index = colorOnly.indexOf(colorSolution.get(i));
            if (index != -1){
                colorOnly.remove(index);
                only_color++;
            }
        }


        System.err.println("Comparing Code: " + this.code + ". To Solution: " + solution + ". Result: W: " + only_color + ". B: " + correct);

        float hmargin = this.boundingBox.width()*0.04f;
        ColorResults cr = new ColorResults(this.boundingBox.left + hmargin,boundingBoxCorrespondingRow.top,this.boundingBox.width()-2*hmargin,this.blockHeight,null);
        cr.setEvaluation(only_color,correct);

        this.listOfResults.add(cr);

        if (correct == this.code.size()){
            return SOLUTION_CODE_WIN;
        }
        else if (this.listOfResults.size() == Utils.MAX_ATTEMPTS){
            return SOLUTION_CODE_LOSS;
        }
        return SOLUTION_CODE_NONE;
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

        for (int i = 0; i < this.listOfResults.size(); i++){
            this.listOfResults.get(i).render(canvas);
        }


    }
}
