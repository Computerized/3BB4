from threading import Thread, Semaphore
from time import sleep
from sys import stdout
from random import randint

N = 8

lockOut = Semaphore(1)
lines = ["A","B","C"]
locks = [i for i in range(N)]

def request(i):
    while locks[0] != i:
        pass
    while len(lines) == 0:
        pass
    lockOut.acquire()
    a = lines.pop()
    locks.pop(0)
    lockOut.release()
    return a

def done(l,i):
    lockOut.acquire()
    lines.append(l)
    locks.append(i)
    locks.sort()
    lockOut.release()
    
class Communicating(Thread):
    def __init__(self, i):
        self.i = i; super().__init__()
        
    def run(self):
        while True:
            sleep(randint(0, 6))
            line = request(self.i)
            stdout.write(str(self.i) + ' communicating on ' + str(line) + '\n')
            sleep(randint(0, 3))
            stdout.write(str(self.i) + ' done\n')
            done(line,self.i)
            
for i in range(N): Communicating(i).start()