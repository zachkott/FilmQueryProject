package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String user = "student";
	private static final String pass = "student";
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";
	private static Connection conn;

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ERROR");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;
		conn = DriverManager.getConnection(URL, user, pass);

		String sql = "SELECT * FROM film WHERE id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);

		ResultSet filmResult = stmt.executeQuery();
		if (filmResult.next()) {
			film = new Film();

			film.setFilmId(filmResult.getInt("id"));
			film.setTitle(filmResult.getString("title"));
			film.setDescription(filmResult.getString("description"));
			film.setReleaseYear(filmResult.getInt("release_year"));
			film.setLanguageId(filmResult.getInt("language_id"));
			film.setRentDuration(filmResult.getInt("rental_duration"));
			film.setRentalRate(filmResult.getDouble("rental_rate"));
			film.setLength(filmResult.getInt("length"));
			film.setReplacementCost(filmResult.getDouble("replacement_cost"));
			film.setRating(filmResult.getString("rating"));
			film.setSpecialFeatures(filmResult.getString("special_features"));
			film.setActorList(findActorsByFilmId(filmId));
			film.setLanguage(convertLanguageId(filmId));

		}

		filmResult.close();
		stmt.close();

		conn.close();

		return film;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;

		conn = DriverManager.getConnection(URL, user, pass);

		String sql = "SELECT * FROM actor WHERE id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);

		ResultSet actorResult = stmt.executeQuery();
		if (actorResult.next()) {
			actor = new Actor();

			actor.setId(actorResult.getInt("id"));
			actor.setFirstName(actorResult.getString("first_name"));
			actor.setLastName(actorResult.getString("last_name"));
		}

		actorResult.close();
		stmt.close();

		conn.close();

		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {
		List<Actor> actors = new ArrayList<>();

		conn = DriverManager.getConnection(URL, user, pass);

		String sql = "SELECT id, first_name, last_name FROM actor JOIN film_actor ON actor.id = film_actor.actor_id WHERE film_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet ar = stmt.executeQuery();

		while (ar.next()) {
			int id = ar.getInt("id");
			String firstName = ar.getString("first_name");
			String lastName = ar.getString("last_name");
			Actor actor = new Actor(id, firstName, lastName);
			actors.add(actor);
		}
		ar.close();
		stmt.close();
		conn.close();

		return actors;
	}

	@Override
	public String convertLanguageId(int filmId) throws SQLException {
		conn = DriverManager.getConnection(URL, user, pass);


		String sql = "SELECT language.name FROM language JOIN film on film.language_id = language.id WHERE film.id = ?";
		String lang = "";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			lang = rs.getString("language.name");
		}
		rs.close();
		stmt.close();
		conn.close();

		return lang;

	}

	@Override
	public List<Film> findFilmByKeyword(String keyword) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
