package ar.com.desktop;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;

import ar.com.terminal.Color;
import ar.com.terminal.QSYPacket;

public final class CommandPanel extends JPanel implements AutoCloseable {

	private static final long serialVersionUID = 1L;
	private static final Color[] colors = { Color.NO_COLOR, Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.WHITE };
	private static final String[] comboBoxPosibilites = { "SIN COLOR", "ROJO", "VERDE", "AZUL", "CYAN", "MAGENTA", "AMARILLO", "BLANCO" };

	private final JComboBox<String> comboBoxColor;
	private final JTextField textDelay;
	private final JCheckBox checkBoxStepId;
	private final JButton btnSendCommand;

	public CommandPanel(QSYFrame parent) {
		this.setLayout(new GridBagLayout());
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Comando"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;

		c.gridx = 0;
		c.gridy = 0;
		this.add(new JLabel("Color:"), c);

		c.gridx = 1;
		c.gridy = 0;
		this.add(comboBoxColor = new JComboBox<>(), c);
		comboBoxColor.setModel(new DefaultComboBoxModel<>(comboBoxPosibilites));

		c.gridx = 0;
		c.gridy = 1;
		this.add(new JLabel("Delay: (ms)"), c);

		c.gridx = 1;
		c.gridy = 1;
		this.add(textDelay = new JTextField("0"), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		this.add(checkBoxStepId = new JCheckBox("Activar sensor"), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		this.add(btnSendCommand = new JButton("Enviar comando"), c);

		btnSendCommand.addActionListener((ActionEvent event) -> {

			try {
				Color color = colors[comboBoxColor.getSelectedIndex()];
				long delay = Long.parseLong(textDelay.getText());
				int nodeId = parent.getSearchPanel().getSelectedNodeId();
				int stepId = (checkBoxStepId.isSelected()) ? 1 : 0;
				QSYPacket.CommandArgs commandArgs = new QSYPacket.CommandArgs(nodeId, color, delay, stepId);
				parent.getTerminal().sendCommand(commandArgs);
			} catch (IndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(null, "Se debe seleccionar un color", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Se debe colocar un numero entero", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		});

	}

	@Override
	public void setEnabled(boolean enabled) {
		comboBoxColor.setEnabled(enabled);
		textDelay.setEnabled(enabled);
		checkBoxStepId.setEnabled(enabled);
		checkBoxStepId.setSelected(false);
		btnSendCommand.setEnabled(enabled);
		comboBoxColor.setSelectedIndex(0);
		textDelay.setText("0");
		super.setEnabled(enabled);
	}

	@Override
	public void close() {
	}

}
