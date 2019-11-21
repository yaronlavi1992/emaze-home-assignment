import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main{

    public static int ticker = 0;

    public static void main(String[]args){
        int numberOfDrones = 20;
        int delayBetweenTicks = 0; // in seconds, to observe the events in the console(or run in debug)

        // initialize height control and drones array
        HeightControl heightControl = new HeightControl(15);
        Drone[] droneArray = new Drone[numberOfDrones];
        for (int i = 0; i < droneArray.length; i++) {
            // all drones share the same height control system
            droneArray[i] = new Drone(heightControl);
        }

        // simulation loop of 4 hours
        while(ticker < 240) { // 240 minutes = 4 hours(240/60)
            for (int i = 0; i < droneArray.length; i++) {
                // update(tick) every drone

                // uncomment to get more details on each drone
//                System.out.println(droneArray[i].toString());
                droneArray[i].tick(ticker);
            }

            // add delay(in seconds) between each tick
            try {
                TimeUnit.SECONDS.sleep(delayBetweenTicks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // increment ticker by 1
            ticker++;
            // print out the available heights
            System.out.println("Available Heights: " + Arrays.toString(heightControl.getAvailableHeights().toArray()));
            System.out.println(); // empty line to separate from previous batch update
        }
    }

}
