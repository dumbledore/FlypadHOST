package org.flypad.util;

public class DataQueue {

    private Node head = null;
    private Node tail = null;

    private int size = 0;
    private final int maxSize;

    public DataQueue(final int maxSize) {
        this.maxSize = maxSize;
    }

    public final boolean isEmpty() {
        return head == null;
    }

    public final synchronized void enqueue(final byte[] data) {
        if(isEmpty()) {
            tail = head = new Node(data);
        } else {
            if (size >= maxSize) {
                /*
                 * Skip next message
                 */
                drop();
            }
            tail = tail.next = new Node(data);
        }
        size++;
    }

    public final synchronized byte[] dequeue() {
        if (isEmpty()) {
            return null;
        }
        
        final byte[] data = head.data;
        drop();
        return data;
    }

    private void drop() {
        head = head.next;
        size--;
    }

    public final synchronized void clear() {
        head = null;
        tail = null;
    }
}

class Node {
    protected Node next = null;
    protected final byte[] data;

    public Node(final byte[] data) {
        this.data = data;
    }

    public Node(final byte[] data, final Node next) {
        this.data = data;
        this.next = next;
    }
}
