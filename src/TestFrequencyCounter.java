import java.util.Random;
class FrequencyCounter {
    int[] occurances;
    boolean lockSection;
	
    FrequencyCounter(int max) {
        occurances = new int[max];
        lockSection = false;
    }
    synchronized void count(int event) {
    	while (lockSection)
    		try { wait(); } catch (InterruptedException e) {}
        occurances[event] ++;
        lockSection = false;
        notifyAll();
    }
    synchronized int frequency(int event) {
        return occurances[event];
    }
}
class Eventer extends Thread {
    FrequencyCounter fc;
    Eventer(FrequencyCounter fc) {
        this.fc = fc;
    }
    public void run() {
        Random r = new Random();
        for (int i = 0; i < 20000; i++) {
            fc.count(r.nextInt(10));
        }
    }
}
class TestFrequencyCounter {
    public static void main(String[] args) {
        FrequencyCounter fc = new FrequencyCounter(10);
        Eventer ev[] = new Eventer[1000];
        for (int i = 0; i < 1000; i++) ev[i] = new Eventer(fc);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) ev[i].start();
        for (int i = 0; i < 1000; i++) {
            try {ev[i].join();} catch (Exception e) {}
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}