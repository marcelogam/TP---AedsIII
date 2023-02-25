import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.Calendar;

class Main {

    /**
     * <b>readString</b> - recebe uma <code>String</code> registro de formato csv e retorna uma <code>String</code>
     * @param registro - String registro em formato csv
     * @param p - int para fazer atualizacao do registro
     * @return String
     */
    public static String readString( String registro , int [] p) {
        int i = 0;
        String s = "";
     // se nao for nulo
        if(registro.charAt(0) != ',') {
         // se tiver ',' no meio da String
            if(registro.charAt(0) == '"') {
                i++;
             // ler até o prox '"' finalizador
                while(registro.charAt(i) != '"') {
                 // tratando caso (""vulgo"") no meio da String
                    if(registro.charAt(i+1) == '"' && registro.charAt(i+2) == '"')  {
                        s+= "\"";
                        i+=2;
                    } // end if
                    s += registro.charAt(i);
                    i++;
                } // end while
                i++;
         // se for uma String comum
            } else {
                // ler até o prox ','
                while (registro.charAt(i) != ',' && registro.charAt(i) != '\n') {
                    s += registro.charAt(i);
                    i++;
                } // end while
            } // end if
        } // end if
        p[0] = i;
        return s;
    } // end readString ()


    /**
     * <b>readStringArr</b> - recebe uma <code>String</code> registro de formato csv e retorna um array
     * <p><b>Exemplo de registro:</b></p> <p><b>Melhor caso:</b> <code>Teste Nome</code></p>
     * <p><b>Pior caso:</b> <code>"Issey Takahashi, Yuu Aoi, Kenta ""vulgo"" Hamano, Toko Miura, ""Koji Ohkura"", Pierre Taki, Eri Watanabe, Kitaro"</code></p>
     * @param registro - String registro em formato csv
     * @param p - int para fazer atualizacao do registro
     * @return <code>String []</code>
     */
    public static String [] readStringArr( String registro , int [] p ) {
        int i = 0;
        int qntVirg = 0;
        String temp = "";
        String [] s = new String [0];
     // se nao for nulo
        if(registro.charAt(0) != ',') {
         // se for um array
            if(registro.charAt(0) == '"') {
                i++;

             // descobrir qnt de virgulas
                while(registro.charAt(i) != '"') {
                    if(registro.charAt(i+1) == '"' && registro.charAt(i+2) == '"')  {
                        i+=2;
                    } // end if
                    if(registro.charAt(i) == ',') qntVirg++;
                    i++;
                } // end while

                i = 1;
                s = new String [qntVirg+1];

             // populando o array de String com ""
                for (int j = 0; j < qntVirg + 1; j++) {
                    s[j] = "";
                } // end for

             // ler até o prox '"' finalizador e divifir em (qntVir + 1) posicoes
                for (int x = 0; x < qntVirg + 1; x++) {

                    while(registro.charAt(i) != '"' && registro.charAt(i) != ',') {
                     // tratando caso (""vulgo"") no meio da String
                        if(registro.charAt(i+1) == '"' && registro.charAt(i+2) == '"')  {
                            temp+= "\"";
                            i+=2;
                        } // end if
                        temp += registro.charAt(i);
                        i++;
                    } // end while
                    s[x] = temp.strip();
                    temp = "";
                    i++;
                } // end for

         // se for apenas umas String
            } else {
                s = new String [1];
                s[0] = "";
             // ler até o prox ','
                while (registro.charAt(i) != ',') {
                    s[0] += registro.charAt(i);
                    i++;
                } // end while
            } // end if
        } // end if
        p[0] = i;
        return s;
    } // end readStringArr ()

    /**
     * <b>parseMonth</b> - converte um mes para numero.
     * @param mes - <code>String</code> com o mês para conversão
     * @return o numero do mes equivalente ou -1 caso não encontre o mes
     */
    public static int parseMonth (String mes) {
        int result = 0;
        switch (mes) {
            case "Jan": result = 0 ; break;
            case "Feb": result = 1 ; break;
            case "Mar": result = 2 ; break;
            case "Apr": result = 3 ; break;
            case "May": result = 4 ; break;
            case "Jun": result = 5 ; break;
            case "Jul": result = 6 ; break;
            case "Aug": result = 7 ; break;
            case "Sep": result = 8 ; break;
            case "Oct": result = 9; break;
            case "Nov": result = 10; break;
            case "Dec": result = 11; break;
            default:    result = -1; break;
        } // end switch
        return result;
    } // end parseMonth()

    /**
     * <b>initDataBaseFromCsv</b> - inicia o banco de dados filme a partir do arquivo CSV netflix_titles.csv
     * @throws Exception
     */
    private static void initDataBaseFromCsv() throws Exception {

     // Deletar o arquivo para recomeçar
        File arq = new File("filmes.db");
        arq.delete();

     // Objeto leitor do csv
        BufferedReader freader = new BufferedReader(new FileReader(new File("netflix_titles.csv")));
     // alterações feitas no csv para permitir leitura concreta
     // s8202 -> remoção do '\n' da parte da descrição
     // s8420 -> troca de '\n' no titulo por ' '

     // Objeto que permite ler e escrever aleatoriamente no arquivo
        RandomAccessFile ras = new RandomAccessFile("filmes.db", "rw");

        int qntReg = 0;

        ras.writeInt(qntReg);

        freader.readLine(); // pular linha de descricao do arquivo
        
        String registro;  // salva o registro do csv como string
        String temp = ""; // para manipulacao de string

        while ((registro = freader.readLine()) != null) {
            Filme filme = new Filme(); // instancia temporaria para gravação
            int i = 1;        // apontador para o registro
            int [] p = new int [1];  // para saber aonde parou o leitura e atualizar o registro 
            registro += '\n'; // fechar registro com '\n'

         // --------------------------------------------------------
         // extrair id (int)
         // --------------------------------------------------------

         // leitura do id (até ',')
            while (registro.charAt(i) != ',') {
                temp += registro.charAt(i);
                i++;
            } // end while
            filme.set_show_id(Integer.parseInt(temp));
            temp = "";
         // atualizar registro
            registro = registro.substring(i + 1, registro.length());
            i = 0;
            System.out.println(filme.get_show_id());
         // --------------------------------------------------------
         // extrair type (char)
         // --------------------------------------------------------

            i++;
         // salva o primeiro char como o tipo (M ou T)
            filme.set_type(registro.charAt(i));

         // ajusta até o fim do tipo
            while (registro.charAt(i) != ',') {
                i++;
            } // end while
         // atualizar registro
            registro = registro.substring(i + 1, registro.length());
            i = 0;

         // --------------------------------------------------------
         // extrair title (String)
         // --------------------------------------------------------

            filme.set_title(readString(registro, p));
         // atualizar registro
            registro = registro.substring(p[0] + 1, registro.length());
            temp = "";
            

         // --------------------------------------------------------
         // extrair director (String [])
         // --------------------------------------------------------

            filme.set_director(readStringArr(registro, p));
         // atualizar registro
            registro = registro.substring(p[0] + 1, registro.length());
            temp = "";
            

         // --------------------------------------------------------
         // extrair cast (String [])
         // --------------------------------------------------------

            filme.set_cast(readStringArr(registro, p));
         // atualizar registro
            registro = registro.substring(p[0] + 1, registro.length());
            temp = "";

         // --------------------------------------------------------
         // extrair country (String [])
         // --------------------------------------------------------

            filme.set_country(readStringArr(registro, p));
         // atualizar registro
            registro = registro.substring(p[0] + 1, registro.length());
            temp = "";
            
         // --------------------------------------------------------
         // extrair date_added (Date)
         // --------------------------------------------------------

            int mes = -1;
            int dia = -1;
            int ano = -1;
            
         // se não for nulo
            if(registro.charAt(0) != ',') {

             // mes
                i++;
                for (int j = 0; j < 3; j++) {
                    temp += registro.charAt(i);
                    i++;
                } // end for

             // converte mes para numero a partir das 3 primeiras letras do mes
                mes = parseMonth(temp);
                temp = "";

             // reposiciona o apontador i para comecar a ler o dia
                while (registro.charAt(i) != ' ') i++;
                i++;

            // dia
                while(registro.charAt(i) != ',') {
                    temp += registro.charAt(i);
                    i++;
                } // end while
                dia = Integer.parseInt(temp);
                temp = "";
                i+=2;

            // ano
                while(registro.charAt(i) != '"') {
                    temp += registro.charAt(i);
                    i++;
                } // end while
                ano = Integer.parseInt(temp);
                temp = "";
                i++;
            } // end if

         // criacao da instancia do tipo Date
            Calendar cal = Calendar.getInstance();
            cal.set(ano, mes, dia, 0, 0, 0);
            filme.set_date_added(cal.getTime());
         // atualizar registro
            registro = registro.substring(i + 1, registro.length());

         // --------------------------------------------------------
         // extrair release_year (short)
         // --------------------------------------------------------
            
            filme.set_release_year(Short.parseShort(temp = registro.substring(0, 4)));
         // atualizar registro
            registro = registro.substring(5, registro.length());
            temp = "";

         // --------------------------------------------------------
         // extrair rating (String)
         // --------------------------------------------------------

            filme.set_rating(readString(registro, p));
         // atualizar registro
            registro = registro.substring(p[0] + 1, registro.length());
            temp = "";


         // --------------------------------------------------------
         // extrair dutarion (String)
         // --------------------------------------------------------

            filme.set_duration(readString(registro, p));
         // atualizar registro
            registro = registro.substring(p[0] + 1, registro.length());
            temp = ""; 
            

         // --------------------------------------------------------
         // extrair listed_in (String[])
         // --------------------------------------------------------

            filme.set_listed_in(readStringArr(registro, p));
         // atualizar registro
            registro = registro.substring(p[0] + 1, registro.length());
            temp = "";

         // --------------------------------------------------------
         // extrair description (String)
         // --------------------------------------------------------

            filme.set_description(readString(registro, p));
         // atualizar registro
            registro = registro.substring(p[0] + 1, registro.length());
            temp = "";


         // escrever registro no arquivo
            byte [] barr = filme.toByteArray();
            ras.writeBoolean(true);    // escrever lapide
            ras.writeInt(barr.length); // escrever tamanho
            ras.write(barr);           // escrever registro

         // aumento da quantidade de registros
            qntReg++;

        } // end while

     // acessa o comeco do arquivo e escreve a quantidade de registros
        ras.seek(0);
        ras.writeInt(qntReg);
        
     // fechamento dos manipuladores de arquivo
        ras.close();
        freader.close();

    } // end initDataBaseFromCsv ()

    public static void lerTodoBD () throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream("filmes.db"));
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
        }
        dis.close();
    } // end lerTodoBD ()

    public static void main(String[] args) throws Exception {
        initDataBaseFromCsv();
        lerTodoBD(); // debug
        
        // TODO: create ()
        // TODO: read ()
        // TODO: update ()
        // TODO: delete ()
        // TODO: crud menu

    } // end main
} // end class Main