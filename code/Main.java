import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Calendar;

import ClasseFilme.Filme;
import Crud.Crud;
import LeitorCSV.LeitorCSV;

class Main {

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

    public static void main(String[] args) throws Exception {
        LeitorCSV.iniciarBdPeloCSV("DataBase/filmes.db");
     // lerTodoBD(); // debug

        Crud arquivo = new Crud("DataBase/filmes.db");
        Calendar cal = Calendar.getInstance();
        arquivo.create(new Filme(0, 'T', "filme1", new String [0], new String [0], new String[0], cal.getTime(), (short)2002, "teste", "teste", new String [0], "teste"));
        arquivo.create(new Filme(0, 'M', "filme2", new String [0], new String [0], new String[0], cal.getTime(), (short)2002, "teste", "teste", new String [0], "teste"));

     // lerTodoBD();
        arquivo.create(null);
        System.out.println(arquivo.read(10));
        // TODO: read ()
        // TODO: update ()
        // TODO: delete ()
        // TODO: crud menu

        // TODO: ordenação

    } // end main ()
} // end class Main