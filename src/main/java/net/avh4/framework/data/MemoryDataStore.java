package net.avh4.framework.data;

import java.util.ArrayList;
import java.util.List;

public class MemoryDataStore implements DataStore {
    private List<Object> values = new ArrayList<>();

    @Override
    public <T> void write(T object) {
        values.add(object);
    }

    @Override
    public <T> List<T> read() {
        return (List<T>) values;
    }
}
