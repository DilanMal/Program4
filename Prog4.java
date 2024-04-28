/*
 * Authors: Aditya Gupta, Dilan Maliyagoda, Noah Sher
 * Course: CSC 460
 * Assignment: Prog4.java
 * 
 * Due Date: 04/29/2024
 * Instructor: L McCan
 * TAs: Amhad Musa, Jake Bode, Priyansh Nayak
 * 
 * Description: Program for an arcade, that employees can use to add/track transactions
 *              member data, prizes, gifts and even coupons. Determine high scores/leaderboard stats 
 *              for the clients and give discounts based on their tiers.
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Prog4 class represents the main class of the program
 * for connecting to the JDBC server using commandline attributes
 * and also getting the data as the user wants
 * 
 * Pre-requisites: Data should be in the database, and JDBC should be setup
 * 
 * Other methods inside this class:
 * private static int promptInput(Scanner kb)
 * private static int generateRandomId(Statement stmt, String tableName, String idColumnName)
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
     * generates a random ID within a specified range and ensures its uniqueness within a given database table
     *
     * @param stmt SQL statement object for executing queries
     * @param tableName name of the table in the database
     * @param idColumnName name of the column containing IDs in the table
     * @return randomId unique random ID
     * 
     * pre-conditions: stmt must be valid and DB connection must be established
     * post-conditions: returns a unique id
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
                    // add a member
                    try {
                        int randomMemberId = generateRandomId(stmt, "dilanm.Membership", "MemberID");
                
                        System.out.print("Enter member name: ");
                        String name = kb.nextLine();
                        System.out.print("Enter telephone number: ");
                        String phoneNumber = kb.nextLine();
                        if (!phoneNumber.matches("\\d{10}")) {
                            System.out.println("Error: Number not 10 digits");
                            break;
                        }
                        System.out.print("Enter home address: ");
                        String address = kb.nextLine();

                        int totalSpent = 0;
                        
                        java.util.Date currentDate = new java.util.Date();
                        java.sql.Date lastDate = new java.sql.Date(currentDate.getTime());
                        
                        int tickets = 0;
                        int tokens = 0;

                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        String formattedDate = df.format(lastDate);

                        String insertQuery = "INSERT INTO dilanm.Membership (MemberID, Name, TeleNum, Addr, TotalSpend, LastDate, Tickets, Tokens) " +
                        "VALUES (" + randomMemberId + ", '" + name + "', '" + phoneNumber + "', '" + address + "', " +
                        totalSpent + ", TO_DATE('" + formattedDate + "', 'MM/DD/YYYY'), " + tickets + ", " + tokens + ")";

                        
                        stmt.executeUpdate(insertQuery);
                        System.out.println("New member added successfully!");

                    } catch (Exception e) {
                        System.out.println("Error: Invalid data");
                    }
                    break;
            
                case 2:
                    // update a member
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
                        String checkMemberQuery = "SELECT COUNT(*) FROM dilanm.Membership WHERE MemberID = " + memberId;
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
                                columnToUpdate = "Name";
                                System.out.print("Enter new name: ");
                                break;
                            case 2:
                                columnToUpdate = "TeleNum";
                                System.out.print("Enter new telephone number: ");
                                break;
                            case 3:
                                columnToUpdate = "Addr";
                                System.out.print("Enter new home address: ");
                                break;
                            default:
                                System.out.println("Error: Invalid choice.");
                                break;
                        }
                
                        // Update member details based on user choice
                        if (choice == 1 || choice == 2 || choice == 3) {
                            String newValue = kb.nextLine();
                
                            String updateQuery = "UPDATE dilanm.Membership SET " + columnToUpdate + " = '" + newValue + "' WHERE MemberID = " + memberId;
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
                    // delete a member
                    try {
                        System.out.print("Enter member ID to delete: ");
                        int memberIdToDelete = Integer.parseInt(kb.nextLine());
                
                        String deleteQuery = "DELETE FROM dilanm.Membership WHERE MemberID = " + memberIdToDelete;
                        int rowsDeleted = stmt.executeUpdate(deleteQuery);

                        // Delete associated data from other tables
                        String deleteGameXactQuery = "DELETE FROM dilanm.GameXact WHERE MemberID = " + memberIdToDelete;
                        stmt.executeUpdate(deleteGameXactQuery);

                        String deleteTTXactQuery = "DELETE FROM dilanm.TTXact WHERE MemberID = " + memberIdToDelete;
                        stmt.executeUpdate(deleteTTXactQuery);

                        String deletePrizeXactQuery = "DELETE FROM dilanm.PrizeXact WHERE MemberID = " + memberIdToDelete;
                        stmt.executeUpdate(deletePrizeXactQuery);

                        String deleteCouponXactQuery = "DELETE FROM dilanm.CouponXact WHERE MemberID = " + memberIdToDelete;
                        stmt.executeUpdate(deleteCouponXactQuery);

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
                    // add a game
                    try {
                        int gameId = generateRandomId(stmt, "dilanm.Game", "GameID");
                        System.out.print("Enter game name: ");
                        String gameName = kb.nextLine();
                        System.out.print("Enter token cost for the game: ");
                        int cost = Integer.parseInt(kb.nextLine());
                
                        String insertQuery = "INSERT INTO dilanm.Game (GameID, GName, TokenCost) VALUES (" + gameId + ", '" + gameName + "', " + cost + ")";
                        stmt.executeUpdate(insertQuery);
                        System.out.println("New game added successfully!");
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input for ticket cost.");
                    }
                    break;

                case 5:
                    // delete a member
                    try {
                        System.out.print("Enter game ID to delete: ");
                        int gameId = Integer.parseInt(kb.nextLine());
                
                        String deleteQuery = "DELETE FROM dilanm.Game WHERE GameID = " + gameId;
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
                    // add a prize
                    try {
                        int prizeId = generateRandomId(stmt, "dilanm.Prize", "PrizeID");
                        System.out.print("Enter prize name: ");
                        String prizeName = kb.nextLine();
                        System.out.print("Enter ticket cost for the prize: ");
                        int ticketCost = Integer.parseInt(kb.nextLine());

                        String insertQuery = "INSERT INTO dilanm.Prize (PrizeID, PName, TicketCost) VALUES (" + prizeId + ", '" + prizeName + "', " + ticketCost + ")";
                        stmt.executeUpdate(insertQuery);
                        System.out.println("New prize added successfully!");
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid input for ticket cost.");
                    }
                    break;

                case 7:
                    // delete a prize
                    try {
                        System.out.print("Enter prize ID to delete: ");
                        int prizeId = Integer.parseInt(kb.nextLine());
                
                        String deleteQuery = "DELETE FROM dilanm.Prize WHERE PrizeID = " + prizeId;
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
                    //high scores
                	try {
                		System.out.println("Game: High Score Holder");
                		System.out.println("---------------------");
                		  
                		//SQL query to get game names
                		String query = "SELECT DISTINCT Game.GameID, Game.GName FROM dilanm.Game";
                		ResultSet game_rslt = stmt.executeQuery(query);
                        
                		//Iterate through game names and get highest scorer for game
                		while (game_rslt.next()) {
                			int gid = game_rslt.getInt("GameID");
                			query = "SELECT Name, Score FROM ("
                					+ "SELECT Membership.Name, GameXact.Score FROM dilanm.GameXact "
                					+ "JOIN dilanm.Membership ON GameXact.GameID = " + String.valueOf(gid)
                					+ " AND Membership.MemberID = GameXact.MemberID ORDER BY Score DESC)"
                					+ " WHERE ROWNUM = 1";
                			
                			ResultSet score_rslt = stmt.executeQuery(query);
                			
                			//Print Game and top scorer
                			if(score_rslt.next()) {
                				System.out.println(game_rslt.getString("GName") + ": " + score_rslt.getString("Name"));
                			}
                		}
                	} catch (Exception e) {
                        System.out.println("Error");
                	}
                    break;

                case 9:
                    //100 spend
                    
                    System.out.println("Members who have spent a 100$ in the last month");
                    System.out.println("------------------------------------------------");
                    
                    try {
                        // Execute SQL query to get all members who have spent 100 dollars in the current month
                        String query = "SELECT memberID, sum(Amount) FROM dilanm.TTXact WHERE "
                        		+ "Category = 'tokens' AND Amount > 0 AND "
                        		+ "TDate BETWEEN trunc (sysdate, 'mm') AND SYSDATE "
                        		+ "GROUP BY memberID "
                        		+ "HAVING sum(Amount) > 10";
                        ResultSet resultSet = stmt.executeQuery(query);
                        
                        //Prints Member info
                        while (resultSet.next()) {
                        	//Get member
                        	query = "SELECT * FROM dilanm.Membership WHERE memberID = " + 
                        			String.valueOf(resultSet.getInt("memberID"));
                        	ResultSet memberInfo = stmt.executeQuery(query);
                        	
                        	if(!memberInfo.next()) {
                        		System.out.println("No members");
                        		break;
                        	}
                        	
                        	//Print Info
                            System.out.println("MemberID: " + memberInfo.getString("MemberID")
                            		+ "Name: " + memberInfo.getString("Name")
                            		+ "TeleNum: " + String.valueOf(memberInfo.getInt("TeleNum"))
                            		+ "Address: " + memberInfo.getString("Addr")
                            		+ "TotalSpend: " + String.valueOf(memberInfo.getInt("TotalSpend"))
                            		+ "LastDate: " + memberInfo.getDate("LastDate")
                            		+ "Tickets: " + String.valueOf(memberInfo.getInt("Tickets"))
                            		+ "Tokens: " + String.valueOf(memberInfo.getInt("Tokens")));
                        }
                        
                    } catch (Exception e) {
                        System.out.println("Error");
                    }
                    break;

                case 10:
                    // prizes they can buy
                    try {

                        String query = "SELECT Prize.PName, Prize.TicketCost " +
                                    "FROM dilanm.Prize";
                        ResultSet resultSet = stmt.executeQuery(query);

                        List<String> prizeList = new ArrayList<>();
                        System.out.println("Arcade rewards that members can purchase with tickets:");
                        while (resultSet.next()) {
                            String prizeName = resultSet.getString("PName");
                            int ticketCost = resultSet.getInt("TicketCost");
                            prizeList.add(prizeName + " - Ticket Cost: " + ticketCost);
                            System.out.println(prizeName + " - Ticket Cost: " + ticketCost);
                        }

                        // input the member ID to check available tickets
                        System.out.print("\nEnter member ID to check available tickets: ");
                        int memberId = Integer.parseInt(kb.nextLine());

                        // retrieve member's available tickets
                        String ticketQuery = "SELECT Tickets " +
                                            "FROM dilanm.Membership " +
                                            "WHERE MemberID = " + memberId;
                        ResultSet ticketResult = stmt.executeQuery(ticketQuery);
                        int availableTickets = 0;
                        if (ticketResult.next()) {
                            availableTickets = ticketResult.getInt("Tickets");
                        }

                        System.out.println("\nAvailable tickets for member " + memberId + ": " + availableTickets);

                        // Check affordability of prizes using the stored list
                        System.out.println("\nAffordable prizes for member " + memberId + ":");
                        for (String prize : prizeList) {
                            int ticketCost = Integer.parseInt(prize.split(" - Ticket Cost: ")[1]);
                            if (availableTickets >= ticketCost) {
                                System.out.println(prize);
                            }
                        }
                    } catch (SQLException | NumberFormatException e) {
                        e.printStackTrace();
                        System.out.println("An error occurred while processing the request.");
                    }
                    break;
                
                case 11:
                    // Custom query
                    // Get inputs
                    System.out.print("Enter member ID: ");
                    String memID = kb.nextLine();
                    System.out.print("Enter game name: ");
                    String gameName = kb.nextLine();
                
                    System.out.println("Scores for " + memID + " on game, " + gameName);
                    System.out.println("---------------------------------------------");
                
                    try {
                        // Execute SQL query to get all plays
                        String query = "SELECT Score FROM dilanm.GameXact WHERE GameID = (SELECT GameID FROM dilanm.Game WHERE GName = '" + gameName + "') AND MemberID = '" + memID + "' ORDER BY Score";
                        ResultSet resultSet = stmt.executeQuery(query);

                        if (!resultSet.next()) {
                            System.out.println("No scores found for member " + memID + " on game " + gameName);
                        } else {
                            // Prints scores for user
                            int count = 1;
                            do {
                                System.out.println(String.valueOf(count) + ". " + resultSet.getInt("Score"));
                                count++;
                            } while (resultSet.next());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Error executing SQL query: " + e.getMessage());
                    }
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
