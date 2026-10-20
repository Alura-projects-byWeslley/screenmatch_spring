package br.com.alura.screenmatch.model;

/**
 * Categoria
 */
public enum Categoria {
    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String categoriaOmdb;

    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text){/*retorna um objeto do tipo categoria*/
        for (Categoria cat : Categoria.values()){/*foreach pra checar todos os valores dentro da classe categoria */
            if(cat.categoriaOmdb.equals(text)) return cat;/*se a classe atual for igual ao oq esta escrito, retorna a classe atual */
        }
        /*TODO:usar optional<categoria> ou algo assim pra checar o valor n funcionaria aqui? */
        /*se nao da erro */
        throw new IllegalArgumentException("Nenhum categoria encontrada para a string fornecida");
    }
}
