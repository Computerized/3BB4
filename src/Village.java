

class Pub {
	int currentCapacity, maxCapacity;
	boolean closed;
	
	Pub(int c){
		maxCapacity = c;
		currentCapacity = 0;
		closed = false;
	}
	
    synchronized boolean enter() {
    	while (currentCapacity >= maxCapacity && !closed) {
    		try { wait(); } catch (InterruptedException e) { }
    	}
    	currentCapacity ++;
    	notifyAll();
    	return !closed;
    }
    
    synchronized void exit() {
    	currentCapacity --;
    }
    
    synchronized void closing(){
    	closed = true;
    }
}
class Villager extends Thread {
    Pub p;
    Villager(Pub p) {
        this.p = p;
    }
    public void run() {
        try {Thread.sleep((long)(Math.random() * 6000));
        } catch (Exception e) {}
        if (p.enter()) {
            System.out.print("🙂"); // entered pub
            try {Thread.sleep((long)(Math.random() * 1000));
            } catch (Exception e) {}
            System.out.print("😋"); // full and happy
            p.exit();
        } else System.out.print("🙁"); // turned down
    }
}
class Manager extends Thread {
    Pub p;
    Manager(Pub p) {
        this.p = p;
    }
    public void run() {
        try {Thread.sleep(4000);
        } catch (Exception e) {}
        System.out.print("🔒");
        p.closing();
    }
}
class Village {
    public static void main(String[] args) {
        Pub p = new Pub(8); // capacity 8
        new Manager(p).start();
        for (int i = 0; i < 20; i++) new Villager(p).start();
    }
}