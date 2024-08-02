package com.service.vix.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.service.vix.enums.EmailCategory;

public class EmailTest {

    private Email email;

    @BeforeEach
    public void setUp() {
        email = new Email();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(email);
    }

    @Test
    public void testAllArgsConstructor() {
        Email emailWithArgs = new Email(EmailCategory.BUSINESS, "test@example.com");
        assertEquals(EmailCategory.BUSINESS, emailWithArgs.getEmailCategory());
        assertEquals("test@example.com", emailWithArgs.getEmail());
    }

    @Test
    public void testSetEmailCategory() {
        email.setEmailCategory(EmailCategory.PERSONAL);
        assertEquals(EmailCategory.PERSONAL, email.getEmailCategory());
    }

    @Test
    public void testSetEmail() {
        email.setEmail("personal@example.com");
        assertEquals("personal@example.com", email.getEmail());
    }

    @Test
    public void testToString() {
        email.setEmailCategory(EmailCategory.OTHER);
        email.setEmail("other@example.com");
        String expectedString = "Email(emailCategory=OTHER, email=other@example.com)";
        assertEquals(expectedString, email.toString());
    }
}
