package ca.ualberta.papaya.interfaces;

/**
 * Created by adlawren on 07/03/16.
 */
public interface IObserver<T> {

    void update(T data);
}