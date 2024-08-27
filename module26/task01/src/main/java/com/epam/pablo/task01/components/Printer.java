package com.epam.pablo.task01.components;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Arrays;

@Component
public class Printer {

    public void printTable(List<String[]> table, String[] headers) {
        // Calculate the maximum width of each column
        int[] columnWidths = Arrays.stream(headers)
            .mapToInt(String::length)
            .toArray();

        for (String[] row : table) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }

        // Print the headers
        printRow(headers, columnWidths);
        printSeparator(columnWidths);

        // Print the table rows
        for (String[] row : table) {
            printRow(row, columnWidths);
        }
    }

    private void printRow(String[] row, int[] columnWidths) {
        for (int i = 0; i < row.length; i++) {
            System.out.print("| " + padRight(row[i], columnWidths[i]) + " ");
        }
        System.out.println("|");
    }

    private void printSeparator(int[] columnWidths) {
        for (int width : columnWidths) {
            System.out.print("+-" + "-".repeat(width) + "-");
        }
        System.out.println("+");
    }

    private String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }
}
