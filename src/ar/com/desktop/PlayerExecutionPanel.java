package ar.com.desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

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

	public PlayerExecutionPanel(QSYFrame parent) {
		this.setLayout(new GridLayout(0, 1, 2, 2));
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Rutina Aleatoria"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JLabel lblNodesCount = new JLabel("Nodos :");
		add(lblNodesCount);

		txtNodesCount = new JTextField("");
		add(txtNodesCount);

		checkBoxWaitForAllPlayers = new JCheckBox("Esperar a todos los jugadores");
		add(checkBoxWaitForAllPlayers);

		JLabel lblStepDelay = new JLabel("Delay");
		add(lblStepDelay);

		txtStepDelay = new JTextField();
		add(txtStepDelay);

		JLabel lblStepTimeOut = new JLabel("TimeOut por paso");
		add(lblStepTimeOut);

		txtStepTimeOut = new JTextField();
		add(txtStepTimeOut);

		checkBoxStopOnStepTimeOut = new JCheckBox("Frenar ante un time out");
		add(checkBoxStopOnStepTimeOut);

		JLabel lblNumberOfSteps = new JLabel("Numero de pasos");
		add(lblNumberOfSteps);

		txtNumberOfSteps = new JTextField();
		add(txtNumberOfSteps);

		JLabel lblExecutionTimeOut = new JLabel("TimeOut total");
		add(lblExecutionTimeOut);

		txtExecutionTimeOut = new JTextField();
		add(txtExecutionTimeOut);

		btnStartExecution = new JButton("Empezar Rutina");
		add(btnStartExecution);
		btnStartExecution.addActionListener((ActionEvent e) -> {
			// TODO
		});

		btnStopExecution = new JButton("Finalizar Rutina");
		add(btnStopExecution);
		btnStopExecution.addActionListener((ActionEvent e) -> {
			// TODO
		});
	}

	@Override
	public void close() {
	}

}
