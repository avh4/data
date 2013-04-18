package net.avh4.framework.data;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DataServiceTest {
    private DataService subject;
    private TestDefinition object;

    @Before
    public void setUp() {
        subject = new DataService();
        object = subject.create(TestDefinition.class);
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

    private static interface TestDefinition {
        void setName(String name);
        String getName();
        void setTitle(String title);
        String getTitle();
    }
}
