package IntercalacaoBalanceada;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
     * <b>menu</b> - mostra menu das intercalações
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

        /**
         * <b>swap</b> - Trocar elementos na lista
         * @param pos1 - poisicao para a troca
         * @param pos2 - posicao para a troca
         * @param filmearr <code>Filme []</code> a ser trocado
         */
        private static void swap(int pos1, int pos2, Filme [] filmearr) {
            Filme aux = filmearr[pos1];
            filmearr[pos1] = filmearr[pos2];
            filmearr[pos2] = aux;
        } // end swap

        /**
         * <b>quicksort</b> - primeira chamada do quicksort
         * @param filmearr - <code>Filme []</code> a ser ordenado por id
         */
        public static void quicksort(Filme [] filmearr) {
            quicksort( 0, filmearr.length - 1, filmearr);
        } // end quicksort ()

        /**
         * <b>quicksort</b> - ordenacao quicksort
         * @param esq - valor da esquerda
         * @param dir - valor da direita
         * @param filmearr - <code>Filme [] </code> a ser ordenado por id
         */
        private static void quicksort(int esq, int dir, Filme [] filmearr) {
            int pivo = filmearr[(dir+esq)/2].get_show_id();
            int i = esq;
            int j = dir;
         // faz as trocas até que o i passe o j
            while (i <= j) {
             // enquanto o id do pivo for maior do que o elemento da esquerda procura o valor para troca
                while (pivo > filmearr[i].get_show_id()) {
                    i++;
                } // end while

             // enquanto o id do pivo for menor do que o elemento da direita procura o valor para troca
                while (pivo < filmearr[j].get_show_id()) {
                    j--;
                } // end while

             // se o i estiver em j ou não chegou em j, trocar esses jogos
                if (i <= j) {
                    swap(i, j, filmearr);
                    i++;
                    j--;
                } // end if
            } // end while

         // se necessario, fazer um novo quicksort com um array de elementos a esquerda do pivo
            if (esq < j) {
                quicksort(esq, j, filmearr);
            } // end if

         // se necessario, fazer um novo quicksort com um array de elementos a direita de do pivo
            if (dir > i) {
                quicksort(i, dir, filmearr);
            } // end if
        } // end quicksort ()

    /**
     * 
     * @param qntRegistros
     * @param qntCaminhos
     * @param rasDB
     * @param ras
     * @throws Exception
     */
    public static void distribuicao (int qntRegistros, int qntCaminhos, RandomAccessFile rasDB, RandomAccessFile [] ras) throws Exception {

         // pular o marcador de ultimo id
            rasDB.readInt();

         // controlar a chegada do fim do arquivo
            long controle = 4;

         // controlar a escrita do arquivo
            int qntRegistrosReal = 0;

         // repetir enquanto nao chegar no fim do arquivo
            while( controle < rasDB.length() ) {
             // para salvar o tamanho dos registros
                int tam = 0;
             // array de filmes comd tamanho igual a quantidade de registros que vao ser lidos
                Filme [] filmes = new Filme [qntRegistros];

             // repeticao para mudar o arquivo temp a ser escrito
                for (int x = 0; x < qntCaminhos; x++) {
                    qntRegistrosReal = 0;

                 // repeticao para gravar filmes no arranjo de filmes
                    for (int i = 0; controle < rasDB.length() && i < qntRegistros; i++, controle += tam + 5) {
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
                            qntRegistrosReal++;
                        } // end if
                    } // end for

                 // quicksort do array de filmes
                    quicksort (filmes);

                 // repeticao para gravar registros em arquivo
                    for (int j = 0; j < qntRegistrosReal; j++) {
                        byte [] ba = filmes[j].toByteArray();
                        // System.out.println("ESCREVENDO NO ARQUIVO " + x + " " + filmes[j].get_title());
                        ras[x].writeInt(ba.length);
                        ras[x].write(ba);
                    } // end for
                } // end for
            } // end for
    } // end distribuicao ()

    public static int getMenor (Filme [] filmes) {
        int temp = 0;
        for (int i = 1; i < filmes.length; i++){
            if(filmes[temp] != null){
                if(filmes[temp].get_show_id() > filmes[i].get_show_id()){
                    temp = i;
                } // end if
            }else{
                temp++;
            } // end if
        } // end for
        return temp;
    } // end getMenor ()

    public static void intercalacoes ( int qntRegistros, int qntCaminhos, RandomAccessFile [] ras) throws Exception {
        /* 
        Tam do arquivo0 = 8
        Tam do arquivo1 = 6
        quantRegistros = 4
        quantCaminhos = 2
        Quantidade de arquivos = 4

        Os registros vao estar escrito nos 2 primeiros arquivos, 
        1passo- vamos ter que pegar o primeiro registro em cada um dos 2 primeiros arquivos
        2passo- pegar um registro do arquivo0, pegar um registro do arquivo1, salvar os 2 num Filme [].
        3passo- selecionar o menor e salvar em um arquivo2
        4passo- atualizar Filme[arquivoComMenor] com o proximo registro do arquivoComMenor.

        */

        for (int i = 0; i < ras.length; i++) {
            ras[i].seek(0);
        } // end for

        int tam = -1;
        Filme [] filmes = new Filme [qntCaminhos];

        for (int y = 0; y < qntRegistros/qntCaminhos; y++) {
            for (int x = 0; x < qntCaminhos; x++) {
                tam = ras[x].readInt();
                byte [] ba = new byte[tam];
                ras[x].read(ba);
                filmes[x] = new Filme(ba);
            } // end for

            for (int i = 0; i < filmes.length; i++) {
                int arquivoComMenor = getMenor(filmes);
                byte [] ba = filmes[arquivoComMenor].toByteArray();
                //System.out.println("ESCREVENDO NO ARQUIVO " +  + " " + filmes[i].get_title());
                ras[5].writeInt(ba.length);
                ras[5].write(ba);
                filmes[arquivoComMenor] = null;
            } // end for
        } // end for

    } // end intercalacoes ()

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
        intercalacoes(qntRegistros, qntCaminhos, ras);

     // debug
        
        for (int i = 0; i < ras.length; i++) {
            
            ras[i].seek(0);
            System.out.println("\nArqtemp" + i);
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
        for (int i = 0; i < tempF.length; i++) tempF[i].delete(); 
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
