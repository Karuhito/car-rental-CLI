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

  /**
   * コンストラクタ
   * 
   * @param id 車のID
   * @param vehicleModel 車の車種名
   * @param color 車の色
   * @param maker 車のメーカー CarMaker
   * @param status 車の状態。CarStatus
   * @param cumulativeMileage 走行距離
   * @param currentUserId 車を借りているユーザー 誰も借りていない場合はnull intではnullを表せないのでラッパークラスのIntegerで管理
   * @param transmission 車のトランスミッションの判別 CarTransmission
   */
  public Car(int id, String vehicleModel, String color, CarMaker maker, CarStatus status,
      int cumulativeMileage, Integer currentUserId, CarTransmission transmission) {
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
