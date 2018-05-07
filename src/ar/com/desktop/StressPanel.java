package ar.com.desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

import ar.com.terminal.internal.Color;
import ar.com.terminal.internal.QSYPacket;
import ar.com.terminal.internal.Terminal;

public final class StressPanel extends JPanel implements AutoCloseable {

	private static final long serialVersionUID = 1L;

	private final Terminal terminal;
	private final JButton btnStartStressTest;
	private final JButton btnStopStressTest;

	private StressTask stressTask;

	public StressPanel(QSYFrame parent) {
		this.setLayout(new GridLayout(0, 1, 2, 2));

		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Prueba de Stress"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		this.terminal = parent.getTerminal();

		btnStartStressTest = new JButton("Iniciar Prueba de Stress");
		btnStartStressTest.setEnabled(true);
		this.add(btnStartStressTest);

		btnStopStressTest = new JButton("Finalizar Prueba de Stress");
		btnStopStressTest.setEnabled(false);
		this.add(btnStopStressTest);

		btnStartStressTest.addActionListener((ActionEvent e) -> {
			stressTask = new StressTask();
			btnStartStressTest.setEnabled(false);
			btnStopStressTest.setEnabled(true);
		});

		btnStopStressTest.addActionListener((ActionEvent e) -> {
			stressTask.close();
			stressTask = null;
			btnStopStressTest.setEnabled(false);
			btnStartStressTest.setEnabled(true);
		});

	}

	@Override
	public void setEnabled(boolean enabled) {
		btnStopStressTest.setEnabled(false);
		if (stressTask != null)
			stressTask.close();
		stressTask = null;
		btnStartStressTest.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public void close() {
		if (stressTask != null)
			stressTask.close();
	}

	private final class StressTask implements Runnable, AutoCloseable {

		private final QSYPacket.CommandArgs[] command;

		private volatile boolean running;
		private final Thread thread;

		public StressTask() {
			command = new QSYPacket.CommandArgs[10];
			for (int i = 0; i < command.length; i++) {
				command[i] = new QSYPacket.CommandArgs(i, Color.CYAN, 500, 3);
			}
			this.running = true;
			this.thread = new Thread(this, "StressTask");
			thread.start();
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.sleep(137);
					for (int i = 0; i < command.length; i++) {
						terminal.sendCommand(command[i]);
						terminal.sendCommand(command[command.length - i - 1]);
						terminal.sendCommand(command[i]);
						terminal.sendCommand(command[command.length - i - 1]);
					}
				} catch (InterruptedException e) {
					running = false;
				}
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
