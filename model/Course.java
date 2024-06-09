package model;

import java.util.ArrayList;

import control.Data_Manager;

/**
 * Course object.
 */
public class Course {
    private String code;
    private String name;
    private ArrayList<String> block_ID = new ArrayList<>(); // store only ID Numbers

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Setters and Getters
    public void setCourseCode(String code) {
        this.code = code;
    }

    public void setCourseName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return this.code;
    }

    public String getCourseName() {
        return this.name;
    }

    /**
     * Check if the block is empty.
     * 
     * @return true if the block is empty, otherwise false.
     */
    public boolean isEmpty() {
        return block_ID.isEmpty();
    }

    /**
     * Get the IDs of the enrolled students.
     * 
     * @return IDs of the enrolled students.
     */
    public ArrayList<String> getBlockIDs() {
        return this.block_ID;
    }

    /**
     * Invoke when deleting the course.
     */
    public void courseDelete() {
        // Transfer all students to unenrolled course then clear the block after
        for (int student_count = 0; student_count < block_ID.size(); student_count++) {
            // get the student
            String unenrolled_student_key = new StudentKeyMaker().keyMaker(this.getCourseCode(),
                    block_ID.get(student_count));
            Student unenrolled_student = Data_Manager.studentList().get(unenrolled_student_key);

            // change the student's course code attribute to unenrolled
            unenrolled_student.setCourseCode(Data_Manager.notEnrolled().getCourseCode());

            // add the student's ID to the unenrolled course
            Data_Manager.notEnrolled().getBlockIDs().add(unenrolled_student.getIDNumber());

            // create a new key for the student
            String new_student_key = new StudentKeyMaker().keyMaker(Data_Manager.notEnrolled().getCourseCode(),
                    unenrolled_student.getIDNumber());

            /*
             * Update the key of the student in the hashmap by removing it then adding it
             * again with a different key.
             */
            Data_Manager.studentList().remove(unenrolled_student_key);
            Data_Manager.studentList().put(new_student_key, unenrolled_student);
        }

        block_ID.clear(); // after transferring the students, clear the list
    }
}
