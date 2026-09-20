package main;

import java.util.List;
import model.Car;
import model.CarStatus;
import model.User;
import service.CarService;
import service.UserService;
import util.InputUtil;


public class UserMenu {
  private InputUtil inputUtil;
  private UserService userService;
  private CarService carService;
  private List<User> users;
  private List<Car> cars;


  public UserMenu(InputUtil inputUtil, UserService userService, CarService carService,
      List<User> users, List<Car> cars) {
    this.inputUtil = inputUtil;
    this.userService = userService;
    this.carService = carService;
    this.users = users;
    this.cars = cars;
  }

  /**
   * 
   */
  public void start() {
    while (true) {
      int choice = inputUtil.readIntInRange(
          "1: レンタル可能な車一覧を表示\n2: 自分のユーザー情報を確認する\n3: 車をレンタルする\n4: 車を返却する\n5: 新規ユーザー登録\n6: ユーザー情報の更新\n0: トップメニューに戻る",
          0, 6);

      // 一覧
      if (choice == 1) {
        List<Car> availableCars = carService.filterByStatus(cars, CarStatus.AVAILABLE);
        System.out.println("レンタルできる車一覧");
        System.out.println("ID | 車種 | 色 | メーカー | トランスミッション");
        for (Car car : availableCars) {
          System.out.println(car.getId() + "|" + car.getVehicleModel() + "|" + car.getColor() + "|"
              + car.getMaker().getLabel() + "|" + car.getTransmission().getLabel());
        }
        // 自分のユーザーデータを確認
      } else if (choice == 2) {
        System.out.println("自分のユーザー情報を調べる");
        while (true) {
          String userName = inputUtil.readLine("あなたの名前を入力してください(0でもどる)");
          if (userName.equals("0")) {
            break;
          }
          User myUserData = userService.findUserByName(users, userName);
          if (myUserData == null) {
            System.out.println("ユーザー情報を取得できませんでした。");
            continue;
          }
          String atLimited = "AT限定";
          if (!myUserData.isATLimited()) {
            atLimited = "MT解放済み";
          }
          System.out.println("あなたのユーザーデータ");
          System.out.println("ID: " + myUserData.getId() + "名前: " + myUserData.getName() + "年齢: "
              + myUserData.getAge() + "MT解放状況: " + atLimited);
        }
        // 車を借りる
      } else if (choice == 3) {
        System.out.println("車を借りる");
        while (true) {
          int userId = inputUtil.readInt("あなたのユーザーIDを入力してください(戻る場合は0を押してください):");
          if (userId == 0) {
            break;
          }
          User rentalUser = userService.findUserById(users, userId);
          if (rentalUser == null) {
            System.out.println("あなたのユーザーIDが見つかりませんでした。");
            continue;
          }

          if (carService.rentCar(cars, rentalUser, inputUtil.readInt("レンタルしたい車のIDを入力してください:"))) {
            System.out.println("車を借りました。");
            break;
          } else {
            System.out.println("車をレンタルできませんでした");
            continue;
          }
        }
        // 車を返却する
      } else if (choice == 4) {
        while (true) {
          int userId = inputUtil.readInt("あなたのユーザーIDを入力してください(0で戻る):");
          if (userId == 0) {
            break;
          }
          if (carService.returnCar(cars, inputUtil.readInt("あなたのレンタルしている車のIDを入力:"), userId)) {
            System.out.println("車を返却しました。ご利用ありがとうございました。");
            break;
          } else {
            System.out.println("車を返却できませんでした");
            continue;


          }
        }
        // 新規ユーザー登録
      } else if (choice == 5) {
        System.out.println("新規登録(戻りたい場合は0を入力してください)");
        while (true) {
          int newUserId = userService.generateNextUserId(users);
          String newUserName = inputUtil.readLine("あなたの名前を入力:");
          if (newUserName.equals("0")) {
            break;
          }
          int newUserAge = inputUtil.readInt("年齢を入力");
          if (newUserAge == 0) {
            break;
          }
          userService.addUser(users, new User(newUserId, newUserName, newUserAge, choiceToBoolean(
              inputUtil.readIntInRange("免許がAT限定の場合は1、マニュアル免許の場合は2を入力してください;", 1, 2))));
          System.out.println("ユーザー登録が完了しました。あなたのユーザーIDは" + newUserId + "です。");
        }
        // ユーザー更新。更新パターンが複数あるのでServiceにメソッドを用意するのではなく、UserMenu側でsetterを使用して更新する形にする。
      } else if (choice == 6) {
        System.out.println("ユーザー情報更新");
        while (true) {
          int userId = inputUtil.readInt("あなたのユーザーIDを入力してください(0で戻る):");
          if (userId == 0) {
            break;
          }
          User user = userService.findUserById(users, userId);
          // ユーザーIDが見つからなかった時
          if (user == null) {
            System.out.println("ユーザーIDが見つかりませんでした");
            continue;
          }
          // 名前の更新 isBlankで入力が空でない時のみsetterで名前を更新する
          String updateName = inputUtil.readLine("名前を入力してください(更新しない場合はEnter入力でスキップ):");
          if (!updateName.isBlank()) {
            user.setName(updateName);
          }

          // 年齢の更新
          String updateAge = inputUtil.readLine("年齢を入力してください(更新しない場合はEnter入力でスキップ):");
          if (!updateAge.isBlank()) {
            // intへの変換する時にNumberFormatExceptionが起きる可能性がある
            try {
              user.setAge(Integer.parseInt(updateAge));
            } catch (NumberFormatException e) {
              System.out.println("数値で入力を行ってください。");
            }
          }

          // MT取得状況の更新、更新したいユーザーがすでにMT免許を持っている場合は表示しない。
          if (user.isATLimited()) {
            String updateAtLimited =
                inputUtil.readLine("AT限定を解除した場合はyを入力してください(更新しない場合はEnter入力でスキップ);");
            if (updateAtLimited.equals("y")) {
              user.setATLimited(false);
            }
          }
        }
      } else {
        System.out.println("トップメニューに戻ります。");
        break;
      }
    }
  }

  /**
   * intで受け取ったyes/noなど2択の選択をbooleanに変換するメソッド
   * 
   * @param choice 選択した番号
   * @return 1の場合はtrue、2の場合はfalseを返す
   */
  public boolean choiceToBoolean(int choice) {
    if (choice == 1) {
      return true;
    }
    return false;
  }
}
