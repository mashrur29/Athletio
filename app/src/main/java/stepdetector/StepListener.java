package stepdetector;

/**
 * Created by tanvir on 8/28/17.
 */
//Interface to listen for steps
public interface StepListener {

    public void step(long timeNs);

}