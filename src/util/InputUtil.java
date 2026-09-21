package util;

import java.util.Scanner;

public class InputUtil {
	// 1つのScannerを使い回す設計
	private static final Scanner sc = new Scanner(System.in);
	
	/**
	 * 入力を文字列で受け取り、空白・改行を削除するメソッド
	 * @param prompt 入力された文字列
	 * @return 入力をtrimで空白、改行を削除して返す
	 */
	public String readLine(String prompt) {
		System.out.print(prompt);
		return sc.nextLine().trim();
	}
	
	/**
	 * 入力された文字列を文字列からint型に変換するメソッド
	 * @param prompt 
	 * @return 数値にパースすることができた場合はそのままnumberをreturn、Exceptionが起きた時は数値を入力してくださいとメッセージを出力
	 * @throws NumberFormatException  int型に変換を失敗した時throwされるException
	 */
	public int readInt(String prompt) throws NumberFormatException {
		while(true) {
			System.out.print(prompt);
			// 入力を受けとる。この時点ではまだ文字列のまま
			String input = sc.nextLine();
			try {
				int number = Integer.parseInt(input);
				return number;
			} catch (NumberFormatException e) {
				System.out.println("数値を入力してください");
			}
		}
	}
	
	/**
	 * 入力された番号が min ~ max の間の値かを確認するメソッド
	 * @param prompt 入力値
	 * @param min 入力の最小値
	 * @param max 入力の最大値
	 * @return 最小値から最大値の間であれば入力された番号を返す、
	 */
	public int readIntInRange(String prompt, int min, int max) {
		while(true) {
			int inputNumber = readInt(prompt);
			if (inputNumber > max || inputNumber < min) {
				System.out.println(min + "~" + max + "の数値を入力してください");
			} else {
				return inputNumber;
			}
		}
	}
	/**
	 * 終了メソッド
	 */
	public void close() {
		sc.close();
	}
}