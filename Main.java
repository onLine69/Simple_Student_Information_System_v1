import javax.swing.SwingUtilities;
import view.SSISMainDisplay;

/**
 * Runs the SSIS app.
 *
 * @author Caine Ivan R. Bautista
 * @date January 31, 2024 to February 22, 2024
 */
public class Main {
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(SSISMainDisplay::new);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}