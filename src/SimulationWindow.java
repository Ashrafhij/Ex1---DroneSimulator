import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SimulationWindow {

    private JFrame frame;
    private Timer returnHomeTimer;
    private final int timerDelay = 4 * 60 * 1000; // 4 minutes in milliseconds

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SimulationWindow window = new SimulationWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SimulationWindow() {
        initialize();
    }

    public static JLabel info_label;
    public static JLabel info_label2;
    public static boolean return_home = false;
    boolean toogleStop = true;

    private void initialize() {
        frame = new JFrame();
        frame.setSize(1800, 1200);
        frame.setTitle("Drone Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Create panel for buttons and labels using GridBagLayout
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding

        // Create panels for button groups
        JPanel startPausePanel = new JPanel(new GridLayout(1, 1));
        JPanel speedPanel = new JPanel(new GridLayout(2, 1)); // Change layout to 2 rows, 1 column
        JPanel spinPanel = new JPanel(new GridLayout(2, 4));
        JPanel togglePanel = new JPanel(new GridLayout(2, 1)); // Change layout to 2 rows, 1 column
        JPanel otherPanel = new JPanel(new GridLayout(1, 2));

        // Add buttons to their respective panels with action listeners
        JButton stopBtn = new JButton("Start/Pause");
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (toogleStop) {
                    CPU.stopAllCPUS();
                } else {
                    CPU.resumeAllCPUS();
                }
                toogleStop = !toogleStop;
            }
        });

        JButton speedUpBtn = new JButton("Speed Up");
        speedUpBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                algo1.speedUp();
            }
        });

        JButton speedDownBtn = new JButton("Speed Down");
        speedDownBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                algo1.speedDown();
            }
        });

        JButton toogleMapBtn = new JButton("Toggle Map");
        toogleMapBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toogleRealMap = !toogleRealMap;
            }
        });

        JButton toogleAIBtn = new JButton("Toggle AI");
        toogleAIBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toogleAI = !toogleAI;
                if (toogleAI) {
                    startReturnHomeTimer();
                } else {
                    stopReturnHomeTimer();
                }
            }
        });

        JButton returnBtn = new JButton("Return Home");
        returnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnHome();
            }
        });

        JButton graphBtn = new JButton("Open Graph");
        graphBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                algo1.mGraph.drawGraph();
            }
        });

        // Enhance button design
        enhanceButton(stopBtn);
        enhanceButton(speedUpBtn);
        enhanceButton(speedDownBtn);
        enhanceButton(toogleMapBtn);
        enhanceButton(toogleAIBtn);
        enhanceButton(returnBtn);
        enhanceButton(graphBtn);

        startPausePanel.add(stopBtn);
        speedPanel.add(speedUpBtn); // Add Speed Up button to speedPanel
        speedPanel.add(speedDownBtn); // Add Speed Down button to speedPanel
        addButton("Spin 180", e -> algo1.spinBy(180), spinPanel);
        addButton("Spin 90", e -> algo1.spinBy(90), spinPanel);
        addButton("Spin 60", e -> algo1.spinBy(60), spinPanel);
        addButton("Spin 45", e -> algo1.spinBy(45), spinPanel);
        addButton("Spin 30", e -> algo1.spinBy(30), spinPanel);
        addButton("Spin -30", e -> algo1.spinBy(-30), spinPanel);
        addButton("Spin -45", e -> algo1.spinBy(-45), spinPanel);
        addButton("Spin -60", e -> algo1.spinBy(-60), spinPanel);
        togglePanel.add(toogleMapBtn); // Add Toggle Map button to togglePanel
        togglePanel.add(toogleAIBtn); // Add Toggle AI button to togglePanel
        otherPanel.add(returnBtn);
        otherPanel.add(graphBtn);

        // Create a panel for all button groups
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Horizontal layout with spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0)); // Add border and spacing
        buttonPanel.setBackground(Color.LIGHT_GRAY); // Set background color
        buttonPanel.add(startPausePanel);
        buttonPanel.add(speedPanel);
        buttonPanel.add(spinPanel);
        buttonPanel.add(togglePanel);
        buttonPanel.add(otherPanel);

        // Update constraints to position the buttonPanel on the left side
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2; // Span across two rows
        controlPanel.add(buttonPanel, gbc);

        // Add labels
        info_label = new JLabel();
        gbc.gridx = 1; // Position labels to the right
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        controlPanel.add(info_label, gbc);

        info_label2 = new JLabel();
        gbc.gridx = 1; // Position labels to the right
        gbc.gridy = 1; // Update the gridy value for the second label
        gbc.gridwidth = 1;
        controlPanel.add(info_label2, gbc);

        // Example list of points
        String[] pointsData = {
            "Point A",
            "Point B",
            "Point C",
            "Point D",
            "Point E"
        };

        JList<String> pointsList = new JList<>(pointsData);
        pointsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Single selection mode
        JScrollPane pointsScrollPane = new JScrollPane(pointsList);
        
        // Create panel for the list of points
        JPanel pointsPanel = new JPanel(new BorderLayout());
        JLabel pointsLabel = new JLabel("List of Points:");
        pointsPanel.add(pointsLabel, BorderLayout.NORTH);
        pointsPanel.add(pointsScrollPane, BorderLayout.CENTER);

        // Update constraints to position the pointsPanel next to the map
        gbc.gridx = 1; // Adjust according to your layout needs
        gbc.gridy = 0;
        gbc.gridheight = 1; // Reset grid height
        gbc.gridwidth = 1; // Reset grid width
        gbc.weightx = 1.0; // Horizontal weight to occupy remaining space
        gbc.fill = GridBagConstraints.BOTH; // Fill both horizontal and vertical space
        controlPanel.add(pointsPanel, gbc);

        frame.getContentPane().add(controlPanel, BorderLayout.PAGE_END);

        // Create and add the map painter to the center of the frame
        main();
    }

    private void addButton(String text, ActionListener action, JPanel panel) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        enhanceButton(button);
        panel.add(button);
    }

    private void enhanceButton(JButton button) {
        button.setBackground(Color.WHITE); // Set background color
        button.setForeground(Color.BLACK); // Set text color
        button.setFocusPainted(false); // Remove focus border
        button.setPreferredSize(new Dimension(120, 30)); // Set preferred size
        button.setFont(new Font("Arial", Font.BOLD, 12)); // Set font

        // Reduce padding to show more of the map
        button.setMargin(new Insets(5, 5, 5, 5)); // Adjust insets as needed
    }

    public static boolean toogleRealMap = true;
    public static boolean toogleAI = false;
    public static AutoAlgo1 algo1;

    public void main() {
        int map_num = 4;
        Point[] startPoints = {
            new Point(100, 50),
            new Point(50, 60),
            new Point(73, 68),
            new Point(84, 73),
            new Point(92, 100)
        };

        Map map = new Map("Maps/p16.png", startPoints[map_num - 1]);

        algo1 = new AutoAlgo1(map);

        Painter painter = new Painter(algo1);

        painter.setPreferredSize(new Dimension(1400, 800)); // Adjust dimensions as needed
        frame.getContentPane().add(painter, BorderLayout.CENTER);

        CPU painterCPU = new CPU(200, "painter"); // 60 FPS painter
        painterCPU.addFunction(frame::repaint);
        painterCPU.play();

        algo1.play();

        CPU updatesCPU = new CPU(60, "updates");
        updatesCPU.addFunction(algo1.drone::update);
        updatesCPU.play();

        CPU infoCPU = new CPU(6, "update_info");
        infoCPU.addFunction(this::updateInfo);
        infoCPU.play();
    }

    public void updateInfo(int deltaTime) {
        info_label.setText(algo1.drone.getInfoHTML());
        info_label2.setText("<html>" + String.valueOf(algo1.counter) + " <BR>isRisky:" + String.valueOf(algo1.is_risky) +
                "<BR>" + String.valueOf(algo1.risky_dis) + "</html>");
    }

    private void startReturnHomeTimer() {
        if (returnHomeTimer == null) {
            returnHomeTimer = new Timer(timerDelay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    returnHome();
                    stopReturnHomeTimer();
                }
            });
            returnHomeTimer.setRepeats(false); // Ensure the timer only runs once
        }
        returnHomeTimer.start();
    }

    private void stopReturnHomeTimer() {
        if (returnHomeTimer != null) {
            returnHomeTimer.stop();
        }
    }

    private void returnHome() {
        return_home = true;
        algo1.speedDown();
        algo1.spinBy(180, true, new Func() {
            @Override
            public void method() {
                algo1.speedUp();
            }
        });
    }
}
