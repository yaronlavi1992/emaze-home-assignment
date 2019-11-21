import java.util.stream.Stream;

public class HeightControl {
    private int[] heights;

    public HeightControl() {
        setHeights();
    }

    public int[] getHeights() {
        return heights;
    }

    public void setHeights() {
        this.heights = new int[15];
    }

    public boolean isHeightAvailable(int height) {
        if (getHeights()[height] == 0) {
            getHeights()[height] = 1;
            return true;
        }
        System.out.println("height: " + height + " is not available");
        return false;
    }

    public void heightVacancy(int height) {
        getHeights()[height - 1] = 0;
    }
}
