package control;

import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import model.Course;
import model.Student;
import model.StudentKeyMaker;
import model.Table_Manager;

/**
 * Facilitates the editing of the rows in the tables and their relations.
 */
public class Edit_Process {
    private JTable table;
    private JDialog edit_dialog;
    private boolean theresDuplicate;

    private int filter_column;
    private String filter_input;

    public Edit_Process(JTable table, JDialog edit_dialog, int filter_column, String filter_input) {
        this.table = table;
        this.edit_dialog = edit_dialog;
        this.filter_column = filter_column;
        this.filter_input = filter_input;
    }

    /**
     * If the item to be edited is in the student table.
     * 
     * @param table_row_selected
     * @param new_surname
     * @param new_first_name
     * @param new_middle_name
     * @param new_ID_number
     * @param new_year_level
     * @param new_gender
     * @param new_course_code
     */
    public void studentEdit(int table_row_selected, String new_surname, String new_first_name, String new_middle_name,
            String new_ID_number, String new_year_level, String new_gender, String new_course_code) {
        JTable student_table = this.table;
        theresDuplicate = false;

        /*
         * Filter the table and check if there are rows with the same name as the edit
         * student. Also, check if the attribute of the student is not the same with the
         * original to avoid checking itself.
         */
        String[] selected_row = new String[student_table.getColumnCount()];
        for (int column = 0; column < student_table.getColumnCount(); column++) {
            selected_row[column] = student_table.getValueAt(table_row_selected, column).toString();
        }

        String[] remaining_row = new String[student_table.getColumnCount()]; // remaining row after filtering

        // filter the fullname
        String[] column_data = { new_surname, new_first_name, new_middle_name };
        int[] column_indices = { 0, 1, 2 };
        Filter_Data.multipleFilter(student_table, column_data, column_indices);

        if (student_table.getRowCount() > 0) {
            for (int column = 0; column < student_table.getColumnCount(); column++) {
                remaining_row[column] = student_table.getValueAt(0, column).toString();
            }

            // if the remaining row and selected row are not the same
            if (!Arrays.equals(remaining_row, selected_row)) {
                JOptionPane.showMessageDialog(this.edit_dialog,
                        "Student: " + new_surname + ", " + new_first_name + " " + new_middle_name
                                + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        /*
         * Filter the table and check if there are rows with the same ID number as the
         * edited ID. Check only if there is no duplicate with the name to prevent
         * multiple errors popping up.
         */
        if (!theresDuplicate) {
            Filter_Data.rowFilter(student_table, new_ID_number, 3);
            if (student_table.getRowCount() > 0) {
                for (int column = 0; column < student_table.getColumnCount(); column++) {
                    remaining_row[column] = student_table.getValueAt(0, column).toString();
                }

                // if the remaining row and selected row are not the same
                if (!Arrays.equals(remaining_row, selected_row)) {
                    JOptionPane.showMessageDialog(this.edit_dialog,
                            "ID Number: " + new_ID_number
                                    + "\nalready belongs to another student.",
                            "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                    theresDuplicate = true;
                }
            }
        }

        Filter_Data.cancelFilter(student_table);

        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            // Tables are need to be filtered because of sorting.
            Filter_Data.rowFilter(student_table, selected_row[3], 3);

            // get the old course key of the student and its ID number
            String course_key = student_table.getValueAt(0, 6).toString();
            String ID_num = student_table.getValueAt(0, 3).toString();

            // get the student
            String old_student_key = new StudentKeyMaker().keyMaker(course_key, ID_num);
            Student editing_student = Data_Manager.studentList().get(old_student_key);

            // change the attributes
            editing_student.setSurname(new_surname);
            editing_student.setFirstName(new_first_name);
            editing_student.setMiddleName(new_middle_name);
            editing_student.setIDNumber(new_ID_number);
            editing_student.setYearLevel(new_year_level);
            editing_student.setGender(new_gender);
            editing_student.setCourseCode(new_course_code);

            /*
             * If the course and/or the ID number of the student has been changed,
             * unenrolled from its previous course then enroll to its new. Also change
             * the key of the student.
             */
            if (!selected_row[3].equals(new_ID_number) || !selected_row[6].equals(new_course_code)) {
                // create a new key
                String new_student_key = new StudentKeyMaker().keyMaker(new_course_code, new_ID_number);
                // get the old course of the student
                Course old_course = Data_Manager.coursesList().get(course_key);
                // get the new course of the student
                Course new_course = Data_Manager.coursesList().get(new_course_code);

                /*
                 * Update the key of the student by removing it and adding it again with a new
                 * key.
                 */
                Data_Manager.studentList().remove(old_student_key);
                Data_Manager.studentList().put(new_student_key, editing_student);

                old_course.getBlockIDs().remove(ID_num); // remove student from the old course
                new_course.getBlockIDs().add(new_ID_number); // enroll student to the new course
            }

            // change the values in the row of the edited course
            student_table.setValueAt(new_surname, 0, 0);
            student_table.setValueAt(new_first_name, 0, 1);
            student_table.setValueAt(new_middle_name, 0, 2);
            student_table.setValueAt(new_ID_number, 0, 3);
            student_table.setValueAt(new_year_level, 0, 4);
            student_table.setValueAt(new_gender, 0, 5);
            student_table.setValueAt(new_course_code, 0, 6);

            // if the table is filtered, replicate the filter
            if (filter_column >= 0)
                Filter_Data.regexFilter(student_table, this.filter_input, this.filter_column);
            else
                Filter_Data.cancelFilter(student_table);

            // for confirmation
            JOptionPane.showMessageDialog(this.edit_dialog, "Edit Success.");
            this.edit_dialog.dispose();
        }
    }

    /**
     * If the item to be edited is in the course table.
     * 
     * @param table_row_selected
     * @param new_course_code
     * @param new_course_name
     */
    public void courseEdit(int table_row_selected, String new_course_code, String new_course_name) {
        JTable course_table = this.table;
        theresDuplicate = false;

        theresDuplicate = false;
        // Get the original values.
        String[] selected_row = new String[course_table.getColumnCount()];
        for (int column = 0; column < course_table.getColumnCount(); column++) {
            selected_row[column] = course_table.getValueAt(table_row_selected, column).toString();
        }

        /*
         * Since the course code is the key for every course, check if there is already
         * the same key in the hashmap and if it is changed.
         */
        if (Data_Manager.coursesList().containsKey(new_course_code)
                && !selected_row[0].equals(new_course_code)) {
            JOptionPane.showMessageDialog(this.edit_dialog, "Course Code: " + new_course_code + "\nalready exist.",
                    "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
            theresDuplicate = true;
        }

        /*
         * Filter the table and check if there are rows with the same course name as the
         * new course. Check only if there is no duplicate with the name to prevent
         * multiple errors popping up.
         */
        if (!theresDuplicate && !course_table.getValueAt(table_row_selected, 1).equals(new_course_name)) {
            Filter_Data.rowFilter(course_table, new_course_name, 1);
            // if there are still rows remaining, it means there is a duplicate
            if ((course_table.getRowCount() > 0) && (!course_table.getValueAt(0, 1).equals(selected_row[1]))) {
                JOptionPane.showMessageDialog(this.edit_dialog,
                        "Course Name: " + new_course_name + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        Filter_Data.cancelFilter(course_table);

        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            // Tables are need to be filtered because of sorting.
            Filter_Data.multipleFilter(course_table, selected_row, new int[] { 0, 1 });

            // get the course to be edited
            Course editing_course = Data_Manager.coursesList().get(course_table.getValueAt(0, 0));

            // if the course code was edited
            if (!selected_row[0].equals(new_course_code)) {
                // change the attributes for course code of every student inside the course
                for (String ID_num : editing_course.getBlockIDs()) {
                    // get the student
                    String old_student_key = new StudentKeyMaker().keyMaker(editing_course.getCourseCode(), ID_num);
                    Student student = Data_Manager.studentList().get(old_student_key);

                    student.setCourseCode(new_course_code); // change the course code attribute

                    // new student key
                    String new_student_key = new StudentKeyMaker().keyMaker(new_course_code, ID_num);

                    /*
                     * Update the key of the student by removing it and adding it again with a new
                     * key.
                     */
                    Data_Manager.studentList().remove(old_student_key);
                    Data_Manager.studentList().put(new_student_key, student);
                }

                // change the course code in student table
                JTable student_table = Table_Manager.getStudentTable();
                String old_course_code = course_table.getValueAt(0, 0).toString();

                /*
                 * Filter and traverse the student table since the JTable doesn't update
                 * automatically.
                 */
                Filter_Data.rowFilter(student_table, old_course_code, 6);
                for (int student_row_count = 0; student_row_count < student_table.getRowCount(); student_row_count++) {
                    student_table.setValueAt(new_course_code, student_row_count, 6);
                }
                Filter_Data.cancelFilter(student_table);

                editing_course.setCourseCode(new_course_code); // change the course code
                Data_Manager.coursesList().remove(old_course_code); // remove the old map
                Data_Manager.coursesList().put(new_course_code, editing_course); // add the new map w/ the edited key

                course_table.setValueAt(new_course_code, 0, 0); // change the code in the course table
            }

            // change the course name of the edited course if changed
            if (!selected_row[1].equals(new_course_name)) {
                editing_course = Data_Manager.coursesList().get(new_course_code);

                editing_course.setCourseName(new_course_name); // change the course name

                course_table.setValueAt(new_course_name, 0, 1); // change the name in course table
            }

            // if the table is filtered, replicate the filter
            if (filter_column >= 0)
                Filter_Data.regexFilter(course_table, new_course_name, table_row_selected);
            else
                Filter_Data.cancelFilter(course_table);

            // for confirmation
            JOptionPane.showMessageDialog(this.edit_dialog, "Edit Success.");
            this.edit_dialog.dispose();
        }
    }
}
