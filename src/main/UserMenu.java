package main;

import java.util.List;
import model.Car;
import model.CarMaker;
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
   * 利用者メニューを出力するメソッド 1. レンタル可能な車一覧を表示 2. 自分のユーザー情報を確認する 3. 車をレンタルする 4. 車を返却する 5. 新規ユーザー登録 6.
   * ユーザー情報の更新
   */
  public void start() {
    // 改行
    System.out.println();

    System.out.println("利用者メニュー");
    while (true) {
      int choice = inputUtil.readIntInRange(
          "1: レンタル可能な車一覧を表示\n2: 自分のユーザー情報を確認する\n3: 車をレンタルする\n4: 車を返却する\n5: 新規ユーザー登録\n6: ユーザー情報の更新\n0: トップメニューに戻る",
          0, 6);
      System.out.println();

      if (choice == 1) {
        showCarList();
        System.out.println();

      } else if (choice == 2) {
        System.out.println("自分のユーザー情報を調べる");
        while (true) {
          String userName = inputUtil.readLine("あなたの名前を入力してください(0でもどる)");
          if (userName.equals("0")) {
            break;
          }
          User myUserData = userService.findUserByName(users, userName);
          if (myUserData == null) {
            System.out.println("名前と一致するユーザー情報を取得できませんでした。");
            continue;
          }
          String atLimited = "AT車のみレンタル可能";
          if (!myUserData.isATLimited()) {
            atLimited = "MT車もレンタル可能";
          }
          Car currentRentalCar = carService.findCarByUserId(cars, myUserData.getId());
          // デフォルトでは借りている車はないと表示
          String currentRental = "現在あなたがレンタルしている車はありません";
          // 借りている車が存在する場合は借りている車のIDを表示
          if (currentRentalCar != null) {
            currentRental = "現在あなたが借りている車のIDは" + currentRentalCar.getId() + "です";
          }

          System.out.println("あなたのユーザーデータ");
          System.out.println("ID: " + myUserData.getId() + " 名前: " + myUserData.getName() + " 年齢: "
              + myUserData.getAge() + " MT解放状況: " + atLimited);
          System.out.println(currentRental);
        }
        System.out.println();

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
          break;
        }
        System.out.println();
        // ユーザー更新。更新パターンが複数あるのでServiceにメソッドを用意するのではなく、UserMenu側でsetterを使用して更新する形にする。
      } else if (choice == 6) {
        System.out.println("ユーザー情報更新");
        while (true) {
          int userId = inputUtil.readInt("あなたのユーザーIDを入力してください(0で戻る):");
          if (userId == 0) {
            break;
          }
          User user = userService.findUserById(users, userId);
          // ユーザーが見つからなかった時
          if (user == null) {
            System.out.println("ユーザーIDと一致するユーザーが見つかりませんでした");
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


  /**
   * レンタル可能な車一覧を押した時、全て表示かメーカーによって絞り込むかを選択させ、全て表示の場合はそのまま表示、メーカー絞り込みの場合はメーカー選択・絞り込みも行って一覧を表示させるメソッド
   */
  private void showCarList() {
    List<Car> availableCars = carService.filterByStatus(cars, CarStatus.AVAILABLE);
    System.out.println("レンタル可能な車一覧を表示(0でもどる)");
    while (true) {
      int listChoice =
          inputUtil.readIntInRange("表示形式を選択してください\n1: レンタル可能な車を全て表示\n2: メーカーで絞り込んで表示", 0, 2);

      if (listChoice == 1) {
        System.out.println("レンタル可能な車を全て表示");
        System.out.println(availableCars.size() + "件の車が見つかりました");
        System.out.println("ID | 車種名 | 色 | メーカー | 累計走行距離 | トランスミッション方式");
        for (Car car : availableCars) {
          showCarInfo(car);
        }
        System.out.println();
        continue;
      } else if (listChoice == 2) {
        System.out.println("レンタル可能な車をメーカーで絞り込んで表示");
        while (true) {
          CarMaker carMaker = selectCarMaker();
          if (carMaker == null) {
            break;
          }
          List<Car> filteredCars = carService.filterByMaker(availableCars, carMaker);
          // 絞り込んだ結果0件だった時
          if (filteredCars.isEmpty()) {
            System.out.println(carMaker.getLabel() + "の車は見つかりませんでした");
            continue;
          }
          System.out.println(filteredCars.size() + "件の車が見つかりました");
          System.out.println("ID | 車種名 | 色 | メーカー | 累計走行距離 | トランスミッション方式");
          for (Car car : filteredCars) {
            showCarInfo(car);
          }
          continue;
        }
      } else {
        break;
      }
    }
  }

  /**
   * 車の情報を出力するメソッド EmployeeMenuにも同様のメソッドがあるが、こちらは現在借りているユーザーと状態は表示しない。
   * 
   * @param car 情報を出力させたい車のオブジェクト
   */
  private void showCarInfo(Car car) {
    System.out.println(car.getId() + " | " + car.getVehicleModel() + " | " + car.getColor() + " | "
        + car.getMaker().getLabel() + " | " + car.getCumulativeMileage() + " | "
        + car.getTransmission().getLabel());
  }

  /**
   * メーカーを選択させるときに使用するメソッド
   * 
   * @return 番号の入力に応じて返り値に入れるメーカーを切り替える。0が入力されたときはnullを返す
   */
  private CarMaker selectCarMaker() {
    CarMaker maker;
    int makerChoice =
        inputUtil.readIntInRange("絞り込みたい車のメーカーを選択してください\n1:" + CarMaker.TOYOTA.getLabel() + "\n2:"
            + CarMaker.NISSAN.getLabel() + "\n3:" + CarMaker.HONDA.getLabel() + "\n4:"
            + CarMaker.SUBARU.getLabel() + "\n5:" + CarMaker.OTHER.getLabel(), 0, 5);
    if (makerChoice == 1) {
      maker = CarMaker.TOYOTA;
    } else if (makerChoice == 2) {
      maker = CarMaker.NISSAN;
    } else if (makerChoice == 3) {
      maker = CarMaker.HONDA;
    } else if (makerChoice == 4) {
      maker = CarMaker.SUBARU;
    } else if (makerChoice == 5) {
      maker = CarMaker.OTHER;
    } else {
      return null;
    }
    return maker;
  }
}
