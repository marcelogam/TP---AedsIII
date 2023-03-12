package intercalacao;


import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;

import arquivo.Arquivo;
import filme.Filme;

public class IntercalacaoBalanceada extends OrdenacaoExterna {

    /**
     * <b>distribuicao</b> - 1 Etapa da intercalacao
     * <p>Pega <code>qntRegistros</code> registros de <code>Filme</code> do <code>bd</code> divide em <code>qntCaminhos</code> arquivos </p>
     * @param qntRegistros - tamanho do segmento
     * @param qntCaminhos - quantida de caminhos
     * @param bd - <code>Arquivo</code> para acessar o banco de dados
     * @param temps - <code>Arquivo []</code> para acessar os arquivos temporarios
     * @throws Exception
     */
    private static void distribuicao (int qntRegistros, int qntCaminhos, Arquivo bd, Arquivo [] temps) throws Exception {
            
         // pular o marcador de ultimo id
            bd.pularNBytes(Arquivo.TAMANHO_INT);

         // controlar a chegada do fim do arquivo
            long controle = Arquivo.TAMANHO_INT;

         // repetir enquanto nao chegar no fim do arquivo
            while( controle < bd.tamanhoDoArquivo() ) {
             // para salvar o tamanho dos registros
                int tam = 0;
             // array de filmes comd tamanho igual a quantidade de registros que vao ser lidos
                LinkedList <Filme> filmes = new LinkedList<Filme>();
                
             // repeticao para mudar o arquivo temp a ser escrito
             // OBS: escreve nos primeiros arquivos temp, o resto tornará os arquivos de saida da intercalacao
                for (int x = 0; x < qntCaminhos; x++) {

                 // repeticao para gravar filmes na lista de filmes
                    for (int i = 0; controle < bd.tamanhoDoArquivo() && i < qntRegistros; i++, controle += tam + 5) {
                     // se for lapide pular registro
                        if (bd.lerLapide() == true) { 
                            tam = bd.lerInt();
                            bd.pularNBytes(tam);
                            i--;
                        } else {
                         // se nao for lapide adicionar a lista o proximo registro
                            tam = bd.lerInt();
                            filmes.add(new Filme(bd.lerRegistro(tam)));
                        } // end if
                    } // end for
                    
                 // ordenacao do array de filmes
                    Filme filmesArr [] = new Filme [filmes.size()];
                    filmes.toArray(filmesArr);
                    Arrays.sort(filmesArr);

                 // repeticao para gravar registros em arquivo
                    for (int j = 0; j < filmes.size(); j++) {
                        temps[x].escreverRegistro(filmesArr[j].toByteArray());
                    } // end for

                    filmes.clear();
                } // end for
            } // end for
    } // end distribuicao ()

 // --------------------------------------------------------------------------- INTERCALACAO BALANCEADA COMUM

    /**
     * <b>intercalacaoComum</b> - intercalacao propriamente dita
     * @param qntRegistros - tamanho dos segmentos que vao ser usado
     * @param qntCaminhos - quantidade de caminhos
     * @param temps - arquivos temporarios criados anteriormente
     * @param ocorrencia - controle para alternar leituras entre arquivos de saidas e entradas
     * @throws Exception
     */
    private static boolean intercalacaoComum ( int qntRegistros, int qntCaminhos, Arquivo [] temps, int ocorrencia) throws Exception {
        Arquivo.todosVaoParaPos(temps, 0);

        int indexDoArquivoLido = -1;

        Entrada [] entrada = new Entrada[qntCaminhos];
        Arquivo [] saida = new Arquivo[qntCaminhos];
        int indexSaida = 0;
        
     // atribuicao de entradas e saidas de acordo com a ocorrencia
        definirEntradaESaida(entrada, saida, temps, ocorrencia, qntCaminhos);

     // repeticao para alternar entre os arquivos de saida
        while(indexSaida < saida.length) {
                // System.out.println("\nGravação no arquivo de saida " + indexSaida);

             // leitura inicial
                for (int i = 0; i < entrada.length; i++) {
                    entrada[i].leituraInicial(i);
                } // end for

             // enquanto tiver alguma entrada com leitura valida
                while(temArquivoValido(entrada)) {

                 // pegar o indice do arquivo que contem o menor filme (id) valido
                    indexDoArquivoLido = getIndiceDaMenorChaveValida(entrada);
                    
                 // escrever registro no arquivo de saida selecionado
                    saida[indexSaida].escreverRegistro(entrada[indexDoArquivoLido].filmes.toByteArray());
                    // System.out.println("\t\t\tESCREVEU " + entrada[indexDoArquivoLido].filmes.get_show_id() + " NO ARQUIVO DE SAIDA");

                 // tenta ler de onde o indice foi retirado
                    entrada[indexDoArquivoLido].leitura(qntRegistros, indexDoArquivoLido);
                    //if(!temArquivoValido(entrada)) System.out.println("\t\tNAO TEM MAIS ARQUIVOS VALIDOS PARA LEITURA");
                } // end while

             // permite novamente que leia qntRegitros vezes
                for (int i = 0; i < entrada.length; i++) entrada[i].leu = 0;

             // controle da repeticao 
                if(temRegistroPraLer(entrada)) {
                    indexSaida = (indexSaida+1)%saida.length;
                } else {
                    indexSaida++;
                } // end if

        } // end while

     // para a quantidade de vezes que a intercalacao tem que ser feita
        boolean resp = true;

     // se tudo estiver em um arquivo só
        if(saida[1].tamanhoDoArquivo() == 0) {
         // escreve o banco de dados
            escreverBDAPartirDeTemp(saida[0]);
         // booleano é retornado falso e cancela a prox intercalacao
            resp = false;
        } // end if

     // limpa os arquivos de entrada antes da prox intercalacao
     // OBS: esses dados ja foram gravados nos de saida, que serao a entrada da prox intercalacao
        for (int i = 0; i < entrada.length; i++) entrada[i].arquivo.limpar();
        
        return resp;
    } // end intercalacaoComum ()

    /**
     * <b>intercalacoesComum</b> - funcao para fazer e controlar as intercalacoes
     * @param qntRegistros - tamanho dos segmentos iniciais
     * @param qntCaminhos - quantidade de caminhos
     * @param temps - arquivos temporarios criados anteriormente
     * @throws Exception
     */
    private static void intercalacoesComum (int qntRegistros, int qntCaminhos, Arquivo [] temps) throws Exception {
        int i = 0;
        while(intercalacaoComum(qntRegistros, qntCaminhos, temps, i)) {
            qntRegistros *= qntCaminhos;
            i++;
        } // end while
    } // end intercalacoesComum ()

    /**
     * <b>iBComum</b> - Algoritmo que ordena os registros por meio da intercalação de registros de várias fontes balanceadas
     * @param qntRegistros - tamanho inicial dos segmentos
     * @param qntCaminhos - quantidade de caminhos
     * @throws Exception
     */
    public static void iBComum ( int qntRegistros, int qntCaminhos ) throws Exception {
        Arquivo bd = new Arquivo("database/filmes.db");
        Arquivo [] temps = new Arquivo [qntCaminhos * 2];
        File [] tempF = new File [qntCaminhos * 2];

     // criacao dos arquivos temporarios
        criarArquivosTemporarios(qntCaminhos, tempF, temps);

     // distribuicao ETAPA 1 da intercalacao
        distribuicao(qntRegistros, qntCaminhos, bd, temps);

     // intercalacoes comum ETAPA 2 da intercalacao
        intercalacoesComum(qntRegistros, qntCaminhos, temps);

     // fechar arquivos que acessam as temps
        Arquivo.fecharArquivos(temps);
        
     // deletando arquivos temp
        deletarTemporarios(tempF);

     // fechar arquivo que acessa o banco de dados
        bd.fechar();

    } // end iBComum ()

 // --------------------------------------------------------------------------- INTERCALACAO COM SEGMENTOS DE TAM VARIAVEL

    private static void descobrirTamanhoDeCadaSegmento(int qntRegistros, int [] tamDoSegmento, Entrada [] entrada) throws Exception {
        Filme filme = null;
        Filme proxFilme = null;

     // salva as posicoes iniciais de todas as entradas para voltar depois
        long [] posIniciais = new long[entrada.length];
        for (int x = 0; x < entrada.length; x++) posIniciais[x] = entrada[x].arquivo.getPos();

     // define o tamanho dos segmentos de todas as entradas com o tamanho inicial dos segmentos
        for (int i = 0; i < tamDoSegmento.length; i++) tamDoSegmento[i] = qntRegistros;

     // repeticao para chegar no fim do primeiro segmento
        int tam = -1;
        for (int x = 0; x < entrada.length; x++) {
            do {
                for (int i = 0; i < qntRegistros; i++ ) {
                    if( !entrada[x].arquivo.chegouNoFimDoArquivo()) {
                     // pega ultimo filme do segmento ordenado atual
                        tam = entrada[x].arquivo.lerInt();
                        filme = new Filme(entrada[x].arquivo.lerRegistro(tam));
                    } // end if
                } // end while

                if( !entrada[x].arquivo.chegouNoFimDoArquivo() ) {
                 // pega o primeiro filme do prox segmento ordenado
                    tam = entrada[x].arquivo.lerInt();
                    proxFilme = new Filme(entrada[x].arquivo.lerRegistro(tam));

                    entrada[x].arquivo.irParaPos(entrada[x].arquivo.getPos() - tam - Integer.BYTES);

                 // se o primeiro do prox segmento for maior que o ultimo do segmento atual
                    if (filme.get_show_id() < proxFilme.get_show_id()) {
                        // System.out.println(filme.get_show_id() + " < " + proxFilme.get_show_id());
                    // aumenta o tamanho do segmento dessa entrada
                        tamDoSegmento[x] += qntRegistros;
                    } else {
                        // System.out.println(filme.get_show_id() + " > " + proxFilme.get_show_id());
                        break;
                    } // end if
                } // end if
            } while (!entrada[x].arquivo.chegouNoFimDoArquivo());
        } // end for

     // volta para o inicio do segmento
        for (int i = 0; i < entrada.length; i++) entrada[i].arquivo.irParaPos(posIniciais[i]);
        // for (int i = 0; i < tamDoSegmento.length; i++) System.out.println("tamSeg ["+i+"] = " + tamDoSegmento[i]);

    } // end descobrirTamanhoDeCadaSegmento ()

    /**
     * <b>intercalacaoSegTamVariavel</b> - intercalacao de segmento de tamanho variavel propriamente dita
     * @param qntRegistros - tamanho dos segmentos que vao ser usado
     * @param qntCaminhos - quantidade de caminhos
     * @param temps - arquivos temporarios criados anteriormente
     * @param ocorrencia - controle para alternar leituras entre arquivos de saidas e entradas
     * @throws Exception
     */
    private static boolean intercalacaoSegTamVariavel ( int qntRegistros, int qntCaminhos, Arquivo [] temps, int ocorrencia) throws Exception {
        Arquivo.todosVaoParaPos(temps, 0);

        int indexDoArquivoLido = -1;

        Entrada [] entrada = new Entrada[qntCaminhos];
        Arquivo [] saida = new Arquivo[qntCaminhos];
        int indexSaida = 0;   

        int [] tamDoSegmento = new int [qntCaminhos];
        for (int i = 0; i < tamDoSegmento.length; i++) tamDoSegmento[i] = qntRegistros;

     // atribuicao de entradas e saidas de acordo com a ocorrencia
        definirEntradaESaida(entrada, saida, temps, ocorrencia, qntCaminhos);
        
     // repeticao para alternar entre os arquivos de saida
        while(indexSaida < saida.length) {
                
                // System.out.println("\nGravação no arquivo de saida " + indexSaida);
                descobrirTamanhoDeCadaSegmento(qntRegistros, tamDoSegmento, entrada);

             // leitura inicial
                for (int i = 0; i < entrada.length; i++) {
                    entrada[i].leituraInicial( i);
                } // end for

             // enquanto tiver alguma entrada com leitura valida
                while(temArquivoValido(entrada)) {

                 // pegar o indice do arquivo que contem o menor filme (id) valido
                    indexDoArquivoLido = getIndiceDaMenorChaveValida(entrada);

                 // escrever registro no arquivo de saida selecionado
                    saida[indexSaida].escreverRegistro(entrada[indexDoArquivoLido].filmes.toByteArray());
                    // System.out.println("\n\t\t\tESCREVEU " + entrada[indexDoArquivoLido].filmes.get_show_id() + " NO ARQUIVO DE SAIDA");
                    
                 // tenta ler de onde o indice foi retirado
                    entrada[indexDoArquivoLido].leitura(tamDoSegmento[indexDoArquivoLido], indexDoArquivoLido);
                    // if(!temArquivoValido(entrada)) System.out.println("\t\tNAO TEM MAIS ARQUIVOS VALIDOS PARA LEITURA");

                } // end while

             // permite novamente que leia qntRegitros vezes
                for (int i = 0; i < entrada.length; i++) entrada[i].leu = 0;

             // controle da repeticao 
                if(temRegistroPraLer(entrada)) {
                    indexSaida = (indexSaida+1)%saida.length;
                } else {
                    indexSaida++;
                } // end if

        } // end while

     // para a quantidade de vezes que a intercalacao tem que ser feita
        boolean resp = true;

     // se tudo estiver em um arquivo só
        if(saida[1].tamanhoDoArquivo() == 0) {
         // escreve o banco de dados
            escreverBDAPartirDeTemp(saida[0]);
         // booleano é retornado falso e cancela a prox intercalacao
            resp = false;
        } // end if

     // limpa os arquivos de entrada antes da prox intercalacao
     // OBS: esses dados ja foram gravados nos de saida, que serao a entrada da prox intercalacao
        for (int i = 0; i < entrada.length; i++) entrada[i].arquivo.limpar();
        
        return resp;
    } // end intercalacaoSegTamVariavel ()

    /**
     * <b>intercalacoesSegTamVariavel</b> - chamada das intercalacoes de segmento de tamanho variavel
     * @param qntRegistros - tamanho dos segmentos iniciais
     * @param qntCaminhos - quantidade de caminhos
     * @param temps - arquivos temporarios criados anteriormente
     * @throws Exception
     */
    private static void intercalacoesSegTamVariavel (int qntRegistros, int qntCaminhos, Arquivo [] temps) throws Exception {
        int i = 0;
        while(intercalacaoSegTamVariavel(qntRegistros, qntCaminhos, temps, i)) {
            i++;
        } // end while
        
    } // end intercalacoesSegTamVariavel ()

    /**
     * <b>iBComBlocosDeTamanhoVariavel</b> - Algoritmo que ordena os registros por meio da intercalação de registros de várias fontes balanceadas usando blocos de tamanho variavel
     * @param qntRegistros - tamanho inicial dos segmentos
     * @param qntCaminhos - quantidade de caminhos
     * @throws Exception
     */
    public static void iBComBlocosDeTamanhoVariavel( int qntRegistros, int qntCaminhos ) throws Exception {
        
        Arquivo bd = new Arquivo("database/filmes.db");
        Arquivo [] temps = new Arquivo [qntCaminhos * 2];
        // criacao dos arquivos temporarios
        File [] tempF = new File [qntCaminhos * 2];
        for (int i = 0; i < qntCaminhos * 2; i++) {
            temps[i] = new Arquivo("Arqtemp"+ i + ".tmp");
            tempF[i] = new File("Arqtemp" + i + ".tmp");
        } // end for
        
        distribuicao(qntRegistros, qntCaminhos, bd, temps);
        intercalacoesSegTamVariavel(qntRegistros, qntCaminhos, temps);
        Arquivo.fecharArquivos(temps);
        
     // deletando arquivos temp
        for (int i = 0; i < tempF.length; i++) tempF[i].delete(); 

        bd.fechar();
    } // end iBComBlocosDeTamanhoVariavel ()

 // --------------------------------------------------------------------------- INTERCALACAO BALANCEADA COM SELECAO POR SUBSTITUICAO

    private static void distribuicaoPeloHeap ( Arquivo bd, Arquivo [] temps) throws Exception {
        final int heapSize = 7;
        final int qntCaminhos = 2;
        bd.irParaPos(Integer.BYTES);

        PriorityQueue<FilmeComPeso> heap = new PriorityQueue<FilmeComPeso>(heapSize);
        // Iterator<FilmeComPeso> it = heap.iterator();
        int tam = -1;

        for (int i = 0; i < heapSize; i++) {
            if(!bd.lerLapide()) {
                tam = bd.lerInt();
                FilmeComPeso filme = new FilmeComPeso(new Filme(bd.lerRegistro(tam)), 0);
                heap.add(filme);
                // System.out.println("\tadicionou "+filme.peso+"-"+filme.filme.get_show_id()+" no heap");
            } else {
                tam = bd.lerInt();
                bd.pularNBytes(tam);
                i--;
            } // end if
        } // end for

        while (!bd.chegouNoFimDoArquivo()) {
            FilmeComPeso hProxFilme = null;
            FilmeComPeso filme = heap.remove();
            temps[filme.peso % qntCaminhos].escreverRegistro(filme.filme.toByteArray());
            // System.out.println("escreveu "+filme.peso+"-"+filme.filme.get_show_id()+" no arquivo " + filme.peso % qntCaminhos);

         // ler novofilme do bd
            if(!bd.lerLapide()) {
                tam = bd.lerInt();
                Filme proxFilme = new Filme(bd.lerRegistro(tam));

                if(proxFilme.get_show_id() < filme.filme.get_show_id()) {
                    hProxFilme = new FilmeComPeso(proxFilme, (filme.peso + 1));
                } else {
                    hProxFilme = new FilmeComPeso(proxFilme, (filme.peso)    );
                } // end if

                heap.add(hProxFilme);

            } else {
                tam = bd.lerInt();
                bd.pularNBytes(tam);
            } // end if
        } // end while

        while (!heap.isEmpty()) {
            FilmeComPeso filme = heap.remove();
            temps[filme.peso % qntCaminhos].escreverRegistro(filme.filme.toByteArray());
            // System.out.println("escreveu "+filme.peso+"-"+filme.filme.get_show_id()+" no arquivo " + filme.peso % qntCaminhos);
        } // end while

    } // end distribuicaoPeloHeap ()

    private static void descobrirTamanhoDeCadaSegmento(int [] tamDoSegmento, Entrada [] entrada) throws Exception {
        Filme filme = null;
        Filme proxFilme = null;

     // salva as posicoes iniciais de todas as entradas para voltar depois
        long [] posIniciais = new long[entrada.length];
        for (int x = 0; x < entrada.length; x++) posIniciais[x] = entrada[x].arquivo.getPos();

     // define o tamanho dos segmentos de todas as entradas com o tamanho inicial dos segmentos
        for (int i = 0; i < tamDoSegmento.length; i++) tamDoSegmento[i] = 1;

     // repeticao para chegar no fim do primeiro segmento
        int tam = -1;
        for (int x = 0; x < entrada.length; x++) {
            do {
                if( !entrada[x].arquivo.chegouNoFimDoArquivo()) {
                 // pega ultimo filme do segmento ordenado atual
                    tam = entrada[x].arquivo.lerInt();
                    filme = new Filme(entrada[x].arquivo.lerRegistro(tam));
                } // end if

                if( !entrada[x].arquivo.chegouNoFimDoArquivo() ) {
                 // pega o primeiro filme do prox segmento ordenado
                    tam = entrada[x].arquivo.lerInt();
                    proxFilme = new Filme(entrada[x].arquivo.lerRegistro(tam));

                    entrada[x].arquivo.irParaPos(entrada[x].arquivo.getPos() - tam - Integer.BYTES);

                 // se o primeiro do prox segmento for maior que o ultimo do segmento atual
                    if (filme.get_show_id() < proxFilme.get_show_id()) {
                        // System.out.println(filme.get_show_id() + " < " + proxFilme.get_show_id());
                    // aumenta o tamanho do segmento dessa entrada
                        tamDoSegmento[x]++;
                    } else {
                        // System.out.println(filme.get_show_id() + " > " + proxFilme.get_show_id());
                        break;
                    } // end if
                } // end if
            } while (!entrada[x].arquivo.chegouNoFimDoArquivo());
        } // end for

     // volta para o inicio do segmento
        for (int i = 0; i < entrada.length; i++) entrada[i].arquivo.irParaPos(posIniciais[i]);
        // for (int i = 0; i < tamDoSegmento.length; i++) System.out.println("tamSeg ["+i+"] = " + tamDoSegmento[i]);

    } // end descobrirTamanhoDeCadaSegmento ()

    private static boolean intercalacaoSelecaoPorSubstituicao(Arquivo [] temps, int ocorrencia) throws Exception {
        Arquivo.todosVaoParaPos(temps, 0);
        final int qntCaminhos = 2;

        int indexDoArquivoLido = -1;

        Entrada [] entrada = new Entrada[qntCaminhos];
        Arquivo [] saida = new Arquivo[qntCaminhos];
        int indexSaida = 0;   

        int [] tamDoSegmento = new int [qntCaminhos];
        for (int i = 0; i < tamDoSegmento.length; i++) tamDoSegmento[i] = 0;

     // atribuicao de entradas e saidas de acordo com a ocorrencia
        definirEntradaESaida(entrada, saida, temps, ocorrencia, qntCaminhos);
        
     // repeticao para alternar entre os arquivos de saida
        while(indexSaida < saida.length) {
                
                // System.out.println("\nGravação no arquivo de saida " + indexSaida);
                descobrirTamanhoDeCadaSegmento( tamDoSegmento, entrada);

             // leitura inicial
                for (int i = 0; i < entrada.length; i++) {
                    entrada[i].leituraInicial( i);
                } // end for

             // enquanto tiver alguma entrada com leitura valida
                while(temArquivoValido(entrada)) {

                 // pegar o indice do arquivo que contem o menor filme (id) valido
                    indexDoArquivoLido = getIndiceDaMenorChaveValida(entrada);

                 // escrever registro no arquivo de saida selecionado
                    saida[indexSaida].escreverRegistro(entrada[indexDoArquivoLido].filmes.toByteArray());
                    // System.out.println("\n\t\t\tESCREVEU " + entrada[indexDoArquivoLido].filmes.get_show_id() + " NO ARQUIVO DE SAIDA");
                    
                 // tenta ler de onde o indice foi retirado
                    entrada[indexDoArquivoLido].leitura(tamDoSegmento[indexDoArquivoLido], indexDoArquivoLido);
                    // if(!temArquivoValido(entrada)) System.out.println("\t\tNAO TEM MAIS ARQUIVOS VALIDOS PARA LEITURA");

                } // end while

             // permite novamente que leia qntRegitros vezes
                for (int i = 0; i < entrada.length; i++) entrada[i].leu = 0;

             // controle da repeticao 
                if(temRegistroPraLer(entrada)) {
                    indexSaida = (indexSaida+1)%saida.length;
                } else {
                    indexSaida++;
                } // end if

        } // end while

     // para a quantidade de vezes que a intercalacao tem que ser feita
        boolean resp = true;

     // se tudo estiver em um arquivo só
        if(saida[1].tamanhoDoArquivo() == 0) {
         // escreve o banco de dados
            escreverBDAPartirDeTemp(saida[0]);
         // booleano é retornado falso e cancela a prox intercalacao
            resp = false;
        } // end if

     // limpa os arquivos de entrada antes da prox intercalacao
     // OBS: esses dados ja foram gravados nos de saida, que serao a entrada da prox intercalacao
        for (int i = 0; i < entrada.length; i++) entrada[i].arquivo.limpar();
        
        return resp;
    } // end intercalacaoSelecaoPorSubstituicao ()

    /**
     * 
     * @param qntRegistros - tamanho dos segmentos iniciais
     * @param qntCaminhos - quantidade de caminhos
     * @param temps - arquivos temporarios criados anteriormente
     * @throws Exception
     */
    private static void intercalacoesSelecaoPorSubstituicao (Arquivo [] temps) throws Exception {
        int i = 0;
        while(intercalacaoSelecaoPorSubstituicao(temps, i)) {
            i++;
        } // end while
    } // end intercalacoesSelecaoPorSubstituicao ()

 // TODO
    public static void iBComSelecaoPorSubstituicao() throws Exception {
        final int qntCaminhos = 2;
        Arquivo bd = new Arquivo("database/filmes.db");
        Arquivo [] temps = new Arquivo [qntCaminhos*2];

     // criacao dos arquivos temporarios
        File [] tempF = new File [qntCaminhos*2];
        for (int i = 0; i < qntCaminhos*2; i++) {
            temps[i] = new Arquivo("Arqtemp"+ i + ".tmp");
            tempF[i] = new File("Arqtemp" + i + ".tmp");
        } // end for

        distribuicaoPeloHeap(bd, temps);
        intercalacoesSelecaoPorSubstituicao(temps);

        Arquivo.fecharArquivos(temps);
        
     // deletando arquivos temp
        for (int i = 0; i < tempF.length; i++) tempF[i].delete(); 

        bd.fechar();
    } // end iBComComSelecaoPorSubstituicao ()

} // end class IntercalacaoBalanceada
