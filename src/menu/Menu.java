package menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import arquivo.Arquivo;
import crud.Crud;
import filme.Filme;
import intercalacao.IntercalacaoBalanceada;
import leitorCSV.LeitorCSV;

public class Menu {


    // BufferedReader para leituras do teclado
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));

     /**
      * <b>mostrarOpcoesIntercalacao</b> - mostra as opções do menu de IntercalacaoBalanceada
      */
      public static void mostrarOpcoesIntercalacao() {
        System.out.println();
        System.out.println("---------------------------------------------------------- ");
        System.out.println("Trabalho Pratico 1                                         ");
        System.out.println("netflix_titles                                             ");
        System.out.println("Intercalação Balanceada                                    ");
        System.out.println();
        System.out.println(" 1. Intercalação Balanceada Comum                          ");
        System.out.println(" 2. Intercalação Balanceada com blocos de tamanho variável ");
        System.out.println(" 3. Intercalação Balanceada com seleção por substituição   ");
        System.out.println();
        System.out.println(" 0. Voltar                                                 ");
        System.out.println("---------------------------------------------------------- ");
        System.out.print(  "Selecione uma opção: ");
    } // end mostrarOpcoesIntercalacao ()

    /**
     * <b>lerFilmePelaEntrada<b> - pega informações da entrada (teclado) e vai atribuindo a um <code>Filme</code>, já tratando erros
     * @return <code>Filme</code> com informações registradas pela função
     * @throws Exception exceções do BufferedReader
     */
    public static Filme lerFilmePelaEntrada () throws Exception {
        Filme filme = new Filme(); // instancia de filme
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy"); // para converter data
        boolean valid = true;

        do {
            valid = true;
            System.out.print("ID: ");
            try {
                filme.set_show_id(Integer.parseInt(reader.readLine()));
            } catch (Exception e) {
                System.out.println("\nValor invalido, tente novamente.\n");
                valid = false;
            } // end try
        } while (!valid);

        do {
            valid = true;
            System.out.print("Tipo ('M' para \"Movie\" ou 'T' para \"Tv Show\"): ");
            filme.set_type(reader.readLine().charAt(0));
            if( filme.get_type() != 'M' && filme.get_type() != 'T' ) {
                System.out.println("\nValor invalido, tente novamente.\n");
                valid = false;
            } // end if
        } while (!valid);

        System.out.print("Titulo: ");
        filme.set_title(reader.readLine().strip());

        do {
            valid = true;
            System.out.print("Diretores (nomes separado por vigulas ','): ");
            try {
                filme.set_director(reader.readLine().split(","));
            } catch (Exception e) {
                System.out.println("\nPadrão invalido, tente novamente.\n");
                valid = false;
            } // end try
        } while (!valid);

        do {
            valid = true;
            System.out.print("Atores (nomes separado por vigulas ','): ");
            try {
                filme.set_cast(reader.readLine().split(","));
            } catch (Exception e) {
                System.out.println("\nPadrão invalido, tente novamente.\n");
                valid = false;
            } // end try
        } while (!valid);

        do {
            valid = true;
            System.out.print("Paises (nomes separado por vigulas ','): ");
            try {
                filme.set_country(reader.readLine().split(","));
            } catch (Exception e) {
                System.out.println("\nPadrão invalido, tente novamente.\n");
                valid = false;
            } // end try
        } while (!valid);

        do {
            valid = true;
            System.out.print("Data da adição na netflix (dia/mes/ano): ");
            try {
                filme.set_date_added(sdf.parse(reader.readLine().strip()));
            } catch (Exception e) {
                System.out.println("\nPadrão invalido, tente novamente.\n");
                valid = false;
            } // end try
        } while (!valid);

        do {
            valid = true;
            System.out.print("Ano de lançamento: ");
            try {
                filme.set_release_year(Short.parseShort(reader.readLine().substring(0, 4)));
            } catch (Exception e) {
                System.out.println("\nPadrão invalido, tente novamente.\n");
                valid = false;
            } // end try
        } while (!valid);

        System.out.print("Classificação indicativa:");
        filme.set_rating(reader.readLine().strip());

        System.out.print("Duração: ");
        filme.set_duration(reader.readLine().strip());

        do {
            valid = true;
            System.out.print("Genero (nomes separado por vigulas ','): ");
            try {
                filme.set_listed_in(reader.readLine().split(","));
            } catch (Exception e) {
                System.out.println("\nPadrão invalido, tente novamente.\n");
                valid = false;
            } // end try
        } while (!valid);

        System.out.print("Descrição: ");
        filme.set_description(reader.readLine().strip());

        return filme;
    } // end lerFilmePelaEntrada ()

    /**
     * <b>mostrarOpcoes</b> - mostra as opções do menu inicial
     */
    public static void mostrarOpcoes() {
        System.out.println();
        System.out.println("----------------------------------------------------------");
        System.out.println("Trabalho Pratico 1                                        ");
        System.out.println("netflix_titles                                            ");
        System.out.println();
        System.out.println(" 1. Create                                                ");
        System.out.println(" 2. Read                                                  ");
        System.out.println(" 3. Update                                                ");
        System.out.println(" 4. Delete                                                ");
        System.out.println(" 5. Intercalações                                         ");
        System.out.println(" 6. Reiniciar banco de dados a partir do CSV              ");
        System.out.println();
        System.out.println(" 0. Exit                                                  ");
        System.out.println("----------------------------------------------------------");
        System.out.print(  "Selecione uma opção: ");
    } // end mostrarOpcoes

    /**
     * <b>menuInicial</b> - funcao que mostra o menu inicial
     * @throws Exception
     */
    public static void menuInicial () throws Exception {

         // criar instancia de crud
            Crud arquivo = new Crud("database/filmes.db");

         // variavel para selecionar opção
            int option = -1; // -1 = opção invalida
            final int quantidadeDeOpcoes = 6;
            do {
                mostrarOpcoes();

                try {
                 // atualizar opcao
                    option = Integer.parseInt(reader.readLine());

                 // se a opcao não existe
                    if( option < 0 || option > quantidadeDeOpcoes ) {
                        System.out.print("\nOpção invalida, tente novamente.\n");
                        System.out.println("aperte enter para continuar.\n");
                        reader.readLine();
                        System.out.println();
                        option = -1;
                    } // end if
             // se entrada não for um inteiro valido
                } catch (Exception e) {
                    System.out.print("\nValor invalido, tente novamente.\n");
                    System.out.println("aperte enter para continuar.\n");
                    reader.readLine();
                    System.out.println();
                    option = -1;
                } // end try
                
                switch ( option ) {
                    case 0: break;

                    case 1: System.out.println("\nRegistro criado com o ID " + arquivo.create(lerFilmePelaEntrada()));
                            System.out.println("aperte enter para continuar.\n");
                            reader.readLine();
                            System.out.println();
                            break;

                    case 2:
                            System.out.print("\nID do filme: ");
                            int i = Integer.parseInt(reader.readLine());
                            Filme temp = arquivo.read(i);
                            if(temp != null) {
                                System.out.println("\n" + temp.toString());
                            } else {
                                System.out.println("\nFilme não encontrado");
                            } // end if
                            System.out.println("aperte enter para continuar.\n");
                            reader.readLine();
                            System.out.println();
                            break;

                    case 3: System.out.println("\nDigite o id do filme a ser atualizado e suas novas informações.\n");
                            if(!arquivo.update(lerFilmePelaEntrada())) {
                                System.out.println("\nRegistro não encontrado.");
                                System.out.println("aperte enter para continuar.\n");
                                reader.readLine();
                                System.out.println();
                            } else {
                                System.out.println("\nRegistro atualizado.");
                                System.out.println("aperte enter para continuar.\n");
                                reader.readLine();
                                System.out.println();
                            } // end if
                            break;

                    case 4: System.out.print("digite o id do filme a ser deletado: ");
                            if(!arquivo.delete(Integer.parseInt(reader.readLine()))) {
                                System.out.println("\nRegistro não encontrado.");
                                System.out.println("aperte enter para continuar.\n");
                                reader.readLine();
                                System.out.println();
                            } else {
                                System.out.println("\nRegistro deletado com sucesso.");
                                System.out.println("aperte enter para continuar.\n");
                                reader.readLine();
                                System.out.println();
                            } // end if
                            break;

                    case 5: menuIntercalacoes();
                            break;

                    case 6: System.out.println("TEM CERTEZA? (digite 's' ou 'S' para confirmar).");
                            if(reader.readLine().charAt(0) == 's' || reader.readLine().charAt(0) == 'S') {
                                Arquivo bd = new Arquivo("database/filmes.db");
                                bd.limpar();
                                LeitorCSV.iniciarBdPeloCSV("database/filmes.db");
                            } // end if
                            break;

                    default:option = -1;
                            break;
                } // end switch
            } while (option != 0);
    } // end menuInicial ()
    
     /**
      * <b>menuIntercalacoes</b> - mostra menu das intercalações
      * @throws Exception
      */
      public static void menuIntercalacoes () throws Exception {
        int option = -1; // -1 = opção invalida
        do {
            mostrarOpcoesIntercalacao();

            try {
                 // atualizar opcao
                    option = Integer.parseInt(reader.readLine());

                 // se a opcao não existe
                    if( option < 0 || option > 3 ) {
                        System.out.print("\nOpção invalida, tente novamente.\n");
                        System.out.println("aperte enter para continuar.\n");
                        reader.readLine();
                        System.out.println();
                        option = -1;
                    } // end if
             // se entrada não for um inteiro valido
                } catch (Exception e) {
                    System.out.print("\nValor invalido, tente novamente.\n");
                    System.out.println("aperte enter para continuar.\n");
                    reader.readLine();
                    System.out.println();
                    option = -1;
                } // end try
                int qntRegistros;
                int qntCaminhos;

                switch ( option ) {
                    case 0: break;
                    case 1: System.out.print("\n Digite a quantidade de registros: ");
                            qntRegistros = Integer.parseInt(reader.readLine());
                            System.out.print("\n Digite a quantidade de caminhos: ");
                            qntCaminhos = Integer.parseInt(reader.readLine());
                            IntercalacaoBalanceada.iBComum(qntRegistros, qntCaminhos);
                            break;
                    case 2: System.out.print("\n Digite a quantidade de registros: ");
                            qntRegistros = Integer.parseInt(reader.readLine());
                            System.out.print("\n Digite a quantidade de caminhos: ");
                            qntCaminhos = Integer.parseInt(reader.readLine());
                            IntercalacaoBalanceada.iBComBlocosDeTamanhoVariavel(qntRegistros, qntRegistros);
                            break;
                    case 3: IntercalacaoBalanceada.iBComSelecaoPorSubstituicao();
                            break;
                    default: break;
                } // end switch

        } while (option != 0);
    } // end menuIntercalacoes ()

} // end class Menu
