public class Drone {
    private static int dronesCreated = 1;
    private int droneNum;
    private AirGround airGroundState;
    private BoardedAwait boardingState;
    private int timeLeftToLand;
    private int timeLeftForBoarding;
    private HeightControl heightControl;
    private int droneHeight;

    public int getDroneHeight() {
        return droneHeight;
    }

    public void setDroneHeight(int droneHeight) {
        this.droneHeight = droneHeight;
    }

    public BoardedAwait getBoardingState() {
        return boardingState;
    }

    public void setBoardingState(BoardedAwait boardingState) {
        this.boardingState = boardingState;
    }

    public HeightControl getHeightControl() {
        return heightControl;
    }

    public void setHeightControl(HeightControl heightControl) {
        this.heightControl = heightControl;
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

    public void setDroneNum(int droneNum) {
        this.droneNum = droneNum;
    }

    public Drone(HeightControl heightControl) {
        setHeightControl(heightControl);
        setAirGroundState(AirGround.ON_GROUND);
        setBoardingState(BoardedAwait.AWAITING_BOARD);
        setTimeLeftToLand(0);
        setDroneHeight(-1);
        setTimeLeftForBoarding((int)(Math.random() * 15) + 1);
        setDroneNum(getDronesCreated());
        incrementDronesCreated();
    }

    public AirGround getAirGroundState() {
        return airGroundState;
    }

    public void setAirGroundState(AirGround airGroundState) {
        this.airGroundState = airGroundState;
    }

    public int getTimeLeftToLand() {
        return timeLeftToLand;
    }

    public void setTimeLeftToLand(int timeLeftToLand) {
        this.timeLeftToLand = timeLeftToLand;
    }

    public int getTimeLeftForBoarding() {
        return timeLeftForBoarding;
    }

    public void setTimeLeftForBoarding(int timeLeftForBoarding) {
        this.timeLeftForBoarding = timeLeftForBoarding;
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
            setTimeLeftToLand(getTimeLeftToLand() - 1); // reduce by 1 (60 km/h = 1 km per minute)
        } else if (getTimeLeftToLand() == 0) {

            if (getAirGroundState() == AirGround.IN_AIR) {
                // drone landed

                // 1. report event
                System.out.println(ticker + ": Drone " + this.getDroneNum() + " has arrived");

                // 2. update drone state
                setAirGroundState(AirGround.ON_GROUND);
                setBoardingState(BoardedAwait.AWAITING_BOARD);

                // 3. update HeightControl vacancy
                if (getDroneHeight() > 0) {
                    getHeightControl().heightVacancy(getDroneHeight());
                }

                // 4. update drone height
                setDroneHeight(-1);

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

                // 2. update drone status
                setBoardingState(BoardedAwait.BOARDED);

                // 2. ask for new height
                int requestedHeight = (int)(Math.random() * 14);
                if (getHeightControl().isHeightAvailable(requestedHeight)) { // if new height approved
                    // 1. set the newly approved height
                    setDroneHeight(requestedHeight);

                    // 2. take off
                    int randomFlightLength = (int)(Math.random() * 5) + 1;
                    setTimeLeftToLand(randomFlightLength);
                    setAirGroundState(AirGround.IN_AIR);
                    System.out.println(ticker + ": Drone " + this.getDroneNum() + " has departed");
                } else {
                    // wait for one minute, and ask again(on next tick)
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
                '}';
    }
}
