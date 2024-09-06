package com.epam.pablo.task02.storage.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValueGeneratorImplTest {

    private PostgresValueGeneratorImpl valueGenerator;

    @BeforeEach
    void setUp() {
        valueGenerator = new PostgresValueGeneratorImpl();
    }

    @Test
    void testGenerateValueForBoolean() {
        Object value = valueGenerator.generateValue("bool");
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
    }

    @Test
    void testGenerateValueForBit() {
        Object value = valueGenerator.generateValue("bit");
        assertNotNull(value);
        assertTrue(value instanceof Boolean);
    }

    @Test
    void testGenerateValueForInt8() {
        Object value = valueGenerator.generateValue("int8");
        assertNotNull(value);
        assertTrue(value instanceof Long);
    }

    @Test
    void testGenerateValueForOid() {
        Object value = valueGenerator.generateValue("oid");
        assertNotNull(value);
        assertTrue(value instanceof Long);
    }

    @Test
    void testGenerateValueForBytea() {
        Object value = valueGenerator.generateValue("bytea");
        assertNotNull(value);
        assertTrue(value instanceof byte[]);
    }

    @Test
    void testGenerateValueForChar() {
        Object value = valueGenerator.generateValue("char");
        assertNotNull(value);
        assertTrue(value instanceof Character);
    }

    @Test
    void testGenerateValueForBpchar() {
        Object value = valueGenerator.generateValue("bpchar");
        assertNotNull(value);
        assertTrue(value instanceof Character);
    }

    @Test
    void testGenerateValueForNumeric() {
        Object value = valueGenerator.generateValue("numeric");
        assertNotNull(value);
        assertTrue(value instanceof java.math.BigDecimal);
    }

    @Test
    void testGenerateValueForInt4() {
        Object value = valueGenerator.generateValue("int4");
        assertNotNull(value);
        assertTrue(value instanceof Integer);
    }

    @Test
    void testGenerateValueForInt2() {
        Object value = valueGenerator.generateValue("int2");
        assertNotNull(value);
        assertTrue(value instanceof Short);
    }

    @Test
    void testGenerateValueForFloat4() {
        Object value = valueGenerator.generateValue("float4");
        assertNotNull(value);
        assertTrue(value instanceof Float);
    }

    @Test
    void testGenerateValueForFloat8() {
        Object value = valueGenerator.generateValue("float8");
        assertNotNull(value);
        assertTrue(value instanceof Double);        
    }

//    @Test
//    void testGenerateValueForMoney() {
//        Object value = valueGenerator.generateValue("money");
//        assertNotNull(value);
//        assertTrue(value instanceof Double);
//    }

    @Test
    void testGenerateValueForName() {
        Object value = valueGenerator.generateValue("name");
        assertNotNull(value);
        assertTrue(value instanceof String);
        assertTrue(((String) value).startsWith("Name"));
    }

    @Test
    void testGenerateValueForText() {
        Object value = valueGenerator.generateValue("text");
        assertNotNull(value);
        assertTrue(value instanceof String);
        assertTrue(((String) value).startsWith("Text"));
    }

    @Test
    void testGenerateValueForVarchar() {
        Object value = valueGenerator.generateValue("varchar");
        assertNotNull(value);
        assertTrue(value instanceof String);
        assertTrue(((String) value).startsWith("Varchar"));
    }

    @Test
    void testGenerateValueForDate() {
        Object value = valueGenerator.generateValue("date");
        assertNotNull(value);
        assertTrue(value instanceof java.sql.Date);
    }

    @Test
    void testGenerateValueForTime() {
        Object value = valueGenerator.generateValue("time");
        assertNotNull(value);
        assertTrue(value instanceof java.sql.Time);
    }

    @Test
    void testGenerateValueForTimestamp() {
        Object value = valueGenerator.generateValue("timestamp");
        assertNotNull(value);
        assertTrue(value instanceof java.sql.Timestamp);
    }

    @Test
    void testGenerateValueForUnknownType() {
        assertThrows(IllegalArgumentException.class, () -> valueGenerator.generateValue("unknown"));
    }
}