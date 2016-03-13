package ca.ualberta.papaya.util;

import java.util.ArrayList;

import ca.ualberta.papaya.interfaces.IObserver;

/**
 * Created by adlawren on 13/03/16.
 */
public class Observable<T> {

    private T data = null;

    private ArrayList<IObserver> observers;
    private void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update(data);
        }
    }

    public Observable(T initialData) {
        data = initialData;
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
