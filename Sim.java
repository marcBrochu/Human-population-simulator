public class Sim implements SimsCaracteristics, Comparable<Sim>{
    private String sex;
    private double birthTime;
    private double deathTime;
    private Sim mother;
    private Sim father;
    private Sim mate;

    //Founders with no father or mother
    public Sim(double birthTime, double deathTime, String sex){
        this.birthTime = birthTime;
        this.deathTime = deathTime;
        this.sex = sex;
    }

    public Sim(double birthTime, double deathTime, Sim mother, Sim father, String sex){
        this.mother = mother;
        this.deathTime = deathTime;
        this.father = father;
        this.birthTime = birthTime;
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public double getBirthTime() {
        return birthTime;
    }

    public double getDeathTime() {
        return deathTime;
    }

    public Sim getMother() {
        return mother;
    }

    public Sim getFather() {
        return father;
    }

    public Sim getMate() {
        return mate;
    }

    public void setMate(Sim mate) {
        this.mate = mate;
    }

    //order by time of death = birth date + death date
    @Override
    public int compareTo(Sim simToCompare) {
        if(this.deathTime == simToCompare.deathTime){
            return 0;
        }

        return this.deathTime < simToCompare.deathTime?-1:1;
    }

    @Override
    public String toString() {
        return this.birthTime + "";
    }
}