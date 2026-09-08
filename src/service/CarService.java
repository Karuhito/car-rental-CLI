package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Car;
import model.CarMaker;
import model.CarStatus;
import model.CarTransmission;

public class CarService{
	private static final String CARS_CSV = "data/cars.csv";
	
	/**
	 * 車の新規登録を行う処理
	 * @param cars 車一覧
	 * @param newCar 新規登録する車のインスタンス
	 */
	public void addCar(List<Car> cars, Car newCar) {
		cars.add(newCar);
	}
	
	/**
	 * 車の一覧を返すメソッド
	 * @param cars
	 * @return
	 */
	public List<Car> listCars(List<Car> cars) {
		return cars;
	}
	
	/**
	 * 車のstatusで絞り込んだ一覧を返す。
	 * @param cars
	 * @param status
	 * @return
	 */
	public List<Car> filterByStatus(List<Car> cars, CarStatus status){
		return null;
	}
	
	/**
	 * メーカーで車を絞り込んだ一覧を返す
	 * @param cars
	 * @param maker
	 */
	public List<Car> filterByMaker(List<Car> cars, CarMaker maker) {
		return null;
	}
	
	/**
	 * 車の情報を更新するメソッド
	 * @param cars
	 * @param carId
	 */
	public void updateCar(List<Car> cars, int carId) {
		
	}
	
	/**
	 * 車の情報を削除するメソッド
	 * @param cars
	 * @param carId
	 */
	public void deleteCar(List<Car> cars, int carId) {
		
	}
	
	/**
	 * cars.csvを読み込んで、List<Car>を作って返すメソッド
	 * @return cars.csvにあるCarのデータをリストにして返す
	 * @throws IOException
	 */
	public List<Car> loadFromCsv() throws IOException {
		// Carの配列を定義
		List<Car> cars = new ArrayList<Car>();
		try (BufferedReader reader = new BufferedReader(new FileReader(CARS_CSV))) {
			// 1行目はcsvの構造の説明なので読み捨てる
			String header = reader.readLine();
			
			// whileでcsvファイルの終わりまで1行ずつ読み込む
			String line;
			while ((line = reader.readLine()) != null) {
				// カンマ区切りでその車のフィールドの配列を作る
				String[] data = line.split(",", -1);
				// 各フィールドに型を変換する
				int id = Integer.parseInt(data[0]);
				String vehicleModel = data[1];
				String color = data[2];
				CarMaker maker = CarMaker.valueOf(data[3]);
				CarStatus status = CarStatus.valueOf(data[4]);
				int cumulativeMileage = Integer.parseInt(data[5]);
				// 使用中のユーザーがいない場合はnull
				Integer currentUserId = data[6].isEmpty() ? null : Integer.valueOf(data[6]);
 				CarTransmission transmission = CarTransmission.valueOf(data[7]);
 				
 				// carsに読み込んだCarを追加
				cars.add(new Car(id, vehicleModel, color, maker, status, cumulativeMileage, currentUserId, transmission));
			}
		} catch (Exception e) {
			throw new IOException("CSVの読み込みに失敗しました",e);
		}
		return cars;
	}
	
	/**
	 * List<Car>を引数に受け取りcars.csvに書き込み処理を行うメソッド
	 * @param cars Carの入ったリスト
	 * @throws IOException ファイルの処理で問題が起きた場合throwする
	 */
	public void saveToCsv(List<Car> cars) throws IOException {
		// BufferedWriterでcars.csvにcarsを保存させる
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(CARS_CSV))) {
			writer.write("id,vehicleModel,color,maker,status,cumulativeMileage,currentUserId,transmission");
			// 改行
			writer.newLine();
			
			// forでCarごとにフィールドを取り出し、文字列に変換、csvに書き込む
			// id | vehicleModel | color | maker | status | cumulativeMileage | currentUserId | transmission
			for (Car car : cars) {
				String id = String.valueOf(car.getId());
				String vehicleModel = car.getVehicleModel();
				String color = car.getColor();
				String maker = car.getMaker().name();
				String status = car.getStatus().name();
				String cumulativeMileage = String.valueOf(car.getCumulativeMileage());
				// nullの時は空文字でcsvに保存する
				String currentUserId = (car.getCurrentUserId() == null) ? "" : String.valueOf(car.getCurrentUserId());
				String transmission = car.getTransmission().name();
				
				writer.write(String.join(",", id, vehicleModel, color, maker, status, cumulativeMileage, currentUserId, transmission));
				writer.newLine();
			}
		} catch (Exception e) {
			throw new IOException("CSVへの書き込み保存に失敗しました", e);
		}
	}
	
}