package Service;


import Interfaces.RegistroArvoreBMais;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIDSerieAtor implements RegistroArvoreBMais<ParIDSerieAtor> {
    
    private int idSerie;
    private int idAtor;
    
    public ParIDSerieAtor() {
        this.idSerie = -1;
        this.idAtor = -1;
    }
    
    public ParIDSerieAtor(int idSerie, int idAtor) {
        this.idSerie = idSerie;
        this.idAtor = idAtor;
    }
    
    public int getIdSerie() {
        return this.idSerie;
    }
    
    public void setIdSerie(int idSerie) {
        this.idSerie = idSerie;
    }
    
    public int getIdAtor() {
        return this.idAtor;
    }
    
    public void setIdAtor(int idAtor) {
        this.idAtor = idAtor;
    }
    
    @Override
    public short size() {
        return 8; // 4 bytes para cada int (idSerie e idAtor)
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(idSerie);
        dos.writeInt(idAtor);
        
        return baos.toByteArray();
    }
    
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        this.idSerie = dis.readInt();
        this.idAtor = dis.readInt();
    }

    @Override
    public int compareTo(ParIDSerieAtor outro) {
        // Primeiro compara pelo idSerie
        if (this.idSerie != outro.idSerie) {
            return this.idSerie - outro.idSerie;
        }
        
        // Caso especial: se estamos buscando todos os episódios de uma série
        // usando idAtor = -1, consideramos que é menor que qualquer outro par
        if (this.idAtor == -1) {
            return -1; // Sempre menor que qualquer episódio real
        } else if (outro.getIdAtor() == -1) {
            return 1; // Sempre maior que o elemento de busca
        }
        
        // Caso normal: se idSerie for igual, compara pelo idAtor
        return this.idAtor - outro.idAtor;
    }


    @Override
    public ParIDSerieAtor clone() {
        return new ParIDSerieAtor(this.idSerie, this.idAtor);
    }
    
    @Override
    public String toString() {
        return "Serie: " + idSerie + ", Ator: " + idAtor;
    }

}
