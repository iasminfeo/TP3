package TP2.Model;

import TP2.Interfaces.Registro;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Ator implements Registro {
    protected int ID;
    protected String Nome;
    protected int IDSerie;
    protected char Genero;

    public Ator(){
        this.ID = 0;
        this.Nome = "";
        this.IDSerie = 0;
        this.Genero = ' ';
    }

    public Ator(int i,String n, int is, char g){
        this.ID = i;
        this.Nome = n;
        this.IDSerie = is;
        this.Genero = g;
    }
    
    public Ator(String n, int is, char g){
        this.ID = 0;
        this.Nome = n;
        this.IDSerie = is;
        this.Genero = g;
    }

    public int getId(){
        return ID;
    }

    public void setId(int i){
        this.ID = i;
    }

    public String getNome(){
        return Nome;
    }

    public void setNome(String n){
        this.Nome = n;
    }

    public char getGenero(){
        return Genero;
    }

    public void setGenero(char g){
        this.Genero = g;
    }

    public int getIDSerie(){
        return IDSerie;
    }

    public void setIDSerie(int i){
        this.IDSerie = i;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(b);
        dos.writeInt(ID);
        dos.writeUTF(Nome);
        dos.writeChar(Genero);
        dos.writeInt(IDSerie);
        
        return b.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        ID = dis.readInt();
        Nome = dis.readUTF();
        Genero = dis.readChar();
        IDSerie = dis.readInt();
    }

}
