class IntSet {
    int a[];
    int n;
    boolean intSetInvariantOK() {
    	assert n >= 0;
    	assert n <= a.length;	
    	for (int i = 0; i < n; i++)
    		for (int j = i + 1; j < n; j++)
    			assert a[i] != a[j];
    	return true;
    }
    IntSet(int capacity) {
        a = new int[capacity]; n = 0;
        assert intSetInvariantOK();
    }
    void add(int x) {
        int i = 0;
        assert (i <= n);
        while (i < n && a[i] != x) {
            i += 1;
        }
        assert (i <= n);
        if (i == n) {
            a[n] = x; n += 1;
        }
        assert intSetInvariantOK();
    }
    boolean has(int x) {
        int i = 0;
        assert (i <= n);
        while (i < n && a[i] != x) {
            i += 1;
        }
        assert (i <= n);
        assert intSetInvariantOK();
        return i < n;
    }
}
class MaxIntSet extends IntSet {
    int m;
    boolean maxIntSetInvariantOK() {
        assert intSetInvariantOK();
        assert n >= 0;
        int tempmax = 0;
        for (int i = 0; i < a.length; i++)
        	tempmax = Math.max(tempmax, a[i]);
        assert m == tempmax;
        return true;
    }
    MaxIntSet(int capacity) {
        super(capacity);
        assert maxIntSetInvariantOK();
    }
    void add(int x) {
        super.add(x);
        if (n == 1) m = x;
        else m = m > x ? m : x;
        assert maxIntSetInvariantOK();
    }
    int maximum(){
    	assert maxIntSetInvariantOK();
        return m;
    }
}
class TestMaxIntSet {
    public static void main(String[] args) {
        MaxIntSet s = new MaxIntSet(3); s.add(5); s.add(7);
        System.out.println(s.maximum());
    }
}