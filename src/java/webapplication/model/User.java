package webapplication.model;

public class User {
    
    private int id;
    private String nazwisko, imie;
    
    public User(int id,String nazwisko,String imie){
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getNazwisko(){
        return this.nazwisko;
    }
    
    public String getImie(){
        return this.imie;
    }
    
}
