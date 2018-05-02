package ar.com.desktop;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public final class QSYTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] columnsName = { "ID", "IP Node", "Status" };
	private final List<Integer> nodes;

	public QSYTableModel() {
		this(new Object[][] {});
	}

	public QSYTableModel(final Object[][] rowData) {
		super(rowData, columnsName);
		this.nodes = new LinkedList<>();
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return false;
	}

	public void addNode(final int physicalId, final InetAddress nodeAddress) {
		addRow(new Object[] { physicalId, nodeAddress, "enabled" });
		nodes.add(physicalId);
	}

	public void removeNode(final int physicalId, final InetAddress nodeAddress) {
		final int rowToDelete = nodes.indexOf(physicalId);
		if (rowToDelete != -1) {
			removeRow(rowToDelete);
			nodes.remove(rowToDelete);
		}

	}

}
