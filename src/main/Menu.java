package main;

import util.InputUtil;

public class Menu {

  private InputUtil inputUtil;
  private UserMenu userMenu;
  private EmployeeMenu employeeMenu;

  public Menu(InputUtil inputUtil, UserMenu userMenu, EmployeeMenu employeeMenu) {
    this.inputUtil = inputUtil;
    this.userMenu = userMenu;
    this.employeeMenu = employeeMenu;
  }


  public void start() {
    while (true) {
      //
      int choice = inputUtil.readIntInRange("1: 利用者メニュー\n2: 業者用メニュー\n0: 終了\n選択してください: ", 0, 2);

      // 入力された番号に応じて処理を記述
      if (choice == 1) {
        userMenu.start();
      } else if (choice == 2) {
        employeeMenu.start();
      } else {
        System.out.println("終了します");
        break;
      }
    }
  }
}
