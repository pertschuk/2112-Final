package util;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/** An implementation of RingBuffer that implements the BlockingQueue interface */
public class RingBuffer<E> implements BlockingQueue<E> {
	private E[] queue;
	private int head;
	private int tail;
	
	public RingBuffer() {
		queue = (E[]) new Object[4];//change later
		head = 0;
		tail = 0;
	}
	
	@Override
	public boolean add(Object e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public E remove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(Object e) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public E element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean offer(Object e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public E peek() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/** Do these need to be implemented? */
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public E take() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int remainingCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int drainTo(Collection c) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int drainTo(Collection c, int maxElements) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/** The following methods are left unimplemented */
	
	@Override
	public boolean offer(Object e, long timeout, TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public E poll(long timeout, TimeUnit unit) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
