package com.bridgelabz.addressbook;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.addressbook.AddressBookService.IOService.DB_IO;

public class AddressBookTest {
    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchThePeopleCount() {
        AddressBookService addressBookService = new AddressBookService();
        List<AddressBookData> addressBookDataList = addressBookService.readAddressBookData(DB_IO);
        Assert.assertEquals(1,addressBookDataList.size());
    }

    @Test
    public void givenNewPhoneNumber_ShouldUpdateTheRecorAndSyncWithDataBase() throws AddressBookException {
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

    
}
