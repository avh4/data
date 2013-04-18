package net.avh4.framework.data;

import java.util.List;

public interface DataStore<T> {
    void write(T object);

    List<T> read();
}
