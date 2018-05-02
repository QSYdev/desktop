package ar.com.desktop;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import ar.com.terminal.shared.Color;

public final class QSYTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] columnsName = { "ID", "IP Node", "Color" };
	private final List<Integer> nodes;

	public QSYTableModel() {
		this(new Object[][] {});
	}

	public QSYTableModel(Object[][] rowData) {
		super(rowData, columnsName);
		this.nodes = new LinkedList<>();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public synchronized void addNode(int physicalId, InetAddress nodeAddress, Color color) {
		addRow(new Object[] { physicalId, nodeAddress, color });
		nodes.add(physicalId);
	}

	public synchronized void editNode(int physicalId, Color color) {
		int rowToedit = nodes.indexOf(physicalId);
		if (rowToedit != -1) {
			setValueAt(color, rowToedit, 2);
		}
	}

	public synchronized void removeNode(int physicalId) {
		int rowToDelete = nodes.indexOf(physicalId);
		if (rowToDelete != -1) {
			removeRow(rowToDelete);
			nodes.remove(rowToDelete);
		}

	}

}
