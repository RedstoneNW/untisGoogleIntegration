/*
 * Created by JFormDesigner on Sat Feb 10 21:29:33 CET 2024
 */

package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import dataHandlement.Config;
import dataHandlement.GoogleCalendarAPI;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

/**
 * @author redst
 */
public class GoogleCalendarForm extends JPanel {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        JFrame frame = new JFrame();
        frame.add(new GoogleCalendarForm());
        frame.pack();
        frame.setTitle("Untis Google Calendar Integration");
        frame.setMinimumSize(new Dimension(705,400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        System.out.println("FINISHED");
    }
    public GoogleCalendarForm() {
        initComponents();
    }

    private void next(ActionEvent e) {
        Config config;
        try {
            config = new Config();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        GoogleCalendarAPI calendarAPI;
        try {
            calendarAPI = new GoogleCalendarAPI(new Config());
        } catch (IOException | GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
        String pageToken = null;
        do {
            CalendarList calendarList;
            try {
                calendarList = calendarAPI.getCalendars(pageToken);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            List<CalendarListEntry> items = calendarList.getItems();
            for (CalendarListEntry calendarListEntry : items) {
                if (Objects.equals(calendarListEntry.getAccessRole(), "writer") || Objects.equals(calendarListEntry.getAccessRole(), "owner")) {
                    if (calendarListEntry.getSummary().equals(list1.getSelectedValue().toString())) {
                        config.setCalendarToStore(calendarListEntry.getId());
                    }
                }
            }
            pageToken= calendarList.getNextPageToken();
        } while (pageToken != null);
        // TODO Open Next Screen
    }

    private void button1(ActionEvent e) {
        GoogleCalendarAPI calendarAPI = null;
        try {
            calendarAPI = new GoogleCalendarAPI(new Config());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        String pageToken = null;
        do {
            CalendarList calendarList = null;
            try {
                calendarList = calendarAPI.getCalendars(pageToken);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            List<CalendarListEntry> items = calendarList.getItems();
            for (CalendarListEntry calendarListEntry : items) {
                if (Objects.equals(calendarListEntry.getAccessRole(), "writer") || Objects.equals(calendarListEntry.getAccessRole(), "owner")) {
                    listModel.addElement(calendarListEntry.getSummary());
                }
            }
            pageToken= calendarList.getNextPageToken();
        } while (pageToken != null);
        list1.setModel(listModel);
        list1.updateUI();
    }

    private void list1ValueChanged(ListSelectionEvent e) {
        next.setEnabled(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - N
        this2 = new JPanel();
        heading1 = new JLabel();
        vSpacer1 = new JPanel(null);
        hSpacer1 = new JPanel(null);
        button1 = new JButton();
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        list1 = new JList();
        next = new JButton();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
        0,0,0,0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder
        .BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt.Color.
        red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.
        beans.PropertyChangeEvent e){if("bord\u0065r".equals(e.getPropertyName()))throw new RuntimeException();}});
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]"));

        //======== this2 ========
        {
            this2.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[238,fill]" +
                "[94,fill]" +
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
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[24]" +
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
                "[0]" +
                "[]" +
                "[]" +
                "[0]" +
                "[]" +
                "[]" +
                "[]"));

            //---- heading1 ----
            heading1.setText("Google Integration");
            heading1.setHorizontalAlignment(SwingConstants.CENTER);
            heading1.setFont(heading1.getFont().deriveFont(heading1.getFont().getStyle() | Font.BOLD, heading1.getFont().getSize() + 3f));
            this2.add(heading1, "cell 0 1 39 1");
            this2.add(vSpacer1, "cell 2 2");
            this2.add(hSpacer1, "cell 0 3");

            //---- button1 ----
            button1.setText("Connect your Google Account");
            button1.addActionListener(e -> button1(e));
            this2.add(button1, "cell 2 3");

            //---- label1 ----
            label1.setText("Select the calendar you want to store the timetable in:");
            label1.setAlignmentY(1.0F);
            this2.add(label1, "cell 2 7,aligny bottom,growy 0");

            //======== scrollPane1 ========
            {

                //---- list1 ----
                list1.addListSelectionListener(e -> list1ValueChanged(e));
                scrollPane1.setViewportView(list1);
            }
            this2.add(scrollPane1, "cell 2 8 1 24");

            //---- next ----
            next.setText("Next");
            next.setEnabled(false);
            next.addActionListener(e -> next(e));
            this2.add(next, "cell 24 33 13 1");
        }
        add(this2, "cell 0 0 1 3");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - N
    private JPanel this2;
    private JLabel heading1;
    private JPanel vSpacer1;
    private JPanel hSpacer1;
    private JButton button1;
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JList list1;
    private JButton next;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
