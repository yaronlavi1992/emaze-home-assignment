import java.util.Stack;

public class HeightControl {
    private Stack availableHeights;
    private Stack occupiedHeights;

    // constructor with given amount of heights
    public HeightControl(int heights) {
        this.availableHeights = new Stack();
        for (int i = 0; i < heights; i++) {
            availableHeights.push(i);
        }
        this.occupiedHeights = new Stack();
    }

    // default constructor
    public HeightControl() {
        this(15);
    }

    public Stack getAvailableHeights() {
        return availableHeights;
    }

    public Stack getOccupiedHeights() {
        return occupiedHeights;
    }
}
