package stepdetector;

/**
 * Created by tanvir on 8/28/17.
 */
//Interface to listen for steps

/// Stolen from : http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/#.We8VRp-WZhE
public interface StepListener {

    public void step(long timeNs);

}