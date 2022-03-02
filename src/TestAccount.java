import java.util.ArrayList;

class Account{
	int balance = 0;
	ArrayList<Integer> q = new ArrayList<Integer>();
	
	synchronized void deposit(int amount) {
		try { Thread.sleep(1); } catch (InterruptedException e2) { }
		balance += amount;
		System.out.println(balance);
	}
	
	synchronized void withdraw(int amount) {
		q.add(amount);
		while (balance - q.get(0) < 0) {
			try { wait(); } catch(InterruptedException e3) { }
		}
		balance -= q.remove(0);
		System.out.println(balance);
		notifyAll();
	}
}

class Depositer extends Thread {
    Account a;
    int amount;
    Depositer(Account a, int amount) {
        this.a = a; this.amount = amount;
    }
    public void run() {
    	System.out.println("Depositing " + amount);
        a.deposit(amount);
    }
}
class Withdrawer extends Thread {
    Account a;
    int amount;
    Withdrawer(Account a, int amount) {
        this.a = a; this.amount = amount;
    }
    public void run() {
    	System.out.println("Withdrawing " + amount);
        a.withdraw(amount);
    }
}
class TestAccount {
    public static void main(String[] args) {
        Account a = new Account();
        Withdrawer w0 = new Withdrawer(a, 300);
        Withdrawer w1 = new Withdrawer(a, 100);
        Withdrawer w2 = new Withdrawer(a, 200);
        Depositer d0 = new Depositer(a, 100);
        Depositer d1 = new Depositer(a, 200);
        Depositer d2 = new Depositer(a, 300);
        d0.start();  d1.start(); w1.start(); w2.start();   w0.start();  d2.start();
        try {w0.join(); w1.join(); w2.join(); d0.join(); d1.join(); d2.join(); 
        } catch(Exception e) {}
        System.out.println("final balance: " + a.balance);
    }
}