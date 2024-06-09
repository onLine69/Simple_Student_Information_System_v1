package model;

/**
 * Student object.
 */
public class Student {
    private String surname;
    private String first_name;
    private String middle_name;
    private String ID_number;
    private String year_level;
    private String gender;
    private String course_code;

    public Student(String surname, String first_name, String middle_name, String ID_number, String year_level,
            String gender, String course_code) {
        this.surname = surname;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.ID_number = ID_number;
        this.year_level = year_level;
        this.gender = gender;
        this.course_code = course_code;
    }

    // Setters and Getters

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public void setMiddleName(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getMiddleName() {
        return this.middle_name;
    }

    public void setIDNumber(String ID_number) {
        this.ID_number = ID_number;
    }

    public String getIDNumber() {
        return this.ID_number;
    }

    public void setYearLevel(String year_level) {
        this.year_level = year_level;
    }

    public String getYearLevel() {
        return this.year_level;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setCourseCode(String course_code) {
        this.course_code = course_code;
    }

    public String getCourseCode() {
        return this.course_code;
    }
}
