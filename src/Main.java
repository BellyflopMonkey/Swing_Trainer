import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Golf Swing Pro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 420);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout()) {
            Image bg;
            {
                try {
                    var url = getClass().getResource("/golf.jpg");
                    bg = javax.imageio.ImageIO.read(url);
                } catch (Exception ex) {
                    System.out.println("Image not found: " + ex.getMessage());
                }
            }
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                // Dark overlay behind the form
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRoundRect(60, 60, 350, 280, 20, 20);
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Golf Swing Pro");
        title.setFont(new Font("Georgia", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(title, c);
        c.gridwidth = 1;

        JTextField clubheadField  = new JTextField(10);
        JTextField ballSpeedField = new JTextField(10);
        JTextField clubPathField  = new JTextField(10);

        addRow(panel, c, "Clubhead Speed (mph):", clubheadField,  1);
        addRow(panel, c, "Ball Speed (mph):",      ballSpeedField, 2);
        addRow(panel, c, "Club Path (degrees):",   clubPathField,  3);

        ButtonGroup faceGroup = new ButtonGroup();
        JRadioButton faceOpen   = new JRadioButton("Open");
        JRadioButton faceClosed = new JRadioButton("Closed");
        faceOpen.setForeground(Color.WHITE);   faceOpen.setOpaque(false);
        faceClosed.setForeground(Color.WHITE); faceClosed.setOpaque(false);
        faceGroup.add(faceOpen);
        faceGroup.add(faceClosed);
        faceClosed.setSelected(true);
        JPanel facePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        facePanel.setOpaque(false);
        facePanel.add(faceOpen);
        facePanel.add(faceClosed);
        JLabel faceLabel = new JLabel("Face Angle:");
        faceLabel.setForeground(Color.WHITE);
        c.gridx = 0; c.gridy = 4; panel.add(faceLabel, c);
        c.gridx = 1; c.gridy = 4; panel.add(facePanel, c);

        JButton btn = new JButton("Analyze Swing");
        btn.setBackground(new Color(20, 60, 20));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Georgia", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        panel.add(btn, c);

        JLabel resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("Georgia", Font.BOLD, 13));
        resultLabel.setForeground(Color.WHITE);
        c.gridx = 0; c.gridy = 6; c.gridwidth = 2;
        panel.add(resultLabel, c);

        btn.addActionListener(e -> {
            try {
                double  clubhead = Double.parseDouble(clubheadField.getText().trim());
                double  ball     = Double.parseDouble(ballSpeedField.getText().trim());
                double  path     = Double.parseDouble(clubPathField.getText().trim());
                boolean isOpen   = faceOpen.isSelected();
                double  smash    = ball / clubhead;

                String outcome;
                String drill = "";

                if (smash >= 1.35) {
                    if (path > 2.0)        { outcome = "Neutral Strike / Draw Pattern"; drill = "Consistency Drills"; }
                    else if (!isOpen)      { outcome = "Draw Pattern";                  drill = "Continue with feel"; }
                    else                   { outcome = "Push Slice";                    drill = "Headcover Inside-Out Drill"; }
                } else if (path < -2.0)   { outcome = "Neutral Strike"; }
                else if (isOpen)          { outcome = ball < 130 ? "Push-Slice" : "Big Slice"; }
                else if (path < -4.0)     { outcome = "Pull Hook"; }
                else if (path < -2.0)     { outcome = "Pull Slice"; }
                else                      { outcome = "Inefficient Strike"; drill = "Gate Consistency Drill"; }

                boolean isIssue = outcome.contains("Slice") || outcome.contains("Hook") || outcome.contains("Inefficient");
                resultLabel.setForeground(isIssue ? Color.RED : Color.GREEN);
                resultLabel.setText(String.format("<html>Smash Factor: %.3f<br>Outcome: %s%s</html>",
                        smash, outcome, drill.isEmpty() ? "" : "<br>Drill: " + drill));

            } catch (NumberFormatException ex) {
                resultLabel.setForeground(Color.RED);
                resultLabel.setText("Please enter valid numbers in all fields.");
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void addRow(JPanel p, GridBagConstraints c, String labelText, JTextField field, int row) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Georgia", Font.BOLD, 13));
        c.gridx = 0; c.gridy = row; p.add(label, c);
        c.gridx = 1; c.gridy = row; p.add(field, c);
    }
}