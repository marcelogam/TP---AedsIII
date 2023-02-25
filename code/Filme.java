import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.util.Date;

public class Filme {

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

    public static String [] readUTFarray (DataInputStream dis) throws IOException {
        int tam = dis.readInt();
        String [] sarr = new String [tam];
        for (int i = 0; i < tam; i++) {
            sarr[i] = dis.readUTF();
        } // end for
        return sarr;
    } // end readUTFarray ()

    public static void writeUTFarray (String [] sarr, DataOutputStream dos) throws IOException {
        dos.writeInt(sarr.length);
        for (int i = 0; i < sarr.length; i++) {
            dos.writeUTF(sarr[i]);
        } // end for
    } // end writeUTFarray ()

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
        String s = "id = " + this.show_id + '\n';
        s += "title = " + this.title + '\n';
        s += "director = " + stringArrayToString(this.director) + '\n';
        s += "cast = " + stringArrayToString(this.cast) + '\n';
        s += "country = " + stringArrayToString(this.country) + '\n';
        s += "date_added = " + this.date_added + '\n';
        s += "release_year = " + this.release_year + '\n';
        s += "rating = " + this.rating + '\n';
        s += "duration = " + this.duration + '\n';
        s += "listed_in = " + stringArrayToString(this.listed_in) + '\n';
        s += "description = " + this.description + '\n';
        return s;
    } // end toString()

} // end Filme
