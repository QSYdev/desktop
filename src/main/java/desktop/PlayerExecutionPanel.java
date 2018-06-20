package desktop;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import terminal.Color;
import terminal.Event.ExternalEvent.ExecutionFinished;
import terminal.Event.ExternalEvent.ExecutionInterrupted;
import terminal.Event.ExternalEvent.ExternalEventVisitor;
import terminal.Terminal;

public final class PlayerExecutionPanel extends JPanel implements AutoCloseable, ExternalEventVisitor {

	private static final long serialVersionUID = -2940823866642994658L;

	private final JTextField txtPlayersCount;
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

		this.setLayout(new GridBagLayout());
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Rutina Aleatoria"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.2;
		this.add(new JLabel("Jugadores"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.8;
		this.add(txtPlayersCount = new JTextField("0"), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.2;
		this.add(new JLabel("Nodos"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.8;
		this.add(txtNodesCount = new JTextField("0"), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(checkBoxWaitForAllPlayers = new JCheckBox("Esperar a todos los jugadores"), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0.2;
		this.add(new JLabel("Delay"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0.8;
		this.add(txtStepDelay = new JTextField("0"), c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 0.2;
		this.add(new JLabel("StepTimeOut"), c);

		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 0.8;
		this.add(txtStepTimeOut = new JTextField("0"), c);

		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(checkBoxStopOnStepTimeOut = new JCheckBox("Frenar ante un time out"), c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		c.weightx = 0.2;
		this.add(new JLabel("Steps"), c);

		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		c.weightx = 0.8;
		this.add(txtNumberOfSteps = new JTextField("0"), c);

		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		c.weightx = 0.2;
		this.add(new JLabel("TimeOut total"), c);

		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		c.weightx = 0.8;
		this.add(txtExecutionTimeOut = new JTextField("0"), c);

		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(btnStartExecution = new JButton("Empezar Rutina"), c);

		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(btnStopExecution = new JButton("Finalizar Rutina"), c);

		btnStartExecution.addActionListener((ActionEvent event) -> {
			try {
				int playersCount = Integer.parseInt(txtPlayersCount.getText());
				ArrayList<Color> playersAndColors = new ArrayList<>(playersCount);

				playersAndColors.add(Color.RED);
				playersAndColors.add(Color.RED);
				playersAndColors.add(Color.RED);

				int numberOfNodes = Integer.parseInt(txtNodesCount.getText());
				boolean waitForAllPlayers = checkBoxWaitForAllPlayers.isSelected();
				long stepDelay = Long.parseLong(txtStepDelay.getText());
				long stepTimeOut = Long.parseLong(txtStepTimeOut.getText());
				boolean stopOnStepTimeOut = checkBoxStopOnStepTimeOut.isSelected();
				int numberOfSteps = Integer.parseInt(txtNumberOfSteps.getText());
				long executionTimeOut = Long.parseLong(txtExecutionTimeOut.getText());
				terminal.startPlayerExecution(numberOfNodes, playersAndColors, waitForAllPlayers, stepDelay, stepTimeOut, stopOnStepTimeOut, numberOfSteps, executionTimeOut);
				btnStartExecution.setEnabled(false);
				btnStopExecution.setEnabled(true);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Se debe seleccionar un numero entero", "Error", JOptionPane.ERROR_MESSAGE);
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
		txtPlayersCount.setEnabled(enabled);
		txtNodesCount.setEnabled(enabled);
		checkBoxWaitForAllPlayers.setEnabled(enabled);
		txtStepDelay.setEnabled(enabled);
		txtStepTimeOut.setEnabled(enabled);
		checkBoxStopOnStepTimeOut.setEnabled(enabled);
		txtNumberOfSteps.setEnabled(enabled);
		btnStartExecution.setEnabled(enabled);
		btnStopExecution.setEnabled(false);
		txtPlayersCount.setText("0");
		txtNodesCount.setText("0");
		checkBoxWaitForAllPlayers.setSelected(false);
		txtStepDelay.setText("0");
		txtStepTimeOut.setText("0");
		checkBoxStopOnStepTimeOut.setSelected(false);
		txtNumberOfSteps.setText("0");
		super.setEnabled(enabled);
	}

	@Override
	public void close() {
		terminal.stopRoutine();
	}

	@Override
	public void visit(ExecutionFinished event) {
		setEnabled(true);
	}

	@Override
	public void visit(ExecutionInterrupted event) {
		setEnabled(true);
	}

}
