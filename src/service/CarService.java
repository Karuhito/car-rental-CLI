
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
	 * @return 車の一覧を返す。
	 */
	public List<Car> listCars(List<Car> cars) {
		return cars;
	}
	
	/**
	 * 状態で車を絞り込む。リスト内ループを行い、getStatusrとstatusが一致した場合filterdCarsにその車を追加する
	 * @param cars 車一覧
	 * @param status 絞り込みたい車の状態
	 * @return 絞り込んだ状態の車の一覧を返す。0件の場合は空のListを返す。
	 */
	public List<Car> filterByStatus(List<Car> cars, CarStatus status){
		List<Car> filteredCars = new ArrayList<Car>();
		for (Car car : cars ) {
			if (car.getStatus() == status) {
				filteredCars.add(car);
			}
		}
		return filteredCars;
	}
	
	/**
	 * メーカーで車を絞り込むメソッド。リスト内ループを行い、getMakerとmakerが一致した場合filterdCarsにその車を追加する
	 * @param cars 車の一覧
	 * @param maker 絞り込みたい車のメーカー
	 * @return 絞り込んだメーカーの車の一覧を返す。0件の場合は空のListを返す
	 */
	public List<Car> filterByMaker(List<Car> cars, CarMaker maker) {
		List<Car> filteredCars = new ArrayList<Car>();
		for (Car car : cars ) {
			if (car.getMaker() == maker) {
				filteredCars.add(car);
			}
		}
		return filteredCars;
	}
	

	/**
	 * 車の情報を更新するメソッド。
	 * 現段階では走行距離の更新のみを行う。
	 * @param cars 車の一覧
	 * @param carId 更新したい車のID
	 * @param mileage 更新後の走行距離
	 * @return　carIdが存在する車のIDで更新に成功した場合はtrue、存在せず、更新に失敗する場合はfalse
	 */
	public boolean updateCar(List<Car> cars, int carId, int mileage) {
		Car car = findCarById(cars, carId);
		if (car != null) {
			car.setCumulativeMileage(mileage);
			return true;
		}
		return false;
	}
	
	/**
	 * 車の状態を見て、貸出中の場合はfalse、貸出中でない場合はtrueを返して、車の情報を消しても良いか判断するメソッド
	 * @param car 消したい車
	 * @return CarStatus.RENTEDの場合はfalse、RENTEDではない場合はtrue
	 */
	public boolean canDeleteCar(Car car) {
		if (car.getStatus() == CarStatus.RENTED) {
			return false;
		}
		return true;
	}
	
	/**
	 * 車の情報を削除するメソッド
	 * @param cars 車の一覧
	 * @param carId 削除したい車のID
	 * @return carIdがcarsに存在する車のIDかつStatusが貸出中ではない場合は削除してtrue、carIdが存在しないまたはStatusが貸出中の場合はfalse
	 */
	public boolean deleteCar(List<Car> cars, int carId) {
		Car car = findCarById(cars, carId);
		if (car != null && canDeleteCar(car)) {
			cars.remove(car);
			return true;
		}
		return false;
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
	
	/**
	 * 車のIDでその車の情報を取得するメソッド
	 * @param cars 車の一覧
	 * @param id 調べたい車のID
	 * @return 存在するIDの場合はその車のオブジェクトを返す、存在しない場合はnullで返す。
	 */
	public Car findCarById(List<Car> cars, int id) {
		for (Car car : cars) {
			if (car.getId() == id) {
				return car;
			}
		}
		return null;
	}
	
	
	
	
}