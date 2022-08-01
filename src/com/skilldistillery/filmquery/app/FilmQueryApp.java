package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();

		app.launch();
	}

	private void launch() {
		Scanner sc = new Scanner(System.in);

		startUserInterface(sc);

		sc.close();
	}

	private void startUserInterface(Scanner sc) {
		mainMenu();
		boolean continueLoop = true;
		try {
			do {
				int menuOption = sc.nextInt();

				switch (menuOption) {
				case 1:
					listByFilmId(sc);
					break;

				case 2:
					listByKeyword(sc);
					break;

				case 3:
					terminateApp();
					break;

				default:
					System.out.println("Invalid input. Please try again.");
					break;

				}
				mainMenu();

			} while (continueLoop);
		} catch (Exception e) {
			System.out.println("Invalid input. Please try again.");
			sc.nextLine();
			startUserInterface(sc);
		}
	}

	public void mainMenu() {
		System.out.println();
		System.out.println("///////////////////////////////////////////////////////");
		System.out.println("/                                                     /");
		System.out.println("/                                                     /");
		System.out.println("/               --- Film Query App ---                /");
		System.out.println("/                                                     /");
		System.out.println("/        Please choose an option from the menu        /");
		System.out.println("/                                                     /");
		System.out.println("/  1.) Look up a film by its id.                      /");
		System.out.println("/  2.) Look up a film by a search keyword             /");
		System.out.println("/  3.) Exit the  application                          /");
		System.out.println("/                                                     /");
		System.out.println("/                                                     /");
		System.out.println("///////////////////////////////////////////////////////");

	}

	public void listByFilmId(Scanner sc) {
		System.out.println("Please enter a film ID to display the film's information:");

		try {
			int filmId = sc.nextInt();
			Film film = db.findFilmById(filmId);
			if (film != null) {
				System.out.println(film);

			} else {
				System.out.println("Sorry, there is no film with that ID.");
				sc.nextLine();
				startUserInterface(sc);

			}

		} catch (Exception e) {
			System.out.println("Invalid input. Please try again.");
			sc.nextLine();
			startUserInterface(sc);
			e.printStackTrace();
		}

	}

	public void listByKeyword(Scanner sc) {
		System.out.println("Please enter a keyword: ");
		String input = sc.next();
		String keyword = "%" + input + "%";

		try {
			List<Film> keyFilm = new ArrayList<>();
			keyFilm = db.findFilmByKeyword(keyword);

			if (keyFilm.size() > 0) {
				System.out.println(keyFilm);
				System.out.println();
			} else {
				System.out.println("There are no results that match your search.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void terminateApp() {
		System.out.println("Thank you for using the film query app! Have a wonderful day!");
		System.exit(0);
	}

}
