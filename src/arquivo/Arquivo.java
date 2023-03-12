package arquivo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Arquivo {

 // constantes
    public static final int TAMANHO_INT = 4;

 // atributos do objeto
    private RandomAccessFile ras;

    public Arquivo (String nome) throws FileNotFoundException {
        ras = new RandomAccessFile( nome, "rw");
    } // end constructor

    public RandomAccessFile get_ras () {
        return this.ras;
    } // end get_ras ()

    public long tamanhoDoArquivo () throws IOException {
        return this.ras.length();
    } // end tamanhnoDoArquivo ()

    public void escreverRegistro ( byte [] ba ) throws IOException {
        this.ras.writeInt(ba.length);
        this.ras.write(ba);
    } // end escreverRegistro ()

    public void escreverLapide ( boolean lapide ) throws IOException {
        this.ras.writeBoolean(lapide);
    } // end escreverLapide ()

    public void escreverInt ( int x ) throws IOException {
        this.ras.writeInt(x);
    } // end escreverInt ()

    public boolean lerLapide () throws IOException {
        return this.ras.readBoolean();
    } // end lerLapide ()

    public int lerInt () throws IOException {
        return this.ras.readInt();
    } // end lerInt ()

    public byte [] lerRegistro (int tam) throws IOException {
        byte [] ba = new byte [tam];
        this.ras.read(ba);
        return ba;
    } // end ler ()

    public void pularNBytes( int n) throws IOException {
        this.ras.skipBytes(n);
    } // end pularNBytes ()

    public void irParaPos(long pos) throws IOException {
        this.ras.seek(pos);
    } // end pularNBytes ()

    public void fechar() throws IOException {
        this.ras.close();
    } // end fechar ()

    public static void fecharArquivos(Arquivo [] arqs) throws IOException {
        for (int i = 0; i < arqs.length; i++) arqs[i].fechar();
    } // end fecharArquivos ()

    public static void todosVaoParaPos(Arquivo [] arqs, int pos) throws IOException {
        for (int i = 0; i < arqs.length; i++) arqs[i].irParaPos(pos);
    } // end irParaPos ()

    public void limpar () throws IOException {
        this.ras.setLength(0);
    } // end limpar ()

    public long getPos () throws IOException {
        return this.ras.getFilePointer();
    } // end getPos ()

    public boolean vazio () throws IOException {
        return (ras.length() == 0) ? true : false;
    } // end vazio ()

    public boolean chegouNoFimDoArquivo () throws IOException {
        return (ras.getFilePointer() < ras.length()) ? false : true;
    } // end chegouNoFimDoArquivo

} // end class Arquivo
