package br.com.alura.screenmatch.principal;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

        // System.out.println("\nTop 10 episodios:");
        // dadosEpisodios.stream()//PEEK SEMPRE ANTES
        //         .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
        //         .peek(e -> System.out.println("Primeiro filtro (N/A): "+e))
        //         .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
        //         .peek(e -> System.out.println("Ordenação: "+e))
        //         .limit(10)
        //         .peek(e -> System.out.println("Limite: "+e))
        //         .map(e -> e.titulo().toUpperCase())
        //         .peek(e -> System.out.println("Mapeamento: "+e))
        //         .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        // System.out.println("Digite um trecho do titulo do episodio: ");
        // var trechoTitulo = scan.nextLine();
        // //Optional gera um container que pode ou nao conter um valor nao nulo
        // //bom pra API REST
        // //basicamente a gente gera um conteudo que diz se o conteudo da busca existe ou nao

        // /*Exemplo: Optional<Integer> n = numerosDe1a100.parallelStream()
        //                 .filter(x -> x % 10 == 0)
        //                 .findAny();
        // Retorna um numero aleatorio que condiza ao pedido, ele processa varias em paralelo entao o resultado varia */

        // Optional<Episodio> episodioBuscado = episodios.stream()
        //                 .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
        //                 .findFirst();//findAny retorna um Optional
        // if(episodioBuscado.isPresent()){
        //         System.out.println("Episodio encontrado!");
        //         System.out.println("Temporada: "+episodioBuscado.get().getTemporada());
        // }else{
        //         System.out.println("Episodio não encontrado!");
        // }

        // System.out.println("A partir de que ano voce deseja ver os episodios? ");
        // var ano = scan.nextInt();
        // scan.nextLine();

        // LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        
        // DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // episodios.stream()
        //             .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
        //             .forEach(e -> System.out.println(
        //                 "Temporada:  " + e.getTemporada() +
        //                         " Episodio: " + e.getTitulo() +
        //                         " Data lançamento: " + e.getDataLancamento().format(formatador)
        //             ));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                        .filter(e -> e.getAvaliacao() > 0.0)
                        .collect(Collectors.groupingBy(Episodio::getTemporada,
                                Collectors.averagingDouble(Episodio::getAvaliacao)
                        ));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                        .filter(e -> e.getAvaliacao() > 0.0)
                        .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println(String.format(Locale.US, "Média: %.1f", est.getAverage()));
        System.out.println("Melhor episodio: "+est.getMax());
        System.out.println("Pior episodio: "+est.getMin());
        System.out.println("Quantidade: "+est.getCount());
        //summaryStatistics so retorna um monte de informações padroes, tipo min max medium
    }
}


// List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Rodrigo", "Nico");
// nomes.stream()
//         .sorted()
//         .limit(3)//limita a quantidade de objetos que vao ser mostrados
//         .filter(n -> n.startsWith("N"))
//         .map(n -> n.toUpperCase())
//         .forEach(System.out::println);