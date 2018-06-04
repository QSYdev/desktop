package ar.com.desktop;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ar.com.terminal.Color;
import ar.com.terminal.Event.ExternalEvent;
import ar.com.terminal.Event.ExternalEvent.ExternalEventVisitor;
import ar.com.terminal.EventListener;
import ar.com.terminal.Terminal;

public final class QSYFrame extends JFrame implements AutoCloseable {

	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 550;
	private static final int HEIGHT = 600;

	private final ExternalEventHandler eventHandler;

	private final SearchPanel searchPanel;
	private final CommandPanel commandPanel;
	private final CustomRoutinePanel customRoutinePanel;
	private final PlayerExecutionPanel playerExecutionPanel;

	private final Terminal terminal;

	public QSYFrame(Terminal terminal) {
		super("QSY Packet Sender");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, WIDTH, HEIGHT);
		setLocationRelativeTo(null);

		this.terminal = terminal;

		searchPanel = new SearchPanel(this);
		commandPanel = new CommandPanel(this);
		customRoutinePanel = new CustomRoutinePanel(this);
		playerExecutionPanel = new PlayerExecutionPanel(this);

		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 6;
		contentPane.add(searchPanel, c);

		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 2;
		contentPane.add(commandPanel, c);

		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 2;
		contentPane.add(customRoutinePanel, c);

		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 2;
		contentPane.add(playerExecutionPanel, c);

		setContentPane(contentPane);

		searchPanel.setEnabled(true);
		commandPanel.setEnabled(false);
		customRoutinePanel.setEnabled(true);
		playerExecutionPanel.setEnabled(true);

		this.eventHandler = new ExternalEventHandler();
		setVisible(true);
	}

	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

	public CommandPanel getCommandPanel() {
		return commandPanel;
	}

	public CustomRoutinePanel getCustomRoutinePanel() {
		return customRoutinePanel;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public ExternalEventHandler getEventHandler() {
		return eventHandler;
	}

	@Override
	public void close() {
		searchPanel.close();
		commandPanel.close();
		customRoutinePanel.close();
		eventHandler.close();
	}

	private final class ExternalEventHandler extends EventListener<ExternalEvent> implements Runnable, ExternalEventVisitor, AutoCloseable {

		private final Thread thread;
		private volatile boolean running;

		public ExternalEventHandler() {
			this.running = true;
			this.thread = new Thread(this, "ExternalEventHandler");
			thread.start();
		}

		@Override
		public void run() {
			while (running) {
				try {
					ExternalEvent event = getEvent();
					SwingUtilities.invokeLater(() -> {
						event.accept(ExternalEventHandler.this);
					});
				} catch (InterruptedException e) {
					running = false;
				}
			}
		}

		@Override
		public void visit(ExternalEvent.ConnectedNode event) {
			searchPanel.connectedNode(event.getPhysicalId(), event.getNodeAddress(), Color.NO_COLOR);
		}

		@Override
		public void visit(ExternalEvent.DisconnectedNode event) {
			searchPanel.disconnectedNode(event.getPhysicalId());
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

	public static void main(String[] args) throws Exception {
		Terminal terminal = new Terminal("192.168.1.112");

		QSYFrame view = new QSYFrame(terminal);
		terminal.addListener(view.getEventHandler());
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				view.close();
				terminal.close();
			}
		});
		terminal.start();
	}

}
