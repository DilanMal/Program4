/*
 * Authors: Aditya Gupta, Dilan Maliyagoda, Noah Sher
 * Course: CSC 460
 * Assignment: Prog4.java
 * 
 * Due Date: 04/29/2024
 * Instructor: L McCan
 * TAs: Amhad Musa, Jake Bode, Priyansh Nayak
 * 
 * Description: **********************************************************************************
 * 
 * Language: Java 16
 * Other Requirements: None
 * Missing Features: None
 * Other Comments: Some of the code is borrowed from websites jdbc example
 * 
 * Run the program: java JDBC <oracle username> <oracle password> 
 * Classpath requirements: export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 * 
 */
import java.io.*;
import java.sql.*;                 // For access to the SQL interaction methods
import java.util.Scanner;
import java.util.Random;


/**
 * Prog4 class represents the main class of the program
 * for connecting to the JDBC server using commandline attributes
 * and also getting the data as the user wants
 * 
 * Pre-requisites: Data should be in the database, and JDBC should be setup
 * 
*/
public class Prog4{

    //text to be displayed when prompted
    private final static String text = "\nText menu. Select out of options 0-11: \n" +
            "Administrative: \n" +
            "1 -- Add a new member\n" +
            "2 -- Update member details.\n" +
            "3 -- Delete a member\n" +
            "4 -- Add a new game \n" +
            "5 -- Delete a game \n" +
            "6 -- Add a Prize \n" +
            "7 -- Delete a prize \n" +

            "\nStats and other data: \n" +

            "8 -- All games in the arcade & names of the members who have the current high scores \n" +
            "9 -- Members who have spent more than $100 in the past month \n" +
            "10 -- Arcade rewards the members can purchase with tickets \n" +
            "11 -- Member scores in each game-play for a specific game \n" +

            "0 -- exit\n";

    /**
     * returns the user input at the prompt
     *
     * @param kb scanner object
     * @return integer input given by user
     * 
     * pre-conditions: scanner object is passed properly
     * post-conditions: returns the number that has been input by user
     */
    private static int promptInput(Scanner kb) {
        while (true) {
            System.out.print(text);
            String result = kb.next();
            switch (result) {
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":               
                case "6":                
                case "7":                
                case "8":                
                case "9":                
                case "10":                
                case "11":                
                kb.nextLine();
                return Integer.parseInt(result);
            }
        }
    }

    /**
     *
     *
     * @param
     * @param
     * @param
     * @return
     * 
     * pre-conditions:
     * post-conditions:
     */
    private static int generateRandomId(Statement stmt, String tableName, String idColumnName) {
        int minId = 10000;
        int maxId = 99999;
    
        Random random = new Random();
        int randomId = random.nextInt(maxId - minId + 1) + minId;
    
        try {
            String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + idColumnName + " = " + randomId;
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                randomId = generateRandomId(stmt, tableName, idColumnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return randomId;
    }
    
    
    /**
     * Main method of the program.
     * It loads the JDBC driver and loads the oracle database
     * and queries are sent which in order get the result
     * A switch case is used to check for user input and other functions
     * greatDistanceCircle and PromptInput are called.
     * 
     * Pre-conditions: JDBC login details must be passed as command line arguments.
     *                  Database must exist and user must enter input
     * Post-conditions: Data as required
     * @param args Command line arguments
     */
    public static void main (String [] args){    
        final String oracleURL =   // Magic lectura -> aloe access spell
                        "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

        String username = null,    // Oracle DBMS username
            password = null;    // Oracle DBMS password

        if (args.length == 2) {    // get username/password from cmd line args
            username = args[0];
            password = args[1];
        } else {
            System.out.println("\nUsage:  java JDBC <username> <password>\n"
                            + "    where <username> is your Oracle DBMS"
                            + " username,\n    and <password> is your Oracle"
                            + " password (not your system password).\n");
            System.exit(-1);
        }

        // load the (Oracle) JDBC driver by initializing its base
        // class, 'oracle.jdbc.OracleDriver'.
        try {
                Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {
                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);
        }

        // make and return a database connection to the user's
        // Oracle database
        Connection dbconn = null;

        try {
                dbconn = DriverManager.getConnection
                            (oracleURL,username,password);

        } catch (SQLException e) {
                System.err.println("*** SQLException:  "
                    + "Could not open JDBC connection.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                System.exit(-1);
        }

        //send the query to the DBMS, and get and display the results
        Scanner kb = new Scanner(System.in);
        Statement stmt = null;
        ResultSet answer = null;
        
        try {
        //prompt the user for which choice they'd like to make
        int menuChoice = promptInput(kb);
        System.out.println();
    
        while (menuChoice != 0) {
            stmt = dbconn.createStatement();

            switch (menuChoice) {

                case 1:

                    try {
                        int randomMemberId = generateRandomId(stmt, "Members", "member_id");
                
                        System.out.print("Enter member name: ");
                        String name = kb.nextLine();
                        System.out.print("Enter telephone number: ");
                        String phoneNumber = kb.nextLine();
                        System.out.print("Enter home address: ");
                        String address = kb.nextLine();
                
                        String insertQuery = "INSERT INTO Members (member_id, name, phone_number, address) " +
                                            "VALUES (" + randomMemberId + ", '" + name + "', '" + phoneNumber + "', '" + address + "')";
                        stmt.executeUpdate(insertQuery);
                        System.out.println("New member added successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: Invalid data");
                    }
                    break;
            
                case 2:

                    try {
                        System.out.print("Enter member ID to update: ");
                        int memberId;
                        try {
                            memberId = Integer.parseInt(kb.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Invalid input for member ID");
                            break;
                        }
                
                        //does the member exist
                        String checkMemberQuery = "SELECT COUNT(*) FROM Members WHERE member_id = " + memberId;
                        ResultSet checkResult = stmt.executeQuery(checkMemberQuery);
                        checkResult.next();
                        int memberCount = checkResult.getInt(1);
                        if (memberCount == 0) {
                            System.out.println("Error: No member found");
                            break;
                        }
                
                        System.out.println("What would you like to update?");
                        System.out.println("1. Name");
                        System.out.println("2. Telephone number");
                        System.out.println("3. Home address");
                        System.out.print("Enter your choice: ");
                        int choice = Integer.parseInt(kb.nextLine());

                        String columnToUpdate = "";
                        switch (choice) {
                            case 1:
                                columnToUpdate = "name";
                                System.out.print("Enter new name: ");
                                break;
                            case 2:
                                columnToUpdate = "phone_number";
                                System.out.print("Enter new telephone number: ");
                                break;
                            case 3:
                                columnToUpdate = "address";
                                System.out.print("Enter new home address: ");
                                break;
                            default:
                                System.out.println("Error: Invalid choice.");
                                break;
                        }
                
                        // Update member details based on user choice
                        if (choice == 1 || choice == 2 || choice == 3) {
                            String newValue = kb.nextLine();
                
                            String updateQuery = "UPDATE Members SET " + columnToUpdate + " = '" + newValue + "' WHERE member_id = " + memberId;
                            int rowsAffected = stmt.executeUpdate(updateQuery);
                            if (rowsAffected > 0) {
                                System.out.println("Member details updated successfully!");
                            } else {
                                System.out.println("Error: Member details could not be updated.");
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error: Database error occurred.");
                    }
                    break;
                                    
                case 3:

                    try {
                        System.out.print("Enter member ID to delete: ");
                        int memberIdToDelete = Integer.parseInt(kb.nextLine());
                
                        String deleteQuery = "DELETE FROM Members WHERE member_id = " + memberIdToDelete;
                        int rowsDeleted = stmt.executeUpdate(deleteQuery);

                        if (rowsDeleted > 0) {
                            System.out.println("Member deleted successfully!");
                        } else {
                            System.out.println("No member found with ID " + memberIdToDelete);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input for member ID");
                    }
                    break;
                
                case 4:

                    try {
                        int gameId = generateRandomId(stmt, "Game", "ID");
                        System.out.print("Enter game name: ");
                        String gameName = kb.nextLine();
                        System.out.print("Enter token cost for the game: ");
                        int cost = Integer.parseInt(kb.nextLine());
                
                        String insertQuery = "INSERT INTO Game (ID, name, ticket_cost) VALUES (" + gameId + ", '" + gameName + "', " + cost + ")";
                        stmt.executeUpdate(insertQuery);
                        System.out.println("New game added successfully!");
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input for ticket cost.");
                    }
                    break;

                case 5:

                    try {
                        System.out.print("Enter game ID to delete: ");
                        int gameId = Integer.parseInt(kb.nextLine());
                
                        String deleteQuery = "DELETE FROM Game WHERE ID = " + gameId;
                        int rowsAffected = stmt.executeUpdate(deleteQuery);
                        if (rowsAffected > 0) {
                            System.out.println("Game deleted successfully!");
                        } else {
                            System.out.println("Error: No game found with ID " + gameId);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input for game ID.");
                    }
                    break;

                case 6:

                    try {
                        int prizeId = generateRandomId(stmt, "Prizes", "Prize_ID");
                        System.out.print("Enter prize name: ");
                        String prizeName = kb.nextLine();
                        System.out.print("Enter ticket cost for the prize: ");
                        int ticketCost = Integer.parseInt(kb.nextLine());

                        String insertQuery = "INSERT INTO Prizes (Prize_ID, name, tickets) VALUES (" + prizeId + ", '" + prizeName + "', " + ticketCost + ")";
                        stmt.executeUpdate(insertQuery);
                        System.out.println("New prize added successfully!");
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input for ticket cost.");
                    }
                    break;

                case 7:
    
                    try {
                        System.out.print("Enter prize ID to delete: ");
                        int prizeId = Integer.parseInt(kb.nextLine());
                
                        String deleteQuery = "DELETE FROM Prizes WHERE Prize_ID = " + prizeId;
                        int rowsAffected = stmt.executeUpdate(deleteQuery);
                        if (rowsAffected > 0) {
                            System.out.println("Prize deleted successfully!");
                        } else {
                            System.out.println("Error: No prize found");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input for prize ID.");
                    }
                    break;

                case 8:
                    break;

                case 9:
                    break;

                case 10:

                    try {
                        // Execute SQL query to list arcade rewards that members can purchase with tickets
                        String query = "SELECT Prizes.name, Prizes.tickets " +
                                       "FROM Prizes";
                        ResultSet resultSet = stmt.executeQuery(query);
                
                        // Display results
                        System.out.println("Arcade rewards that members can purchase with tickets:");
                        while (resultSet.next()) {
                            String prizeName = resultSet.getString("name");
                            int ticketCost = resultSet.getInt("tickets");
                            System.out.println(prizeName + " - Ticket Cost: " + ticketCost);
                        }
                
                        //input the member ID to check available tickets
                        System.out.print("\nEnter member ID to check available tickets: ");
                        int memberId = Integer.parseInt(kb.nextLine());
                
                        //retrieve member's available tickets
                        String ticketQuery = "SELECT Tickets " +
                                             "FROM Membership " +
                                             "WHERE Member_ID = " + memberId;
                        ResultSet ticketResult = stmt.executeQuery(ticketQuery);
                        int availableTickets = 0;
                        if (ticketResult.next()) {
                            availableTickets = ticketResult.getInt("Tickets");
                        }
                
                        //available tickets
                        System.out.println("Available tickets for member " + memberId + ": " + availableTickets);
                
                        //Iterate through prizes again to check affordability
                        resultSet.beforeFirst(); // Reset resultSet cursor
                        System.out.println("Affordable prizes for member " + memberId + ":");
                        while (resultSet.next()) {
                            String prizeName = resultSet.getString("name");
                            int ticketCost = resultSet.getInt("tickets");
                            if (availableTickets >= ticketCost) {
                                System.out.println(prizeName + " - Ticket Cost: " + ticketCost);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error");
                    }

                    break;
                
                case 11:
                    break;        

            }
            stmt.close();
            menuChoice = promptInput(kb);
        }
        System.out.println("Exiting.");
        // Close everything down
        dbconn.close();
        } catch (SQLException e) {

                System.err.println("*** SQLException:  "
                    + "Could not fetch query results.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                System.exit(-1);

        }
    }
}