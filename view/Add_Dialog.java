package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import control.Add_Process;
import control.Data_Manager;
import model.Course;
import model.Table_Manager;

/**
 * Display and facilitates the adding row dialog and editing the data of the
 * selected rows
 */
public class Add_Dialog extends JDialog {
    private final int DIALOG_WIDTH = 400;
    private final int DIALOG_HEIGHT = 300;
    private final Dimension dialog_dimension = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);

    private final GridBagLayout grid_bag_layout = new GridBagLayout();
    private final GridBagConstraints layout_Constraints = new GridBagConstraints();

    // for student data
    private JLabel surname_label;
    private JTextField surname_data;
    private JLabel first_name_label;
    private JTextField first_name_data;
    private JLabel middle_name_label;
    private JTextField middle_name_data;
    private JLabel ID_number_label;
    private JTextField ID_number_data;
    private JLabel year_level_label;
    private JComboBox<String> year_level_data;
    private JLabel gender_label;
    private JTextField gender_data;
    private JLabel course_label;
    private JComboBox<String> course_data;

    // for course data
    private JLabel course_code_label;
    private JTextField course_code_data;
    private JLabel course_name_label;
    private JTextField course_name_data;

    private JButton add_button;

    private Add_Process add_data;

    public Add_Dialog(JTable table, JFrame main) {
        // setup the dialog
        this.setTitle("Adding Item:");
        this.getContentPane().setPreferredSize(dialog_dimension);
        this.setResizable(false);
        this.setLayout(grid_bag_layout);
        this.pack();
        this.setLocationRelativeTo(main);
        this.setModalityType(DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        add_data = new Add_Process(table, this);

        // if the table selected is the student table, else use the data from the course
        // table
        if (table.equals(Table_Manager.getStudentTable()))
            displayAddStudent(table);
        else
            displayAddCourse(table);
    }

    /**
     * JDialog for adding course.
     * 
     * @param course_table
     */
    private void displayAddCourse(JTable course_table) {
        // Arranging the displays
        course_code_label = new JLabel("Course Code: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 0;
        layout_Constraints.gridwidth = 2;
        this.add(course_code_label, layout_Constraints);

        course_code_data = new JTextField();
        course_code_data.setPreferredSize(new Dimension(200, 30));
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 1;
        layout_Constraints.gridwidth = 2;
        this.add(course_code_data, layout_Constraints);

        course_name_label = new JLabel("Course Name: ");
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 2;
        layout_Constraints.gridwidth = 2;
        this.add(course_name_label, layout_Constraints);

        course_name_data = new JTextField();
        course_name_data.setPreferredSize(new Dimension(200, 30));
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 3;
        layout_Constraints.gridwidth = 2;
        this.add(course_name_data, layout_Constraints);

        // arranging the button and setting its functionality
        add_button = new JButton("Add Item");
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 9;
        layout_Constraints.gridwidth = 2;
        add_button.setFocusable(false);
        add_button.setToolTipText("Add the item to the table.");
        add_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if there is at least one field empty
                if (course_code_data.getText().isEmpty() || course_name_data.getText().isEmpty())
                    JOptionPane.showMessageDialog(Add_Dialog.this, "Fill all fields.");
                // add the new data to the table then close the dialog
                else
                    add_data.courseAdd(course_code_data.getText().toString(), course_name_data.getText().toString());
            }
        });
        this.add(add_button, layout_Constraints);
    }

    /**
     * JDialog for adding students.
     * 
     * @param student_table
     */
    private void displayAddStudent(JTable student_table) {
        // Arranging the displays
        surname_label = new JLabel("Surname: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 0;
        this.add(surname_label, layout_Constraints);

        surname_data = new JTextField();
        surname_data.setPreferredSize(new Dimension(200, 30));
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 0;
        this.add(surname_data, layout_Constraints);

        first_name_label = new JLabel("First Name: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 1;
        this.add(first_name_label, layout_Constraints);

        first_name_data = new JTextField();
        first_name_data.setPreferredSize(new Dimension(200, 30));
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 1;
        this.add(first_name_data, layout_Constraints);

        middle_name_label = new JLabel("Middle Name: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 2;
        this.add(middle_name_label, layout_Constraints);

        middle_name_data = new JTextField();
        middle_name_data.setPreferredSize(new Dimension(200, 30));
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 2;
        this.add(middle_name_data, layout_Constraints);

        ID_number_label = new JLabel("ID Number: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 3;
        this.add(ID_number_label, layout_Constraints);

        ID_number_data = new JTextField();
        ID_number_data.setPreferredSize(new Dimension(200, 30));
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 3;
        this.add(ID_number_data, layout_Constraints);

        year_level_label = new JLabel("Year Level: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 4;
        this.add(year_level_label, layout_Constraints);

        year_level_data = new JComboBox<>(
                new String[] { "1st year", "2nd year", "3rd year", "4th year", "5th year", "6th year", "More..." });
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 4;
        this.add(year_level_data, layout_Constraints);

        gender_label = new JLabel("Gender: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 5;
        this.add(gender_label, layout_Constraints);

        gender_data = new JTextField();
        gender_data.setPreferredSize(new Dimension(200, 30));
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 5;
        this.add(gender_data, layout_Constraints);

        course_label = new JLabel("Course Code: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 6;
        this.add(course_label, layout_Constraints);

        // list the courses available in a readable way
        String[] courses_listed = new String[Data_Manager.coursesList().size()];
        int course_count = 0;
        for (Course course : Data_Manager.coursesList().values()) {
            courses_listed[course_count] = course.getCourseCode() + "-" + course.getCourseName();
            course_count++;
        }
        Arrays.sort(courses_listed);

        course_data = new JComboBox<>(courses_listed);
        course_data.setPreferredSize(new Dimension(300, 30));
        // set the default selection to unenrolled
        course_data.setSelectedItem(
                Data_Manager.notEnrolled().getCourseCode() + "-" + Data_Manager.notEnrolled().getCourseName());
        course_data.addActionListener(e -> {
            course_data.setToolTipText(course_data.getSelectedItem().toString());
        });
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 7;
        layout_Constraints.gridwidth = 2;
        this.add(course_data, layout_Constraints);

        // arranging the button and setting its functionality
        add_button = new JButton("Add Item");
        add_button.setFocusable(false);
        add_button.setToolTipText("Add the item to the table.");
        add_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if there is at least one field empty
                if (surname_data.getText().isEmpty() || first_name_data.getText().isEmpty() ||
                        middle_name_data.getText().isEmpty() || ID_number_data.getText().isEmpty() ||
                        gender_data.getText().isEmpty())
                    JOptionPane.showMessageDialog(Add_Dialog.this, "Fill all fields.");

                // add the new data to the table then close the dialog
                else
                    add_data.studentAdd(surname_data.getText().toString(), first_name_data.getText().toString(),
                            middle_name_data.getText().toString(), ID_number_data.getText().toString(),
                            year_level_data.getSelectedItem().toString(),
                            gender_data.getText().toString(), course_data.getSelectedItem().toString().split("-")[0]);
            }
        });
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 9;
        layout_Constraints.gridwidth = 2;
        this.add(add_button, layout_Constraints);
    }
}
