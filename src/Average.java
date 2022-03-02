import java.util.Arrays;
import java.util.Random;

class Worker extends Thread {
	int [] nums;
	double avg = 0;
	int [] range = new int[2];
	public Worker (int [] numbers, int start, int end) {
		nums = numbers;
		range[0] = start; range[1] = end;
	}
	
    public void run() {
    	for (int i = range[0]; i < range[1]; i++) avg += nums[i];
    	avg /= nums.length;
    }
}

public class Average {
    static double sequentialaverage(int a[]) {
        double s = 0;
        for (int i = 0; i < a.length; i++) s += a[i];
        return s / a.length;
    }
    static double parallelaverage(int[] a) {
    	int halfway = (int) a.length/2;
        Worker thread1 = new Worker(a,0, halfway);
        Worker thread2 = new Worker(a,halfway, a.length);
        thread1.start(); thread2.start();
        try {
        	thread1.join(); thread2.join();
        } catch (Exception e) {}
        return thread1.avg+thread2.avg;
    }
    
    public static void main(String args[]) {
        int n = Integer.parseInt(args[0]); // compute the average of n random numbers
        int[] a = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) a[i] = rand.nextInt(100);
        
        long start = System.currentTimeMillis();
        double avg = sequentialaverage(a);
        long end = System.currentTimeMillis();
        System.out.println("Sequential: " + avg + " Time: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        avg = parallelaverage(a);
        end = System.currentTimeMillis();
        System.out.println("Parallel: " + avg + " Time: " + (end - start) + " ms");
    }
    
}