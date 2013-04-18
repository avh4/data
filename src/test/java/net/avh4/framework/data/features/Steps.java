package net.avh4.framework.data.features;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.avh4.framework.data.DataService;
import net.avh4.framework.data.DataStore;
import net.avh4.framework.data.MemoryDataStore;
import net.avh4.framework.data.TestDefinition;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@SuppressWarnings("UnusedDeclaration")
public class Steps {
    private DataStore store = new MemoryDataStore();
    private DataService dataService = new DataService(store);
    private List<TestDefinition> dataList;

    @Given("^a data object definition$")
    public void a_data_object_definition() throws Throwable {
    }

    @When("^I instantiate a list of data objects$")
    public void I_instantiate_a_list_of_data_objects() throws Throwable {
        dataList = dataService.getList(TestDefinition.class);
        dataList.add(dataService.create(TestDefinition.class));
    }

    @Then("^the data is persisted$")
    public void the_data_is_persisted() throws Throwable {
        final List<TestDefinition> persistedData = dataService.getList(TestDefinition.class);
        assertThat(persistedData).isEqualTo(dataList);
    }
}
