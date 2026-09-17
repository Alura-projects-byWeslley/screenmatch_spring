package br.com.alura.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	public void run(String... args) throws Exception{
		//sequencia:
		//Consumo de alguma api JÁ convertendo em json
		//imprimo para checar
		//instancio um conversor
		//crio uma variavel com o conversor passar o valor para ela já no formato dadosserie
		//meio confuso mas basicamente o mapper ja tem a funcao de converter um json em algo
		//o dados serie tem seus alias proprios
		//converto meu arquivo baseado nos alias do dadosserie

		var consumoAPI = new ConsumoAPI();
		String apikey = "";
		String api = "https://www.omdbapi.com/?t=gilmore+girls&&apikey="+apikey;
		var json = consumoAPI.obterDados(api);
		// System.out.println(json);
		// json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
