package com.bookit.application;

import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.azure.identity.*;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import java.io.*;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {
//		System.out.println(env.getProperty("spring.datasource.url"));
//		System.out.println(env.getProperty("spring.azureblobsource.url"));
//		jdbcTemplate.update("INSERT into movies(name, duration, image, genre, releaseDate) VALUES ('Toy Story', 120, 'http://localhost:1234', '{Animation, Family}', '2004-07-08') ");

//		jdbcTemplate.query("SELECT * FROM movies",
//						(rowset, index) -> rowset.getString("name"))
//				.forEach(s -> System.out.println(s));
//		jdbcTemplate.execute("DROP TABLE movies IF EXISTS");
//		jdbcTemplate.execute("CREATE TYPE movieGenre AS ENUM (" +
//				"'Action'," +
//				"'Adventure'," +
//				"'Animation'," +
//				"'Biography'," +
//				"'Comedy'," +
//				"'Crime'," +
//				"'Documentary'," +
//				"'Drama'," +
//				"'Family'," +
//				"'Fantasy'," +
//				"'Film-Noir'," +
//				"'History'," +
//				"'Horror'," +
//				"'Musical'," +
//				"'Mystery'," +
//				"'Romance'," +
//				"'Sci-Fi'," +
//				"'Short'," +
//				"'Sport'," +
//				"'Thriller'," +
//				"'War'," +
//				"'Western'" +
//				")");
//
//
//		jdbcTemplate.execute("CREATE TABLE movies(" +
//				"id bigint GENERATED ALWAYS AS IDENTITY, name VARCHAR(255), duration int, image text, genre TEXT ARRAY, releaseDate date);");
//
//		List<Object[]> movies = Stream.of("ToyStory1<g>120<g>http://localhost:1234<g>2026-7-17",
//													"FindingNemo<g>135<g>http://localhost:5678<g>2026-7-16")
//						.map(movie -> movie.split("<g>")).collect(Collectors.toList());
//
//
//		jdbcTemplate.batchUpdate("INSERT INTO movies(name, duration, image, genre, releaseDate) VALUES (?, ?, ?, ('Animation', 'Family'), ?)", movies);


		//AZURE BLOBBY BULLSHIT IS HERE
		/*
		InputStream s = new FileInputStream("/etc/secrets/movieSecrets.properties");

		Properties props = new Properties();
		props.load(s);

		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.endpoint("https://moviestorage1.blob.core.windows.net/")
				.connectionString(props.getProperty("AZURE_STORAGE_CONNECTION_STRING"))
				.buildClient();

		String filePath = "/home/vboxuser/Workspace/screenshot.png";
		BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient("container1");

		BlobClient blobClient = blobContainerClient.getBlobClient("screenshot.png");

		BlobSasPermission blobSasPermission = new BlobSasPermission().setReadPermission(true);

		OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(2);
		BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, blobSasPermission)
				.setStartTime(OffsetDateTime.now());

		System.out.println(blobClient.generateSas(values));
		*/
	}
}
