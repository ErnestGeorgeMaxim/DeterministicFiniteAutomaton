import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State {
    private static int  nextId = 0;
    private int id;
    private boolean isStartNode;
    private boolean isEndNode;

    public State() {
        this.id = nextId++;
        this.isStartNode = false;
        this.isEndNode = false;
    }

    public static void resetId() {
        nextId = 0;
    }

    @Override
    public String toString() {
        return "q" + this.id;
    }
}
