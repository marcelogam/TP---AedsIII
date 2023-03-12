package intercalacao;

import filme.Filme;

public class FilmeComPeso implements Comparable<FilmeComPeso> {
    public Filme filme;
    public int   peso;

    public FilmeComPeso (Filme filme, int peso) {
        this.filme = filme;
        this.peso = peso;
    } // end FilmeComPeso

    @Override
    public int compareTo(FilmeComPeso o) {
        if(this.peso > o.peso) {
            return 1;
        } else if (this.peso < o.peso) {
            return -1;
        } else if (this.filme.get_show_id() > o.filme.get_show_id()) {
            return 1;
        } else if (this.filme.get_show_id() < o.filme.get_show_id()) {
            return -1;
        } else {
            return 0;
        } // end if
    } // end compareTo ()
} // end class FilmeComPeso

