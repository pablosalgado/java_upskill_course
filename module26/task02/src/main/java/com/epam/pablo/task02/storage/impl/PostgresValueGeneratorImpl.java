package com.epam.pablo.task02.storage.impl;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import com.epam.pablo.task02.storage.ValueGenerator;
import java.math.BigDecimal;

@Component
public class PostgresValueGeneratorImpl implements ValueGenerator {
    private static final Random random = new Random();
    private final Map<String, Supplier<Object>> valueGenerator = new HashMap<>();

    public PostgresValueGeneratorImpl() {
        valueGenerator.put("bool", this::generateBoolean);
        valueGenerator.put("bit", this::generateBoolean);
        valueGenerator.put("int8", this::generateLong);
        valueGenerator.put("oid", this::generateLong);
        valueGenerator.put("bytea", this::generateByteArray);
        valueGenerator.put("char", this::generateChar);
        valueGenerator.put("bpchar", this::generateChar);
        valueGenerator.put("numeric", this::generateBigDecimal);
        valueGenerator.put("int4", this::generateInteger);
        valueGenerator.put("int2", this::generateShort);
        valueGenerator.put("float4", this::generateFloat);
        valueGenerator.put("float8", this::generateDouble);
        valueGenerator.put("money", this::generateFloat);
        valueGenerator.put("name", () -> generateString("Name"));
        valueGenerator.put("text", () -> generateString("Text"));
        valueGenerator.put("varchar", () -> generateString("Varchar"));
        valueGenerator.put("date", this::generateDate);
        valueGenerator.put("time", this::generateTime);
        valueGenerator.put("timestamp", this::generateTimestamp);
    }

    private Boolean generateBoolean() {
        return random.nextBoolean();
    }

    private Long generateLong() {
        return random.nextLong();
    }

    private byte[] generateByteArray() {
        byte[] byteArray = new byte[random.nextInt(10) + 1];
        random.nextBytes(byteArray);
        return byteArray;
    }

    private Character generateChar() {
        return (char) (random.nextInt(65536));
    }

    private BigDecimal generateBigDecimal() {
        return new BigDecimal(random.nextDouble());
    }

    private Integer generateInteger() {
        return random.nextInt();
    }

    private Short generateShort() {
        return (short) random.nextInt(Short.MAX_VALUE + 1);
    }

    private Float generateFloat() {
        return random.nextFloat();
    }

    private Double generateDouble() {
        return random.nextDouble();
    }

    private String generateString(String prefix) {
        return prefix + random.nextInt(1000);
    }

    private java.sql.Date generateDate() {
        int year = random.nextInt(100) + 1900;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        return java.sql.Date.valueOf(String.format("%04d-%02d-%02d", year, month, day));
    }

    private java.sql.Time generateTime() {
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);
        return java.sql.Time.valueOf(String.format("%02d:%02d:%02d", hour, minute, second));
    }

    private java.sql.Timestamp generateTimestamp() {
        return new java.sql.Timestamp(generateDate().getTime());
    }

    @Override
    public Object generateValue(String dataType) {
        Supplier<Object> generator = valueGenerator.get(dataType);
        if (generator != null) {
            return generator.get();
        }
        throw new IllegalArgumentException("No generator defined for data type: " + dataType);
    }
}