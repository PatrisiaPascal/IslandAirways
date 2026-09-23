import java.text.DecimalFormat;
import java.util.Scanner;

public class IslandAirways
{
	static Scanner input = new Scanner(System.in);
	static DecimalFormat currency = new DecimalFormat("0.00");
	
	static double[] ticket;
	static int ticketQty = 12;
	static double adultPrice = 125;
	static double childPrice = 75;
	static double studentPrice = 90;
	static int transactionAdultTickets, transactionChildTickets, transactionStudentTickets;// Variables to track transaction details
	static int adminPin = 1234; // admin pin to access admin mode
	static double average, totalEarnings;// variable to calculate statistics
	static int numTransactions;
	static int totalTransactions;
	static String destination = "";// to store chosen destination
	
	public static void main(String[] args) {
		menu();
	}
	
	public static void menu() {
		System.out.println("--------------------------------------------");//main menu for interaction
		System.out.println("              Island Airways Inc.           ");
		System.out.println("--------------------------------------------");
		System.out.println("1. Book Ticket ");
		System.out.println("2. Check Seat Quantity ");
		System.out.println("3. View Ticket Types ");
		System.out.println("A. Admin Mode ");
		System.out.println("X. Exit System ");
		System.out.println("---------------------------------------------");
		
		String choice = input.next().toUpperCase();//user choice input
		switch(choice){
		case "1"://book a ticket
			bookTicket();
			break;
		case "2"://check available seat quantity
			checkSeatQuantity();
			break;
		case "3"://view available ticket types and prices
			viewTicketTypes();
			break;
		case "A"://enter admin mode with pin verification
			System.out.println("Enter Admin Pin");
			int userPin = input.nextInt();
			if (userPin == adminPin) {
				System.out.println("Pin Correct. Loading Admin Mode");
				admin();
			}	else {
				System.out.println("Invalid Pin. Try Again");
				menu();
			}
			break;
		case "X"://exit the system
			System.out.println("System Shutting Down");
			System.exit(0);
			break;
		}
	}
	public static void bookTicket() {//Method for booking a ticket
		chooseDestination();
		
		System.out.println("Please Select Ticket Quanitiy ");
		int quantity = input.nextInt();
		if (quantity <= 0 || quantity > ticketQty) {
			System.out.println("Invalid quantity. Please choose a valid quantity. ");
			bookTicket();
			return;	
		}
		if (quantity > ticketQty) {//check if there are enough seats available
			System.out.println("Not enough Seats Avaliable. Please Choose Smaller Amount");
			bookTicket();
			return;
		}
		ticketQty -= quantity;//update available seat quantity
		
		double transactionPrice = 0;
		int loop = quantity;
		while (loop != 0) {
			System.out.println("Please Choose Ticket Type");
			System.out.println("1. Adult ");
			System.out.println("2. Child ");
			System.out.println("3. Student ");
			System.out.println("X. Cancel Booking ");
			String ticketChoice = input.next().toUpperCase();
			switch (ticketChoice) {
			case "1"://Record adult ticket transactions
				transactionAdultTickets++;
				loop--;
				transactionPrice += adultPrice;
				break;
			case "2"://Record child ticket transactions
				transactionChildTickets++;
				loop--;
				transactionPrice += childPrice;
				break;
			case "3"://Record student ticket transactions
				transactionStudentTickets++;
				loop--;
				transactionPrice += studentPrice;
				break;
			case "X":
				System.out.println("Your Booking has been cancelled. System Shutting Down. ");
				ticketQty = 12;
				menu();
				break;
			default:
				if (ticketQty == 0) {
					System.out.println("Unfortunately, There are no seats available ");
				}
				break;
			}
		}
		payment(transactionPrice);//proceeds to payment
	}
	
	public static void checkSeatQuantity() {//method to check available seat quantity
		System.out.println("Available seats:" + ticketQty);
		menu();
	}
	public static void viewTicketTypes() {//Method to view available ticket types and prices
		System.out.println("1. Adult -" + currency.format(adultPrice));
		System.out.println("2. Child -" + currency.format(childPrice));
		System.out.println("3. Student -" + currency.format(studentPrice));
		menu();
	}
	
	public static void chooseDestination() {//Method to choose the destination
		System.out.println("Please Choose Destination:");
		System.out.println("1. Galway");
		System.out.println("2. Aran Islands");
		String destinationChoice = input.next();
		switch (destinationChoice) {
			case "1":
				destination = "Galway";
				break;
			case "2":
				destination = "Aran Islands";
				break;
			default:
				System.out.println("Invalid choice. Please choose a valid destination.");
				chooseDestination();
		}
		System.out.println("Destination set to: " + destination);
	}
	public static void payment(double price) {//method for payment processing
		System.out.println("You have chosen " + (transactionAdultTickets + transactionChildTickets + transactionStudentTickets) + "tickets(s)");
		System.out.println("Transaction Price is " + currency.format(price));
		
		String cardNumber;//tells user to enter a 16-digit card number
		boolean validCardNumber;
		do {
			System.out.println("Pleaase enter your 16-digit card number:");
			cardNumber = input.next();
			validCardNumber = isValidCardNumber(cardNumber);
			if (!validCardNumber) {
				System.out.println("Invalid card number. Please enter a 16-digit card number. ");
			}
		} while (!validCardNumber);
		
		String cvvNumber;//tells user to enter a 3-digit CVV number
		boolean validCVVNumber;
		do {
			System.out.println("Please enter your 3-digit CVV number:");
			cvvNumber = input.next();
			validCVVNumber = isValidCVVNumber(cvvNumber);
			if(!validCVVNumber) {
				System.out.println("Invalid CVV number. Please Try Again.");
			}
		} while (!validCVVNumber);
		//displays transaction success message
		System.out.println("---------------------------------------------");
		System.out.println("       Transaction Successful     ");
		System.out.println("---------------------------------------------");
		System.out.println("     Please Collect Your Ticket   ");
		System.out.println("---------------------------------------------");
		
		numTransactions++;//updates transaction and earnings information
		totalEarnings += price;
		menu();
	}//Method to check the validity of a 16-digit number
	private static boolean isValidCardNumber(String cardNumber) {
		return cardNumber.length() == 16 && cardNumber.matches("\\d+");
	}
	private static boolean isValidCVVNumber(String cvvNumber) {//Method to check the validity of the 3-digit cvv number
		return cvvNumber.length() == 3 && cvvNumber.matches("\\d+"); 
	}
	
	public static void admin() {//admin mode for the system admins
		System.out.println("---------------------------------------------");
		System.out.println("     Island Airways Admin Mode    ");
		System.out.println("---------------------------------------------");
		System.out.println("1. Reset Ticket Amount ");
		System.out.println("2. View Airline Statements ");
		System.out.println("X. Return to Main Menu ");
		
		String choice = input.next().toUpperCase();
		switch (choice) {
		case "1"://reset ticket and transaction info
			resetTicket();
			break;
		case "2"://view airline statements and statistics
			viewStats();
			break;
		case "X":
			System.out.println("Shutting Down");
			System.exit(0);
			break;
		}
	}
	public static void resetTicket() {//method to reset ticket and transaction info
		ticketQty = 12;
		transactionAdultTickets = 0;
		transactionChildTickets = 0;
		transactionStudentTickets = 0;
		totalTransactions = 0;
		totalEarnings = 0;
		destination = "";
		System.out.println("Ticket amount has been reset");
	}
	public static void viewStats() {//method to view airline statements and statistics
		System.out.println("---------------------------------------------");
		System.out.println("   Island Airways Statements        ");
		System.out.println("---------------------------------------------");
		System.out.println("Total Number of Transactions " + numTransactions);
		System.out.println("Total Earnings " + currency.format(totalEarnings));
		average = totalEarnings / numTransactions;
		System.out.println("Average Transactions Amount: " + currency.format(average));
		System.out.println("---------------------------------------------");
		menu();
	}
}
