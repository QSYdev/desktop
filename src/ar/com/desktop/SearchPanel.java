package ar.com.desktop;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;

import ar.com.terminal.internal.Color;

public final class SearchPanel extends JPanel implements AutoCloseable {

	private static final long serialVersionUID = 1L;

	private final JTable table;
	private final JButton btnStartSearch;
	private final JButton btnStopSearch;

	private final QSYTableModel model;

	public SearchPanel(QSYFrame parent) {
		this.setBorder(BorderFactory.createTitledBorder("Nodos"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		table = new JTable(new QSYTableModel());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		Dimension tableSize = table.getPreferredSize();
		table.getColumnModel().getColumn(0).setPreferredWidth(Math.round(tableSize.width * 0.15f));
		table.getColumnModel().getColumn(1).setPreferredWidth(Math.round(tableSize.width * 0.45f));
		table.getColumnModel().getColumn(2).setPreferredWidth(Math.round(tableSize.width * 0.40f));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

		JScrollPane scrollPanel = new JScrollPane(table);
		this.add(scrollPanel);

		model = (QSYTableModel) table.getModel();

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
			parent.getCommandPanel().setEnabled(selectedRow != -1 && !parent.getCustomRoutinePanel().isRoutineRunning());
		});

		btnStopSearch.addActionListener((ActionEvent e) -> {
			parent.getTerminal().finalizeNodesSearching();
			btnStopSearch.setVisible(false);
			btnStartSearch.setVisible(true);
		});
	}

	public JTable getTable() {
		return table;
	}

	public void connectedNode(int physicalId, InetAddress nodeAddress, Color color) {
		model.addNode(physicalId, nodeAddress, color);
	}

	public void touche(int physicalId, Color color) {
		model.editNode(physicalId, color);
	}

	public void disconnectedNode(int physicalId) {
		model.removeNode(physicalId);
	}

	@Override
	public void close() {
		return;
	}
}
