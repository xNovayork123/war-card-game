/*package main;

import file.ReadGames;
import file.ReadUsers;
import file.SaveGame;
import file.SaveUsers;
import game.Game;
import game.GameState;
import game.GamesList;
import player.GuestPlayer;
import player.HumanPlayer;
import player.User;
import player.UserList;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuLogic {
    private Scanner scanner;
    private Game game;
    private HumanPlayer user;
    private UserList userList;
    private GamesList gamesList;
    private SaveGame saveGame;
    private ReadUsers readUsers;
    private ReadGames readGames;
    private SaveUsers saveUsers;

    public MenuLogic() {
        scanner = new Scanner(System.in);
        userList = new UserList();
        gamesList = new GamesList();
        saveGame = new SaveGame();
        readUsers = new ReadUsers();
        readGames = new ReadGames();
        saveUsers = new SaveUsers();

        // 1) load the savedusers and add them userList list
        for (String line : readUsers.load()) {
            String[] parts = line.split(" ", 2);
            if (parts.length == 2) {
                HumanPlayer u = new HumanPlayer(parts[0], parts[1]);
                userList.addUser(u);
            }
        }

        // 2) load the saved games and add them gamesList
        for (GameState gameState : readGames.load()) {
            gamesList.add(gameState);
        }
    }

    public void mainMenu() {
        System.out.println("Welcome to the WAR CARD GAME");
        boolean lopMnu = true;
        do {
            System.out.println("1-Play Game\n2- Settings\n3- The Rules Of The Game\n4- Group Info\n5- Exit");
            System.out.println("------------------------------------------------------");

            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1:
                    firstMenu();
                    break;
                    case 2:
                        break;
                        case 3:
                            break;
                            case 4:
                                break;
                case 5: lopMnu = false; break;

            }
        }while (lopMnu);
    }

    public void firstMenu() {
        while (true) {
            System.out.println("1-Sign in\n2-Sign up\n3-Continue as a guest\n4-Go to the main menu");

            int input = scanner.nextInt();
            scanner.nextLine();


            switch (input) {
                case 1:
                    signIn();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    guestGameMenu();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option!!");
                    input = scanner.nextInt();
                    scanner.nextLine();
                    break;
            }
        }

    }


    public void signIn() {
        List<String > usersFromLoad=new ArrayList<String>();
        for(String u:readUsers.load()){
            usersFromLoad.add(u);
        }
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        while (!usersFromLoad.contains(username)&&!userList.idExists(username)) {
            System.out.println("That username does not exist!! Please enter a valid username or click 'Q' to go back..");
            String input = scanner.nextLine();
            if (input.equals("Q")) {
                firstMenu();
            }
            username = input;
        }
        System.out.println("Welcome user -> " + username);


        for (User u : userList.getUsers()) {
            if (u.getId().equals(username)) {
                gameMenu(u);
            }
        }

    }

    public void signUp() {
        System.out.print("Enter a username you want to create: ");
        String username = scanner.nextLine();
        while (userList.idExists(username)) {
            System.out.println("That username already exists!! Please enter a new username or click 'Q' to go back..");
            String input = scanner.nextLine();
            if (input.equals("Q")) {
                return;
            }
            username = input;
        }


        System.out.print("Enter your Name: ");
        String name = scanner.nextLine();
        user = new HumanPlayer(username, name);

        // write the file
        saveUsers.save(user);
        // add the memory inside program
        userList.addUser(user);
        System.out.println("\n--------------\nUser created successfully :) \nUsername: " + username + " \nName: " + name + "\n--------------------\n");
    }

    public void gameMenu(User player) {
        System.out.println("1- Single Player Game\n2- Two Players Game\n3- Go back");
        int input = scanner.nextInt();
        scanner.nextLine();

        while (true) {
            switch (input) {

                // Single Player
                case 1:
                    // search gamelist if a saved single game exist for this user
                    GameState found = null;
                    for (GameState gs : gamesList.getAll()) {
                        if (gs.getUsername().equals(player.getId()) &&
                                gs.getMode().equals("SINGLE")) {
                            found = gs;
                            break;
                        }
                    }


                    Game g = new Game(
                            new HumanPlayer(player.getId(), player.getName()),
                            "SINGLE",
                            gamesList
                    );

                    // if a saved game was found, ask if user wants to continue
                    if (found != null) {
                        System.out.print("Continue saved? (Yes/No): ");
                        String ans = scanner.nextLine().trim();
                        if (ans.equalsIgnoreCase("Yes")) {
                            // if user says yes, load the saved decks and state
                            g.loadState(found);
                        }
                    }

                    // start new game or resume the single player game from the loaded
                    g.singlePlayerGame();
                    saveGame.save(gamesList);
                    return;

                case 2:

                    // search gamelist if a saved two players game exist for this user
                    GameState found2 = null;
                    for (GameState gs : gamesList.getAll()) {
                        if (gs.getUsername().equals(player.getId())
                                && gs.getMode().equals("TWO")) {
                            found2 = gs; break;
                        }
                    }


                    Game g2 = new Game(
                            new HumanPlayer(player.getId(), player.getName()),
                            "TWO",
                            gamesList
                    );

                    // if a saved game was found, ask if user wants to continue
                    if (found2 != null) {
                        System.out.print("Continue saved two-player game? (Yes/No): ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("Yes")) {
                            g2.loadState(found2);
                        }
                    }

                    // load the saved data
                    g2.twoPlayersGame(player.getId());
                    saveGame.save(gamesList);
                    return;
                case 3:
                    return;
                default:
                    System.out.println("That is not a valid option! Please try again");
                    input = scanner.nextInt();
                    scanner.nextLine();
                    break;
            }
        }
    }

    public void guestGameMenu() {
        System.out.println("1- Single Player Game\n2-Two Players Game\n3- Go back to the main menu");
        game = new Game();
        int input = scanner.nextInt();
        scanner.nextLine();

        while (true) {
            switch (input) {
                case 1:
                    game.singlePlayerGameGuest();
                    return;
                case 2:
                    game.twoPlayersGameGuest();
                    return;
                case 3:
                    return;
                default:
                    System.out.println("That is not a valid option! Please try again");
                    input = scanner.nextInt();
                    scanner.nextLine();
                    break;
            }
        }
    }


}
*/ // menu for playing game in terminal