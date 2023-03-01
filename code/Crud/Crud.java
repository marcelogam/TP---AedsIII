package Crud;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import ClasseFilme.Filme;

public class Crud {
    private RandomAccessFile ras;

    public Crud (String arquivo) throws FileNotFoundException {
        this.ras = new RandomAccessFile(arquivo, "rw");
    } // end constructor

    public int create (Filme filme) throws Exception {
        if(filme == null) filme = new Filme();
         // mover o ponteiro para o comeco do arquivo
            ras.seek(0);
         // ler quantidade de ids
            int qntIds = ras.readInt();
         // atualizar id
            filme.set_show_id(qntIds + 1);
         // mover o ponteiro para o comeco do arquivo
            ras.seek(0);
         // escrever quantidade de ids nova
            ras.writeInt( qntIds + 1 );
         // mover o ponteiro para o fim do arquivo
            ras.seek(ras.length()); 
         // escrever lapide
            ras.writeBoolean(true);
         // escrever tamanho do byte
            ras.writeInt(filme.toByteArray().length);
         // escrever registro
            ras.write(filme.toByteArray());
            return filme.get_show_id();
    } // end create ()

    public Filme read (int ID) throws Exception {
        boolean lapide = false;
        int tamRegistro = 0;
     // posicionar o ponteiro no comeco do arquivo depois do cabecalho
        ras.seek(4); // 1 int

     // enquanto nao atingir o fim do arquivo // tamRegistro + 5 -> tamanho do registro + 1 booleano (lapide) + 1 int (tamanho)
        for (long i = 4; i < ras.length(); i += tamRegistro + 5) {

         // leitura da lapide
            lapide = ras.readBoolean();

         // leitura do tamanho
            tamRegistro = ras.readInt();

         // criacao do filme pelo byte array
            byte [] ba = new byte [tamRegistro];
            ras.read(ba);
            Filme filme = new Filme(ba);

         // se lapide for verdadeira
            if(lapide == true)
             // se o id for igual ao pesquisado
                if(filme.get_show_id() == ID) {
                    return filme;
                } // end if
        } // end for
        return null;
    } // end read ()

    public boolean update ( Filme novo ) throws Exception {
        boolean lapide = false;
        int tamRegistro = 0;
     // posicionar o ponteiro no comeco do arquivo depois do cabecalho
        ras.seek(4); // 1 int

     // enquanto nao atingir o fim do arquivo // tamRegistro + 5 -> tamanho do registro + 1 booleano (lapide) + 1 int (tamanho)
        for (long i = 4; i < ras.length(); i += tamRegistro + 5) {

         // salvar a posicao inicial
            long posInicial = ras.getFilePointer();

         // leitura da lapide
            lapide = ras.readBoolean();

         // leitura do tamanho
            tamRegistro = ras.readInt();

         // criacao do filme pelo byte array
            byte [] ba = new byte [tamRegistro];
            ras.read(ba);
            Filme filme = new Filme(ba);

         // se lapide verdadeira
            if(lapide == true)

             // se o filme tiver o mesmo id do novo filme
                if(filme.get_show_id() == novo.get_show_id()) {

                 // criacao de novo registro
                    byte [] baNovo = novo.toByteArray();

                 // se o tamanho do novo registro for igual ao antigo
                    if(baNovo.length <= tamRegistro) {
                        ras.seek(posInicial + 5);
                        ras.write(baNovo);
                        return true;
                    } else {
                        ras.seek(posInicial);
                        ras.writeBoolean(false);
                        this.create(novo);
                        return true;
                    } // end if
                } // end if
        } // end for
        return false;
    } // end update ()

    public boolean delete ( int ID ) throws Exception {
        boolean lapide = false;
        int tamRegistro = 0;
     // posicionar o ponteiro no comeco do arquivo depois do cabecalho
        ras.seek(4); // 1 int

     // enquanto nao atingir o fim do arquivo // tamRegistro + 5 -> tamanho do registro + 1 booleano (lapide) + 1 int (tamanho)
        for (long i = 4; i < ras.length(); i += tamRegistro + 5) {

         // salvar a posicao inicial
            long posInicial = ras.getFilePointer();

         // leitura da lapide
            lapide = ras.readBoolean();

         // leitura do tamanho
            tamRegistro = ras.readInt();

         // criacao do filme pelo byte array
            byte [] ba = new byte [tamRegistro];
            ras.read(ba);
            Filme filme = new Filme(ba);

         // se lapide verdadeira
            if(lapide == true)
             // se o id for igual ao pesquisado
                if(filme.get_show_id() == ID) {
                 // retorna o ponteiro para a lapide desse registro
                    ras.seek(posInicial);
                 // escreve por cima da lapide, ou seja exclusao logica
                    ras.writeBoolean(false);
                    return true;
                } // end if
        } // end for
        return false;
    } // end delete ()
    
} // end class Crud
