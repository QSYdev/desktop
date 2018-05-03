package ar.com.desktop;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ar.com.terminal.internal.Terminal;
import ar.com.terminal.shared.Color;
import ar.com.terminal.shared.EventListener;
import ar.com.terminal.shared.ExternalEvent;
import ar.com.terminal.shared.ExternalEventVisitor;

public final class QSYFrame extends JFrame implements AutoCloseable {

	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 550;
	private static final int HEIGHT = 600;

	private final EventHandler eventHandler;

	private final SearchPanel searchPanel;
	private final CommandPanel commandPanel;
	private final CustomRoutinePanel routinePanel;
	private final StressPanel stressPanel;

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
		routinePanel = new CustomRoutinePanel(this);
		stressPanel = new StressPanel(this);

		Container rightPane = new Container();
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		rightPane.add(commandPanel);
		rightPane.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

		rightPane.add(routinePanel);
		rightPane.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

		rightPane.add(stressPanel);
		rightPane.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(searchPanel, BorderLayout.CENTER);
		contentPane.add(rightPane, BorderLayout.EAST);
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		commandPanel.setEnabled(false);
		stressPanel.setEnabled(false);

		this.eventHandler = new EventHandler();
		setVisible(true);
	}

	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

	public CommandPanel getCommandPanel() {
		return commandPanel;
	}

	public CustomRoutinePanel getRoutinePanel() {
		return routinePanel;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	@Override
	public void close() {
		searchPanel.close();
		commandPanel.close();
		eventHandler.close();
	}

	private final class EventHandler extends EventListener<ExternalEvent> implements Runnable, ExternalEventVisitor, AutoCloseable {

		private final Thread thread;
		private volatile boolean running;

		public EventHandler() {
			this.running = true;

			this.thread = new Thread(this, "View Task");
			thread.start();
		}

		@Override
		public void run() {
			while (running) {
				try {
					ExternalEvent event = getEvent();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							event.accept(EventHandler.this);
						}
					});
				} catch (InterruptedException e) {
					running = false;
				}
			}
		}

		@Override
		public void visit(ExternalEvent.Touche event) {
			searchPanel.editNode(event.getToucheArgs().getPhysicalId(), Color.NO_COLOR);
		}

		@Override
		public void visit(ExternalEvent.ConnectedNode event) {
			searchPanel.addNewNode(event.getPhysicalId(), event.getNodeAddress(), Color.NO_COLOR);
		}

		@Override
		public void visit(ExternalEvent.DisconnectedNode event) {
			searchPanel.removeNode(event.getPhysicalId());
		}

		@Override
		public void visit(ExternalEvent.InternalException event) {
			event.getException().printStackTrace();
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
