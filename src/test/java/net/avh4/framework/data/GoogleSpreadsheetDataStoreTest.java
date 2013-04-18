package net.avh4.framework.data;

import com.google.common.collect.ImmutableMap;
import net.avh4.facade.gdata.v3.GoogleAppsApi;
import net.avh4.facade.gdata.v3.Spreadsheet;
import net.avh4.facade.gdata.v3.Worksheet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class GoogleSpreadsheetDataStoreTest {
    private GoogleSpreadsheetDataStore<TestDefinition> newSubject;
    @Mock
    private GoogleAppsApi googleAppsApi;
    @Mock
    private Spreadsheet spreadsheet;
    @Mock
    private Worksheet worksheet;
    @Mock
    private TestDefinition object1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        stub(googleAppsApi.createSpreadsheet("App Data: TestDefinition")).toReturn(spreadsheet);
        stub(spreadsheet.getWorksheet(0)).toReturn(worksheet);

        newSubject = new GoogleSpreadsheetDataStore<>(TestDefinition.class, googleAppsApi);
    }

    @Test
    public void shouldCreateSpreadsheet() {
        verify(googleAppsApi).createSpreadsheet("App Data: TestDefinition");
    }

    @Test
    public void shouldCreateColumns() throws Exception {
        verify(worksheet).setCell(1, 1, "Name");
        verify(worksheet).setCell(1, 2, "Title");
    }

    @Test
    public void add_shouldAddRow() throws Exception {
        stub(object1.getName()).toReturn("James");
        stub(object1.getTitle()).toReturn("King");
        newSubject.write(object1);
        verify(worksheet).addRow(ImmutableMap.of("Name", "James", "Title", "King"));
    }
}
