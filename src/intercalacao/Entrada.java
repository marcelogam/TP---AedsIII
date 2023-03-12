package intercalacao;

import arquivo.Arquivo;
import filme.Filme;

public class Entrada {

    public Arquivo arquivo;
    public Filme filmes;
    public boolean validoParaLeitura;
    public int leu;
    
    public Entrada (Arquivo arquivo, int qntRegistroNoArquivo, Filme filmes, boolean validoParaLeitura, int leu ) {
        this.arquivo = arquivo;
        this.filmes = filmes;
        this.validoParaLeitura = validoParaLeitura;
        this.leu = leu;
    } // end contructor

    public void leitura (int qntRegistros, int indexDoArquivoLido) throws  Exception {
        // System.out.println("\n\tLeituras no arquivo de entrada " + indexDoArquivoLido );
        int tam = -1;
        if(!arquivo.chegouNoFimDoArquivo()) { // se tiver algo pra ler no arquivo
            if(this.leu < qntRegistros) { // se ainda puder ler desse arquivo

                validoParaLeitura = true;
                tam = this.arquivo.lerInt();
                this.filmes = new Filme (arquivo.lerRegistro(tam));
                // System.out.println("\t\t\tleu registro (" + this.filmes.get_show_id() + ") do arquivo");

                leu++;
                // System.out.println("\t\t\tquantidade de vezes que ja leu desse arquivo = " + this.leu);

            } else {
                // System.out.println("\t\t\tNAO PODE MAIS LER DO ARQUIVO DE ENTRADA " + indexDoArquivoLido + " ja leu " + qntRegistros + " vezes");
                validoParaLeitura = false;
            } // end if
        } else {
            // System.out.println("\t\t\tNAO PODE MAIS LER DO ARQUIVO DE ENTRADA " + indexDoArquivoLido + " acabou o conteudo");
            validoParaLeitura = false;
        } // end if
    } // end leitura ()

    public void leituraInicial ( int i) throws  Exception {
        // System.out.println("\n\tLeituras no arquivo de entrada " + i );
        int tam = -1;
        if(!arquivo.chegouNoFimDoArquivo()) { // se tiver algo pra ler no arquivo
            validoParaLeitura = true;
            tam = this.arquivo.lerInt();
            this.filmes = new Filme (arquivo.lerRegistro(tam));
            // System.out.println("\t\t\tleu registro (" + this.filmes.get_show_id() + ") do arquivo");
            leu++;
            // System.out.println("\t\t\tquantidade de vezes que ja leu desse arquivo = " + this.leu);
        } else {
            // System.out.println("\t\t\tNAO PODE MAIS LER DO ARQUIVO DE ENTRADA " + i + " acabou o conteudo");
            validoParaLeitura = false;
        } // end if
    } // end leituraInicial ()

} // end class Entrada
