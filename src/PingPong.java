import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class Table {
	AtomicInteger counter = new AtomicInteger(0);
	ArrayList<Integer> ints = new ArrayList<Integer>();
	//invariant: 0 <= counter <= 2
	
    synchronized void play(int i) {
    	while (counter.get() > 1) {
    		try { wait(); } catch(InterruptedException e) {}
    	}
    	ints.add(i);
    	counter.incrementAndGet();
    	
    	if (counter.getAcquire() > 1) {
    		System.out.println("Playing " + ints.get(0) + ", " + ints.get(1));
    		ints.clear();
    		counter.set(0);
    	}
    	notifyAll();
    }
}
class Player extends Thread {
    Table t;
    int i;
    Player(Table t, int i) {
        this.t = t; this.i = i; setDaemon(true);
    }
    public void run() {
        while (true) {
            t.play(i);
            try {Thread.sleep(1000);} catch (Exception e) {} // sleep 1 sec
        }
    }
}
public class PingPong {
    public static void main(String[] args) {
        Table t = new Table();
        for (int i = 0; i < 12; i++) new Player(t, i).start();
        try {Thread.sleep(4000);} catch (Exception e) {} // sleep 4 sec
        System.out.println("Done");
    }
}