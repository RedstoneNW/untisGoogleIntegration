package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;
import org.jdesktop.beansbinding.*;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import com.formdev.flatlaf.FlatDarkLaf;
/*
 * Created by JFormDesigner on Sat Oct 28 14:27:11 CEST 2023
 */

/**
 * @author redst
 */
public class PrivacyPolicy extends JPanel {
    static JFrame frame = new JFrame();

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        frame.add(new PrivacyPolicy());
        frame.pack();
        frame.setTitle("Untis Google Calendar Integration");
        frame.setMinimumSize(new Dimension(705,400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        System.out.println("FINISHED");
    }
    public PrivacyPolicy() {
        initComponents();
    }

    private void checkBox1(ActionEvent e) {
    }

    private void next(ActionEvent e) {
        frame.dispose();
        //TODO open next screen
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - N
        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
        // Generated using JFormDesigner Evaluation license - N
        JLabel heading1 = new JLabel();
        JPanel vSpacer1 = new JPanel(null);
        JPanel hSpacer1 = new JPanel(null);
        JScrollPane scrollPane1 = new JScrollPane();
        JTextArea textArea1 = new JTextArea();
        JCheckBox checkBox1 = new JCheckBox();
        JButton next = new JButton();
        // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

        //======== this ========
        setMinimumSize(new Dimension(705, 400));
        setPreferredSize(new Dimension(705, 400));
        setMaximumSize(new Dimension(705, 400));
        setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[366,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[153,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[1,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- heading1 ----
        heading1.setText("Privacy Policy");
        heading1.setHorizontalAlignment(SwingConstants.CENTER);
        heading1.setFont(heading1.getFont().deriveFont(heading1.getFont().getStyle() | Font.BOLD, heading1.getFont().getSize() + 3f));
        add(heading1, "cell 2 0 35 1");
        add(vSpacer1, "cell 2 1");
        add(hSpacer1, "cell 1 2");

        //======== scrollPane1 ========
        {
            scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            //---- textArea1 ----
            String privacyPolicy = "Add Privacy Policy LATER";
            textArea1.setText(privacyPolicy);
            textArea1.setEditable(false);
            textArea1.setCaretPosition(1);
            scrollPane1.setViewportView(textArea1);
        }
        add(scrollPane1, "cell 2 2 33 19,dock center");

        //---- checkBox1 ----
        checkBox1.setText("I have read and agree to the Untis Google Calendar Integration Privacy Policy");
        checkBox1.addActionListener(this::checkBox1);
        add(checkBox1, "cell 2 24");

        //---- next ----
        next.setText("Next");
        next.setEnabled(false);
        next.addActionListener(this::next);
        add(next, "cell 22 24 13 1");

        //---- bindings ----
        BindingGroup bindingGroup = new BindingGroup();
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
            checkBox1, BeanProperty.create("selected"),
            next, BeanProperty.create("enabled")));
        bindingGroup.bind();
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
}
