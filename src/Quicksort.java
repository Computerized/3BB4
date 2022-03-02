import java.util.Random;

class ParallelSorter extends Thread  {
	public int p, r;
	
	public ParallelSorter(int p, int r) {
		this.p = p;
		this.r = r;
	}
	
	public void run() {
		Quicksort.parallelsort(p, r);
	}
}

public class Quicksort {

    static int N;   // number of elements to be sorted
    static int S;   // threshold for creating a sub-thread
    static int a[]; // array to be sorted
    
    static int partition(int p, int r)	 {
        int x = a[r];
        int i = p - 1;
        for (int j = p; j <= r - 1; j++) {
            if (a[j] <= x) {
                i++; 
                int t = a[i]; a[i] = a[j]; a[j] = t;
            }
        }
        int t = a[i + 1]; a[i + 1] = a[r]; a[r] = t;
        return i + 1;
    }

    static void sequentialsort(int p, int r) {
        if (p < r) {
            int q = partition(p, r);
            sequentialsort(p, q - 1);
            sequentialsort(q + 1, r);
        }
    }

    static void parallelsort(int p, int r) {
    	if (p < r) {
    		int q = partition(p,r);
    		if (q < S) {
    			ParallelSorter PS = new ParallelSorter(p,q-1);
    			PS.start();
    			parallelsort(q+1,r);
    			try {PS.join();} catch (Exception e) {}
    		} else {
    			sequentialsort(p, r);
    		}
    	}
    }

    public static void main(String args[]) {
        N = Integer.parseInt(args[0]);
        S = Integer.parseInt(args[1]);
        a = new int[N + 1];
        Random random = new Random();
        for (int i = 0; i < N; i++) a[i] = random.nextInt(10000);
        
        final long start2 = System.currentTimeMillis();
        parallelsort(0, N - 1);
        final long end2 = System.currentTimeMillis();
        
        for (int i = 1; i < N; i++) assert a[i - 1] <= a[i];
        System.out.println((end2 - start2) + " ms");
    }
}