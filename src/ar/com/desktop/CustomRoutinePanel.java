package ar.com.desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public final class CustomRoutinePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final File ROOT_FILE = FileSystemView.getFileSystemView().createFileObject("resources");

	private final JFileChooser fileChooser;
	private final JButton btnChooseRoutine;
	private final JTextField txtRoutinePath;
	private final JTextField txtRoutineName;
	private final JTextField txtRoutineNodesCount;
	private final JButton btnStartRoutine;
	private final JButton btnStopRoutine;

	public CustomRoutinePanel(QSYFrame parent) {
		this.setLayout(new GridLayout(0, 1, 2, 2));
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Rutina Personalizada"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccione una rutina para ejecutar");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo en formato JSON", "json"));

		btnChooseRoutine = new JButton("Seleccionar...");
		btnChooseRoutine.setEnabled(true);
		this.add(btnChooseRoutine);

		btnChooseRoutine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				fileChooser.setCurrentDirectory(ROOT_FILE);
				int result = fileChooser.showOpenDialog(CustomRoutinePanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						// TODO cargar la rutina seleccionada y guardarlo en una variable.
						// System.out.println(selectedFile.getAbsolutePath());
						txtRoutinePath.setText(selectedFile.getName());
						txtRoutineName.setText(""); // TODO llenarlo con la informacion de la variable.
						txtRoutineNodesCount.setText("0"); // TODO llenarlo con la informacion de la variable.
						btnChooseRoutine.setEnabled(true);
						btnStartRoutine.setEnabled(true);
						btnStopRoutine.setEnabled(false);
					} catch (IllegalArgumentException e) {
						JOptionPane.showMessageDialog(null, "Se debe seleccionar una rutina valida", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

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
		this.add(routineInfoPanel);

		JLabel lblRoutineNodesCount = new JLabel("Nodos : ");
		routineInfoPanel.add(lblRoutineNodesCount);

		txtRoutineNodesCount = new JTextField();
		txtRoutineNodesCount.setEnabled(false);
		routineInfoPanel.add(txtRoutineNodesCount);

		btnStartRoutine = new JButton("Empezar Rutina");
		btnStartRoutine.setEnabled(false);
		this.add(btnStartRoutine);

		btnStartRoutine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					// TODO iniciar la rutina guardada en la variable.
					btnChooseRoutine.setEnabled(false);
					btnStartRoutine.setEnabled(false);
					btnStopRoutine.setEnabled(true);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnStopRoutine = new JButton("Terminar Rutina");
		btnStopRoutine.setEnabled(false);
		this.add(btnStopRoutine);

		btnStopRoutine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					// TODO frenar la rutina.
					// TODO poner la variable de la rutina en null.
					txtRoutinePath.setText("");
					txtRoutineName.setText("");
					txtRoutineNodesCount.setText("");
					btnChooseRoutine.setEnabled(true);
					btnStartRoutine.setEnabled(false);
					btnStopRoutine.setEnabled(false);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

	}

	public boolean isRoutineRunning() {
		return btnStopRoutine.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO frenar la rutina.
		// TODO poner la variable de la rutina en null.
		txtRoutinePath.setText("");
		txtRoutineName.setText("");
		txtRoutineNodesCount.setText("");
		btnChooseRoutine.setEnabled(enabled);
		btnStartRoutine.setEnabled(false);
		btnStopRoutine.setEnabled(false);
		super.setEnabled(enabled);
	}

}
