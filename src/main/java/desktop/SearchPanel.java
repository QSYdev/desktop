package desktop;

import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;

import terminal.Event.ExternalEvent.ConnectedNode;
import terminal.Event.ExternalEvent.DisconnectedNode;
import terminal.Event.ExternalEvent.ExternalEventVisitor;

public final class SearchPanel extends JPanel implements AutoCloseable, ExternalEventVisitor {

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

		btnStartSearch = new JButton("Buscar de nodos");
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

	@Override
	public void visit(ConnectedNode event) {
		table.addNode(event.getPhysicalId(), event.getNodeAddress());
	}

	@Override
	public void visit(DisconnectedNode event) {
		table.removeNode(event.getPhysicalId());
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
