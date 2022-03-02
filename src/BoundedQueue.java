
class BoundedQueue<T> {
    T[] buf;
    int in = 0, out = 0, n = 0;
    BoundedQueue(int cap) {
    	assert (0 <= out && 0 <= n && n <= cap && in == (out + n) % cap);
        buf = (T[]) new Object[cap];
        assert buf != null;
    }
    void put(T x) {
    	assert n < buf.length && buf != null;
        buf[in] = x; in = (in + 1) % buf.length; n += 1;
    }
    T get() {
    	assert n > 0 && buf != null;
        T x = buf[out]; out = (out + 1) % buf.length; n -= 1;
        return x;
    }
    int size() {
        return n;
    }
}