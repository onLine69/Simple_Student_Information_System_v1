package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.swing.JTable;

import model.Course;
import model.Student;
import model.StudentKeyMaker;
import model.Table_Manager;

/**
 * Facilitates the process of converting the data from writer files to objects
 * of Course and Student.
 */
public class Data_Manager {
    // files
    private static final String student_file = "CSV_Files\\Students.csv";
    private static final String course_file = "CSV_Files\\Courses.csv";

    private static HashMap<String, Course> courses = new HashMap<>(); // store the registered courses
    private static HashMap<String, Student> students = new HashMap<>(); // store the registered students
    private static Course unenrolled_course = new Course("N/A", "Unenrolled"); // default course
    private static String[] course_column;
    private static String[] student_column;

    private BufferedReader reader;
    private String line;

    public Data_Manager() {
        courseFileReader();
        studentFileReader();
    }

    /**
     * Process the reading of the data from the student writer file to the student
     * table.
     */
    private void studentFileReader() {
        try {
            // setup the reader
            reader = new BufferedReader(new FileReader(student_file, StandardCharsets.UTF_8));

            // save the column names
            student_column = reader.readLine().split(",");

            // read the lines from the file
            while ((line = reader.readLine()) != null) {
                // split the lines per column from writer
                String[] row = line.split(",");

                // incase there is not enough split words
                if (row.length != student_column.length) {
                    for (String word : row)
                        System.out.printf("%-15s", word);
                    System.out.println();
                    continue;
                }

                // incase the course recorded was deleted or something happened
                Course course = unenrolled_course;
                if (courses.containsKey(row[6])) {
                    course = courses.get(row[6]);
                }

                // create a new student
                Student new_student = new Student(row[0], row[1], row[2], row[3], row[4], row[5],
                        course.getCourseCode());

                // store the new student to the student hashmap with its key
                String new_student_key = new StudentKeyMaker().keyMaker(new_student.getCourseCode(),
                        new_student.getIDNumber());
                students.put(new_student_key, new_student);

                // add the ID number of the student to the enrolled course
                course.getBlockIDs().add(new_student.getIDNumber());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the reading of the data from the course writer file to the course
     * table.
     */
    private void courseFileReader() {
        courses.put(unenrolled_course.getCourseCode(), unenrolled_course);
        try {
            // setup the reader
            reader = new BufferedReader(new FileReader(course_file, StandardCharsets.UTF_8));
            course_column = reader.readLine().split(",");

            while ((line = reader.readLine()) != null) {
                // split the lines per column from writer
                String[] row = line.replaceAll("\"", "").split(",");

                // incase there is not enough split words
                if (row.length != course_column.length) {
                    for (String word : row)
                        System.out.printf("%-15s", word);
                    System.out.println();
                    continue;
                }

                // add the course in the course list
                courses.put(row[0], new Course(row[0], row[1]));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the saving of the data from the course table to the course writer
     * file.
     */
    public static void courseFileSaver() {
        try {
            // setup the table and the writer
            JTable course_table = Table_Manager.getCourseTable();
            FileWriter writer = new FileWriter(new File(course_file), StandardCharsets.UTF_8);

            // save the column names first
            for (int column = 0; column < course_table.getColumnCount(); column++) {
                if (column == course_table.getColumnCount() - 1)
                    writer.write(course_table.getColumnName(column)); // to avoid ',' in the end
                else
                    writer.write(course_table.getColumnName(column) + ",");
            }

            // save the data from the table
            for (int row = 0; row < course_table.getRowCount(); row++) {
                writer.write("\n");
                for (int column = 0; column < course_table.getColumnCount(); column++) {
                    if (column == course_table.getColumnCount() - 1)
                        writer.write(course_table.getValueAt(row, column).toString()); // to avoid ',' in the end
                    else
                        writer.write(course_table.getValueAt(row, column).toString() + ",");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the saving of the data from the student table to the student writer
     * file.
     */
    public static void studentFileSaver() {
        try {
            // setup the table and the writer
            JTable student_table = Table_Manager.getStudentTable();
            FileWriter writer = new FileWriter(new File(student_file), StandardCharsets.UTF_8);

            // save the column names first
            for (int column = 0; column < student_table.getColumnCount(); column++) {
                if (column == student_table.getColumnCount() - 1)
                    writer.write(student_table.getColumnName(column)); // to avoid ',' in the end
                else
                    writer.write(student_table.getColumnName(column) + ",");
            }

            // save the data from the table
            for (int row = 0; row < student_table.getRowCount(); row++) {
                writer.write("\n");
                for (int column = 0; column < student_table.getColumnCount(); column++) {
                    if (column == student_table.getColumnCount() - 1)
                        writer.write(student_table.getValueAt(row, column).toString()); // to avoid ',' in the end
                    else
                        writer.write(student_table.getValueAt(row, column).toString() + ",");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the course list.
     * 
     * @return courses
     */
    public static HashMap<String, Course> coursesList() {
        return courses;
    }

    /**
     * Return the student list.
     * 
     * @return students
     */
    public static HashMap<String, Student> studentList() {
        return students;
    }

    /**
     * Return the column names for the student column.
     * 
     * @return student_column
     */
    public static String[] getStudentColumn() {
        return student_column;
    }

    /**
     * Return the column names for the course column.
     * 
     * @return course_column
     */
    public static String[] getCourseColumn() {
        return course_column;
    }

    /**
     * Return the "Course" for unenrolled student.
     * 
     * @return unenrolled_course
     */
    public static Course notEnrolled() {
        return unenrolled_course;
    }
}
