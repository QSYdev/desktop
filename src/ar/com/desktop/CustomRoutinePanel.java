package ar.com.desktop;

import java.awt.GridLayout;
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

import ar.com.terminal.Routine;
import ar.com.terminal.RoutineManager;
import ar.com.terminal.Terminal;

public final class CustomRoutinePanel extends JPanel implements AutoCloseable {

	private static final long serialVersionUID = 1L;
	private static final File ROOT_FILE = FileSystemView.getFileSystemView().createFileObject("resources");

	private final Terminal terminal;
	private final JFileChooser fileChooser;
	private final JButton btnChooseRoutine;
	private final JTextField txtRoutinePath;
	private final JTextField txtRoutineName;
	private final JTextField txtRoutineNodesCount;
	private final JButton btnStartRoutine;
	private final JButton btnStopRoutine;

	private Routine routine;

	public CustomRoutinePanel(QSYFrame parent) {
		this.terminal = parent.getTerminal();

		this.setLayout(new GridLayout(0, 1, 2, 2));
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Rutina Personalizada"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccione una rutina para ejecutar");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo en formato JSON", "json"));

		btnChooseRoutine = new JButton("Seleccionar...");
		this.add(btnChooseRoutine);

		txtRoutinePath = new JTextField();
		txtRoutinePath.setEnabled(false);
		this.add(txtRoutinePath);

		JLabel lblRoutineName = new JLabel("Nombre : ");
		this.add(lblRoutineName);

		txtRoutineName = new JTextField();
		txtRoutineName.setEnabled(false);
		this.add(txtRoutineName);

		JPanel routineInfoPanel = new JPanel();
		routineInfoPanel.setLayout(new GridLayout(0, 2, 2, 2));

		JLabel lblRoutineNodesCount = new JLabel("Nodos : ");
		routineInfoPanel.add(lblRoutineNodesCount);

		txtRoutineNodesCount = new JTextField();
		txtRoutineNodesCount.setEnabled(false);
		routineInfoPanel.add(txtRoutineNodesCount);
		this.add(routineInfoPanel);

		btnStartRoutine = new JButton("Empezar Rutina");
		this.add(btnStartRoutine);

		btnStopRoutine = new JButton("Terminar Rutina");
		this.add(btnStopRoutine);

		btnChooseRoutine.addActionListener((ActionEvent event) -> {
			fileChooser.setCurrentDirectory(ROOT_FILE);
			int result = fileChooser.showOpenDialog(CustomRoutinePanel.this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				try {
					routine = RoutineManager.loadRoutine(selectedFile.getAbsolutePath());
					txtRoutinePath.setText(selectedFile.getName());
					txtRoutineName.setText(routine.getName());
					txtRoutineNodesCount.setText("" + routine.getNumberOfNodes());
					btnChooseRoutine.setEnabled(true);
					btnStartRoutine.setEnabled(true);
					btnStopRoutine.setEnabled(false);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, "Se debe seleccionar una rutina valida", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnStartRoutine.addActionListener((ActionEvent event) -> {
			try {
				parent.getTerminal().startCustomRoutine(routine);
				btnChooseRoutine.setEnabled(false);
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
			txtRoutineName.setText("");
			txtRoutineNodesCount.setText("");
			btnChooseRoutine.setEnabled(true);
			btnStartRoutine.setEnabled(false);
			btnStopRoutine.setEnabled(false);
		});

	}

	@Override
	public void setEnabled(boolean enabled) {
		terminal.stopRoutine();
		routine = null;
		txtRoutinePath.setText("");
		txtRoutineName.setText("");
		txtRoutineNodesCount.setText("");
		btnChooseRoutine.setEnabled(enabled);
		btnStartRoutine.setEnabled(false);
		btnStopRoutine.setEnabled(false);
		super.setEnabled(enabled);
	}

	@Override
	public void close() {
	}

}
