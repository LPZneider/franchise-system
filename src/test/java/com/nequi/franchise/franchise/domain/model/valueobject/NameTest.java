package com.nequi.franchise.franchise.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    void constructor_WithValidName_ShouldCreateName() {
        String validName = "Test Name";
        
        Name name = new Name(validName);
        
        assertEquals(validName, name.getValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void constructor_WithInvalidName_ShouldThrowException(String invalidName) {
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    void equals_WithSameValue_ShouldReturnTrue() {
        Name name1 = new Name("Test Name");
        Name name2 = new Name("Test Name");
        
        assertEquals(name1, name2);
    }

    @Test
    void equals_WithDifferentValue_ShouldReturnFalse() {
        Name name1 = new Name("Test Name 1");
        Name name2 = new Name("Test Name 2");
        
        assertNotEquals(name1, name2);
    }

    @Test
    void hashCode_WithSameValue_ShouldBeSame() {
        Name name1 = new Name("Test Name");
        Name name2 = new Name("Test Name");
        
        assertEquals(name1.hashCode(), name2.hashCode());
    }

    @Test
    void toString_ShouldReturnValue() {
        String value = "Test Name";
        Name name = new Name(value);
        
        assertEquals(value, name.toString());
    }
}
