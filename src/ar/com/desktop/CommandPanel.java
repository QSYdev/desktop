package ar.com.desktop;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

import ar.com.terminal.Color;
import ar.com.terminal.QSYPacket;
import ar.com.terminal.QSYPacket.CommandArgs;

public final class CommandPanel extends JPanel implements AutoCloseable {

	private static final long serialVersionUID = 1L;
	private static final Color[] colors = { Color.NO_COLOR, Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.WHITE };
	private static final String[] comboBoxPosibilites;
	static {
		comboBoxPosibilites = new String[colors.length];
		for (int i = 0; i < colors.length; i++) {
			comboBoxPosibilites[i] = colors[i].toString();
		}
	}

	private final TreeMap<Integer, CommandTask> commandTasksList;
	private final JComboBox<String> comboBoxColor;
	private final JTextField textDelay;
	private final JCheckBox checkBoxStepId;
	private final JButton btnSendCommand;

	public CommandPanel(QSYFrame parent) {
		this.commandTasksList = new TreeMap<>();
		this.setLayout(new GridLayout(0, 1, 2, 2));
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Comando"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JLabel lblColor = new JLabel("Color:");
		this.add(lblColor);

		comboBoxColor = new JComboBox<>();
		comboBoxColor.setModel(new DefaultComboBoxModel<>(comboBoxPosibilites));
		this.add(comboBoxColor);

		JLabel lblDelay = new JLabel("Delay: (ms)");
		this.add(lblDelay);

		textDelay = new JTextField();
		textDelay.setHorizontalAlignment(SwingConstants.LEFT);
		textDelay.setText("0");
		textDelay.setMaximumSize(new Dimension(Integer.MAX_VALUE, textDelay.getPreferredSize().width));
		this.add(textDelay);

		checkBoxStepId = new JCheckBox("Activar sensor");
		checkBoxStepId.setMnemonic(KeyEvent.VK_A);
		this.add(checkBoxStepId);

		btnSendCommand = new JButton("Enviar comando");
		this.add(btnSendCommand);

		btnSendCommand.addActionListener((ActionEvent e) -> {

			try {
				JTable table = parent.getSearchPanel().getTable();

				Color color = getSelectedColorFromComboBox();
				long delay = Long.parseLong(textDelay.getText());
				int nodeId = (Integer) table.getValueAt(table.getSelectedRow(), 0);
				int stepId = (checkBoxStepId.isSelected()) ? 1 : 0;
				QSYPacket.CommandArgs commandArgs = new QSYPacket.CommandArgs(nodeId, color, delay, stepId);
				parent.getTerminal().sendCommand(commandArgs);
				if (commandArgs.getDelay() == 0)
					parent.getSearchPanel().touche(nodeId, color);
				else
					commandTasksList.add(new CommandTask(parent, commandArgs));

			} catch (NullPointerException exception) {
				JOptionPane.showMessageDialog(null, "Se debe seleccionar un color", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(null, "Se debe colocar un numero entero de 4 Bytes sin signo.", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		});

	}

	@Override
	public void setEnabled(boolean enabled) {
		for (CommandTask commandTask : commandTasksList)
			commandTask.close();
		comboBoxColor.setEnabled(enabled);
		textDelay.setEnabled(enabled);
		checkBoxStepId.setEnabled(enabled);
		checkBoxStepId.setSelected(false);
		btnSendCommand.setEnabled(enabled);
		if (!enabled) {
			comboBoxColor.setSelectedIndex(0);
			textDelay.setText("0");
		}
		super.setEnabled(enabled);
	}

	public void connectedNode(int physicalId) {

	}

	public void touche(int physicalId) {

	}

	public void disconnectedNode(int physicalId) {

	}

	private Color getSelectedColorFromComboBox() throws NullPointerException {
		int index = comboBoxColor.getSelectedIndex();
		if (index < 0 || index >= colors.length) {
			throw new NullPointerException();
		}
		return colors[index];
	}

	@Override
	public void close() {
		for (CommandTask commandTask : commandTasksList)
			commandTask.close();
	}

	private final class CommandTask implements Runnable, AutoCloseable {

		private final QSYFrame parent;
		private final CommandArgs commandArgs;
		private final Thread thread;

		public CommandTask(QSYFrame parent, CommandArgs commandArgs) {
			this.parent = parent;
			this.commandArgs = commandArgs;
			this.thread = new Thread(this, "CommandTask");
			thread.start();
		}

		@Override
		public void run() {
			try {
				Thread.sleep(commandArgs.getDelay());
				parent.getSearchPanel().touche(commandArgs.getPhysicialId(), commandArgs.getColor());
			} catch (InterruptedException e) {
			}
		}

		@Override
		public void close() {
			thread.interrupt();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
