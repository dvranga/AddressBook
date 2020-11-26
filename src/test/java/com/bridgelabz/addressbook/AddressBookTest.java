package com.bridgelabz.addressbook;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.addressbook.AddressBookService.IOService.DB_IO;
import static com.bridgelabz.addressbook.AddressBookService.IOService.REST_IO;

public class AddressBookTest {
    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchThePeopleCount() {
        AddressBookService addressBookService = new AddressBookService();
        List<AddressBookData> addressBookDataList = addressBookService.readAddressBookData(DB_IO);
        Assert.assertEquals(1,addressBookDataList.size());
    }

    @Test
    public void givenNewPhoneNumber_ShouldUpdateTheRecordAndSyncWithDataBase() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(DB_IO);
        addressBookService.updateRecord("ranganath", "7483247031");
        boolean ranganath = addressBookService.checkRecordSyncWithDB("ranganath");
        Assert.assertTrue(ranganath);
    }

    @Test
    public void givenDate_ShouldRetrieveTheAddressBookRecord_BasedOnThePerticularRange() {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        List< AddressBookData> employeePayrollData=
                addressBookService.readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(1,employeePayrollData.size());
    }

    @Test
    public void givenCity_ShouldRetrieveTheNumberOfContacts_BasedOnCity() {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(DB_IO);
        Map<String, Double> contactsByCity = addressBookService.contactsByCity(DB_IO);
        System.out.println(contactsByCity.containsKey("anatapur")+" "+contactsByCity.containsValue(1.0));
        Assert.assertTrue(contactsByCity.containsKey("anatapur") &&
                contactsByCity.containsValue(1.0));
    }

    @Test
    public void givenNewContact_ShouldAddIntoTheAddressBookDataBase() {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(DB_IO);
        addressBookService.addNewContact("friend","mokshagna","vatti","7483247010",
                "devangmranganath","anatapur","AP","515231","Gorantla",LocalDate.now());
        boolean result = addressBookService.checkRecordSyncWithDB("mokshagna");
        Assert.assertTrue(result);
    }

    @Test
    public void givenMultipleContacts_ShouldAddIntoTheDB_AndShouldSyncWithDBUsingThread() throws AddressBookException {
        AddressBookData[] arrayOfEmps = {new AddressBookData("friend","rajesh","vatti","7483247011","devangmranganath","anatapur","AP","515231","Gorantla",LocalDate.now()),
                                        new AddressBookData("family","mukesh","vatti","7483247012","devangmranganath","anatapur","AP","515231","Gorantla",LocalDate.now()),
                                        new AddressBookData("professional","naresh","vatti","7483247013","devangmranganath","anatapur","AP","515231","Gorantla",LocalDate.now()),
                                        new AddressBookData("friend","lokesh","vatti","7483247014","devangmranganath","anatapur","AP","515231","Gorantla",LocalDate.now())
                        };
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(DB_IO);
        Instant start = Instant.now();
        addressBookService.addMultipleContacts(Arrays.asList(arrayOfEmps));
        Instant end = Instant.now();
        System.out.println("Duration with thread: " + Duration.between(start, end));
        boolean result = addressBookService.checkRecordSyncWithDB("lokesh");
        Assert.assertTrue(result);
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }
    private AddressBookData[] getAddressList() {
        Response response = RestAssured.get("/addressbook");
        System.out.println("AddressBook Entries in the json server:\n" + response.asString());
        AddressBookData[] addressBookData = new Gson().fromJson(response.asString(), AddressBookData[].class);
        return addressBookData;
    }

    @Test
    public void givenAddressBookDataInJson_WhenRetrievedShouldMatchTheCount() {
        AddressBookData[] addressBookData= getAddressList();
        AddressBookService addressBookService=new AddressBookService(Arrays.asList(addressBookData));
        long entries = addressBookService.countEntries(REST_IO);
        Assert.assertEquals(2,entries);
    }


}










