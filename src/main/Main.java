package main;

import java.io.IOException;
import java.util.List;
import model.Car;
import model.User;
import service.CarService;
import service.UserService;
import util.InputUtil;


public class Main {
  public static void main(String[] args) throws IOException {
    // InputUtil、 CarService、 UserServiceを生成
    InputUtil inputUtil = new InputUtil();
    CarService carService = new CarService();
    UserService userService = new UserService();
    List<User> users = null;
    List<Car> cars = null;


    try {
      // CSVから車とユーザーのデータをを読み込む
      try {
        users = userService.loadFromCsv();
        cars = carService.loadFromCsv();
      } catch (IOException e) {
        System.out.println("データの読み込みに失敗しました");
        return; // 早期リターンで先に進ませない
      }
      // 利用者、従業員、トップメニューそれぞれを生成
      UserMenu userMenu = new UserMenu(inputUtil, userService, carService, users, cars);
      EmployeeMenu employeeMenu = new EmployeeMenu(inputUtil, userService, carService, users, cars);
      Menu menu = new Menu(inputUtil, userMenu, employeeMenu);
      // トップメニューを起動
      menu.start();

      // データをCSVへ書き込む
      try {
        carService.saveToCsv(cars);
        userService.saveToCsv(users);
      } catch (IOException e) {
        System.out.println("データの保存に失敗しました");
      }
    } finally {
      inputUtil.close();
    }
  }
}
