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

  public void start() {
    if (login()) {
      while (true) {
        int choice = inputUtil.readIntInRange(
            "1:車の新規登録\n2:レンタカー一覧\n3: 車の情報更新\n4:車のデータ削除\n5:ユーザー一覧表示\n6:ユーザーデータ削除\n0:トップメニューに戻る", 0,
            6);
        // 車の新規登録
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
            CarMaker carMaker;
            int makerChoice =
                inputUtil.readIntInRange("車のメーカーを選択してください\n1:" + CarMaker.TOYOTA.getLabel() + "\n2:"
                    + CarMaker.NISSAN.getLabel() + "\n3:" + CarMaker.HONDA.getLabel() + "\n4:"
                    + CarMaker.SUBARU.getLabel() + "\n5:" + CarMaker.OTHER.getLabel(), 0, 5);
            if (makerChoice == 1) {
              carMaker = CarMaker.TOYOTA;
            } else if (makerChoice == 2) {
              carMaker = CarMaker.NISSAN;
            } else if (makerChoice == 3) {
              carMaker = CarMaker.HONDA;
            } else if (makerChoice == 4) {
              carMaker = CarMaker.SUBARU;
            } else if (makerChoice == 5) {
              carMaker = CarMaker.OTHER;
            } else {
              break;
            }
            CarTransmission transmission;
            int transmissionChoice = inputUtil.readIntInRange("車のトランスミッション方式を選択してください\n1:"
                + CarTransmission.AT.getLabel() + "\n2:" + CarTransmission.MT.getLabel(), 0, 2);
            if (transmissionChoice == 1) {
              transmission = CarTransmission.AT;
            } else if (transmissionChoice == 2) {
              transmission = CarTransmission.MT;
            } else {
              break;
            }
            carService.addCar(cars, new Car(newCarId, vehicleModel, color, carMaker,
                CarStatus.AVAILABLE, 0, null, transmission));
            System.out.println("新しい車の情報を登録しました。この車のIDは" + newCarId + "です。");
          }
        } else if (choice == 2) {
          System.out.println("車の一覧表示(0で戻る)");
          while (true) {
            int listChoice = inputUtil.readIntInRange(
                "表示したい形式を選んでください\n1:全て表示\n2:メーカーで絞り込み\n3:車の状態で絞り込み\n4:車のIDから検索", 0, 4);
            if (listChoice == 1) {
              System.out.println("全ての車の一覧(0で戻る)");
              System.out.println("ID | 車種名 | 色 | メーカー | 状態 | 累計走行距離 | 借りているユーザーID | トランスミッション方式");
              for (Car car : cars) {
                showCarInfo(car);
              }
              continue;
            } else if (listChoice == 2) {
              System.out.println("メーカーで絞り込んで表示(0で戻る)");
              while (true) {


                CarMaker maker;
                int makerChoice = inputUtil
                    .readIntInRange("車のメーカーを選択してください\n1:" + CarMaker.TOYOTA.getLabel() + "\n2:"
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
                  break;
                }

                List<Car> filteredCars = carService.filterByMaker(cars, maker);
                System.out
                    .println("ID | 車種名 | 色 | メーカー | 状態 | 累計走行距離 | 現在借りているユーザーのID | トランスミッション方式");
                for (Car car : filteredCars) {
                  showCarInfo(car);
                }
              }
            } else if (listChoice == 3) {
              System.out.println("車の状態で絞り込んで表示");
              while (true) {
                int statusChoice = inputUtil.readIntInRange(
                    "絞り込みたい車の状態を選択してください(0で戻る)\n1:" + CarStatus.AVAILABLE.getLabel() + "\n2:"
                        + CarStatus.RENTED.getLabel() + "\n3:" + CarStatus.MAINTENANCE.getLabel(),
                    0, 3);
                CarStatus status;
                if (statusChoice == 1) {
                  status = CarStatus.AVAILABLE;
                } else if (statusChoice == 2) {
                  status = CarStatus.RENTED;
                } else if (statusChoice == 3) {
                  status = CarStatus.MAINTENANCE;
                } else {
                  break;
                }
                List<Car> filteredCars = carService.filterByStatus(cars, status);
                for (Car car : filteredCars) {
                  showCarInfo(car);
                }
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
            } else {
              break;
            }
          }

        } else if (choice == 3) {
          System.out.println("車の情報更新(0で戻る)");
          while (true) {
            int carId = inputUtil.readInt("更新したい車のIDを入力してください:");
            if (carId == 0) {
              break;
            }
            int additionalMilage = inputUtil.readInt("走行距離を入力してください");
            if (additionalMilage == 0) {
              break;
            }
            if (carService.updateCar(cars, carId, additionalMilage)) {
              System.out.println("車の情報を更新しました");
            } else {
              System.out.println("車のIDが見つかりませんでした");
            }
            continue;
          }

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

  private void showCarInfo(Car car) {
    System.out.println(car.getId() + " | " + car.getVehicleModel() + " | " + car.getColor() + " | "
        + car.getMaker().getLabel() + " | " + car.getStatus().getLabel() + " | "
        + car.getCumulativeMileage() + " | " + car.getCurrentUserId() + " | "
        + car.getTransmission().getLabel());
  }

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
}
