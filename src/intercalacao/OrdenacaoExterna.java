package intercalacao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import arquivo.Arquivo;
import filme.Filme;


public class OrdenacaoExterna {
  // BufferedReader para leituras do teclado
     public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));



 // ---------------------------------------------------------------- operacoes com Arquivos temporarios
 
     public static void mostrarArquivos (Arquivo [] temps) throws Exception {
         for (int i = 0; i < temps.length; i++) {

             temps[i].irParaPos(0);
 
             System.out.println("\nArqtemp" + i);
             while (true) {
                 int tam = 0;
                 try {
                     tam = temps[i].lerInt();
                 } catch (Exception e) {
                     break;
                 } // end try
                 byte [] ba = new byte [tam];
                 temps[i].get_ras().read(ba);
                 Filme temp = new Filme(ba);
                 System.out.println(temp.get_show_id() + " " + temp.get_title());
             } // end while
 
             // reader.readLine();
 
         } // end for
     } // end mostrarArquivos ()
 
    public static void escreverBDAPartirDeTemp (Arquivo temp) throws Exception {
        int tam = -1;
        int id = -1;
        int controle = 0;

        temp.irParaPos(0);

        while (controle < temp.tamanhoDoArquivo()) {
            tam = temp.lerInt();
            id = temp.lerInt();
            temp.pularNBytes(tam - 4);
            controle += (tam + 4);
        } // end while
        controle = 0;

        Arquivo bd = new Arquivo("database/filmes.db");
        bd.limpar();
        bd.irParaPos(0);
        bd.escreverInt(id);

        temp.irParaPos(0);
        while(controle < temp.tamanhoDoArquivo()) {
            bd.escreverLapide(false);
            tam = temp.lerInt();
            bd.escreverRegistro(temp.lerRegistro(tam));
            controle += (tam + 4);
        } // end while
    } // end escreverDBAPartirDeTemp ()

    protected static void criarArquivosTemporarios (int qntCaminhos, File [] tempF, Arquivo [] temps) throws FileNotFoundException {
        for (int i = 0; i < qntCaminhos * 2; i++) {
            temps[i] = new Arquivo("Arqtemp"+ i + ".tmp");
            tempF[i] = new File("Arqtemp" + i + ".tmp");
        } // end for
    } // endd criarArquivosTemporarios ()

    protected static void deletarTemporarios (File [] tempF) {
        for (int i = 0; i < tempF.length; i++) tempF[i].delete(); 
    } // end deletarTemporarios ()

 // ---------------------------------------------------------------- operacoes com Entradas

    protected static boolean temRegistroPraLer (Entrada [] entrada) throws IOException {
        boolean resp = false;
        for (int i = 0; i < entrada.length; i++) {
            if(!entrada[i].arquivo.chegouNoFimDoArquivo()) {
                resp = true;
                i = entrada.length;
            } // end if
        } // end for
        return resp;
    } // end temRegistroPraLer ()

    protected static boolean temRegistroPraLer (Arquivo [] entrada) throws IOException {
        boolean resp = false;
        for (int i = 0; i < entrada.length; i++) {
            if(!entrada[i].chegouNoFimDoArquivo()) {
                resp = true;
                i = entrada.length;
            } // end if
        } // end for
        return resp;
    } // end temRegistroPraLer ()

    protected static boolean temArquivoValido (Entrada [] entrada) {
        boolean resp = false;
        for (int i = 0; i < entrada.length; i++) {
            if (entrada[i].validoParaLeitura == true)  {
                resp = true;
                i = entrada.length;
            } // end if
        } // end for
        return resp;
    } // end temArquivoValido()

    protected static int getIndiceDaMenorChaveValida (Entrada [] entrada) {
        int menor = - 1;
        for (int i = 0; i < entrada.length; i++) {
            if(entrada[i].validoParaLeitura == true) {
                menor = i;
                i = entrada.length;
            } // end if
        } // end if

        if( menor != -1 ) {
            for (int i = menor; i < entrada.length; i++) {
                if(entrada[i].validoParaLeitura) {
                    if( entrada[menor].filmes.get_show_id() > entrada[i].filmes.get_show_id()) {
                        menor = i;
                    } // end if
                } // end if
            } // end for
        } // end if
        return menor;
    } // end getIndiceDaMenorChaveValida ()

 // Atribuicao dos arquivos que serao entradas ou saidas
    protected static void definirEntradaESaida(Entrada [] entrada, Arquivo [] saida, Arquivo [] temps, int ocorrencia, int qntCaminhos) {
        int i = 0;
        if((ocorrencia%2 == 0)) {
            for (int j = 0; j < qntCaminhos; j++, i++) {
                entrada[j] = new Entrada (temps[i], 0, null, false, 0);
            } // end for
            for (int j = 0; j < qntCaminhos; j++, i++) {
                saida[j] = temps[i];
            } // end for
        } else {
            for (int j = 0; j < qntCaminhos; j++, i++) {
                saida[j] = temps[i];
            } // end for
            for (int j = 0; j < qntCaminhos; j++, i++) {
                entrada[j] = new Entrada (temps[i], 0, null, false, 0);
            } // end for
        } // end if
    } // end definirEntradaESaida ()

    protected static void definirEntradaESaida(Arquivo [] entrada, Arquivo [] saida, Arquivo [] temps, int ocorrencia, int qntCaminhos) {
        int i = 0;
        if((ocorrencia%2 == 0)) {
            for (int j = 0; j < qntCaminhos; j++, i++) {
                entrada[j] = temps[i];
            } // end for
            for (int j = 0; j < qntCaminhos; j++, i++) {
                saida[j] = temps[i];
            } // end for
        } else {
            for (int j = 0; j < qntCaminhos; j++, i++) {
                saida[j] = temps[i];
            } // end for
            for (int j = 0; j < qntCaminhos; j++, i++) {
                entrada[j] = temps[i];
            } // end for
        } // end if
    } // end definirEntradaESaida ()

} // end class OrdenacaoExterna
