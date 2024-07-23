package com.service.vix.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customer = new Customer();
        customer.setCustomerName("Test Customer");
        customer.setCustomerParentAccount("Parent Account");
        customer.setCustomerAccountNumber(123456789L);
        // Add other necessary fields here
    }

    @Test
    public void testCustomerName() {
        assertEquals("Test Customer", customer.getCustomerName());
    }

    @Test
    public void testPrimaryContactFirstName() {
        customer.setPrimaryContactFirstName("John");
        assertEquals("John", customer.getPrimaryContactFirstName());
    }

    @Test
    public void testStoredServiceLocations() {
        StoredServiceLocation location1 = new StoredServiceLocation();
        StoredServiceLocation location2 = new StoredServiceLocation();
        customer.setStoredServiceLocations(new ArrayList<>(Arrays.asList(location1, location2)));
        assertEquals(2, customer.getStoredServiceLocations().size());
    }

    @Test
    public void testEmails() {
        Email email1 = new Email();
        Email email2 = new Email();
        customer.setEmails(new ArrayList<>(Arrays.asList(email1, email2)));
        assertEquals(2, customer.getEmails().size());
    }

}
