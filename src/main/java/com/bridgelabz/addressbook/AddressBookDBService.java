package com.bridgelabz.addressbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
    private static AddressBookDBService addressBookDBService;

    public AddressBookDBService() {
    }

    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null) {
             addressBookDBService = new AddressBookDBService();
        }
        return addressBookDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_db?useSSL=false";
        String userName = "root";
        String password = "root";
        Connection connection;
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println(connection + " connection succesful");
        return connection;
    }

    public List<AddressBookData> readDate() {
        String query = "SELECT * from address_book";
        return this.getAddressBookDataUsingDB(query);
    }

    private List<AddressBookData> getAddressBookDataUsingDB(String query) {
        List<AddressBookData> addressBookList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            addressBookList = this.getAddressBookData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }

    private List<AddressBookData> getAddressBookData(ResultSet resultSet) {
        List<AddressBookData> addressBookList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int personId = resultSet.getInt("person_id");
                int typeId= resultSet.getInt("type_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phoneNumber = resultSet.getString("phone_number");
                String email = resultSet.getString("email");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                addressBookList.add(new AddressBookData(id,personId,typeId,firstName,lastName,phoneNumber,email,city,state,zip ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }
}
