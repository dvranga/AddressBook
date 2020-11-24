package com.bridgelabz.addressbook;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.bridgelabz.addressbook.AddressBookService.IOService.DB_IO;

public class AddressBookTest {
    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchThePeopleCount() {
        AddressBookService addressBookService = new AddressBookService();
        List<AddressBookData> addressBookDataList = addressBookService.readAddressBookData(DB_IO);
        Assert.assertEquals(1,addressBookDataList.size());
    }
}
