from threading import Thread, Lock, Condition
from sys import stdout

class BarberShop:
    def __init__(self):
        self.lock = Lock()
        self.barber, self.chair, self.exit = 0,0,0
        self.barberAvailable = Condition(self.lock)
        self.chairOccupied = Condition(self.lock)
        self.exitOpen = Condition(self.lock)
        self.customerLeft = Condition(self.lock)
        

    def getHaircut(self, i):
        with self.lock:
            while(self.barber == 0):
                self.barberAvailable.wait()
            self.barber -= 1
            self.chair += 1; self.chairOccupied.notify()
            while(self.exit == 0):
                self.exitOpen.wait()
            self.exit -= 1; self.customerLeft.notify()
            
    def getNextCustomer(self):
        with self.lock:
            self.barber += 1; self.barberAvailable.notify()
            while(self.chair == 0):
                self.chairOccupied.wait()
            self.chair -= 1

    def finishedCut(self):
        with self.lock:
            self.exit += 1; self.exitOpen.notify()
            while(self.exit > 0):
                self.customerLeft.wait()

class Barber(Thread):
    def __init__(self, shop):
        Thread.__init__(self); self.shop = shop
    def run(self):
        for _ in range(20):
            self.shop.getNextCustomer()  # wait for a customer to sit in the barber's chair
            stdout.write("barber cutting hair\n")
            self.shop.finishedCut()      # allow the customer to leave; returns after the customer left

class Customer(Thread):
    def __init__(self, i, shop):
        Thread.__init__(self); self.i, self.shop = i, shop
    def run(self):
        for _ in range(5):
            stdout.write(str(self.i) + " living happily\n")
            self.shop.getHaircut(self.i)  # returns after the customer has received a the haircut


s = BarberShop(); b = Barber(s)
c0 = Customer(0, s); c1 = Customer(1, s); c2 = Customer(2, s); c3 = Customer(3, s)
b.start(); c0.start(); c1.start(); c2.start(); c3.start()