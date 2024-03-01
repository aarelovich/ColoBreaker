package org.colobreaker;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends Screen {

    private Board board;
    private MarbleRow playedSolution;
    private ColorSelector colorSelector;
    private CheckSolutionButton upArrowButton;
    private SolutionDisplay solution;
    private SolutionCheckList solutionChecks;
    private NewGameButton newGameButton;
    private SettingsButton settings;
    private boolean autoSolveOnce;

    public GameScreen(int W, int H, Context context, ScreenControllerComm comm, UIElement.AnimationInterface ai) {
        super(W, H, context ,comm, ai);

        this.autoSolveOnce = false;

        float resultBorderWidth = 0.2f*W;
        float colorSelectionWidth = 0.15f*W;
        float boardW = W - resultBorderWidth - colorSelectionWidth;
        float topMargin = 0.08f*H;
        float bottomMargin = 0.02f*H;
        float boardH = 0.8f*H;

        this.backGroundColor = Utils.COLOR_BG_300;

        // Making the board.
        this.board = new Board(resultBorderWidth,topMargin,boardW,boardH,this.animationInterface);
        this.elements.add(this.board);
        float rowH    = this.board.getRowHeight();

        // Making where the user puts the solution.
        this.playedSolution = new MarbleRow(resultBorderWidth,H - rowH - bottomMargin,boardW,rowH,this.animationInterface);
        this.playedSolution.setSlotsSelectable(true);
        this.elements.add(this.playedSolution);

        // Making the color selector
        float hMargin = colorSelectionWidth*0.05f;
        this.colorSelector = new ColorSelector((W - colorSelectionWidth) + hMargin,topMargin,colorSelectionWidth - 2*hMargin,0.6f*H,this.animationInterface);
        this.colorSelector.setEnabled(false);
        this.elements.add(this.colorSelector);

        // Making the check solution button.
        float buttonW = colorSelectionWidth*0.8f;
        float btnX = W - colorSelectionWidth + (colorSelectionWidth - buttonW)/2.0f;
        this.upArrowButton = new CheckSolutionButton(btnX,H - rowH - bottomMargin,buttonW,rowH,this.animationInterface);
        this.upArrowButton.setVisible(false);
        this.elements.add(this.upArrowButton);

        // Making the solution check.s
        this.solutionChecks = new SolutionCheckList(0,topMargin,resultBorderWidth,boardH,null);
        this.elements.add(this.solutionChecks);

        // Making the new game button take the same place as the check button.
        this.newGameButton = new NewGameButton(this.upArrowButton.getBoundingBox().left,this.upArrowButton.getBoundingBox().top,this.upArrowButton.getBoundingBox().width(), this.upArrowButton.getBoundingBox().height(),null);
        this.elements.add(this.newGameButton);

        // Making the settings button
        float hSettings = this.playedSolution.getBoundingBox().height();
        float wSettings = resultBorderWidth*0.7f;
        float xSettings = (resultBorderWidth - wSettings)/2.0f;
        float ySettings = this.playedSolution.getBoundingBox().top;
        this.settings = new SettingsButton(xSettings,ySettings,wSettings,hSettings,null);
        this.elements.add(this.settings);

        // Making the solution display. This should always be at the end.
        float hBanner = H*0.15f;
        hBanner = topMargin;
        this.solution = new SolutionDisplay(0,0,W,hBanner,this.animationInterface);
        this.solution.hideMessage();
        this.elements.add(this.solution);

        // this.newGame();

    }

    public void newGame(){
        int ncolors  = Preferences.GetAsInt(this.context,Preferences.KEY_COLORS,6);
        int codeSize = Preferences.GetAsInt(this.context,Preferences.KEY_CODE_SIZE,4);

        this.board.clearBoard();
        this.playedSolution.setRowSize(codeSize);
        this.colorSelector.setMarbleDimension(this.playedSolution.getMarbleDiameter(),ncolors);
        this.colorSelector.setEnabled(true);
        this.newGameButton.setVisible(false);

        List<Integer> code = Utils.GenerateCode(codeSize,ncolors);
        System.err.println("NEW GAME: " + code);

        this.solution.setCode(code);
        this.solution.hideMessage();
        this.solutionChecks.setCode(code);

        // Instantly solving it. Used for debugging.
        if (!this.autoSolveOnce){
            this.autoSolveOnce = true;
            //this.board.setNewRow(code,this.playedSolution.getBoundingBox().top);
        }


        this.commUp.sendMessageScreenController(Utils.SCREEN_REFRESH);
    }

    public int fingerDown(int x, int y){
        int code = super.fingerDown(x,y);
        if (code == Utils.TOUCH_CODE_SLOT_SELECTED){
            // We only do this if there is a color selected
            int color = this.colorSelector.getCurrentlySelectedColor();
            if (color != -1){
                this.playedSolution.setColorCodeToSelectedSlot(color);
                this.commUp.sendMessageScreenController(Utils.SCREEN_COM_PLAY_PLOP);
                if (this.playedSolution.isRowComplete()){
                    this.upArrowButton.setVisible(true);
                }
            }
        }
        return code;
    }

    public void notifyAnimationStop(int animationCode){
        if (animationCode == Utils.ANIMATION_SLIDE_ATTEMPT){
            // We need to now display the results.
            int winLoseCode = this.solutionChecks.checkSolution(this.board.getLastAttempt(),this.board.getLastRowRect());
            if (winLoseCode == SolutionCheckList.SOLUTION_CODE_WIN){
                this.commUp.sendMessageScreenController(Utils.SCREEN_COM_PLAY_GOT_IT);
                this.solution.showMessage(Utils.MESSAGE_SUCCESS);
                this.colorSelector.setEnabled(false);
                this.newGameButton.setVisible(true);
            }
            else if (winLoseCode == SolutionCheckList.SOLUTION_CODE_LOSS) {
                this.solution.showMessage(Utils.MESSAGE_FAILURE);
                this.colorSelector.setEnabled(false);
                this.newGameButton.setVisible(true);
            }
        }
    }


    public int fingerUp(int x, int y){
        int code = super.fingerUp(x,y);
        if (code == Utils.TOUCH_CODE_CHECK_BUTTON){
            // This was pressed so we set the new color row.
            this.board.setNewRow(this.playedSolution.getColorCodeList(),this.playedSolution.getBoundingBox().top);
            this.playedSolution.clearColorsFromRow();
            this.upArrowButton.setVisible(false);
            this.commUp.sendMessageScreenController(Utils.SCREEN_COM_PLAY_CHECK);
        }
        else if (code == Utils.TOUCH_CODE_NEW_GAME){
            this.newGame();
        }
        else if (code == Utils.TOUCH_CODE_SETTINGS){
            this.commUp.sendMessageScreenController(Utils.SCREEN_CHANGE);
        }
        return code;
    }

    public int fingerMove(int x, int y){
        int code = super.fingerMove(x,y);
        // TODO check what to do foreach element that is active.
        return code;
    }

}
