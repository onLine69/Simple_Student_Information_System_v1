package control;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Facilitates the filtering/searching of rows based on the inputs.
 */
public class Filter_Data {
    private static DefaultTableModel def_model;
    private static TableRowSorter<DefaultTableModel> row_sorter;

    public Filter_Data() {
    }

    /**
     * Filter the tables based on a regex following the parameter.
     * 
     * @param table
     * @param input_search
     * @param column_index
     */
    public static void regexFilter(JTable table, String input_search, int column_index) {
        def_model = (DefaultTableModel) table.getModel();
        row_sorter = new TableRowSorter<DefaultTableModel>(def_model);
        table.setRowSorter(row_sorter);
        table.getRowSorter().toggleSortOrder(0); // to still sort the filtered data based on the first column

        row_sorter.setRowFilter(RowFilter.regexFilter(input_search, column_index));
    }

    /**
     * Filter the tables based on the exact parameter.
     * 
     * @param table
     * @param input_search
     * @param column_index
     */
    public static void rowFilter(JTable table, String input_search, int column_index) {
        def_model = (DefaultTableModel) table.getModel();
        row_sorter = new TableRowSorter<DefaultTableModel>(def_model);
        table.setRowSorter(row_sorter);
        table.getRowSorter().toggleSortOrder(0); // to still sort the filtered data based on the first column

        // to filter the exact pattern
        String text_regex = Pattern.quote(input_search);
        String search_text = String.format("^%s$", text_regex);

        row_sorter.setRowFilter(RowFilter.regexFilter(search_text, column_index));
    }

    /**
     * Filter the tables with respect to multiple columns. Used for searching.
     * 
     * @param table
     * @param columns
     * @param indices
     */
    public static void multipleFilter(JTable table, String[] columns, int[] indices) {
        def_model = (DefaultTableModel) table.getModel();
        row_sorter = new TableRowSorter<DefaultTableModel>(def_model);
        table.setRowSorter(row_sorter);

        List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(columns.length);

        // filter the exact pattern
        for (int count = 0; count < columns.length; count++)
            filters.add(RowFilter.regexFilter(String.format("^%s$", Pattern.quote(columns[count])), indices[count]));

        row_sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    /**
     * Cancel the filter.
     * 
     * @param table
     */
    public static void cancelFilter(JTable table) {
        def_model = (DefaultTableModel) table.getModel();
        row_sorter = new TableRowSorter<DefaultTableModel>(def_model);
        table.setRowSorter(row_sorter);
        table.getRowSorter().toggleSortOrder(0); // to still sort the filtered data based on the first column

        row_sorter.setRowFilter(null);
    }
}
