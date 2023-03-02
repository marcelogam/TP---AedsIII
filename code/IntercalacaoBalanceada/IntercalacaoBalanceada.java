package IntercalacaoBalanceada;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import ClasseFilme.Filme;

public class IntercalacaoBalanceada {

 // BufferedReader para leituras do teclado
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));


    /**
     * <b>mostrarOpcoes</b> - mostra as opções do menu de IntercalacaoBalanceada
     */
    public static void mostrarOpcoes() {
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
    } // end mostrarOpcoes ()

    /**
     * <b>menu</b> mostra menu das intercalações
     * @throws Exception
     */
    public static void menu () throws Exception {
        int option = -1; // -1 = opção invalida
        do {
            mostrarOpcoes();

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
               
                switch ( option ) {
                    case 0: break;
                    case 1: iBComum(4, 2);
                            break;
                    case 2: iBComBlocosDeTamanhoVariavel();
                            break;
                    case 3: iBComSelecaoPorSubstituicao();
                            break;
                } // end switch

        } while (option != 0);
    } // end menu ()


    public static void distribuicao (int qntRegistros, int qntCaminhos, RandomAccessFile rasDB, RandomAccessFile [] ras) throws Exception {
        
             // pular o marcador de ultimo id
                rasDB.readInt();

             // para nao chegar ao fim do arquivo
                long controle = 4;

         // repetir enquanto nao chegar no fim do arquivos
            while( controle < rasDB.length()) {
             // para salvar o tamanho dos registros
                int tam = 0;
             // array de filmes comd tamanho igual a quantidade de registros que vao ser lidos
                Filme [] filmes = new Filme [qntRegistros];
                
             // repeticao para mudar o arquivo temp a ser escrito
                for (int x = 0; x < qntCaminhos; x++) {

                 // repeticao para gravar filmes no arranjo de filmes
                    for (int i = 0; i < qntRegistros && controle < rasDB.length(); i++, controle += tam + 5) {
                        if (rasDB.readBoolean() == true) { // se for lapide pular registro
                            tam = rasDB.readInt();
                            rasDB.seek(rasDB.getFilePointer() + tam);
                            i--;
                        } else {
                            tam = rasDB.readInt();
                            byte [] ba = new byte [tam];
                            rasDB.read(ba);
                         // pegar qntRegistro do banco de dados e salvar no array filme
                            filmes[i] = new Filme(ba);
                        } // end if
                    } // end for

                 // repeticao para gravar registros em arquivo
                    for (int j = 0; j < qntRegistros; j++) {
                        ras[x].writeInt(filmes[j].toByteArray().length);
                        ras[x].write(filmes[j].toByteArray());
                    } // end for
                } // end for
            } // end for
    } // end distribuicao

    /**
     * 
     */
    public static void iBComum ( int qntRegistros, int qntCaminhos ) throws Exception {
        RandomAccessFile rasDB = new RandomAccessFile("DataBase/filmes.db", "rw");
        RandomAccessFile [] ras = new RandomAccessFile [qntCaminhos * 2];
        
     // criacao dos arquivos temporarios
        File [] tempF = new File [qntCaminhos * 2];
        for (int i = 0; i < qntCaminhos * 2; i++) {
            ras[i] = new RandomAccessFile("Arqtemp"+ i + ".tmp", "rw");
            tempF [i] = new File("Arqtemp" + i + ".tmp");
        } // end for

        distribuicao(qntRegistros, qntCaminhos, rasDB, ras);

        for (int i = 0; i < ras.length; i++) {
            
            ras[i].seek(0);
            System.out.println("Arqtemp" + i);
            while (true) {
                int tam = 0;
                try {
                    tam = ras[i].readInt();
                } catch (Exception e) {
                    break;
                } // end try
                byte [] ba = new byte [tam];
                ras[i].read(ba);
                Filme temp = new Filme(ba);
                System.out.println(temp.get_show_id() + " " + temp.get_title());
            } // end while
        } // end for

        for (int i = 0; i < ras.length; i++)  ras[i].close();
        // for (int i = 0; i < tempF.length; i++) tempF[i].delete(); 
        rasDB.close();
    } // end iBComum ()

    /**
     * 
     */
    private static void iBComSelecaoPorSubstituicao() {

    } // end iBComSelecaoPorSubstituicao ()

    /**
     * 
     */
    private static void iBComBlocosDeTamanhoVariavel() {

    } // end iBComBlocosDeTamanhoVariavel ()
} // end class IntercalacaoBalanceada
