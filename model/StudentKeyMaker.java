package model;

/**
 * Create a student key to minimize typo in the code.
 */
public class StudentKeyMaker {
    public StudentKeyMaker() {
    }

    /**
     * Student Key maker.
     * 
     * @param course_code
     * @param ID_num
     * @return student key
     */
    public String keyMaker(String course_code, String ID_num) {
        String point = "(/* 0 *)/->"; // key point
        String separator = "|/(* 0 *)\\|"; // details separator
        return course_code + separator + ID_num + point;
    }
}
