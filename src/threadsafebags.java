	class Node {
    int e;
    Node next = null;
}
	
public class threadsafebags{
	
}

class Bag {
	Node first, last;
	private boolean inDelete = false;
	private boolean inInsert = false;
	private int countInserts = 0;

	public Bag() {
		first = new Node();
		last = first;
	}
	
	boolean has(int e) {
		while(countInserts > 1 || inDelete) {
			try { wait(); } catch(InterruptedException e3) {}
		}
		Node n = first;
        while (n != last && n.next.e != e) n = n.next;
        return n != last;
	}
	
	synchronized void delete(int e) {
		while (inDelete || inInsert) {
			try { wait(); } catch(InterruptedException e1) {}
		}
		inDelete = true;
		Node n = first;
        while (n != last && n.next.e != e) n = n.next;
        if (n != last) {
            if (n.next == last) last = n;
            n.next = n.next.next;
        }
        inDelete = false;
        notify();
	}
	
	synchronized void insert(int e) {
		while (inDelete || inInsert) {
			try { wait(); } catch(InterruptedException e2) {}
		}
		inInsert = true;
		countInserts ++;
		Node n = new Node();
        n.e = e; last.next = n; last = n;
        inInsert = false;
        countInserts --;
        notifyAll();
	}
}

class TestBag{
	public static void main(String[] args) {
		Bag b = new Bag();
        System.out.println(b.has(5)); // false
        b.delete(7); b.insert(5);
        System.out.println(b.has(5)); // true
        System.out.println(b.has(7)); // false
        System.out.println(b.has(3)); // false
        b.insert(7); b.delete(5);
        System.out.println(b.has(5)); // false
        System.out.println(b.has(7)); // true
        System.out.println(b.has(3)); // false
    }
}
