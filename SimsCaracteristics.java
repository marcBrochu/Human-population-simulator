public interface SimsCaracteristics {
    public final String WOMEN = "women";
    public final String MEN = "men";
    public final String[] SEX = {"men","women"};
    public enum EventTypes {birth, death, coupling};
    public final int MAX_REPRODUCTION_AGE_FOR_MEN = 73;
    public final int MIN_REPRODUCTION_AGE_FOR_MEN_AND_WOMEN = 16;
    public final int MAX_REPRODUCTION_AGE_FOR_WOMEN = 50;
    public final double FIDELITY_PERCENTAGE = 0.90;
    public final int AVERAGE_CHILD_PER_FAMILY = 1;
}
