package net.avh4.framework.data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
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
            HashMap<String, Object> values = new HashMap<>();

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("hashCode")) {
                    return 0;
//                } else if (method.getName().equals("equals")) {
//                    return true;
                } else if (args != null) {
                    String attributeName = method.getName().substring(1);
                    values.put(attributeName, args[0]);
                    return null;
                } else {
                    String attributeName = method.getName().substring(1);
                    return values.get(attributeName);
                }
            }
        };
        @SuppressWarnings("unchecked")
        T object = (T) Proxy.newProxyInstance(definition.getClassLoader(),
                new Class[]{definition},
                handler);
        return object;
    }
}
