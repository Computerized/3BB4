class Kitchen {
	int servings;
	
	public Kitchen() {
		servings = 0;
	}
	
	synchronized void putServingsInPot(int n) {
		while (servings > 0) {
			try { wait(); } catch(InterruptedException e) {}
		}
		servings += n;
		System.out.println("cook putting " + n + " servings, total: " + servings);
		notify();
	}
	
	synchronized void getServingFromPot() {
		while (servings <= 0) {
			try { wait(); } catch(InterruptedException e) {}
		}
		servings --;
		System.out.println("scout getting 1 serving, total: " + servings);
		notify();
	}
}

class Cook extends Thread {
    Kitchen k;
    Cook(Kitchen k) {
        this.k = k; setDaemon(true);
    }
    public void run() {
        while (true) {
            try {Thread.sleep(500); // sleep 0.5 seconds
            } catch (Exception e) {}
            k.putServingsInPot(10); // 10 servings
        }
    }
}
class Scout extends Thread {
    Kitchen k;
    Scout(Kitchen k) {
        this.k = k; setDaemon(true);
    }
    public void run() {
        while (true) {
            k.getServingFromPot();
            try {Thread.sleep(500); // eat 0.5 seconds
            } catch (Exception e) {}
        }
    }
}

class TestScouts {
    public static void main(String[] args) {
        Kitchen k = new Kitchen();
        Cook c = new Cook(k); c.start();
        Scout sc[] = new Scout[20];
        for (int i = 0; i < 20; i++) {sc[i] = new Scout(k); sc[i].start();}
        try {Thread.sleep(5000);} catch (Exception e) {}
        System.out.println("Done");
    }
}