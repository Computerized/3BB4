import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

class HighFrequencyCounter extends FrequencyCounter {
	AtomicIntegerArray currentCounters;
	
    HighFrequencyCounter(int max) {
        super(max);
        currentCounters = new AtomicIntegerArray(max);
    }
    void count(int event) {
    	while (currentCounters.get(event) != 0) {
    		//do nothing
    	}
    	
    	synchronized(this) {
    		currentCounters.incrementAndGet(event);
        	occurances[event] ++;
        	currentCounters.decrementAndGet(event);
    	}
    	
    }
    
    void getTotal() {
    	int sum = 0;
    	for (int i : occurances)
    		sum += i;
    	System.out.println(sum);
    }
}
class HEventer extends Thread {
    HighFrequencyCounter hfc;
    HEventer(HighFrequencyCounter hfc) {
        this.hfc = hfc;
    }
    public void run() {
        Random r = new Random();
        for (int i = 0; i < 20000; i++) {
            hfc.count(r.nextInt(10));
        }
    }
}
class TestHighFrequencyCounter {
    public static void main(String[] args) {
        HighFrequencyCounter hfc = new HighFrequencyCounter(10);
        HEventer hev[] = new HEventer[1000];
        for (int i = 0; i < 1000; i++) hev[i] = new HEventer(hfc);
        long hstartTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) hev[i].start();
        for (int i = 0; i < 1000; i++) {
            try {hev[i].join();} catch (Exception e) {}
        }
        long hendTime = System.currentTimeMillis();
        System.out.println(hendTime - hstartTime);
        hfc.getTotal();
    }
}