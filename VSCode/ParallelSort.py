from threading import Thread, Semaphore
from time import sleep
from sys import stdout
from random import random, shuffle

class Sorter(Thread):
    def __init__(self, l0, u0, l1, u1, i, r):
        # stdout.write("Sorter " + str(i) + " init " + str(l0) + " " + str(u0) + " " + \
        #             str(l1) + " " + str(u1) + "\n")
        Thread.__init__(self)
        self.l0, self.u0, self.l1, self.u1 = l0, u0, l1, u1
        self.i, self.r = i, r

    def partition(self, p, r):
        global a
        x, i = a[r], p - 1;
        for j in range(p, r):
            if a[j] <= x: i += 1; a[i], a[j] = a[j], a[i]
        a[i + 1], a[r] = a[r], a[i + 1]
        return i + 1

    def sequentialsort(self, p, r):
        if p < r:
            q = self.partition(p, r)
            self.sequentialsort(p, q - 1)
            self.sequentialsort(q + 1, r)

    def barriersync(self):
        barrier[self.i].release()
        if self.i == len(barrier)-1:
            barrier[0].acquire()
        else:
            barrier[self.i+1].acquire()


    def run(self):
        for r in range(self.r):
            stdout.write(str(self.i) + " sorts " + str(self.l0) + " to " + str(self.u0) + " round " + str(r) + "\n")
            self.sequentialsort(self.l0, self.u0)
            sleep(random())
            self.barriersync()
            stdout.write(str(self.i) + " sorts " + str(self.l1) + " to " + str(self.u1) + " round " + str(r) + "\n")
            self.sequentialsort(self.l1, self.u1)
            sleep(random())
            self.barriersync()

def oddevensort(P, M):
    # P: number of sorting threads
    # M: number of elements each thread sorts sequentially
    N = P * M + M // 2 # number of elements to be sorted
    print("size", N)
    global a; a = list(range(N)); shuffle(a)
    print("init", a)
    
    global barrier; barrier = [Semaphore(0) for i in range(P)]

    sorters = []
    for i in range(P):
        # sorter(starting index, endix index up to 2)
        s = Sorter(i * M, (i + 1) * M - 1, i * M + M // 2, (i + 1) * M + M // 2 - 1, i, P + 1)
        s.start(); sorters.append(s)
    for s in sorters: s.join()
    print("sorted", a)

oddevensort(4, 10)
