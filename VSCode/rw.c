#include <pthread.h>
#include <stdio.h>
#include <semaphore.h>
#include <unistd.h>
#include <stdlib.h>

#define SHARED 1

void *Reader(void *);             /* the two threads           */
void *Writer(void *);

/* global varialbes and semaphores */

# YOUR CODE HERE
raise NotImplementedError()

int numIters;

/* main program: read command line, create threads, print result of */

int main(int argc, char *argv[]) {
    pthread_t rid, wid;           /* thread and attributes */
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr, PTHREAD_SCOPE_SYSTEM);
    /* initialization of global varialbes and semaphores */
# YOUR CODE HERE
raise NotImplementedError()
    numIters = atoi(argv[1]);
    pthread_create(&rid, &attr, Reader, NULL);
    pthread_create(&wid, &attr, Writer, NULL);
    pthread_join(wid, NULL);
    printf("Max data %d\n", maxdata);
}

/* deposit 1, ..., numIters into the data buffer */
void *Reader(void *arg) {
    maxdata = 0;
    printf("Reader starting\n");
    while (1) { 
        /* entry protocol */
# YOUR CODE HERE
raise NotImplementedError()
        if (data > maxdata) printf("maxdata reader reading %d\n", data);
        maxdata = data > maxdata ? data : maxdata;
        /* exit protocol */
# YOUR CODE HERE
raise NotImplementedError()
    }
}

/* fetch numIters items from the buffer and sum them */
void *Writer(void *arg) {
    int i;
    printf("Writer starting\n");
    for (i = 0; i < numIters; i++) {
        /* entry protocol */
# YOUR CODE HERE
raise NotImplementedError()
        data = (i + numIters / 2) % numIters;
        printf("Writer writing %d\n", data);
        /* exit protocol */
# YOUR CODE HERE
raise NotImplementedError()
        sleep(1);
    }
}