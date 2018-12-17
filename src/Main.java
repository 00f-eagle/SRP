import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        User user = new User();
        Server server = new Server();

        server.SecurityField();
        user.SecurityField(server.getN(), server.getG(), server.getK());

        Scanner in = new Scanner(System.in);

        listCommand();

        boolean bool = true;

        String command;
        String name;
        String password;

        while (bool) {

            System.out.print("\nВведите команду:\n>>>");
            command = in.nextLine();

            switch (command) {
                case "register":
                    do {
                        System.out.print("\nВведите имя пользователя:\n>>> ");
                        name = in.nextLine();
                    } while (!server.us(name));

                    do {
                        System.out.print("\nВведите пароль:\n>>> ");
                        password = in.nextLine();
                    } while (password.equals(""));

                    user.reg(name, password);
                    server.reg(user.getName(), user.getSalt(), user.getV());
                    break;

                case "login":

                    System.out.print("\nВведите имя пользователя:\n>>> ");
                    name = in.nextLine();
                    System.out.print("\nВведите пароль:\n>>> ");
                    password = in.nextLine();

                    user.log_1(name);
                    if(!server.log_1(user.getName(), user.getA())){
                        System.out.println("Ошибка!");
                        break;
                    }
                    user.scr(server.getB());

                    user.log_2(password, server.getS());
                    server.log_2();

                    user.log_3();
                    if(!server.log_3(user.getM())){
                        System.out.println("Ошибка!");
                        break;
                    }

                    server.log_4();
                    if(!user.log_4(server.getR())){
                        System.out.println("Ошибка!");
                        break;
                    }

                    System.out.println("Соединение установлено!");

                    break;
                case "?":
                    listCommand();
                    break;
                case "exit":
                    bool = false;
                    break;
                default:
                    System.out.println("Вы ввели команду неверно!");
                    break;
            }
        }
    }

    private static void listCommand() {
        System.out.println("\nСписок команд:");
        System.out.println("'register' - зарегистрировать пользователя");
        System.out.println("'login' - Войти в систему");
        System.out.println("'?' - список команд");
        System.out.println("'exit' - выйти");
    }

}
