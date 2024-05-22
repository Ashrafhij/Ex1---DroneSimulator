import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SimulationWindow {

    private JFrame frame;

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
		gbc.insets = new Insets(0, 0, 0, 0);
	
		// Create panels for button groups
		JPanel startPausePanel = new JPanel(new GridLayout(1, 1));
		JPanel speedPanel = new JPanel(new GridLayout(1, 2));
		JPanel spinPanel = new JPanel(new GridLayout(2, 4));
		JPanel togglePanel = new JPanel(new GridLayout(1, 2));
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
		startPausePanel.add(stopBtn);
	
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
		speedPanel.add(speedUpBtn);
		speedPanel.add(speedDownBtn);
	
		addButton("Spin 180", e -> algo1.spinBy(180), spinPanel);
		addButton("Spin 90", e -> algo1.spinBy(90), spinPanel);
		addButton("Spin 60", e -> algo1.spinBy(60), spinPanel);
		addButton("Spin 45", e -> algo1.spinBy(45), spinPanel);
		addButton("Spin 30", e -> algo1.spinBy(30), spinPanel);
		addButton("Spin -30", e -> algo1.spinBy(-30), spinPanel);
		addButton("Spin -45", e -> algo1.spinBy(-45), spinPanel);
		addButton("Spin -60", e -> algo1.spinBy(-60), spinPanel);
	
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
			}
		});
		togglePanel.add(toogleMapBtn);
		togglePanel.add(toogleAIBtn);
	
		JButton returnBtn = new JButton("Return Home");
		returnBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				return_home = !return_home;
				algo1.speedDown();
				algo1.spinBy(180, true, new Func() {
					@Override
					public void method() {
						algo1.speedUp();
					}
				});
			}
		});
		JButton graphBtn = new JButton("Open Graph");
		graphBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				algo1.mGraph.drawGraph();
			}
		});
		otherPanel.add(returnBtn);
		otherPanel.add(graphBtn);
	
		// Create a panel for all button groups
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		buttonPanel.add(startPausePanel);
		buttonPanel.add(speedPanel);
		buttonPanel.add(spinPanel);
		buttonPanel.add(togglePanel);
		buttonPanel.add(otherPanel);
	
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		controlPanel.add(buttonPanel, gbc);
	
		// Add labels
		info_label = new JLabel();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		controlPanel.add(info_label, gbc);
	
		info_label2 = new JLabel();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		controlPanel.add(info_label2, gbc);
	
		frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

        // Create and add the map painter to the center of the frame
        main();
    }

    private void addButton(String text, ActionListener action, JPanel panel) {
		JButton button = new JButton(text);
		button.addActionListener(action);
		panel.add(button);
	}

    public JLabel info_label2;
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
                new Point(92, 100)};

        Map map = new Map("Maps/p15.png", startPoints[map_num - 1]);

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
}
