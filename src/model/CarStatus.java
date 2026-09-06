package model;

public enum CarStatus {
	AVAILABLE("待機中"),
	RENTED("貸出中"),
	MAINTENANCE("点検中");
	
	private final String label;
	
	CarStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}