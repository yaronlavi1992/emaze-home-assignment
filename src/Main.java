import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main{

    public static int ticker = 0;

    public static void main(String[]args){
        int numberOfDrones = 1;
        int delayBetweenTicks = 1; // in seconds, to observe the changes(or run in debug)

        // initialize height control and drones array
        HeightControl heightControl = new HeightControl();
        Drone[] droneArray = new Drone[numberOfDrones];
        for (int i = 0; i < droneArray.length; i++) {
            // all drones share the same height control system
            droneArray[i] = new Drone(heightControl);
        }

        // visualize
//        String[][] visualize = new String[numberOfDrones][15];
//        for (int i = 0; i < numberOfDrones; i++) {
//            for (int j = 0; j < 15; j++) {
//                visualize[i][j] = " ";
//            }
//        }

        // simulation loop of 4 hours
        while(ticker < 240) { // 240 minutes = 4 hours(240/60)
            for (int i = 0; i < droneArray.length; i++) {
                // update every drone
                // TODO: multi-thread
                System.out.println(droneArray[i].toString());
                droneArray[i].tick(ticker);
            }

            // further visualization
//            for (int i = 0; i < numberOfDrones; i++) {
//                for (int j = 0; j < 15; j++) {
//                    visualize[i][j] = " ";
//                }
//            }
//            for (int i = 0; i < numberOfDrones; i++) {
//                if (droneArray[i].getDroneHeight() != -1){
//                    visualize[i][droneArray[i].getDroneHeight()] = "*";
//                }
//            }
//            for (int i = 0; i < visualize.length; i++) {
//                System.out.println(Arrays.toString(visualize[i]));
//            }

            // add delay(in seconds) between each tick
            try {
                TimeUnit.SECONDS.sleep(delayBetweenTicks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // increment ticker by 1
            ticker++;
        }
    }

}
