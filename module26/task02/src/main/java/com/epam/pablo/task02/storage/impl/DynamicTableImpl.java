package com.epam.pablo.task02.storage.impl;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.epam.pablo.task02.config.TableConfig;
import com.epam.pablo.task02.storage.DataType;
import com.epam.pablo.task02.storage.DynamicTable;
import com.epam.pablo.task02.storage.ValueGenerator;

@Component
@Scope("prototype")
public class DynamicTableImpl implements DynamicTable{

    private static final Logger logger = LoggerFactory.getLogger(DynamicTableImpl.class);
    private static final int BATCH_SIZE = 50000;
    private final JdbcTemplate jdbcTemplate;
    private final DataType dataType;
    private final ValueGenerator valueGenerator;

    public DynamicTableImpl(JdbcTemplate jdbcTemplate, DataType dataType, ValueGenerator valueGenerator) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataType = dataType;
        this.valueGenerator = valueGenerator;
    }

    @Override
    public void createAndPopulate(TableConfig tableConfig) {
        var columnTypes = generateColumnTypes(tableConfig.getColumnCount(), tableConfig.getTypeCount());
        var tableName = createTable(columnTypes);
        populateTable(tableName, columnTypes, tableConfig.getRowCount());
    }

    private String generateUniqueTableName() {
        return "table_" + UUID.randomUUID().toString().replace("-", "_");
    }

    private List<String> generateColumnTypes(int columnCount, int typeCount) {
        return new Random().ints(0, typeCount)
                .limit(columnCount)
                .mapToObj(i -> dataType.getTypes().get(i))
                .toList();
    } 

    private String createTable(List<String> columnTypes) {
        var tableName = generateUniqueTableName();

        logger.info("Creating table: {}", tableName);

        var sql = "CREATE TABLE " + tableName + " (id SERIAL PRIMARY KEY, " +
                IntStream.range(0, columnTypes.size())
                .mapToObj(i -> "column_" + i + " " + columnTypes.get(i))
                .reduce((s1, s2) -> s1 + ", " + s2)
                .get() + ");";

        jdbcTemplate.execute(sql);

        logger.info("Table created: {}", tableName);

        return tableName;
    }

    private void populateTable(String tableName, List<String> columnTypes, int rowCount) {
        logger.info("Populating table: {}", tableName);

        String sql = generateInsertStatement(tableName, columnTypes);
        List<Object[]> batchArgs = IntStream.range(0, rowCount)
                .mapToObj(i -> generateInsertArguments(columnTypes))
                .toList();

        int[] argTypes = columnTypes.stream()
                .mapToInt(dataType::getTypeValue)
                .toArray();

        for (var i = 0; i < batchArgs.size(); i += BATCH_SIZE) {
            int end = Math.min(batchArgs.size(), i + BATCH_SIZE);
            List<Object[]> batchPart = batchArgs.subList(i, end);
            jdbcTemplate.batchUpdate(sql, batchPart, argTypes);
        }

        logger.info("Table populated: {}", tableName);
    }

    private String generateInsertStatement(String tableName, List<String> columnTypes) {
        return "INSERT INTO " + tableName + " (" +
                IntStream.range(0, columnTypes.size())
                    .mapToObj(i -> "column_" + i)
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .get() + ") VALUES (" +
                IntStream.range(0, columnTypes.size())
                    .mapToObj(i -> "?")
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .get() + ");";
    }

    private Object[] generateInsertArguments(List<String> columnTypes) {
        return columnTypes.stream()
                .map(valueGenerator::generateValue)
                .toArray();
    }
}
