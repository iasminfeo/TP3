package TP2.Model;

import TP2.Interfaces.Registro;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class Serie implements Registro {
    protected int idSerie;
    protected String Nome;
    protected LocalDate AnoLancamento;
    protected String Sinopse;
    protected int SinopseSize;
    protected String Streaming;
    protected int QtdTemporada;
    protected int IDator;

        //construtor para passar os valores dos atributos
        public Serie(int i, String n, LocalDate a ,  String si,int SS, String st, int QtdTe, int IDat){
            idSerie = i;
            Nome = n;
            AnoLancamento = a;
            Sinopse = si;
            SinopseSize = SS;
            Streaming = st;
            QtdTemporada = QtdTe;
            IDator = IDat;
        }

        public Serie( String n, LocalDate a ,  String si,int SS, String st, int QtdTe, int IDat){
            this.idSerie = 0;
            Nome = n;
            AnoLancamento = a;
            Sinopse = si;
            SinopseSize = SS;
            Streaming = st;
            QtdTemporada = QtdTe;
            IDator = IDat;
        }

        public Serie() {
        idSerie = -1;
        Nome = "";
        AnoLancamento = null;
        Sinopse = "";
        SinopseSize = 0;
        Streaming = "";
        QtdTemporada = 0;
        IDator = 0;
        }

        public void setId(int id) {
            this.idSerie = id;
        }
    
        public int getId() {
            return idSerie;
        }
    
        public String getNome() {
            return Nome;
        }
    
        public void setNome(String Nome) {
            this.Nome = Nome;
        }
    
        public LocalDate getAnoLancamento() {
            return AnoLancamento;
        }
    
        public void setAnoLancamento(LocalDate AnoLancamento) {
            this.AnoLancamento = AnoLancamento;
        }
    
        public String getSinopse() {
            return Sinopse;
        }
    
        public void setSinopse(String Sinopse) {
            this.Sinopse = Sinopse;
        }
    
        public String getStreaming() {
            return Streaming;
        }
    
        public void setStreaming(String Streaming) {
            this.Streaming = Streaming;
        }

        public void setQtdTemporada(int QtdTe){
            this.QtdTemporada = QtdTe;
        }

        public int getQtdTemporada(){
            return QtdTemporada;
        }

        public void setIDator(int IDator){
            this.IDator = IDator;
        }

        public int getIDator(){
            return IDator;
        }

        public void SinopzeSize(int SS){
            this.SinopseSize = SS;
        }

        public int SinopseSize(){
            return SinopseSize;
        }
        


        //METODO QUE DESCREVE A SERIE POR MEIO DE UM VETOR DE BYTES
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(b);
        dos.writeInt(idSerie);
        dos.writeUTF(Nome);
        dos.writeInt((int) this.AnoLancamento.toEpochDay());
        dos.writeInt(SinopseSize);
        dos.writeUTF(Sinopse);
        dos.writeUTF(Streaming);
        dos.writeInt(QtdTemporada);
        dos.writeInt(IDator);

        return b.toByteArray();
    }

    //METODO INVERSO: LE DO ARQUIVO O VETOR DE BYTES E CARREGA O OBJ

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        idSerie = dis.readInt();
        Nome = dis.readUTF();
        AnoLancamento = LocalDate.ofEpochDay(dis.readInt());
        SinopseSize = dis.readInt();
        Sinopse = dis.readUTF();
        Streaming = dis.readUTF();
        QtdTemporada = dis.readInt();
        IDator = dis.readInt();
    }
}


