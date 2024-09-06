package com.epam.pablo.task02.storage;

import com.epam.pablo.task02.config.TableConfig;

public interface DynamicTable {
    void createAndPopulate(TableConfig tableConfig);
}
