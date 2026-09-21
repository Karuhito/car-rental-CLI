package model;


public enum CarTransmission {
  AT("オート"), MT("マニュアル");

  private final String label;

  private CarTransmission(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
