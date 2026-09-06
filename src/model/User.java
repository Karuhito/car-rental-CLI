package model;

public class User {
	private int id;
	private String name;
	private int age;
	private boolean isATLimited;
	
	public User(int id, String name, int age, boolean isATLimited) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.isATLimited = isATLimited;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public boolean isATLimited() {
		return isATLimited;
	}

	public void setATLimited(boolean isATLimited) {
		this.isATLimited = isATLimited;
	}

	public int getId() {
		return id;
	}
	
	
	
}