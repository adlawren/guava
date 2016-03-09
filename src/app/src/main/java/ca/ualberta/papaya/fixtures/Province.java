package ca.ualberta.papaya.fixtures;

/**
 * Created by martin on 10/02/16.
 */
public enum Province {
    BRITISH_COLUMBIA,
    ALBERTA,
    SASKATCHEWAN,
    MANITOBA,
    ONTARIO,
    QUEBEC,
    NOVA_SCOTIA,
    NEW_BRUNSWICK,
    PRINCE_EDWARD_ISLAND,
    NEWFOUNDLAND;
    @Override public String toString() {
        // todo: deal with _
        return name().toLowerCase();
    }
}
