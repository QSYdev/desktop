package desktop;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import terminal.Event.ExternalEvent.ExecutionFinished;
import terminal.Event.ExternalEvent.ExecutionInterrupted;
import terminal.Event.ExternalEvent.ExternalEventVisitor;
import terminal.Routine;
import terminal.RoutineManager;
import terminal.Terminal;

public final class CustomRoutinePanel extends JPanel implements AutoCloseable, ExternalEventVisitor {

	private static final long serialVersionUID = 1L;
	private static final File ROOT_FILE = FileSystemView.getFileSystemView().createFileObject("resources");

	private final JFileChooser fileChooser;
	private final JButton btnChooseRoutine;
	private final JTextField txtRoutinePath;
	private final JButton btnValidateRoutine;
	private final JTextField txtRoutineNodesCount;
	private final JButton btnStartRoutine;
	private final JButton btnStopRoutine;

	private final Terminal terminal;
	private Routine routine;

	public CustomRoutinePanel(QSYFrame parent) {
		this.terminal = parent.getTerminal();

		this.setLayout(new GridBagLayout());
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Rutina Personalizada"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccione una rutina para ejecutar");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo en formato JSON", "json"));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(btnChooseRoutine = new JButton("Seleccionar..."), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(btnValidateRoutine = new JButton("Validar Rutina"), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(txtRoutinePath = new JTextField(), c);
		txtRoutinePath.setEnabled(false);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0.2;
		this.add(new JLabel("Nodos"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0.8;
		this.add(txtRoutineNodesCount = new JTextField(), c);
		txtRoutineNodesCount.setEnabled(false);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(btnStartRoutine = new JButton("Empezar Rutina"), c);

		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.weightx = 1;
		this.add(btnStopRoutine = new JButton("Finalizar Rutina"), c);

		btnChooseRoutine.addActionListener((ActionEvent event) -> {
			fileChooser.setCurrentDirectory(ROOT_FILE);
			int result = fileChooser.showOpenDialog(CustomRoutinePanel.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				try {
					routine = RoutineManager.loadRoutine(selectedFile.getAbsolutePath());
					txtRoutinePath.setText(selectedFile.getName());
					txtRoutineNodesCount.setText("" + routine.getNumberOfNodes());
					btnChooseRoutine.setEnabled(true);
					btnValidateRoutine.setEnabled(true);
					btnStartRoutine.setEnabled(true);
					btnStopRoutine.setEnabled(false);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, "Se debe seleccionar una rutina valida", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnValidateRoutine.addActionListener((ActionEvent event) -> {
			try {
				RoutineManager.validateRoutine(routine);
				JOptionPane.showMessageDialog(null, "La rutina es valida", "Exito", JOptionPane.INFORMATION_MESSAGE);
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			btnChooseRoutine.setEnabled(true);
			btnValidateRoutine.setEnabled(true);
			btnStartRoutine.setEnabled(true);
			btnStopRoutine.setEnabled(false);
		});

		btnStartRoutine.addActionListener((ActionEvent event) -> {
			try {
				parent.getTerminal().startCustomRoutine(routine);
				btnChooseRoutine.setEnabled(false);
				btnValidateRoutine.setEnabled(false);
				btnStartRoutine.setEnabled(false);
				btnStopRoutine.setEnabled(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		btnStopRoutine.addActionListener((ActionEvent event) -> {
			parent.getTerminal().stopRoutine();
			routine = null;
			txtRoutinePath.setText("");
			txtRoutineNodesCount.setText("");
			btnChooseRoutine.setEnabled(true);
			btnValidateRoutine.setEnabled(false);
			btnStartRoutine.setEnabled(false);
			btnStopRoutine.setEnabled(false);
		});

	}

	@Override
	public void setEnabled(boolean enabled) {
		terminal.stopRoutine();
		routine = null;
		txtRoutinePath.setText("");
		txtRoutineNodesCount.setText("");
		btnChooseRoutine.setEnabled(enabled);
		btnValidateRoutine.setEnabled(false);
		btnStartRoutine.setEnabled(false);
		btnStopRoutine.setEnabled(false);
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