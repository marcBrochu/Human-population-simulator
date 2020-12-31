import java.util.Random;

public class BinaryHeap implements SimsCaracteristics{
    private Sim[] heap;
    private int heapSize;

    //sorted from shortest lifetime to bigger lifetime
    public BinaryHeap(){
        heapSize = 0;
        this.heap = new Sim[Simulator.NUMBERS_OF_FOUNDERS +1];
    }

    private void resize(){
        Sim[] heapTemp = new Sim[heapSize*2];
        for (int i = 1; i < heap.length; i++) {
            heapTemp[i] = heap[i];
        }
        heap = heapTemp;
    }

    //compare if parent < child = 1
    private void swim(int index){
        Sim temp;
        while (index > 1 && heap[index/2].compareTo(heap[index]) == 1){
            temp = heap[index/2];
            heap[index/2] = heap[index];
            heap[index] = temp;
            index = index/2;
        }
    }

    //compare if both childs > parent = 1, if that's the case don't do nothing
    private void sink(int index){
        Sim temp;
        while(2*index <= heapSize){
            int smallerChildIndex = getSmallerChild(index);
            if(smallerChildIndex < heapSize && heap[index].compareTo(heap[smallerChildIndex]) == -1) break;
            temp = heap[index];
            heap[index] = heap[smallerChildIndex];
            heap[smallerChildIndex] = temp;
            index = smallerChildIndex;
        }
    }

    private int getSmallerChild(int index){
        if(2*index+1 > heapSize)
            return 2*index;
        return heap[2*index].compareTo(heap[2*index+1]) == -1? 2*index:2*index+1;
    }

    public void insert(Sim sim){
        if(heapSize == heap.length-1)
            resize();
        heap[++heapSize] = sim;
        swim(heapSize);
    }

    public Sim deleteMin(){
        Sim deathSim = heap[1];
        heap[1] = heap[heapSize--];
        heap[heapSize+1] = null;
        sink(1);
        return deathSim;
    }

    public void grimReaperDeath(Sim sim){
        int i = 1;
        Sim simToKill = heap[i];
        while(!simToKill.equals(sim)) {
            i++;
            simToKill = heap[i];
        }
        if(simToKill.getMate() != null)
            simToKill.getMate().setMate(null);
        heap[i] = heap[heapSize--];
        heap[heapSize+1] = null;
        sink(i);
    }

    public Sim getFirstInQueue(){
        return heap[1];
    }

    public boolean menAccepted(boolean hasAMate,boolean hasToAccept){
        if(!hasAMate || hasToAccept){
            return true;
        }else if((double)1-FIDELITY_PERCENTAGE >= Math.random()){
            return true;
        }
        return false;
    }
    //ok
    public void assignAMate(Sim women, double time, boolean menHasToAccept){
        Sim men = heap[(int) (Math.random()*heapSize)+1];
        //while the random sim has not all the qualities, continue searching
        while(!men.getSex().equals(MEN)
                || time-men.getBirthTime() < MIN_REPRODUCTION_AGE_FOR_MEN_AND_WOMEN
                || time-men.getBirthTime() > MAX_REPRODUCTION_AGE_FOR_MEN
                || !menAccepted(men.getMate() != null,menHasToAccept)){
            men = heap[(int) (Math.random()*heapSize)+1];
        }
        infidelity(men,women);
        men.setMate(women);
        women.setMate(men);
    }
    //ok
    private void infidelity(Sim men, Sim women){
        if(men.getMate() != null)
            men.getMate().setMate(null);
        if(women.getMate() != null)
        	women.getMate().setMate(null);
    }

    public int getHeapSize(){
        return heapSize;
    }

    @Override
    public String toString() {
        String deathTimeOfSims = "";
        for (int i = 1; i <= heapSize ; i++) {
            deathTimeOfSims += "["+heap[i].getSex()+"]";
        }
        return deathTimeOfSims;
    }
}
