package net.avh4.framework.data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private ArrayList<?> arrayList;

    public <T> List<T> getList(Class<T> definition) {
        if (arrayList == null)
            arrayList = new ArrayList<>();
        return (List<T>) arrayList;
    }

    public <T> T create(Class<T> definition) {
        InvocationHandler handler = new InvocationHandler() {
            Object lastValue;

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (args != null) {
                    lastValue = args[0];
                    return null;
                } else {
                    return lastValue;
                }
            }
        };
        T object = (T) Proxy.newProxyInstance(definition.getClassLoader(),
                new Class[]{definition},
                handler);
        return object;
    }
}
