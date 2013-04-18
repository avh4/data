package net.avh4.framework.data;

import java.util.ArrayList;
import java.util.List;

public class MemoryDataStore<T> implements DataStore<T> {
    private List<T> values = new ArrayList<>();

    @Override
    public void write(T object) {
        values.add(object);
    }

    @Override
    public List<T> read() {
        return values;
    }
}
