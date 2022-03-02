import java.util.*;
import java.math.*;

public class Average2 {
	
	public static double parallelaverage(int[] arrayToAverage, int numThreads) {
		ArrayList<Worker> threads = new ArrayList<Worker>(); //arraylist of threads
		int arrayLength = arrayToAverage.length;
		if (arrayLength == 0 || numThreads == 0)
			return -1; //return -1 as invalid input
		else if (arrayLength == 1) 
			return arrayToAverage[0]; //if array is a singleton set, return the element
		int threadSize = (int) (arrayLength / numThreads) + 1;
		
		//create a worker thread for each partition, and assign their respective partition
		for (int i = 0; i < arrayLength; i += threadSize) {
			threads.add(new Worker(arrayToAverage,i,Math.min(i+threadSize, arrayLength)));
			threads.get(threads.size() - 1).start(); //start each thread
		}
		try {
			for (Worker workerThread : threads)
				workerThread.join(); //join each worker thread in threads
		} catch(InterruptedException e) {} //catch thread exception
		double sum = 0.0;
		for (Worker workerThread : threads)
			sum += workerThread.avg; //compute the sum from each worker thread
		return sum;
	}
	
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]); // compute the average of n random numbers
		int p = Integer.parseInt(args[1]);
        int[] a = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) a[i] = rand.nextInt(100);
        System.out.println(Arrays.toString(a));
        System.out.println(parallelaverage(a,p));
	}
}
