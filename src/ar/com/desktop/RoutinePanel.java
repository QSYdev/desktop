package ar.com.desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public final class RoutinePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final File ROOT_FILE = FileSystemView.getFileSystemView().createFileObject("resources");

	private final JButton btnCustomRoutineStart;
	private final JButton btnPlayerRoutineStart;
	private final JButton btnStopRoutine;
	private final JFileChooser fileChooser;

	public RoutinePanel(QSYFrame parent) {
		this.setLayout(new GridLayout(0, 1, 2, 2));
		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Rutina"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccione una rutina para ejecutar");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo en formato JSON", "json"));

		btnCustomRoutineStart = new JButton("Empezar CustomRoutine");
		btnCustomRoutineStart.setEnabled(true);
		this.add(btnCustomRoutineStart);

		btnCustomRoutineStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				fileChooser.setCurrentDirectory(ROOT_FILE);
				int result = fileChooser.showOpenDialog(RoutinePanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						System.out.println(selectedFile.getAbsolutePath());
						// TODO cargar la rutina seleccionada.
						btnCustomRoutineStart.setEnabled(false);
						btnPlayerRoutineStart.setEnabled(false);
						btnStopRoutine.setEnabled(true);
					} catch (IllegalArgumentException e) {
						JOptionPane.showMessageDialog(null, "Se debe seleccionar una rutina valida", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		btnPlayerRoutineStart = new JButton("Empezar PlayerRoutine");
		btnPlayerRoutineStart.setEnabled(false);
		this.add(btnPlayerRoutineStart);

		btnPlayerRoutineStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// try {
				// TreeMap<Integer, Integer> nodesIdsAssociations = new TreeMap<>();
				// ArrayList<Color> playersAndColors = new ArrayList<>();
				// for (int i = 0; i <= 8; i++) {
				// playersAndColors.add(new Color((byte) 0x0, (byte) 0, (byte) 0xF));
				// nodesIdsAssociations.put(i - 5, i);
				// }
				// playersAndColors.add(Color.MAGENTA);
				// parent.getLibterminal().executePlayer(null, 1, playersAndColors, true, 0,
				// 500, 0, 256, false, false, false);
				// } catch (Exception e1) {
				// e1.printStackTrace();
				// }
			}
		});

		btnStopRoutine = new JButton("Terminar Rutina");
		btnStopRoutine.setEnabled(false);
		this.add(btnStopRoutine);

		btnStopRoutine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO frenar la rutina.
				btnStopRoutine.setEnabled(false);
				btnCustomRoutineStart.setEnabled(true);
				btnPlayerRoutineStart.setEnabled(true);
			}

		});

	}

}
