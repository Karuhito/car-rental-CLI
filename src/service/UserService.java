package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserService {
	private static final String USERS_CSV = "data/users.csv";
	
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
				// id,name,age,isATLimited
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
			// id,name,age,isATLimited
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