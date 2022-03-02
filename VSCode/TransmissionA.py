from threading import Thread, Semaphore
from time import sleep
from sys import stdout
from random import randint

N = 8

lockOut = Semaphore(1)
lines = ["A","B","C"]

def request():
    while len(lines) == 0:
        pass
    lockOut.acquire()
    a = lines.pop()
    lockOut.release()
    return a

def done(l):
    lockOut.acquire()
    lines.append(l)
    lockOut.release()
    
class Communicating(Thread):
    def __init__(self, i):
        self.i = i; super().__init__()
        
    def run(self):
        while True:
            sleep(randint(0, 6))
            line = request()
            stdout.write(str(self.i) + ' communicating on ' + str(line) + '\n')
            sleep(randint(0, 3))
            stdout.write(str(self.i) + ' done\n')
            done(line)
            
for i in range(N): Communicating(i).start()