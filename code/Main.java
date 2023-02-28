import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import ClasseFilme.Filme;
import Crud.Crud;
import LeitorCSV.LeitorCSV;
import OrdenacaoExterna.OrdenacaoExterna;

class Main {

    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));

    public static void lerTodoBD () throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream("DataBase/filmes.db"));
        Filme temp = new Filme();
        int tam;
        for (int i = dis.readInt(); i > 0 ; i--) {
            System.out.println(dis.readBoolean()); // lapide
            tam = dis.readInt();
            byte [] ba = new byte [tam]; // tamanho do registro
            dis.read(ba);
            temp.fromByteArray(ba);
            System.out.println("------------------------------");
            System.out.println(temp.toString());
            System.out.println("------------------------------\n");
        } // end for
        dis.close();
    } // end lerTodoBD ()

    public static Filme readFilme () throws Exception {
        Filme filme = new Filme();
        System.out.println("ID: ");
        filme.set_show_id(Integer.parseInt(reader.readLine()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        System.out.print("Tipo ('M' para \"Movie\" ou 'T' para \"Tv Show\"): ");
        filme.set_type((char)reader.read());
        System.out.print("Titulo: ");
        filme.set_title(reader.readLine().strip());
        System.out.print("Diretores (nomes separado por vigulas ','): ");
        filme.set_director(reader.readLine().split(","));
        System.out.print("Atores (nomes separado por vigulas ','): ");
        filme.set_cast(reader.readLine().split(","));
        System.out.print("Paises (nomes separado por vigulas ','): ");
        filme.set_country(reader.readLine().split(","));
        System.out.print("Data da adição na netflix (dia/mes/ano): ");
        filme.set_date_added(sdf.parse(reader.readLine().strip()));
        System.out.print("Ano de lançamento: ");
        filme.set_release_year(Short.parseShort(reader.readLine()));
        System.out.print("Classificação indicativa:");
        filme.set_rating(reader.readLine().strip());
        System.out.print("Duração: ");
        filme.set_duration(reader.readLine().strip());
        System.out.print("Genero (nomes separado por vigulas ','): ");
        filme.set_listed_in(reader.readLine().split(","));
        System.out.print("Descrição: ");
        filme.set_description(reader.readLine().strip());
        return filme;
    } // end readFilme ()

    public static void main(String[] args) throws Exception {
        LeitorCSV.iniciarBdPeloCSV("DataBase/filmes.db");
     // lerTodoBD(); // debug

        Crud arquivo = new Crud("DataBase/filmes.db");

        int option = -1;

        do {
            System.out.println("");
            System.out.println("-----------------------------------------------------------");
            System.out.println("Trabalho Pratico 1");
            System.out.println("netflix_titles");
            System.out.println();
            System.out.println("1. Create ()                                             ");
            System.out.println("2. Read   ()                                             ");
            System.out.println("3. Update ()                                             ");
            System.out.println("4. Delete ()                                             ");
            System.out.println("5. Intercalação balanceada comum                         ");
            System.out.println("6. Intercalação balanceada com blocos de tamanho variável");
            System.out.println("7. Intercalação balanceada com seleção por substituição  ");
            System.out.println("0. Exit");
            System.out.println("-----------------------------------------------------------");
            System.out.print("Selecione uma opção: ");

            try {
                option = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                System.out.print("\n\nOpção invalida!!!!\n\n");
                option = -1;
            } // end try

            switch ( option ) {
                case 1: arquivo.create(readFilme());
                        break;
                case 2: System.out.println(arquivo.read(Integer.parseInt(reader.readLine())));
                        System.out.println("aperte enter para continuar.");
                        reader.readLine();
                        break;
                case 3: arquivo.update(readFilme());
                        if(!arquivo.update(readFilme())) System.out.println("Registro não encontrado.");
                        break;
                case 4: if(!arquivo.delete(Integer.parseInt(reader.readLine()))) System.out.println("Registro não encontrado.");
                        break;
                case 5: OrdenacaoExterna.iBComum();
                        break;
                case 6: OrdenacaoExterna.iBComBlocosDeTamanhoVariavel();
                        break;
                case 7: OrdenacaoExterna.iBComSelecaoPorSubstituicao();
                        break;
            } // end switch
        } while (option != 0);

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