package util;

public interface Lock {
	/** Acquire the lock unless it is already held by this thread. Blocks until
	 *  it is available. */
	void lock();
	
	/** Release the lock. Requires: it is held by this thread. */
	void unlock();
}
