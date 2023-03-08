import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import ClasseFilme.Filme;
import Crud.Crud;
import IntercalacaoBalanceada.IntercalacaoBalanceada;
import LeitorCSV.LeitorCSV;

class Main {

 // BufferedReader para leituras do teclado
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));

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
            Crud arquivo = new Crud("DataBase/filmes.db");

         // variavel para selecionar opção
            int option = -1; // -1 = opção invalida
     
            do {
                mostrarOpcoes();

                try {
                 // atualizar opcao
                    option = Integer.parseInt(reader.readLine());

                 // se a opcao não existe
                    if( option < 0 || option > 5 ) {
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

                    case 1: System.out.println("\nRegistro criado na posição " + arquivo.create(lerFilmePelaEntrada()));
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

                    case 4: if(!arquivo.delete(Integer.parseInt(reader.readLine()))) {
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

                    case 5: IntercalacaoBalanceada.menu();
                            break;

                    default:option = -1;
                            break;
                } // end switch
            } while (option != 0);
    } // end menuInicial ()

 // ---------------------------------------------------------------------------------------------------------- debug

    public static void lerTodoBD () throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream("DataBase/filmes.db"));
        Filme temp = new Filme();
        int tam = dis.readInt();
        while (true) {
            try {
                System.out.println(dis.readBoolean()); // lapide
            } catch (Exception e) {
                break;
            }
            tam = dis.readInt();
            byte [] ba = new byte [tam]; // tamanho do registro
            dis.read(ba);
            temp.fromByteArray(ba);
            System.out.println("------------------------------");
            System.out.println(temp.get_show_id() + " " + temp.get_title());
            System.out.println("------------------------------\n");
        } // end for
        dis.close();
    } // end lerTodoBD ()
    
    public static void testes () throws Exception {

        RandomAccessFile ras = new RandomAccessFile("DataBase/filmes.db", "rw");
        ras.writeInt(0);
        ras.close();

        Crud crud = new Crud("DataBase/filmes.db");
        Filme temp = new Filme();

     // criacao de 100 filmes
        for (int i = 0; i < 100; i++) {
            temp.set_title("filme " + (i + 1));
            crud.create(temp);
        } // end for

     // update de 20 filmes
        for (int i = 0; i < 20; i+=2) {
            temp.set_title("Atualizado Filme " + i);
            temp.set_show_id(i);
            crud.update(temp);
        } // end for

        IntercalacaoBalanceada.iBComum(6, 5);
        // RandomAccessFile ras2 = new RandomAccessFile("IntercalacaoBalanceada/Arq.temp1", "rw ");
    } // end testes ()

    public static void main(String[] args) throws Exception {

        File arq = new File("DataBase/filmes.db");
        System.out.println("arquivo deletado? " + arq.delete());
        testes(); // inicia o banco de dados e bagunca ids

        // lerTodoBD(); // mostra o bd

        // LeitorCSV.iniciarBdPeloCSV("DataBase/filmes.db");
        
        // mostrar menu inicial
        // menuInicial();

        // TODO: CRUD
        // create ()  [x]
        // read   ()  [x]
        // update ()  [x]
        // delete ()  [x]
        // crud menu  [x]

        // TODO: ordenação
        // 1. Intercalação balanceada comum                          [ ]
        // 2. Intercalação balanceada com blocos de tamanho variável [ ]
        // 3. Intercalação balanceada com seleção por substituição   [ ]

    } // end main ()
} // end class Main