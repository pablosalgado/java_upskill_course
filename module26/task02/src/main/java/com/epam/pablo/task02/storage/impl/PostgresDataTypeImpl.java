package com.epam.pablo.task02.storage.impl;

import org.springframework.stereotype.Component;
import com.epam.pablo.task02.storage.DataType;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostgresDataTypeImpl implements DataType {

    private final Map<String, Integer> types = new HashMap<>();

    public PostgresDataTypeImpl() {
        types.put("bool", Types.BOOLEAN);
//        types.put("bit", Types.BIT);
        types.put("int8", Types.BIGINT);
//        types.put("oid", Types.BIGINT);
        types.put("bytea", Types.BINARY);
//        types.put("char", Types.CHAR);
//        types.put("bpchar", Types.CHAR);
        types.put("numeric", Types.REAL);
        types.put("int4", Types.INTEGER);
        types.put("int2", Types.SMALLINT);
        types.put("float4", Types.REAL);
        types.put("float8", Types.DOUBLE);
//        types.put("money", Types.DOUBLE);
        types.put("name", Types.VARCHAR);
        types.put("text", Types.VARCHAR);
        types.put("varchar", Types.VARCHAR);
        types.put("date", Types.DATE);
        types.put("time", Types.TIME);
        types.put("timestamp", Types.TIMESTAMP);
    }

    @Override
    public List<String> getTypes() {
        return types.keySet().stream().sorted().toList();
    }

    @Override
    public int getTypeValue(String type) {
        return types.get(type);
    }
}
