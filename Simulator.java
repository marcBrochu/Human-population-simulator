import java.util.*;

// BROCHU, MARC-ANDRÉ
// DAVID HIGUERA SILVA, RAFAEL
public class Simulator implements SimsCaracteristics {
    private static int MAX_TIME;
    public static int NUMBERS_OF_FOUNDERS;
    private static double time = 0.00;
    private static AgeModel ageModel;
    private static PriorityQueue<Event> events = new PriorityQueue<Event>();
    private static BinaryHeap population;
    private static Random random = new Random();

    private static PriorityQueue<Sim> populationInQueue = new PriorityQueue<>();
    private static TreeMap coalescencePointsMale = new TreeMap();
    private static TreeMap coalescencePointsFemale = new TreeMap();


    public static void main(String[] args) throws InterruptedException {
        MAX_TIME = Integer.parseInt(args[1]);
        NUMBERS_OF_FOUNDERS = Integer.parseInt(args[0]);
        population = new BinaryHeap();
        ageModel = new AgeModel();
        createSimFounders();
        while(!events.isEmpty() && time < MAX_TIME && population.getHeapSize() > 0){
            Event event = events.peek();
            if(events.size() == 0 || population.getHeapSize()== 0 || event == null) {
                break;
            }
            while(event != null && event.getEventTime() <= time){
                if(event.getEventType() == EventTypes.coupling){
                    reproduction(events.poll().getTargetedSim());
                }else if(event.getEventType() == EventTypes.death){
                    population.grimReaperDeath(events.poll().getTargetedSim());
                }else if(event.getEventType() == EventTypes.birth){
                    simRegistrationInPopulation(events.poll().getTargetedSim());
                }
                event = events.peek();
            }
            time = getAdditionedRoundTime(time,1);
            if (time % 100 == 0) {
                System.out.println("Time: " + time + " Population: " + population.getHeapSize());
            }
        }
        coalescence();
    }

    private static void coalescence() {

        while (population.getHeapSize() != 0) {
            populationInQueue.add(population.deleteMin());
        }

        while (!populationInQueue.isEmpty()) {
            Sim child = populationInQueue.remove();
            if (child.getSex().equals(SimsCaracteristics.MEN)) {
                while (child != null) {
                        Sim father = child.getFather();
                        if (father != null) {
                            if (coalescencePointsMale.containsKey(father.getBirthTime())) {
                                coalescencePointsMale.put(father.getBirthTime(),
                                        ((Integer)coalescencePointsMale.get(father.getBirthTime()) + 1));
                            } else {
                                coalescencePointsMale.put(father.getBirthTime(), 0);
                            }
                            populationInQueue.remove(father);
                        }
                        child = father;
                }
            } else {
                while (child != null) {
                    Sim mother = child.getMother();
                    if (mother != null) {
                        if (coalescencePointsFemale.containsKey(mother.getBirthTime())) {
                            coalescencePointsFemale.put(mother.getBirthTime(),
                                    (Integer) coalescencePointsFemale.get(mother.getBirthTime()) + 1);
                        } else {
                            coalescencePointsFemale.put(mother.getBirthTime(), 0);
                        }
                        populationInQueue.remove(mother);
                    }
                    child = mother;
                }

            }
        }
        System.out.println("Male: " + coalescencePointsMale.toString());
        System.out.println("Female: " + coalescencePointsFemale.toString());
    }

    private static void createSimFounders(){
        double deathTime;
        for (int i = 0; i < NUMBERS_OF_FOUNDERS; i++) {
            deathTime = ageModel.randomAge(random);
            Sim sim = new Sim(time, deathTime, SEX[((int) Math.round(Math.random()))]);
            simRegistrationInPopulation(sim);
        }
    }

    private static Sim futureSimBirth(Sim mother, Sim father){
        double deathTime = ageModel.randomAge(random);
        Sim sim = new Sim(time, getAdditionedRoundTime(time,deathTime), mother, father, SEX[(int) Math.round(Math.random())]);
        simRegistrationInPopulation(sim);
        return sim;
    }

    private static void reproduction(Sim women){
        if(women.getDeathTime() <= time || !women.getSex().equals(WOMEN)){
            return;
        }
        if(womenCanReproduce(women)){
            chooseMaleMate(women);
            Event event = new Event(futureSimBirth(women,women.getMate()),EventTypes.birth,time);
            events.add(event);
        }
        setupCouplingDate(women);
    }
    
    //ok
    private static void chooseMaleMate(Sim women){
        if(women.getMate() != null){
            if(Math.random() <= FIDELITY_PERCENTAGE){
                return;
            }else{
                population.assignAMate(women,time,true);
            }
        }
        //does not have a mate
        else{
            population.assignAMate(women,time,false);
        }
    }
    
    //ok
    private static boolean womenCanReproduce(Sim women){
        double ageOfWomen = time-women.getBirthTime();
        if(ageOfWomen < MIN_REPRODUCTION_AGE_FOR_MEN_AND_WOMEN || ageOfWomen > MAX_REPRODUCTION_AGE_FOR_WOMEN){
            return false;
        }
        return true;
    }

    private static void simRegistrationInPopulation(Sim sim) {
        registerFutureDeath(sim);
        //if women, setupCouplingDate
        if(sim.getSex().equals(WOMEN))
            setupCouplingDate(sim);
        population.insert(sim);
    }

    private static void registerFutureDeath(Sim sim){
        Event eventDeath = new Event(sim, EventTypes.death,sim.getDeathTime());
        events.add(eventDeath);
    }

    private static void setupCouplingDate(Sim sim){
        Random random = new Random();
        double couplingDate = ageModel.randomWaitingTime(random,
                AVERAGE_CHILD_PER_FAMILY/ageModel.expectedParenthoodSpan(MIN_REPRODUCTION_AGE_FOR_MEN_AND_WOMEN, MAX_REPRODUCTION_AGE_FOR_WOMEN));
        Event event = new Event(sim,EventTypes.coupling, getAdditionedRoundTime(time,couplingDate));
        events.add(event);
    }

    private static double getAdditionedRoundTime(double x,double y){
        return Math.round((x+y)*100.0)/100.0;
    }
}
