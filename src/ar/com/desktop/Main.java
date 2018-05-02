package ar.com.desktop;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ar.com.terminal.internal.Terminal;

public final class Main {

	public static void main(final String[] args) throws Exception {
		Terminal terminal = new Terminal("192.168.1.112");

		QSYFrame view = new QSYFrame(terminal);
		terminal.addListener(view.getEventHandler());
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				view.close();
				terminal.close();
			}
		});
		terminal.start();
	}

}
