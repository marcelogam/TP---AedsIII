package Crud;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import ClasseFilme.Filme;

public class Crud {
    private RandomAccessFile ras;

    public Crud (String arquivo) throws FileNotFoundException {
        this.ras = new RandomAccessFile(arquivo, "rw");
    } // end constructor

    public void create (Filme filme) throws Exception {
        if(filme != null) {
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
        } // end if
    } // end create ()

    public Filme read (int ID) throws Exception {
        boolean lapide = false;
        int tamRegistro = 0;
     // posicionar o ponteiro no comeco do arquivo depois do cabecalho
        ras.seek(5); // 1 booleano + 1 int = 5 bytes
     // enquanto nao atingir o fim do arquivo
        for (int i = 5; i < ras.length(); i += tamRegistro ) {
            lapide = ras.readBoolean();
            Filme filme = new Filme();
            tamRegistro = ras.readInt();
            byte [] ba = new byte [tamRegistro];
            ras.read(ba);
            filme.fromByteArray(ba);

            if(lapide == true)
                if(filme.get_show_id() == ID) {
                    return filme;
                } // end if
        } // end for
        return null;
    } // end read ()

    public void update () {
        
    } // end update ()

    public void delete () {
        
    } // end delete ()
    
} // end class Crud
