package net.avh4.framework.data;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class DataServiceTest {
    private DataService subject;
    private TestDefinition object;
    private TestDefinition differentObject;
    private List<TestDefinition> list;
    private DataStore store;

    @Before
    public void setUp() {
        store = new MemoryDataStore();
        subject = new DataService(store);
        object = subject.create(TestDefinition.class);
        differentObject = subject.create(TestDefinition.class);
        list = subject.getList(TestDefinition.class);
    }

    @Test
    public void shouldCreateObjects() {
        assertThat(object).isNotNull();
    }

    @Test
    public void createdObjects_shouldHaveNullFields() {
        assertThat(object.getName()).isNull();
    }

    @Test
    public void createdObjects_shouldStoreAttributes() {
        object.setName("Bobby");
        assertThat(object.getName()).isEqualTo("Bobby");
    }

    @Test
    public void createdObject_shouldDistinguishAttributes() {
        object.setTitle("Doctor");
        object.setName("Who");
        assertThat(object.getTitle()).isEqualTo("Doctor");
    }

    @Test
    public void createdObject_testHashCode() {
        assertThat(object.hashCode()).isEqualTo(object.hashCode());
    }

    @Test
    public void createdObject_testEquals() {
        assertThat(object.equals(object)).isTrue();
        assertThat(object.equals(differentObject)).isTrue();

//        differentObject.setName("Something else");
//        assertThat(object.equals(differentObject)).isFalse();
    }

    @Test
    public void shouldCreateList() {
        assertThat(list).isNotNull();
    }

    @Test
    public void createdList_shouldBeEmpty() {
        assertThat(list).isEmpty();
    }

    @Test
    public void createdList_shouldHoldObjects() {
        list.add(object);
        assertThat(list).contains(object);
    }

    @Test
    public void addingToList_shouldPersist() {
        list.add(object);
        List<TestDefinition> persistedList = new DataService(store).getList(TestDefinition.class);
        assertThat(persistedList).isEqualTo(list);
    }

    @Test
    public void addingSecondItemToList_shouldPersist() {
        list.add(object);
        list.add(differentObject);
        List<TestDefinition> persistedList = new DataService(store).getList(TestDefinition.class);
        assertThat(persistedList).isEqualTo(list);
    }

    @Test
    public void loadingList_shouldNotCorruptPersistedData() {
        list.add(object);
        list.add(differentObject);
        new DataService(store).getList(TestDefinition.class);
        List<TestDefinition> persistedList = new DataService(store).getList(TestDefinition.class);
        assertThat(persistedList).isEqualTo(list);
    }

    private static interface TestDefinition {
        void setName(String name);

        String getName();

        void setTitle(String title);

        String getTitle();
    }
}
