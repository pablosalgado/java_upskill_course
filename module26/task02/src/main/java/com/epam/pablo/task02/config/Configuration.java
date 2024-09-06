package com.epam.pablo.task02.config;

import java.util.List;

public class Configuration {

    private int concurrency;
    private List<TableConfig> tableConfigList;

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public List<TableConfig> getTableConfigList() {
        return tableConfigList;
    }

    public void setTableConfigList(List<TableConfig> tableConfigList) {
        this.tableConfigList = tableConfigList;
    }
}
