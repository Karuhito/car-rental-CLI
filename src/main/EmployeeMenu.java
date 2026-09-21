package main;

import java.util.List;
import model.Car;
import model.CarMaker;
import model.CarStatus;
import model.CarTransmission;
import model.User;
import service.CarService;
import service.UserService;
import util.InputUtil;

public class EmployeeMenu {
  private InputUtil inputUtil;
  private UserService userService;
  private CarService carService;
  private List<User> users;
  private List<Car> cars;
  // ログインパスワード セキュリティ上あまり良くないが今回はコード上で管理する
  private static final String PASSWORD = "password";


  public EmployeeMenu(InputUtil inputUtil, UserService userService, CarService carService,
      List<User> users, List<Car> cars) {
    this.inputUtil = inputUtil;
    this.userService = userService;
    this.carService = carService;
    this.users = users;
    this.cars = cars;
  }

  /**
   * メニューを表示させるメソッド ログイン -> メニュー表示 1. 車の新規登録 2. 車一覧 3. 車の情報更新 4. 車データ削除 5. ユーザー一覧表示 6. ユーザーデータ削除
   */
  public void start() {
    if (login()) {
      System.out.println();
      System.out.println("従業員メニュー");
      while (true) {
        int choice = inputUtil.readIntInRange(
            "1:車の新規登録\n2:レンタカー一覧・検索\n3: 車の情報更新\n4:車のデータ削除\n5:ユーザー一覧表示\n6:ユーザーデータ削除\n0:トップメニューに戻る",
            0, 6);

        if (choice == 1) {
          System.out.println("車の情報の新規登録(0を入力でメニューへ戻る)");
          while (true) {
            int newCarId = carService.generateNextCarId(cars);

            String vehicleModel = inputUtil.readLine("車の車種名を入力してください:");
            if (vehicleModel.equals("0")) {
              break;
            } else if (vehicleModel.isBlank()) {
              continue;
            }

            String color = inputUtil.readLine("車の色を入力してください:");
            if (color.equals("0")) {
              break;
            } else if (color.isBlank()) {
              continue;
            }
            CarMaker carMaker = selectCarMaker();
            if (carMaker == null) {
              break;
            }

            CarTransmission transmission = selectCarTransmission();
            if (transmission == null) {
              break;
            }

            carService.addCar(cars, new Car(newCarId, vehicleModel, color, carMaker,
                CarStatus.AVAILABLE, 0, null, transmission));
            System.out.println("新しい車の情報を登録しました。この車のIDは" + newCarId + "です。");
          }
          System.out.println();

        } else if (choice == 2) {
          // 一覧表示の中で絞り込みや検索も行っているので別メソッドに処理を記述
          showCarList();
        } else if (choice == 3) {
          System.out.println("車の情報更新(0で戻る)");
          while (true) {
            int carId = inputUtil.readInt("更新したい車のIDを入力してください:");
            if (carId == 0) {
              break;
            }
            int mileage = inputUtil.readInt("走行距離を入力してください");
            if (mileage == 0) {
              break;
            }
            CarStatus status = selectCarStatus(
                "更新後の車の状態を選択してください(0で戻る)\n1:" + CarStatus.AVAILABLE.getLabel() + "\n2:"
                    + CarStatus.RENTED.getLabel() + "\n3:" + CarStatus.MAINTENANCE.getLabel());
            if (status == null) {
              break;
            }
            if (carService.updateCarMileage(cars, carId, mileage)
                && carService.updateCarStatus(cars, carId, status)) {
              System.out.println("車の情報を更新しました");
            } else {
              System.out.println("車のIDが見つかりませんでした");
            }
            continue;
          }
          System.out.println();

        } else if (choice == 4) {
          System.out.println("車のデータ削除");
          while (true) {
            int carId = inputUtil.readInt("削除したい車のIDを入力してください(0で戻る):");
            if (carId == 0) {
              break;
            }
            String check = inputUtil.readLine("本当に削除しても良いですか？(y/n)");
            if (check.equals("y")) {
              if (carService.deleteCar(cars, carId)) {
                System.out.println("ID:" + carId + "の車のデータを削除しました");
              } else {
                System.out.println("ID:" + carId + "の車のデータの削除に失敗しました");
              }
            } else {
              continue;
            }
          }
          System.out.println();

        } else if (choice == 5) {
          System.out.println("ユーザー一覧表示");
          System.out.println("ID | 名前 | 年齢 | AT/MT ");
          for (User user : userService.listUsers(users)) {
            showUserInfo(user);
          }


        } else if (choice == 6) {
          System.out.println("ユーザーデータ削除");
          while (true) {
            int userId = inputUtil.readInt("削除するユーザーのIDを入力してください(0でキャンセル):");
            if (userId == 0) {
              break;
            }
            String check = inputUtil.readLine("本当に削除しても良いですか？(y/n)");
            if (check.equals("y")) {
              if (userService.deleteUser(users, cars, userId)) {
                System.out.println("ID:" + userId + "のユーザーのデータを削除しました");
              } else {
                System.out.println("ID:" + userId + "のユーザーのデータの削除に失敗しました");
              }
            } else {
              continue;
            }
          }
        } else {
          break;
        }
        System.out.println();
      }
    }

  }

  /**
   * 従業員画面を開くときにパスワードを求めるメソッド
   * 
   * @return 入力がパスワードと一致したときはtrue、3回認証に失敗したときはfalseを返す
   */
  private boolean login() {
    System.out.println("ログイン(0で戻る)");
    int count = 0;
    while (count < 3) {
      String input = inputUtil.readLine("パスワードを入力してください:");
      if (input.equals("0")) {
        break;
      } else if (input.equals(PASSWORD)) {
        return true;
      } else {
        count++;
      }
    }
    if (count == 3) {
      System.out.println("入力に3回失敗したのでトップメニューへ戻ります。");
    }
    return false;
  }

  /**
   * 車の情報を出力するメソッド
   * 
   * @param car 情報を出力させたい車のオブジェクト
   */
  private void showCarInfo(Car car) {
    System.out.println(car.getId() + " | " + car.getVehicleModel() + " | " + car.getColor() + " | "
        + car.getMaker().getLabel() + " | " + car.getStatus().getLabel() + " | "
        + car.getCumulativeMileage() + " | " + car.getCurrentUserId() + " | "
        + car.getTransmission().getLabel());
  }

  /**
   * ユーザー情報を出力するメソッド
   * 
   * @param user 情報を出力させたいユーザーのオブジェクト
   */
  private void showUserInfo(User user) {
    String transmission;
    if (user.isATLimited()) {
      transmission = "AT限定";
    } else {
      transmission = "MT取得済";
    }
    System.out.println(
        user.getId() + " | " + user.getName() + " | " + user.getAge() + " | " + transmission);
  }

  /**
   * メーカーを選択させるときに使用するメソッド
   * 
   * @return 番号の入力に応じて返り値に入れるメーカーを切り替える。0が入力されたときはnullを返す
   */
  private CarMaker selectCarMaker() {
    CarMaker maker;
    int makerChoice = inputUtil.readIntInRange("車のメーカーを選択してください\n1:" + CarMaker.TOYOTA.getLabel()
        + "\n2:" + CarMaker.NISSAN.getLabel() + "\n3:" + CarMaker.HONDA.getLabel() + "\n4:"
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

  /**
   * 車の状態を番号で選択させるメソッド
   * 
   * @param prompt 選択を求めるときに出力するメッセージ
   * @return 1~3の入力がされた場合は番号に応じて状態を返す、0が入力された場合はnullを返す
   */
  private CarStatus selectCarStatus(String prompt) {
    int statusChoice = inputUtil.readIntInRange(prompt, 0, 3);
    CarStatus status;
    if (statusChoice == 1) {
      status = CarStatus.AVAILABLE;
    } else if (statusChoice == 2) {
      status = CarStatus.RENTED;
    } else if (statusChoice == 3) {
      status = CarStatus.MAINTENANCE;
    } else {
      return null;
    }
    return status;
  }

  /**
   * トランスミッションを番号で選択させるメソッド
   * 
   * @return 1のときはAT、2の時はMT、0の時はnullを返す。
   */
  private CarTransmission selectCarTransmission() {
    int transmissionChoice = inputUtil.readIntInRange("車のトランスミッション方式を選択してください\n1:"
        + CarTransmission.AT.getLabel() + "\n2:" + CarTransmission.MT.getLabel(), 0, 2);
    CarTransmission transmission;
    if (transmissionChoice == 1) {
      transmission = CarTransmission.AT;
    } else if (transmissionChoice == 2) {
      transmission = CarTransmission.MT;
    } else {
      return null;
    }
    return transmission;
  }

  /**
   * 一覧表示出力を選択に応じて絞り込みや検索を行えるようにするメソッド startメソッド内のif (choice ==
   * 2)の後に直接記述をしていたが、ネストが深くなってしまってみにくかったので、メソッドに切り出した。
   */
  private void showCarList() {
    System.out.println("車の一覧・検索(0で戻る)");
    while (true) {
      int listChoice = inputUtil
          .readIntInRange("表示したい形式を選んでください\n1:全て表示\n2:メーカーで絞り込み\n3:車の状態で絞り込み\n4:車のIDから検索", 0, 4);

      if (listChoice == 1) {
        System.out.println("全ての車の一覧(0で戻る)");
        System.out.println("ID | 車種名 | 色 | メーカー | 状態 | 累計走行距離 | 借りているユーザーID | トランスミッション方式");
        for (Car car : cars) {
          showCarInfo(car);
        }
        System.out.println();
        continue;

      } else if (listChoice == 2) {
        System.out.println("メーカーで絞り込んで表示(0で戻る)");
        while (true) {

          CarMaker maker = selectCarMaker();
          if (maker == null) {
            break;
          }

          List<Car> filteredCars = carService.filterByMaker(cars, maker);
          System.out.println(filteredCars.size() + "件の車データが見つかりました");
          System.out.println("ID | 車種名 | 色 | メーカー | 状態 | 累計走行距離 | 現在借りているユーザーのID | トランスミッション方式");
          for (Car car : filteredCars) {
            showCarInfo(car);
          }
          System.out.println();
        }

      } else if (listChoice == 3) {
        System.out.println("車の状態で絞り込んで表示");
        while (true) {
          CarStatus status = selectCarStatus(
              "絞り込みたい車の状態を選択してください(0で戻る)\n1:" + CarStatus.AVAILABLE.getLabel() + "\n2:"
                  + CarStatus.RENTED.getLabel() + "\n3:" + CarStatus.MAINTENANCE.getLabel());
          if (status == null) {
            break;
          }

          List<Car> filteredCars = carService.filterByStatus(cars, status);
          System.out.println(filteredCars.size() + "件の車データが見つかりました");
          for (Car car : filteredCars) {
            showCarInfo(car);
          }
          System.out.println();
          continue;
        }

      } else if (listChoice == 4) {
        System.out.println("車のIDから検索");
        int carId = inputUtil.readInt("検索したい車のIDを入力");
        Car car = carService.findCarById(cars, carId);
        if (car == null) {
          System.out.println("IDが" + carId + "の車は見つかりませんでした。");
          continue;
        }
        System.out.println("ID:" + carId + "の車が見つかりました");
        showCarInfo(car);
        System.out.println();
      } else {
        break;
      }

    }
  }
}
