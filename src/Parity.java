public class Parity {
	
	static void parity(int num) {
		System.out.println(num % 2 == 0 ? "even" : "odd");
	}
	
	static void sumFromTo(int a, int b) {
		if (a >= b)
			return;
		int sum = 0;
		for (int i = a; i <= b; i ++) {
			sum += i;
		}
		System.out.println(sum);
	}
	
	static void sumTo(int n) {
		System.out.println(n*(n+1)/2);
	}
	
	public static void main(String[] args) {
		Parity.parity(6);
		Parity.sumFromTo(1, 100);
		Parity.sumTo(100);
	}
}