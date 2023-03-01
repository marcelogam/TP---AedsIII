package IntercalacaoBalanceada;

public class IntercalacaoBalanceada {

    /**
     * <b>mostrarOpcoes</b> - mostra as opções do menu de IntercalacaoBalanceada
     */
    public static void mostrarOpcoes() {
        System.out.println();
        System.out.println("----------------------------------------------------------");
        System.out.println("Trabalho Pratico 1                                        ");
        System.out.println("netflix_titles                                            ");
        System.out.println("Intercalação Balanceada                                   ");
        System.out.println();
        System.out.println(" 1. Intercalação Balanceada                               ");
        System.out.println(" 2. Intercalação Balanceada                               ");
        System.out.println(" 3. Intercalação Balanceada                               ");
        System.out.println();
        System.out.println(" 0. Exit                                                  ");
        System.out.println("-1. Voltar                                                ");
        System.out.println("----------------------------------------------------------");
        System.out.print(  "Selecione uma opção: ");
    } // end mostrarOpcoes

    public static void menu () {
        int option = -2;
        do {
            mostrarOpcoes();
        } while (option != 0);
    } // end menu ()

    private static void iBComSelecaoPorSubstituicao() {

    } // end iBComSelecaoPorSubstituicao ()

    private static void iBComBlocosDeTamanhoVariavel() {

    } // end iBComBlocosDeTamanhoVariavel ()

    private static void iBComum() {

    } // end iBComum ()
    
} // end class IntercalacaoBalanceada
