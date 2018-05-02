package ar.com.desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

import ar.com.terminal.internal.Terminal;
import ar.com.terminal.shared.Color;
import ar.com.terminal.shared.QSYPacket;

public final class StressPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Terminal terminal;
	private final JButton btnStartStressTest;
	private final JButton btnStopStressTest;

	private StressTask stressTask;

	public StressPanel(final QSYFrame parent) {
		this.setLayout(new GridLayout(0, 1, 2, 2));

		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Stress Test"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		this.terminal = parent.getTerminal();

		btnStartStressTest = new JButton("Start Stress Test");
		btnStartStressTest.setEnabled(true);
		this.add(btnStartStressTest);

		btnStartStressTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				stressTask = new StressTask();
				btnStartStressTest.setEnabled(false);
				btnStopStressTest.setEnabled(true);
			}

		});

		btnStopStressTest = new JButton("Stop Stress Test");
		btnStopStressTest.setEnabled(false);
		this.add(btnStopStressTest);

		btnStopStressTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stressTask.close();
				stressTask = null;
				btnStopStressTest.setEnabled(false);
				btnStartStressTest.setEnabled(true);
			}
		});

	}

	private final class StressTask implements Runnable, AutoCloseable {

		private final QSYPacket.CommandArgs[] command;

		private boolean running;
		private final Thread thread;

		public StressTask() {
			command = new QSYPacket.CommandArgs[10];
			for (int i = 0; i < command.length; i++) {
				command[i] = new QSYPacket.CommandArgs(i, Color.CYAN, 500, 3);
			}
			this.running = true;
			this.thread = new Thread(this, "Stress Task");
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
