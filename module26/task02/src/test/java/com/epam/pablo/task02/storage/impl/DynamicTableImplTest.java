package com.epam.pablo.task02.storage.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.pablo.task02.storage.DataType;
import com.epam.pablo.task02.storage.ValueGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.epam.pablo.task02.config.TableConfig;

class DynamicTableImplTest {
    @Mock
    private JdbcTemplate jdbcTemplate;
    private DataType dataType;
    private ValueGenerator valueGenerator;

    @InjectMocks
    private DynamicTableImpl dynamicTableImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dataType = new PostgresDataTypeImpl();
        valueGenerator = new PostgresValueGeneratorImpl();

        dynamicTableImpl = new DynamicTableImpl(jdbcTemplate, dataType, valueGenerator);
    }

    @Test
    void testCreateAndPopulate() {
        TableConfig tableConfig = new TableConfig();
        tableConfig.setColumnCount(3);
        tableConfig.setTypeCount(2);
        tableConfig.setRowCount(5);

        dynamicTableImpl.createAndPopulate(tableConfig);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(1)).execute(sqlCaptor.capture());
        String createTableSql = sqlCaptor.getValue();
        assertTrue(createTableSql.contains("CREATE TABLE"));

        verify(jdbcTemplate, times(1)).batchUpdate(anyString(), anyList(), any());
    }
}