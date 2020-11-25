package com.bridgelabz.addressbook;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AddressBookService {

    private List<AddressBookData> addressBookList;
    public enum IOService{DB_IO}
    private static AddressBookDBService addressBookDBService;

    public AddressBookService() {
        addressBookDBService = AddressBookDBService.getInstance();
    }

    public List<AddressBookData> readAddressBookData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
             this.addressBookList = addressBookDBService.readDate();
        }
        return addressBookList;
    }

    public void updateRecord(String name, String phoneNumber) throws AddressBookException {
        int result = addressBookDBService.updateAddressBookRecord(name, phoneNumber);
        if (result==0)return;
        AddressBookData  addressBookData=this.getAddressBookData(name);
        if (addressBookData!=null) addressBookData.phoneNumber=phoneNumber;
    }

    private AddressBookData getAddressBookData(String name) {
        return this.addressBookList.stream()
                .filter(addressBookData -> addressBookData.firstName.equals(name))
                .findFirst()
                .orElse(null);
    }


    public boolean checkRecordSyncWithDB(String name) {
        List<AddressBookData> addressBookData= addressBookDBService.getAddressBookData(name);
        System.out.println(addressBookData+" **");
        boolean equals = addressBookData.get(0).equals(getAddressBookData(name));
        return equals;
    }


    public List<AddressBookData> readEmployeePayrollForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if (ioService.equals(IOService.DB_IO)) {
            return addressBookDBService.getEmployeePayrollForDateRange(startDate, endDate);
        }
        return null;
    }

    public Map<String, Double> contactsByCity(IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            return addressBookDBService.getCountOfContactsByCity();
        }
        return null;
    }

    public void addNewContact( String type, String first_name, String last_name,
                              String phone_number, String email, String city, String state, String zip, String address, LocalDate localDate) {
        addressBookList.add(addressBookDBService.addNewContact(type, first_name, last_name,
                phone_number,email,city,state,zip,address,localDate));
    }

    public void addMultipleContacts(List<AddressBookData> asList) {
        Runnable task= () ->  {
            for (AddressBookData addressBookData:asList) {
                System.out.println(addressBookData.firstName+" contact is adding");
                addressBookDBService.updateMultipleContacts(addressBookData);
                System.out.println(addressBookData.firstName+" contact is added");
            }
        };
        Thread thread=new Thread(task);
        thread.start();
        while(asList.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}














