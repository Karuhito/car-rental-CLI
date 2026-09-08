package model;


public class Car {
	private int id;
	private String vehicleModel;
	private String color;
	private CarMaker maker;
	private CarStatus status;
	private int cumulativeMileage;
	private Integer currentUserId;
	private CarTransmission transmission;
	
	

	public Car(int id, String vehicleModel, String color, CarMaker maker, CarStatus status, int cumulativeMileage,
			Integer currentUserId, CarTransmission transmission) {
		this.id = id;
		this.vehicleModel = vehicleModel;
		this.color = color;
		this.maker = maker;
		this.status = status;
		this.cumulativeMileage = cumulativeMileage;
		this.currentUserId = currentUserId;
		this.transmission = transmission;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public CarMaker getMaker() {
		return maker;
	}

	public void setMaker(CarMaker maker) {
		this.maker = maker;
	}

	public CarStatus getStatus() {
		return status;
	}

	public void setStatus(CarStatus status) {
		this.status = status;
	}

	public int getCumulativeMileage() {
		return cumulativeMileage;
	}

	public void setCumulativeMileage(int cumulativeMileage) {
		this.cumulativeMileage = cumulativeMileage;
	}

	public CarTransmission getTransmission() {
		return transmission;
	}

	public void setTransmission(CarTransmission transmission) {
		this.transmission = transmission;
	}

	public int getId() {
		return id;
	}

	public void setCurrentUserId(Integer currentUserId) {
		this.currentUserId = currentUserId;
	}

	public Integer getCurrentUserId() {
		return currentUserId;
	}
	
}