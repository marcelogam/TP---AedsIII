package filme;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Filme implements Comparable<Filme>{

    private int show_id; 
    private char type;
    private String title;
    private String [] director;
    private String [] cast; 
    private String [] country; 
    private Date date_added; 
    private short release_year; 
    private String rating;
    private String duration;
    private String [] listed_in;
    private String description;

    public Filme () {
        this(-1, 'N', "", new String [0], new String [0], new String [0], new Date(), (short) -1, "", "", new String [0], "");
    } // end constructor

    public Filme (byte [] ba) throws Exception {
        this.date_added = new Date();
        this.fromByteArray(ba);
    } // end constructor

    public Filme (int show_id, char type, String title, String [] director, String [] cast, String [] country, Date date_added, short release_year, String rating, String duration, String [] listed_in, String description){
        this.show_id = show_id;
        this.type = type;
        this.title = title;
        this.director = director;
        this.cast = cast;
        this.country = country;
        this.date_added = date_added;
        this.release_year = release_year;
        this.rating = rating;
        this.duration = duration;
        this.listed_in = listed_in;
        this.description = description;
    } // end constructor

    public int get_show_id(){
        return this.show_id;
    } // end get_show_id ()

    public char  get_type(){
        return this.type;
    } // end get_type ()

    public String get_title(){
        return this.title;
    } // end get_title ()

    public String [] get_director(){
        return this.director;
    } // end get_director ()

    public String [] get_cast(){
        return this.cast;
    } // end get_cast ()

    public String [] get_country(){
        return this.country;
    } // end get_country ()

    public Date get_date_added(){
        return this.date_added;
    } // end get_date_added ()

    public short get_release_year(){
        return this.release_year;
    } // end get_release_year ()

    public String get_rating(){
        return this.rating;
    } // end get_rating ()

    public String get_duration(){
        return this.duration;
    } // end get_duration ()

    public String [] get_listed_in(){
        return this.listed_in;
    } // end get_listed_in ()

    public String get_description(){
        return this.description;
    } // end get_description ()

    public void set_show_id(int show_id){
        this.show_id = show_id;
    } // end set_show_id ()
    
    public void set_type(char type){
        this.type = type;
    } // end set_type ()
    
    public void set_title(String title){
        this.title = title;
    } // end set_title ()
    
    public void set_director(String [] director){
        this.director = director;
    } // end set_director ()
    
    public void set_cast(String [] cast){
        this.cast = cast;
    } // end set_cast ()
    
    public void set_country(String [] country){
        this.country = country;
    } // end set_country ()
    
    public void set_date_added(Date date_added){
        this.date_added = date_added;
    } // end set_date_added ()
    
    public void set_release_year(short release_year){
        this.release_year = release_year;
    } // end set_release_year ()
    
    public void set_rating(String rating){
        this.rating = rating;
    } // end set_rating ()
    
    public void set_duration(String duration){
        this.duration = duration;
    } // end set_duration ()
    
    public void set_listed_in(String []listed_in){
        this.listed_in = listed_in;
    } // end set_listed_in ()
    
    public void set_description(String description){
        this.description = description;
    } // end set_description ()

    /**
     * <b>readUTFarray</b> - recebe um <code>DataInputStream</code> e le um array de <code>String</code>
     * @param dis - <code>DataInputStream</code> para leitura
     * @return <code>String []</code> com as <code>String</code> extraidas do <code>dis</code>
     * @throws IOException
     */
    public static String [] readUTFarray (DataInputStream dis) throws IOException {
     // ler quantidade de Strings
        int tam = dis.readInt();

     // cria o array de String a partir dessa quantidade
        String [] sarr = new String [tam];

     // le a quantidade de String necessaria
        for (int i = 0; i < tam; i++) {
            sarr[i] = dis.readUTF();
        } // end for
        return sarr;
    } // end readUTFarray ()

    /**
     * <b>writeUTFarray</b> - recebe um <code>String []</code> e escreve no <code>dos</code> o tamanho desse array seguido das strings contidas nele
     * @param sarr - <code>String []</code>
     * @param dos - <code>DataOutputStream</code> para escrita
     * @throws IOException 
     */
    public static void writeUTFarray (String [] sarr, DataOutputStream dos) throws IOException {

     // escreve a quantidade de Strings que vao ser escritas
        dos.writeInt(sarr.length);

     // escreve as Strings
        for (int i = 0; i < sarr.length; i++) {
            dos.writeUTF(sarr[i]);
        } // end for

    } // end writeUTFarray ()

    /**
     * fromByteArray - recebe um <code>byte []</code> e atribui as informações nesse filme
     * @param barr - <code>byte []</code> com informações sobre um filme
     * @throws Exception
     */
    public void fromByteArray ( byte [] barr ) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(barr);
            DataInputStream dis = new DataInputStream(bais);
            this.show_id      = dis.readInt();
            this.type         = dis.readChar();
            this.title        = dis.readUTF();
            this.director     = readUTFarray(dis);
            this.cast         = readUTFarray(dis);
            this.country      = readUTFarray(dis);
            this.date_added.setTime(dis.readLong());
            this.release_year = dis.readShort();
            this.rating       = dis.readUTF();
            this.duration     = dis.readUTF();
            this.listed_in    = readUTFarray(dis);
            this.description  = dis.readUTF();
            dis.close();
            bais.close();
    } // end fromByteArray ()

    /**
     * toByteArray - escreve um <code>byte []</code> com as informações contidas nesse filme
     * @return <code>byte []</code>
     * @throws Exception
     */
    public byte [] toByteArray () throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.show_id);
        dos.writeChar(this.type);
        dos.writeUTF(this.title);
        writeUTFarray(this.director, dos);
        writeUTFarray(this.cast, dos);
        writeUTFarray(this.country, dos);
        dos.writeLong(this.date_added.getTime());
        dos.writeShort(release_year);
        dos.writeUTF(this.rating);
        dos.writeUTF(this.duration);
        writeUTFarray(this.listed_in, dos);
        dos.writeUTF(this.description);

        byte [] barr = baos.toByteArray();
        baos.close();
        dos.close();

        return barr;
    } // end toByteArray ()

    /**
     * <b>stringArrayToString</b> - recebe um <code>String []</code> e retorna uma String representando esse array
     * @param sarr - <code>String []</code>
     * @return <code>String</code>
     */
    public static String stringArrayToString (String [] sarr) {
        String s = "{ ";
        for (int i = 0; i < sarr.length; i++) {
            s += sarr[i];
            if (i != sarr.length -1 ) s += ", ";
        } // end for
        s += " }";
        return s;
    } // end stringArrayToString ()


    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        String s = "id = " + this.show_id + '\n';
        s += "title = " + this.title + '\n';
        s += "director = " + stringArrayToString(this.director) + '\n';
        s += "cast = " + stringArrayToString(this.cast) + '\n';
        s += "country = " + stringArrayToString(this.country) + '\n';
        s += "date_added = " + sdf.format(this.date_added) + '\n';
        s += "release_year = " + this.release_year + '\n';
        s += "rating = " + this.rating + '\n';
        s += "duration = " + this.duration + '\n';
        s += "listed_in = " + stringArrayToString(this.listed_in) + '\n';
        s += "description = " + this.description + '\n';
        return s;
    } // end toString()

    @Override
    public int compareTo(Filme o) {
        if (this.show_id > o.show_id) {
            return 1;
        } else if (this.show_id < o.show_id) {
            return -1;
        } else {
            return 0;
        } // end if
    } // end compareTo

} // end class Filme
