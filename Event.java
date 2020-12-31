public class Event implements SimsCaracteristics,Comparable<Event>{
    private Sim targetedSim;
    private EventTypes eventType;
    private double eventTime;

    public Event(Sim targetedSim, EventTypes eventType, double eventTime) {
        this.targetedSim = targetedSim;
        this.eventType = eventType;
        this.eventTime = eventTime;
    }

    public Sim getTargetedSim() {
        return targetedSim;
    }

    public void setTargetedSim(Sim targetedSim) {
        this.targetedSim = targetedSim;
    }

    public EventTypes getEventType() {
        return eventType;
    }

    public void setEventType(EventTypes eventType) {
        this.eventType = eventType;
    }

    public double getEventTime() {
        return eventTime;
    }

    public void setEventTime(double eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public int compareTo(Event event) {
        if(this.eventTime == event.eventTime)
            return 0;
        return this.eventTime < event.eventTime?-1:1;
    }

    @Override
    public String toString() {
        return "[Sim deathTime : "+getTargetedSim().getDeathTime()+"] [Event time : "+eventTime+"] [Event type : "+eventType+"]";
    }
}

