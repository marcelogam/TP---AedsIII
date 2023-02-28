package LeitorCSV;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;
import ClasseFilme.Filme;

public class LeitorCSV {

    /**
     * <b>readString</b> - recebe uma <code>String</code> registro de formato csv e retorna uma <code>String</code>
     * @param registro - String registro em formato csv
     * @param p - int para fazer atualizacao do registro
     * @return String
     */
    public static String readString( String registro , int [] p) {
        int i = p[0] + 1;
        String s = "";
     // se nao for nulo
        if(registro.charAt(p[0] + 1) != ',') {
         // se tiver ',' no meio da String
            if(registro.charAt(p[0] + 1) == '"') {
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
        int i = p[0] + 1; // apontador
        int itmp = 0;     // para salvar valor de i
        int qntVirg = 0;  // contar as virgulas para saber o tamanho da string
        String temp = ""; // string temporaria
        String [] s = new String [0];
     // se nao for nulo
        if(registro.charAt(p[0] + 1) != ',') {
         // se for um array
            if(registro.charAt(p[0] + 1) == '"') {
                itmp = ++i;
             // descobrir quantidade de virgulas
                while(registro.charAt(i) != '"') {
                    if(registro.charAt(i+1) == '"' && registro.charAt(i+2) == '"')  {
                        i+=2;
                    } // end if
                    if(registro.charAt(i) == ',') qntVirg++;
                    i++;
                } // end while

             // voltando com ponteiro e criando String []
                i = itmp;
                s = new String [qntVirg+1];

             // populando o array de String com ""
                for (int j = 0; j < qntVirg + 1; j++) {
                    s[j] = "";
                } // end for

             // ler até o prox '"' finalizador e dividir em (qntVir + 1) posicoes
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
        if      (mes.charAt(0) == 'J' && mes.charAt(1) == 'a') result = 0;
        else if (mes.charAt(0) == 'F') result = 1;
        else if (mes.charAt(0) == 'M' && mes.charAt(2) == 'r') result = 2;
        else if (mes.charAt(0) == 'A') result = 3;
        else if (mes.charAt(0) == 'M') result = 4;
        else if (mes.charAt(0) == 'J' && mes.charAt(2) == 'n') result = 5;
        else if (mes.charAt(0) == 'J') result = 6;
        else if (mes.charAt(0) == 'A') result = 7;
        else if (mes.charAt(0) == 'S') result = 8;
        else if (mes.charAt(0) == 'O') result = 9;
        else if (mes.charAt(0) == 'N') result = 10;
        else if (mes.charAt(0) == 'D') result = 11;
        return result;
    } // end parseMonth()
    
    /**
     * 
     * @param registro
     * @param p
     * @return
     */
    public static Date readDate (String registro, int [] p) {
        int mes = -1;
        int dia = -1;
        int ano = -1;
        String temp = "";
        int i = p[0] + 1;

     // se não for nulo
        if(registro.charAt( i ) != ',') {

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
        Date date = cal.getTime();
        p[0] = i + 1;
        return date;
    } // end readDate ()

    /**
     * <b>iniciarBdPeloCSV</b> - inicia o banco de dados filme a partir do arquivo CSV netflix_titles.csv
     * @throws Exception
     */
    public static void iniciarBdPeloCSV(String arquivo) throws Exception {

     // Deletar o arquivo para recomeçar
        File arq = new File(arquivo);
        arq.delete();

     // Objeto leitor do csv
        BufferedReader freader = new BufferedReader(new FileReader(new File("./netflix_csv/netflix_titles.csv")));
     // alterações feitas no csv para permitir leitura concreta
     // s8202 -> remoção do '\n' da parte da descrição
     // s8420 -> troca de '\n' no titulo por ' '

     // Objeto que permite ler e escrever aleatoriamente no arquivo
        RandomAccessFile ras = new RandomAccessFile(arquivo, "rw");

        int qntReg = 0;

        ras.writeInt(qntReg);

        freader.readLine(); // pular linha de descricao do arquivo
        
        String registro;  // salva o registro do csv como string
        String temp = ""; // para manipulacao de string
        Filme filme;      // para salvar filme temporario
        while ((registro = freader.readLine()) != null) {

            filme = new Filme(); // cria instancia temporaria para gravação
            registro += '\n';    // fechar registro com '\n'

         // apontadores para o registro
            int i = 1;        
            int [] p = new int [1];
            
         // leitura do id (até ',')
            while (registro.charAt(i) != ',') temp += registro.charAt(i++);
            filme.set_show_id(Integer.parseInt(temp));
            temp = "";

         // leitura do tipo
         // salva o primeiro char como o tipo (M ou T)
            filme.set_type(registro.charAt(++i));

         // ajustar ponteiro
            if (filme.get_type() == 'M') i += 5; else i += 7;
            p[0] = i;

         // ultimas leituras
            filme.set_title(readString(registro, p));
            filme.set_director(readStringArr(registro, p));
            filme.set_cast(readStringArr(registro, p));
            filme.set_country(readStringArr(registro, p));
            filme.set_date_added(readDate(registro, p));
            filme.set_release_year(Short.parseShort(registro.substring(p[0], (p[0] = p[0] + 4))));
            filme.set_rating(readString(registro, p));
            filme.set_duration(readString(registro, p));
            filme.set_listed_in(readStringArr(registro, p));
            filme.set_description(readString(registro, p));

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
    } // end iniciarBdPeloCSV ()

} // end class LeitorCSV
