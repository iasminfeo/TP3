package TP2.Model;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import TP2.Interfaces.Registro;

public class Episodio implements Registro{
    protected int ID;
    protected String Nome;
    protected int temporada;
    protected LocalDate DataLancamento;
    protected long Duracao;
    protected int idSerie;
    protected int NumeroEpisodio;

    public Episodio(int i, String n, int t, LocalDate d, long du, int idSerie, int NumeroEpisodio){
        ID = i;
        Nome = n;
        temporada = t;
        DataLancamento = d;
        Duracao = du;
        this.idSerie = idSerie;
        this.NumeroEpisodio = NumeroEpisodio;
    }

    public Episodio( String n, int t, LocalDate d, long du, int idSerie, int NumeroEpisodio){
        this.ID = 0;
        Nome = n;
        temporada = t;
        DataLancamento = d;
        Duracao = du;
        this.idSerie = idSerie;
        this.NumeroEpisodio = NumeroEpisodio;
    }

    public Episodio( ){
        ID=-1;
        Nome = "";
        temporada = 0;
        LocalDate.now();
        Duracao = 0;
        idSerie = 0;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public int getId() {
        return ID;
    }

     public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public int getTemporada() {
        return temporada;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public LocalDate getDataLancamento() {
        return DataLancamento;
    }

    public void setDataLancamento(LocalDate DataLancamento) {
        this.DataLancamento = DataLancamento;
    }

    public long getDuracao() {
        return Duracao;
    }

    public void setDuracao(long Duracao) {
        this.Duracao = Duracao;
    }

    public void setIdSerie(int id) {
        this.idSerie = id;
    }

    public int getIdSerie() {
        return idSerie;
    }

    public void setNumeroEpisodio(int N){
        this.NumeroEpisodio = N;
    }

    public int getNumero(){
        return NumeroEpisodio;
    }
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(b);
        dos.writeInt(ID);
        dos.writeUTF(Nome);
        dos.writeInt(temporada);
        dos.writeInt((int) this.DataLancamento.toEpochDay());
        dos.writeLong(Duracao);
        dos.writeInt(idSerie);
        return b.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        Nome = dis.readUTF();
        temporada = dis.readInt();
        this.DataLancamento = LocalDate.ofEpochDay(dis.readInt());
        Duracao = dis.readLong();
        idSerie = dis.readInt();
    }

}

