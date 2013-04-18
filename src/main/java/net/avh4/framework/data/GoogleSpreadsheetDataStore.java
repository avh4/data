package net.avh4.framework.data;

import com.google.gdata.util.ServiceException;
import net.avh4.facade.gdata.v3.GoogleAppsApi;
import net.avh4.facade.gdata.v3.Spreadsheet;
import net.avh4.facade.gdata.v3.Worksheet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class GoogleSpreadsheetDataStore<T> implements DataStore<T> {
    public static final String DOCUMENT_NAME_FORMAT = "App Data: %s";
    private final Class<T> definition;
    private final Worksheet worksheet;

    public GoogleSpreadsheetDataStore(Class<T> definition, GoogleAppsApi googleAppsApi) {
        this.definition = definition;
        final Spreadsheet spreadsheet = googleAppsApi.createSpreadsheet(String.format(DOCUMENT_NAME_FORMAT, definition.getSimpleName()));
        try {
            worksheet = spreadsheet.getWorksheet(0);
        } catch (IOException | ServiceException e) {
            throw new RuntimeException(e);
        }

        prepareSchema(definition);
    }

    private void prepareSchema(Class<T> definition) {
        SortedSet<String> columnNames = new TreeSet<>();
        for (Method method : definition.getMethods()) {
            final String attributeName = method.getName().substring(3);
            columnNames.add(attributeName);
        }
        int column = 1;
        for (String columnName : columnNames) {
            try {
                worksheet.setCell(1, column, columnName);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            column++;
        }
    }

    @Override
    public void write(T object) {
        HashMap<String, String> attributes = new HashMap<>();

        for (Method method : definition.getMethods()) {
            if (method.getParameterTypes().length == 0) {
                try {
                    final String attributeName = method.getName().substring(3);
                    attributes.put(attributeName, method.invoke(object).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        try {
            worksheet.addRow(attributes);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public List<T> read() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
