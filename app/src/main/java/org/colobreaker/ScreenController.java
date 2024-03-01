package org.colobreaker;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenController extends View implements UIElement.AnimationInterface, Screen.ScreenControllerComm {

    private List<Screen> screens;
    private int activeScreen;
    private List<Integer> animationRequests;
    private Timer animationTimer;
    private MediaPlayer soundSolved;
    private MediaPlayer soundPlop;
    private MediaPlayer soundSolutionCheck;
    private boolean soundON;

    TimerTask timerTaskObj = new TimerTask() {
        public void run() {
            if (!animationRequests.isEmpty()){
                invalidate();
            }
        }
    };

    public ScreenController(Context context, int width, int height) {
        super (context);
        this.screens = new ArrayList<>();
        this.animationRequests = new ArrayList<>();

        // Setting up the animation timer
        this.animationTimer = new Timer();
        animationTimer.schedule(timerTaskObj, 0, Utils.ANIMATION_TICK_LENGTH);

        // The sound player.
        this.soundSolved = this.loadMediaPlayerFile("got_it.wav");
        this.soundSolutionCheck   = this.loadMediaPlayerFile("plop.mp3");
        this.soundPlop = this.loadMediaPlayerFile("check_solution.wav");

        // Adding the game screen.
        GameScreen gs = new GameScreen(width,height,this,this);
        this.screens.add(gs);

        this.switchScreen(0);

        this.soundON = false;
    }

    private void switchScreen(int index){
        if ((index >= 0) && (index < this.screens.size())){
            this.activeScreen = index;

            for (Screen screen: this.screens){
                screen.setActive(false);
            }

            this.screens.get(index).setActive(true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.activeScreen < 0) return;
        this.screens.get(this.activeScreen).render(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // We need to check tha at least one screen is set.
        if (this.activeScreen < 0) return false;

        int action = event.getActionMasked();

        int x = (int)event.getX();
        int y = (int)event.getY();
        int code;

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                code = this.screens.get(this.activeScreen).fingerDown(x,y);
                if (code != UIElement.TOUCH_CODE_NO_ACTION) {
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                code = this.screens.get(this.activeScreen).fingerMove(x,y);
                if (code != UIElement.TOUCH_CODE_NO_ACTION) {
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                code = this.screens.get(this.activeScreen).fingerUp(x,y);
                if (code != UIElement.TOUCH_CODE_NO_ACTION) {
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    @Override
    public void startAnimation(int id) {
        System.err.println("Starting animation for ID: " + id);
        if (!this.animationRequests.contains(id)){
            this.animationRequests.add(id);
        }
    }

    @Override
    public void stopAnimation(int id){
        System.err.println("Stopping animation for ID: " + id);
        int index = this.animationRequests.indexOf(id);
        if (index != -1) {
            this.animationRequests.remove(index);
            // And we notify all screens.
            for (Screen s: this.screens){
                s.notifyAnimationStop(id);
            }
        }
    }

    @Override
    public void sendMessageScreenController(int messageCode) {
        if (messageCode == Utils.SCREEN_COM_PLAY_PLOP){
            if (!this.soundON) return;
            this.soundPlop.seekTo(0);
            this.soundPlop.start();
        }
        else if (messageCode == Utils.SCREEN_COM_PLAY_CHECK){
            if (!this.soundON) return;
            this.soundSolutionCheck.seekTo(0);
            this.soundSolutionCheck.start();
        }
        else if (messageCode == Utils.SCREEN_COM_PLAY_GOT_IT){
            if (!this.soundON) return;
            this.soundSolved.seekTo(0);
            this.soundSolved.start();
        }
        else if (messageCode == Utils.SCREEN_REFRESH){
            this.invalidate();
        }
        else if (messageCode == Utils.SCREEN_CHANGE_SOUND){
            // TODO: Check Settings to see if sound is on or off.
            this.soundON = true;
        }
    }

    public MediaPlayer loadMediaPlayerFile(String file){

        MediaPlayer mp = new MediaPlayer();

        try {
            AssetFileDescriptor descriptor = this.getContext().getAssets().openFd(file);
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mp.prepare();
            return mp;
        }
        catch (Exception e) {
            System.err.println("Unable to play sound. Reason: " + e.getMessage());
            return null;
        }

    }

}
