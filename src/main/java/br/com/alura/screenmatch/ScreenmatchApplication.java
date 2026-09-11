package br.com.alura.screenmatch;

import java.util.function.Consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.alura.screenmatch.service.ConsumoAPI;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	public void run(String... args) throws Exception{
		var consumoAPI = new ConsumoAPI(); 
		String api = "https://www.omdbapi.com/?t=gilmore+girls&Season=1&apikey=4ef38559";
		var json = consumoAPI.obterDados(api);
		System.out.println(json);
		json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
	}
}
