package br.com.alura.screenmatch.principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

public class Principal {
    private Scanner scan =  new Scanner(System.in);

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=4ef38559";

    public void escolheMenu(){
        //Escolhe o nome da serie
        System.out.println("Digite o nome da serie.");
        var nomeSerie = scan.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        //POST
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        //Temporadas
        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++) {
			json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season="+i+API_KEY);
			DadosTemporada dadosTemporada= conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList()); // gera apartir do collectors uma mutavel yeeeeeh :D
                //.toList() geraria uma lista imutavel = ruim bleeeegh :P

        System.out.println("\nTop 5 episodios:");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano voce deseja ver os episodios? ");
        var ano = scan.nextInt();
        scan.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        episodios.stream()
                    .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                    .forEach(e -> System.out.println(
                        "Temporada:  " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Data lançamento: " + e.getDataLancamento().format(formatador)
                    ));
    }
}


// List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Rodrigo", "Nico");
// nomes.stream()
//         .sorted()
//         .limit(3)//limita a quantidade de objetos que vao ser mostrados
//         .filter(n -> n.startsWith("N"))
//         .map(n -> n.toUpperCase())
//         .forEach(System.out::println);