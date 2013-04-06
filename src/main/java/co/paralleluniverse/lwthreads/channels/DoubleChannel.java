/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.paralleluniverse.lwthreads.channels;

import co.paralleluniverse.lwthreads.LightweightThread;
import co.paralleluniverse.lwthreads.SuspendExecution;
import co.paralleluniverse.lwthreads.datastruct.SingleConsumerArrayDoubleQueue;
import co.paralleluniverse.lwthreads.datastruct.SingleConsumerDoubleQueue;
import co.paralleluniverse.lwthreads.datastruct.SingleConsumerLinkedDoubleQueue;
import co.paralleluniverse.lwthreads.datastruct.SingleConsumerQueue;

/**
 *
 * @author pron
 */
public class DoubleChannel extends Channel<Double> {
    public static DoubleChannel create(Thread owner, int mailboxSize) {
        return new DoubleChannel(owner, mailboxSize > 0 ? new SingleConsumerArrayDoubleQueue(mailboxSize) : new SingleConsumerLinkedDoubleQueue());
    }

    public static DoubleChannel create(LightweightThread owner, int mailboxSize) {
        return new DoubleChannel(owner, mailboxSize > 0 ? new SingleConsumerArrayDoubleQueue(mailboxSize) : new SingleConsumerLinkedDoubleQueue());
    }

    private DoubleChannel(Object owner, SingleConsumerQueue<Double, ?> queue) {
        super(owner, queue);
    }

    public double receiveInt() throws SuspendExecution, InterruptedException {
        return ((SingleConsumerDoubleQueue<Object>)queue).doubleValue(receiveNode());
    }

    public void send(double message) {
        if (sync.isOwnerAlive()) {
            queue.enq(message);
            sync.signal();
        }
    }

    public void sendSync(double message) {
        if (sync.isOwnerAlive()) {
            queue.enq(message);
            sync.signalAndTryToExecNow();
        }
    }
}
