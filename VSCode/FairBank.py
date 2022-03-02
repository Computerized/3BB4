from threading import Thread, Lock, Condition
from sys import stdout
class Account:
    def __init__(self):
        self.lock = Lock()
        self.balance = 0 #balance >= 0
        self.cond = Condition(self.lock)

    def deposit(self, amount): 
        with self.lock:
            self.balance += amount
            self.cond.notifyAll()
        

    def withdraw(self, amount):
        with self.lock:
            while(amount > self.balance):
                self.cond.wait()
            self.balance -= amount
            self.cond.notifyAll()

class Depositer(Thread):
    def __init__ (self, a, amount):
        Thread.__init__(self); self.a = a; self.amount = amount
    def run(self): self.a.deposit(self.amount)

class Withdrawer(Thread):
    def __init__ (self, a, amount):
        Thread.__init__(self); self.a = a; self.amount = amount
    def run(self): self.a.withdraw(self.amount)

a = Account()
w0 = Withdrawer(a, 300); w1 = Withdrawer(a, 100)
d0 = Depositer(a, 100); d1 = Depositer(a, 200); d2 = Depositer(a, 300)
d0.start();  d1.start(); w1.start(); w0.start(); d2.start()
w0.join(); w1.join(); d0.join(); d1.join(); d2.join()
stdout.write("final balance: " + str(a.balance) + "\n")