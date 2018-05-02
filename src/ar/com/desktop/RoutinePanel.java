package ar.com.desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

public final class RoutinePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JButton btnCustomRoutineStart;
	private final JButton btnPlayerRoutineStart;
	private final JButton btnStopRoutine;

	public RoutinePanel(final QSYFrame parent) {
		this.setLayout(new GridLayout(0, 1, 2, 2));

		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Routine"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		btnCustomRoutineStart = new JButton("Start CustomRoutine");
		btnCustomRoutineStart.setEnabled(false);
		this.add(btnCustomRoutineStart);

		btnCustomRoutineStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				/*
				 * try { parent.getLibterminal().executeCustom(
				 * RoutineManager.loadRoutine("src/main/java/resources/routine1.json"), null,
				 * false, false); } catch (Exception e1) { e1.printStackTrace(); }
				 */
			}
		});

		btnPlayerRoutineStart = new JButton("Start PlayerRoutine");
		btnPlayerRoutineStart.setEnabled(false);
		this.add(btnPlayerRoutineStart);

		btnPlayerRoutineStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				/*
				 * try { //final TreeMap<Integer, Integer> nodesIdsAssociations = new
				 * TreeMap<>(); final ArrayList<Color> playersAndColors = new ArrayList<>();
				 * //for (int i = 0; i <= 8; i++) { //playersAndColors.add(new Color((byte) 0x0,
				 * (byte) 0, (byte) 0xF)); //nodesIdsAssociations.put(i - 5, i); //} /*
				 * playersAndColors.add(Color.MAGENTA); parent.getLibterminal().executePlayer(
				 * null, 1, playersAndColors, true, 0, 500, 0, 256, false, false, false); }
				 * catch (Exception e1) { e1.printStackTrace(); }
				 */
			}
		});

		btnStopRoutine = new JButton("Stop Routine");
		btnStopRoutine.setEnabled(false);
		this.add(btnStopRoutine);

		btnStopRoutine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				/*
				 * try { parent.getLibterminal().stopExecution(); } catch (final Exception e1) {
				 * e1.printStackTrace(); }
				 */
			}

		});

	}

}
