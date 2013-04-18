package net.avh4.framework.data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataService {

    private ArrayList<?> list;
    private final DataStore store;

    public DataService(DataStore store) {
        this.store = store;
    }

    public <T> List<T> getList(Class<T> definition) {
        if (this.list == null) {
            ArrayList<T> list = new ArrayList<T>() {
                @Override
                public boolean add(T object) {
                    final boolean add = super.add(object);
                    store.write(object);
                    return add;
                }
            };
            final List<T> persisted = store.read();
            list.addAll(persisted);
            this.list = list;
        }

        return (List<T>) list;
    }

    public <T> T create(Class<T> definition) {
        InvocationHandler handler = new DefinitionImplementation(definition);
        @SuppressWarnings("unchecked")
        T object = (T) Proxy.newProxyInstance(definition.getClassLoader(),
                new Class[]{definition},
                handler);
        return object;
    }

    private static class DefinitionImplementation implements InvocationHandler {
        private final Class<?> definition;
        HashMap<String, Object> values = new HashMap<>();

        public DefinitionImplementation(Class<?> definition) {
            this.definition = definition;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("hashCode")) {
                return 0;
            } else if (method.getName().equals("equals")) {
                return true;
            } else if (method.getName().equals("toString")) {
                return definition.getSimpleName() + values.toString();
            } else if (args != null) {
                String attributeName = method.getName().substring(1);
                values.put(attributeName, args[0]);
                return null;
            } else {
                String attributeName = method.getName().substring(1);
                return values.get(attributeName);
            }
        }
    }
}
