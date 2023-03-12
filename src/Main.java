
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.Random;

import arquivo.Arquivo;
import crud.Crud;
import filme.Filme;
import intercalacao.IntercalacaoBalanceada;
import menu.Menu;

/**
 * Trabalho Pratico 01 - AEDS III
 * ALUNOS: - Leandro Guido Lorenzini Santos
 *         - Marcelo Augusto Moreira Gomes
 */
class Main {

    // ----------------------------------------------------------------------------- DEBUG AREA

    public static void lerTodoBD () throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream("database/filmes.db"));
        Filme temp = new Filme();
        int tam = dis.readInt();
        boolean lapide = false;
        while (true) {
            try {
                lapide = dis.readBoolean(); // lapide
            } catch (Exception e) {
                break;
            }
            tam = dis.readInt();
            byte [] ba = new byte [tam]; // tamanho do registro
            dis.read(ba);
            temp.fromByteArray(ba);
            if(!lapide) {
                System.out.println(temp.get_show_id() + " " + temp.get_title());
            } else {
                System.out.println("REGISTRO MORTO");
            } // end if
        } // end for
        dis.close();
    } // end lerTodoBD ()
    
    public static void testes () throws Exception {

        RandomAccessFile ras = new RandomAccessFile("database/filmes.db", "rw");
        ras.writeInt(0);
        

        Crud crud = new Crud("database/filmes.db");
        Filme temp = new Filme();

        Random random = new Random(0);
        final int x = 150;
        int [] aleatorios = new int [x];
        for (int i = 0; i < aleatorios.length; i++) {
            int numero = random.nextInt(1, x+1);
            boolean ler = true;
            for (int j = 0; j < aleatorios.length; j++) {
                if(numero == aleatorios[j]) {
                    ler = false;
                    j = aleatorios.length;
                } // end if
            } // end for
            if(ler) {
                aleatorios[i] = numero;
            } else {
                i--;
            } // end if
        } // end for

     // criacao de x filmes
        for (int i = 0; i < x; i++) {
            temp.set_title("filme " + (aleatorios[i]));
            temp.set_show_id(aleatorios[i]);
            crud.createSemTrocarId(temp);
        } // end for

        ras.seek(0);
        ras.writeInt(x);

        ras.close();
    } // end testes ()

    // ----------------------------------------------------------------------------- MAIN
    public static void main(String[] args) throws Exception {

        // Arquivo bd = new Arquivo("database/filmes.db");
        // bd.limpar();
        // bd.escreverInt(0);

        //testes(); /* inicia o banco de dados com filmes de ids aleatorios */

        //lerTodoBD(); /* mostra o bd */
        // mostrar menu inicial
        Menu.menuInicial();
        //lerTodoBD(); /* mostra o bd */

    } // end main ()
} // end class Main