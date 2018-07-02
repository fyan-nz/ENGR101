import ecs100.*;

import java.util.*;


/**
 * A FSM controller for an Intersection.
 * The core of the controller is the signal(String sensor) method
 * which is called by the intersection every time a sensor
 * is signalled.
 */
public class LightsController {

    /**
     * The field that stores the current state of the FSM
     */
    private String state = "EWgoReady";
    // Note, you may wish to "factor" the state by having additional variables
    // to represent aspects of the state


    /**
     * The field containing the Intersection.
     * The signal method will call methods on the intersection to
     * change the lights
     */
    private Intersection intersection;  // the intersection that is being controlled.

    // possible actions that you can perform on the intersection:

    // to set the E-W lights
    //   intersection.turnEWred()
    //   intersection.turnEWamber()
    //   intersection.turnEWgreen()

    // to set the N-S lights
    //   intersection.turnNSred()     
    //   intersection.turnNSamber()
    //   intersection.turnNSgreen()

    // to set the pedestrian lights
    //   intersection.turnEWpedStop() to set the E-W Pedestrian lights
    //   intersection.turnEWpedGo()
    //   intersection.turnNSpedStop() to set the N-S Pedestrian lights
    //   intersection.turnNSpedGo()

    // to restart the timer, eg for 1 second (1000 milliseconds)
    //   intersection.restartTimer(1000)  


    /**
     * The Constructor is passed the intersection that it is controlling.
     */
    public LightsController(Intersection intersection) {
        this.intersection = intersection;
    }


    /**
     * Receives a change in a sensor value that may trigger an action and state change.
     * If there is a transition out of the current state associated with this
     * sensor signal,
     * - it will perform the appropriate action (if any)
     * - it will transition to the next state
     * (by changing the state field
     * You can either have one big method, or you can break it up into
     * a separate method for each state
     * <p>
     * possible sensor values that you should respond to:
     * "carEW"  "carNS"  "pedEW"  "pedNS"  "timerExpired"
     * <p>
     * The initial version given here is just a two state FSM which switches
     * state (and changes the lights) whenever a car is sensed.
     * It does not respond to the timer at all!
     * This is a bad controller!! (It needs more states, and more actions.)
     */

    public void signal(String sensor) {
        UI.println("in state: " + this.state + ",  got sensor: " + sensor);
        if (this.state.equals("EWgoReady")) {
            if (sensor.equals("carNS")) {
                this.intersection.turnEWamber();
                this.intersection.restartTimer(3000);
                this.state = "EWstopping";
            }
        } else if (this.state.equals("EWstopping")) {
            if (sensor.equals("carEW")) {
                this.state = "EWstoppingCarEW";
            } else if (sensor.equals("timerExpired")) {
                this.intersection.turnEWred();
                this.intersection.turnNSgreen();
                this.intersection.restartTimer(15000);
                this.state = "NSgo";
            }
        } else if (this.state.equals("EWstoppingCarEW")) {
            if (sensor.equals("timerExpired")) {
                this.intersection.turnEWred();
                this.intersection.turnNSgreen();
                this.intersection.restartTimer(15000);
                this.state = "NSgoMustChange";
            }
        } else if (this.state.equals("NSgo")) {
            if (sensor.equals("carEW")) {
                this.state = "NSgoMustChange";
            } else if (sensor.equals("timerExpired")) {
                this.state = "NSgoReady";
            }
        } else if (this.state.equals("NSgoMustChange")) {
            if (sensor.equals("timerExpired")) {
                this.intersection.turnNSamber();
                this.intersection.restartTimer(3000);
                this.state = "NSstopping";
            }
        } else if (this.state.equals("NSgoReady")) {
            if (sensor.equals("carEW")) {
                this.intersection.turnNSamber();
                this.intersection.restartTimer(3000);
                this.state = "NSstopping";
            }
        } else if (this.state.equals("NSstopping")) {
            if (sensor.equals("carNS")) {
                this.state = "NSstoppingCarNS";
            } else if (sensor.equals("timerExpired")) {
                this.intersection.turnNSred();
                this.intersection.turnEWgreen();
                this.intersection.restartTimer(15000);
                this.state = "EWgo";
            }
        } else if (this.state.equals("NSstoppingCarNS")) {
            if (sensor.equals("timerExpired")) {
                this.intersection.turnNSred();
                this.intersection.turnEWgreen();
                this.intersection.restartTimer(15000);
                this.state = "EWgoMustChange";
            }
        } else if (this.state.equals("EWgo")) {
            if (sensor.equals("carNS")) {
                this.state = "EWgoMustChange";
            } else if (sensor.equals("timerExpired")) {
                this.state = "EWgoReady";
            }
        } else if (this.state.equals("EWgoMustChange")) {
            if (sensor.equals("timerExpired")) {
                this.intersection.turnEWamber();
                this.intersection.restartTimer(3000);
                this.state = "EWstopping";
            }
        }

    }


    public void reset() {
        state = "EWgoReady";
    }


    public static void main(String[] args) {
        LightsSimulation.main(args);
    }

}
