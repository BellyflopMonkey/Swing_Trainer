import javax.swing.*;
import java.awt.*;

public class Main {
    // A class-level variable to store the reasoning for the most recent analysis
    private static String logicExplanation = "Please perform a swing analysis first to see the AI reasoning.";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Golf Swing Pro - AI Expert System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 520);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout()) {
            Image bg;
            {
                try {
                    var url = getClass().getResource("/golf.jpg");
                    if (url != null) bg = javax.imageio.ImageIO.read(url);
                } catch (Exception ex) {
                    System.out.println("Background image not found: " + ex.getMessage());
                }
            }

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
                // Dark rounded overlay for the form background
                g.setColor(new Color(0, 0, 0, 170));
                g.fillRoundRect(40, 40, 400, 410, 20, 20);
            }
        };

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        // Title Section
        JLabel title = new JLabel("Golf Swing Pro: AI Expert System");
        title.setFont(new Font("Georgia", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(title, c);

        // Input Fields
        c.gridwidth = 1;
        JTextField clubheadField = new JTextField(10);
        JTextField ballSpeedField = new JTextField(10);
        JTextField clubPathField = new JTextField(10);

        addRow(panel, c, "Clubhead Speed (mph):", clubheadField, 1);
        addRow(panel, c, "Ball Speed (mph):", ballSpeedField, 2);
        addRow(panel, c, "Club Path (e.g. +3.5 or -2.0):", clubPathField, 3);

        // Face Angle Radio Buttons
        ButtonGroup faceGroup = new ButtonGroup();
        JRadioButton faceOpen = new JRadioButton("Open");
        JRadioButton faceClosed = new JRadioButton("Closed");
        faceOpen.setForeground(Color.WHITE); faceOpen.setOpaque(false);
        faceClosed.setForeground(Color.WHITE); faceClosed.setOpaque(false);
        faceGroup.add(faceOpen); faceGroup.add(faceClosed);
        faceClosed.setSelected(true);

        JPanel facePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        facePanel.setOpaque(false);
        facePanel.add(faceOpen);
        facePanel.add(faceClosed);

        JLabel faceLabel = new JLabel("Face Angle:");
        faceLabel.setForeground(Color.WHITE);
        faceLabel.setFont(new Font("Georgia", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 4; panel.add(faceLabel, c);
        c.gridx = 1; c.gridy = 4; panel.add(facePanel, c);

        // Primary Analyze Button
        JButton btn = new JButton("Analyze Swing Mechanics");
        btn.setBackground(new Color(34, 139, 34));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Georgia", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btn, c);

        // Analysis Results Label
        JLabel resultLabel = new JLabel("<html><i>Enter data and click Analyze</i></html>");
        resultLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
        resultLabel.setForeground(Color.LIGHT_GRAY);
        c.gridy = 6;
        panel.add(resultLabel, c);

        // Explanation Facility Button
        JButton explainBtn = new JButton("Why this result? (AI Reasoning)");
        explainBtn.setBackground(new Color(60, 60, 60));
        explainBtn.setForeground(Color.WHITE);
        explainBtn.setFont(new Font("Georgia", Font.ITALIC, 11));
        c.gridy = 7;
        panel.add(explainBtn, c);

        // Logic Implementation
        btn.addActionListener(e -> {
            try {
                double clubhead = Double.parseDouble(clubheadField.getText().trim());
                double ball = Double.parseDouble(ballSpeedField.getText().trim());
                double path = Double.parseDouble(clubPathField.getText().trim());
                boolean isOpen = faceOpen.isSelected();

                double smash = ball / clubhead;
                String outcome;
                String drill;

                if (smash >= 1.35) {
                    if (path > 2.0) {
                        outcome = "Neutral Strike / Draw Pattern";
                        drill = "Consistency Drills";
                        logicExplanation = "Rule Triggered: High Smash Factor (>=1.35) + In-to-Out Path (>2°). Conclusion: Your contact is efficient and your path promotes a natural draw.";
                    } else if (!isOpen) {
                        outcome = "Strong Draw Pattern";
                        drill = "Continue with current feel";
                        logicExplanation = "Rule Triggered: High Smash Factor (>=1.35) + Square/Closed Face. Conclusion: You are compressing the ball well with a closed face for a draw.";
                    } else {
                        outcome = "Push Slice";
                        drill = "Headcover Inside-Out Drill";
                        logicExplanation = "Rule Triggered: High Smash Factor (>=1.35) + Open Face. Conclusion: You hit the ball solid, but the open face angle is causing a slice.";
                    }
                } else {
                    if (path < -2.0) {
                        outcome = "Pull Pattern (Neutral Strike)";
                        drill = "Gate Consistency Drill";
                        logicExplanation = "Rule Triggered: Low Smash Factor (<1.35) + Out-to-In Path (<-2°). Conclusion: Off-center contact combined with an outside path is leading to pulls.";
                    } else if (isOpen) {
                        outcome = (ball < 130) ? "Push-Slice" : "Big Slice";
                        drill = "Stronger Grip / Impact Bag Drills";
                        logicExplanation = "Rule Triggered: Low Smash Factor (<1.35) + Open Face. Conclusion: Weak contact with an open face creates excessive side-spin.";
                    } else {
                        outcome = "Pull Hook";
                        drill = "Path Correction Drills";
                        logicExplanation = "Rule Triggered: Low Smash Factor (<1.35) + Closed Face. Conclusion: Pull-hooking is likely due to an aggressive closed face at impact.";
                    }
                }

                boolean isMajorIssue = outcome.contains("Slice") || outcome.contains("Hook") || smash < 1.30;
                resultLabel.setForeground(isMajorIssue ? new Color(255, 120, 120) : new Color(150, 255, 150));
                resultLabel.setText(String.format("<html><b>Smash Factor:</b> %.3f<br><b>Outcome:</b> %s<br><b>Drill:</b> %s</html>", smash, outcome, drill));

            } catch (NumberFormatException ex) {
                resultLabel.setForeground(Color.ORANGE);
                resultLabel.setText("Please enter numeric values only.");
            }
        });

        explainBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, logicExplanation, "Expert System Reasoning", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void addRow(JPanel p, GridBagConstraints c, String labelText, JTextField field, int row) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Georgia", Font.BOLD, 13));
        c.gridx = 0; c.gridy = row;
        p.add(label, c);
        c.gridx = 1; c.gridy = row;
        p.add(field, c);
    }
}