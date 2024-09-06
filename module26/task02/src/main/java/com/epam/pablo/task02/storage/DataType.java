package com.epam.pablo.task02.storage;

import java.util.List;

public interface DataType {

    /**
     * Returns a list of string representing the types of data suported by the underlying RDBMS.
     *
     * @return a list of string representing the types of data
     */
    List<String> getTypes();

    int getTypeValue(String type);
}
