package ar.com.desktop;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

import ar.com.terminal.Color;
import ar.com.terminal.Terminal;

public final class PlayerExecutionPanel extends JPanel implements AutoCloseable {

	private static final long serialVersionUID = -2940823866642994658L;

	private final JTextField txtNodesCount;
	private final JCheckBox checkBoxWaitForAllPlayers;
	private final JTextField txtStepDelay;
	private final JTextField txtStepTimeOut;
	private final JCheckBox checkBoxStopOnStepTimeOut;
	private final JTextField txtNumberOfSteps;
	private final JTextField txtExecutionTimeOut;
	private final JButton btnStartExecution;
	private final JButton btnStopExecution;

	private final Terminal terminal;

	public PlayerExecutionPanel(QSYFrame parent) {
		this.terminal = parent.getTerminal();

		this.setLayout(new GridLayout(0, 1, 2, 2));
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Rutina Aleatoria"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JPanel doublePanel = new JPanel(new FlowLayout());
		doublePanel.add(new JLabel("Nodos :"));
		doublePanel.add(txtNodesCount = new JTextField(10));
		add(doublePanel);

		add(checkBoxWaitForAllPlayers = new JCheckBox("Esperar"));

		doublePanel = new JPanel(new GridLayout(0, 2));
		doublePanel.add(new JLabel("Delay"));
		doublePanel.add(txtStepDelay = new JTextField());
		doublePanel.add(new JLabel("TimeOut por paso"));
		doublePanel.add(txtStepTimeOut = new JTextField());
		add(doublePanel);

		add(checkBoxStopOnStepTimeOut = new JCheckBox("Frenar ante un time out"));

		doublePanel = new JPanel(new GridLayout(0, 2));
		doublePanel.add(new JLabel("Numero de pasos"));
		doublePanel.add(txtNumberOfSteps = new JTextField());
		doublePanel.add(new JLabel("TimeOut total"));
		doublePanel.add(txtExecutionTimeOut = new JTextField());
		add(doublePanel);

		add(btnStartExecution = new JButton("Empezar Rutina"));
		add(btnStopExecution = new JButton("Finalizar Rutina"));

		btnStartExecution.addActionListener((ActionEvent event) -> {
			try {
				int numberOfNodes = Integer.parseInt(txtNodesCount.getText());
				ArrayList<Color> playersAndColors = new ArrayList<>();
				playersAndColors.add(Color.RED);
				boolean waitForAllPlayers = checkBoxWaitForAllPlayers.isSelected();
				long stepDelay = Long.parseLong(txtStepDelay.getText());
				long stepTimeOut = Long.parseLong(txtStepTimeOut.getText());
				boolean stopOnStepTimeOut = checkBoxStopOnStepTimeOut.isSelected();
				int numberOfSteps = Integer.parseInt(txtNumberOfSteps.getText());
				long executionTimeOut = Long.parseLong(txtExecutionTimeOut.getText());
				terminal.startPlayerExecution(numberOfNodes, playersAndColors, waitForAllPlayers, stepDelay, stepTimeOut, stopOnStepTimeOut, numberOfSteps, executionTimeOut);
				btnStartExecution.setEnabled(false);
				btnStopExecution.setEnabled(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		btnStopExecution.addActionListener((ActionEvent event) -> {
			terminal.stopRoutine();
			btnStartExecution.setEnabled(true);
			btnStopExecution.setEnabled(false);
		});
	}

	@Override
	public void setEnabled(boolean enabled) {
		terminal.stopRoutine();
		txtNodesCount.setEnabled(enabled);
		checkBoxWaitForAllPlayers.setEnabled(enabled);
		txtStepDelay.setEnabled(enabled);
		txtStepTimeOut.setEnabled(enabled);
		checkBoxStopOnStepTimeOut.setEnabled(enabled);
		txtNumberOfSteps.setEnabled(enabled);
		btnStartExecution.setEnabled(enabled);
		btnStopExecution.setEnabled(false);
		txtNodesCount.setText("");
		checkBoxWaitForAllPlayers.setSelected(false);
		txtStepDelay.setText("");
		txtStepTimeOut.setText("");
		checkBoxStopOnStepTimeOut.setSelected(false);
		txtNumberOfSteps.setText("");
		super.setEnabled(enabled);
	}

	@Override
	public void close() {
		terminal.stopRoutine();
	}

}
