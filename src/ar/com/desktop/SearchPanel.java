package ar.com.desktop;

import java.awt.event.ActionEvent;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;

import ar.com.terminal.Color;

public final class SearchPanel extends JPanel implements AutoCloseable {

	private static final long serialVersionUID = 1L;

	private final QSYTable table;
	private final JButton btnStartSearch;
	private final JButton btnStopSearch;

	public SearchPanel(QSYFrame parent) {
		this.setBorder(BorderFactory.createTitledBorder("Nodos"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.table = new QSYTable();
		JScrollPane scrollPanel = new JScrollPane(table);
		this.add(scrollPanel);

		btnStartSearch = new JButton("Buscar nodos");
		btnStartSearch.setAlignmentX(CENTER_ALIGNMENT);
		this.add(btnStartSearch);

		btnStopSearch = new JButton("Finalizar la busqueda de nodos");
		btnStopSearch.setAlignmentX(CENTER_ALIGNMENT);
		btnStopSearch.setVisible(false);
		this.add(btnStopSearch);

		btnStartSearch.addActionListener((ActionEvent e) -> {
			parent.getTerminal().searchNodes();
			btnStopSearch.setVisible(true);
			btnStartSearch.setVisible(false);
		});

		table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			int selectedRow = table.getSelectedRow();
			parent.getCommandPanel().setEnabled(selectedRow != -1);
		});

		btnStopSearch.addActionListener((ActionEvent e) -> {
			parent.getTerminal().finalizeNodesSearching();
			btnStopSearch.setVisible(false);
			btnStartSearch.setVisible(true);
		});
	}

	public void connectedNode(int physicalId, InetAddress nodeAddress, Color color) {
		table.addNode(physicalId, nodeAddress);
	}

	public void disconnectedNode(int physicalId) {
		table.removeNode(physicalId);
	}

	public Integer getSelectedNodeId() {
		return table.getSelectedNodeId();
	}

	@Override
	public void setEnabled(boolean enabled) {
		table.setEnabled(enabled);
		btnStartSearch.setEnabled(enabled);
		btnStopSearch.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public void close() {
		return;
	}
}
