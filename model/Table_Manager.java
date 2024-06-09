package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import control.Data_Manager;

/**
 * Facilitates the creation and functionalities of the tables
 */
public class Table_Manager {
    private static JTable students_table;
    private static JTable courses_table;
    private static DefaultTableModel courses_table_model;
    private static DefaultTableModel student_table_model;
    private JPopupMenu tablePopupMenu;

    public Table_Manager(JFrame main) {
        new Data_Manager();
        processCourseTable(main);
        processStudentTable(main);
    }

    /**
     * Process the initial data and the functionalities of the student table
     */
    private void processStudentTable(JFrame main) {
        // setup the table and its model
        student_table_model = new DefaultTableModel(0, 0) {
            // prevent editing directly in the cell table
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        student_table_model.setColumnIdentifiers(Data_Manager.getStudentColumn());
        students_table = new JTable(student_table_model);
        students_table.getTableHeader().setReorderingAllowed(false); // to make columns not movable
        students_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // to prevent multiple selection
        // sort the table (click the column header)
        students_table.setAutoCreateRowSorter(true);
        // auto sort based on the first column
        students_table.getRowSorter().toggleSortOrder(0);
        tablePopupMenu = new JPopupMenu();

        JMenuItem count = new JMenuItem("Student Count");
        count.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(main,
                        "There are currently " + students_table.getRowCount() + " student listed.", "Student count",
                        JOptionPane.CLOSED_OPTION);
            }
        });
        tablePopupMenu.add(count);
        students_table.setComponentPopupMenu(tablePopupMenu);

        // traverse the student list
        for (Student student : Data_Manager.studentList().values())
            student_table_model.addRow(new Object[] { student.getSurname(), student.getFirstName(),
                    student.getMiddleName(), student.getIDNumber(), student.getYearLevel(), student.getGender(),
                    student.getCourseCode() });
    }

    /**
     * Process the initial data and the functionalities of the course table
     */
    private void processCourseTable(JFrame main) {
        // setup the table and its model
        courses_table_model = new DefaultTableModel(0, 0) {
            // prevent editing directly in the cell table
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courses_table_model.setColumnIdentifiers(Data_Manager.getCourseColumn());
        courses_table = new JTable(courses_table_model);
        courses_table.getTableHeader().setReorderingAllowed(false); // to make columns not movable
        courses_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // to prevent multiple selection
        // sort the table (click the column header)
        courses_table.setAutoCreateRowSorter(true);
        // auto sort based on the first column
        courses_table.getRowSorter().toggleSortOrder(0);
        tablePopupMenu = new JPopupMenu();

        // show how many course listed
        JMenuItem count = new JMenuItem("Course Count");
        count.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(main,
                        "There are currently " + courses_table.getRowCount() + " courses listed.", "Course count",
                        JOptionPane.CLOSED_OPTION);
            }
        });
        tablePopupMenu.add(count);

        tablePopupMenu.add(new JSeparator());

        // display number of unenrolled students
        JMenuItem unenrolled_count = new JMenuItem("Unenrolled Students");
        unenrolled_count.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(main,
                        "There are currently " + Data_Manager.notEnrolled().getBlockIDs().size()
                                + " student\\s unenrolled.",
                        "Unenrolled count", JOptionPane.CLOSED_OPTION);
            }
        });
        tablePopupMenu.add(unenrolled_count);

        tablePopupMenu.add(new JSeparator());

        // show the student count
        JMenuItem details = new JMenuItem("Student Enrolled");
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (courses_table.getSelectedRow() < 0)
                    JOptionPane.showMessageDialog(main,
                            "Select a course.", "No Selection",
                            JOptionPane.CLOSED_OPTION);
                else {
                    Course selected_course = Data_Manager.coursesList()
                            .get(courses_table.getValueAt(courses_table.getSelectedRow(), 0));
                    JOptionPane.showMessageDialog(main,
                            "There are currently " + selected_course.getBlockIDs().size() + " students enrolled.",
                            "Student enrolled",
                            JOptionPane.CLOSED_OPTION);
                }
            }
        });
        tablePopupMenu.add(details);

        courses_table.setComponentPopupMenu(tablePopupMenu);

        // traverse the course list then add their details to the tables
        for (Course course : Data_Manager.coursesList().values()) {
            // skip showing in the table the unenrolled course
            if (course.getCourseCode().equals("N/A") && course.getCourseName().equals("Unenrolled"))
                continue;

            courses_table_model.addRow(new Object[] { course.getCourseCode(), course.getCourseName() });
        }
    }

    /**
     * For getting the student table.
     * 
     * @return Table_Manager.student_table
     */
    public static JTable getStudentTable() {
        return Table_Manager.students_table;
    }

    /**
     * For getting the course table.
     * 
     * @return Table_Manager.courses_table
     */
    public static JTable getCourseTable() {
        return Table_Manager.courses_table;
    }
}
