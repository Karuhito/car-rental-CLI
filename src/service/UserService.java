package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Car;
import model.User;

public class UserService {
	private static final String USERS_CSV = "data/users.csv";
	
	/**
	 *  ユーザー一覧からIDでユーザーの情報を取り出すメソッド
	 * @param users ユーザーデータ一覧
	 * @param userId 取得したいユーザーのID
	 * @return 存在するユーザーのIDだったらそのユーザーを返す。存在しない場合はnullを返す。
	 */
	public User findUserById(List<User> users, int userId) {
		for (User user : users) {
			if (user.getId() == userId) {
				return user;
			}
		}
		return null;
	}
	
	/**
	 * 新規ユーザーをユーザーデータ一覧に追加するメソッド
	 * @param users ユーザーデータ一覧
	 * @param newUser 新規登録したユーザー
	 */
	public void addUser(List<User> users, User newUser) {
		users.add(newUser);
	}
	
	/**
	 * ユーザー一覧を返すメソッド
	 * @param users ユーザーデータ一覧
	 * @return ユーザーデータ一覧をそのまま返す
	 */
	public List<User> listUsers(List<User> users) {
		return users;
	}
	
	/**
	 * 削除したいユーザーのIDが車一覧の現在借りているユーザーのIDに含まれていないか確認するメソッド
	 * @param user 削除したいユーザー
	 * @param cars 車一覧 
	 * @return 車を借りているユーザーが存在しているかつ削除したいユーザーと車を借りているユーザーのIDが一致する場合はfalse、そうでない場合はtrueを返す
	 */
	public boolean canDeleteUser(User user, List<Car> cars) {
		for (Car car : cars) {
			// 車を借りているユーザーが存在しているかつ削除したいユーザーと車を借りているユーザーのIDが一致する場合
			if ( car.getCurrentUserId() != null && user.getId() == car.getCurrentUserId()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * ユーザー情報をcanDeleteUserで削除できるか確認し削除するメソッド
	 * @param users ユーザー一覧
	 * @param cars 車一覧 canDeleteUserの引数で使う
	 * @param userId 削除したいユーザーのID
	 * @return 削除に成功の場合はtrue、 失敗する場合はfalseを返す
	 */
	public boolean deleteUser (List<User> users, List<Car> cars, int userId) {
		User user = findUserById(users, userId);
		if (user != null && canDeleteUser(user, cars)) {
			users.remove(user);
			return true;
		}
		return false;
	}
	
	/**
	 * users.csvを読み込んで、List<User>を作って返すメソッド
	 * @return List<User> csvに入っているList<User>を返す
	 * @throws IOException csvファイル読み込み時に失敗した時にthrowする
	 */
	public List<User> loadFromCsv() throws IOException {
		List<User> users = new ArrayList<User>();
		try (BufferedReader reader = new BufferedReader(new FileReader(USERS_CSV))) {
			// 1行目はcsvの構造の説明なので読み捨てる
			String header = reader.readLine();
			
			String line;
			while((line = reader.readLine()) != null ) {
				// カンマ区切りでそのユーザーのフィールドの配列を作る
				String[] data = line.split(",", -1);
				// 各フィールドの変数を用意する
				int id = Integer.parseInt(data[0]);
				String name = data[1];
				int age = Integer.parseInt(data[2]);
				boolean isATLimited = Boolean.parseBoolean(data[3]);
				
				users.add(new User(id, name, age, isATLimited));
			}
		} catch (Exception e) {
			throw new IOException("CSVの読み込みに失敗しました",e);
		}
		return users;
	}
	
	/**
	 * List<User>を引数に受け取りusers.csvに書き込み処理を行うメソッド
	 * @param users Userの入ったリスト
	 * @throws IOException ファイルの処理で問題が起きた場合throwする
	 */
	public void saveToCsv(List<User> users) throws IOException {
		// BufferedWriterでusers.csvにuserを保存させる
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_CSV))) {
			writer.write("id,name,age,isATLimited");
			// 改行
			writer.newLine();
			
			// forでUserごとにフィールドを取り出し、文字列に変換、csvに書き込む
			// id | name | age | isATLimited
			for (User user : users) {
				String id = String.valueOf(user.getId());
				String name = user.getName();
				String age = String.valueOf(user.getAge());
				String isATLimited = String.valueOf(user.isATLimited());
				
				writer.write(String.join(",", id, name, age, isATLimited));
				writer.newLine();
			}
		} catch (Exception e) {
			throw new IOException("CSVへの書き込み保存に失敗しました", e);
		}
	}
}