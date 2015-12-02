package view;

public interface Observer<T> {
	/**
	 * Called when this observer is notified
	 * @param event
	 */
	void notify(T event);
}
