package ar.com.desktop;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
				Color color = colors[comboBoxColor.getSelectedIndex()];
				long delay = Long.parseLong(textDelay.getText());
				int nodeId = parent.getSearchPanel().getSelectedNodeId();
				int stepId = (checkBoxStepId.isSelected()) ? 1 : 0;
				QSYPacket.CommandArgs commandArgs = new QSYPacket.CommandArgs(nodeId, color, delay, stepId);
				parent.getTerminal().sendCommand(commandArgs);
			} catch (IndexOutOfBoundsException exception) {
				JOptionPane.showMessageDialog(null, "Se debe seleccionar un color", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(null, "Se debe colocar un numero entero de 4 Bytes sin signo.", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
		if (!enabled) {
			comboBoxColor.setSelectedIndex(0);
			textDelay.setText("0");
		}
		super.setEnabled(enabled);
	}

	@Override
	public void close() {
	}

}
