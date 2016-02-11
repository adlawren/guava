package ca.ualberta.papaya.fixtures;

/**
 * Created by martin on 10/02/16.
 */
public enum Province {
    ALBERTA, SASKATCHEWAN;
    @Override public String toString() {
        // todo: deal with _
        return name().toLowerCase();
    }
}
