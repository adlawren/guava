package ca.ualberta.papaya.util;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.papaya.interfaces.IObserver;

/**
 * Created by adlawren on 13/03/16.
 *
 * Observer Class for updating data and views
 *
 */
public class Observable<T> {

    private T data = null;

    private ArrayList<IObserver> observers = null;

    protected void notifyObservers() {
        if (observers.isEmpty()) return;

        for (IObserver observer : observers) {
            observer.update(data);
        }
    }

    public Observable() {
        observers = new ArrayList<IObserver>();
    }

    public void addObserver(IObserver<T> newObserver) {
        observers.add(newObserver);
    }

    public void setData(T newData) {
        data = newData;
        notifyObservers();
    }

    public T getData() {
        return data;
    }
}
