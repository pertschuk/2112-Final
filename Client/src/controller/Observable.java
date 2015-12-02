package controller;

import view.Observer;

public interface Observable<T> {
	/**
	 * 
	 * @param observer
	 */
	void registerObserver(Observer<T> observer);
	/**
	 * Notifies observers of event e
	 */
	void notifyAllObservers(Event e);
}
