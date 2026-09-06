package model;

public enum CarMaker {
	TOYOTA("トヨタ"),
	NISSAN("日産"),
	HONDA("ホンダ"),
	SUBARU("スバル"),
	OTHER("その他");
	
	private final String label;
	
	CarMaker(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
