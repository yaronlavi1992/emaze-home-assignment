public class Drone {
    private static int dronesCreated = 1;
    private HeightControl heightControl;
    private AirGround airGroundState;
    private int droneNum;
    private int timeLeftToLand;
    private int timeLeftForBoarding;
    private int droneHeight;

    public Drone(HeightControl heightControl) {
        this.heightControl = heightControl;
        setAirGroundState(AirGround.ON_GROUND);
        setDroneNum(getDronesCreated());
        setTimeLeftToLand(0);
        setTimeLeftForBoarding((int)(Math.random() * 15) + 1);
        setDroneHeight(-1);
        incrementDronesCreated();
    }

    public AirGround getAirGroundState() {
        return airGroundState;
    }

    private void setAirGroundState(AirGround airGroundState) {
        this.airGroundState = airGroundState;
    }

    public int getTimeLeftToLand() {
        return timeLeftToLand;
    }

    private void setTimeLeftToLand(int timeLeftToLand) {
        this.timeLeftToLand = timeLeftToLand;
    }

    public int getTimeLeftForBoarding() {
        return timeLeftForBoarding;
    }

    private void setTimeLeftForBoarding(int timeLeftForBoarding) {
        this.timeLeftForBoarding = timeLeftForBoarding;
    }

    public int getDroneHeight() {
        return droneHeight;
    }

    private void setDroneHeight(int droneHeight) {
        this.droneHeight = droneHeight;
    }

    public HeightControl getHeightControl() {
        return heightControl;
    }

    public static int getDronesCreated() {
        return dronesCreated;
    }

    public static void incrementDronesCreated() {
        Drone.dronesCreated++;
    }

    public int getDroneNum() {
        return droneNum;
    }

    private void setDroneNum(int droneNum) {
        this.droneNum = droneNum;
    }

    public void tick(int ticker) {

        // TIME TO LAND
        updateTimeToLand(ticker);

        // TIME FOR BOARDING
        if (getTimeLeftToLand() > 0) {
            // drone is still in flight, no need to update boarding time
        } else {
            updateTimeForBoarding(ticker);
        }
    }

    private void updateTimeToLand(int ticker) {
        if (getTimeLeftToLand() > 0) {
            setTimeLeftToLand(getTimeLeftToLand() - 1); // reduce by 1 (60 km/h = 1 km per minute), assuming interval is constant
        } else if (getTimeLeftToLand() == 0) {

            if (getAirGroundState() == AirGround.IN_AIR) {
                // drone landed

                // 1. report event
                System.out.println(ticker + ": Drone " + this.getDroneNum() + " has arrived");

                // 2. update drone state
                setAirGroundState(AirGround.ON_GROUND);

                // 3. update HeightControl vacancy
                if (getDroneHeight() > 0) {
                    getHeightControl().getOccupiedHeights().remove(this);
                    getHeightControl().getAvailableHeights().push(getDroneHeight());
                }

                // 4. update drone height
                setDroneHeight(-1); // a value outside of the available heights range(=ground)

                // 5. add a random boarding time
                int randomBoardingTime = (int)(Math.random() * 5) + 1;
                setTimeLeftForBoarding(randomBoardingTime);

            } else if (getAirGroundState() == AirGround.ON_GROUND) {
                // waiting board
            }
        }
    }

    private void updateTimeForBoarding(int ticker) {
        if (getTimeLeftForBoarding() > 0) {
            setTimeLeftForBoarding(getTimeLeftForBoarding() - 1); // reduce by 1
        } else if (getTimeLeftForBoarding() == 0) {
            // drone boarded

            if (getAirGroundState() == AirGround.IN_AIR) {
                // wait for land
            } else if (getAirGroundState() == AirGround.ON_GROUND) {
                // 1. report event
                System.out.println(ticker + ": Drone " + this.getDroneNum() + " was boarded");

                // 2. ask for new height
                if (!getHeightControl().allHeightsOccupied()) { // if not all heights occupied(there's at least one height available)
                    // 1. set the newly approved height
                    int availableHeight = (int)getHeightControl().getAvailableHeights().pop();
                    setDroneHeight(availableHeight);

                    // 2. update HeightControl occupied height
                    getHeightControl().getOccupiedHeights().push(availableHeight);

                    // 3. take off
                    int randomFlightLength = (int)(Math.random() * 5) + 1;
                    setTimeLeftToLand(randomFlightLength);
                    setAirGroundState(AirGround.IN_AIR);
                    System.out.println(ticker + ": Drone " + this.getDroneNum() + " was approved with height " + availableHeight);
                } else {
                    // no available heights

                    // 1. report event
                    System.out.println(ticker + ": Drone " + this.getDroneNum() + " take off denied");

                    // 2. wait for one minute(a single interval), and ask again(on next tick)
                    // or basically do nothing.
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Drone{" +
                "airGroundState=" + getAirGroundState() +
                ", timeLeftToLand=" + getTimeLeftToLand() +
                ", timeLeftForBoarding=" + getTimeLeftForBoarding() +
                ", droneNum=" + getDroneNum() +
                ", droneHeight=" + getDroneHeight() +
                '}';
    }
}
