package ar.com.desktop;

import java.awt.Dimension;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class QSYTable extends JTable {

	private static final long serialVersionUID = -8491727113914256516L;

	private final QSYTableModel model;

	public QSYTable() {
		super(new QSYTableModel());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setFillsViewportHeight(true);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);

		Dimension tableSize = getPreferredSize();
		getColumnModel().getColumn(0).setPreferredWidth(Math.round(tableSize.width * 0.15f));
		getColumnModel().getColumn(1).setPreferredWidth(Math.round(tableSize.width * 0.45f));
		getColumnModel().getColumn(2).setPreferredWidth(Math.round(tableSize.width * 0.40f));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

		this.model = (QSYTableModel) getModel();
	}

	public void addNode(int physicalId, InetAddress nodeAddress) {
		model.addNode(physicalId, nodeAddress);
	}

	public void removeNode(int physicalId) {
		model.removeNode(physicalId);
	}

	public Integer getSelectedNodeId() {
		return (Integer) getValueAt(getSelectedRow(), 0);
	}

	private static final class QSYTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;

		private static final String[] columnsName = { "ID", "IP", "BATERIA" };
		private final List<Integer> nodes;

		public QSYTableModel() {
			super(new Object[][] {}, columnsName);
			this.nodes = new LinkedList<>();
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		public void addNode(int physicalId, InetAddress nodeAddress) {
			addRow(new Object[] { physicalId, nodeAddress, "100%" });
			nodes.add(physicalId);
		}

		public void removeNode(int physicalId) {
			int rowToDelete = nodes.indexOf(physicalId);
			if (rowToDelete != -1) {
				removeRow(rowToDelete);
				nodes.remove(rowToDelete);
			}
		}

	}

}
