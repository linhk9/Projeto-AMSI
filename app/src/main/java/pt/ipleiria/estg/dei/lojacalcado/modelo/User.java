package pt.ipleiria.estg.dei.lojacalcado.modelo;

public class User {
    private int id, telemovel;
    private String username, email, primeiroNome, ultimoNome, morada;

    public User(int id, String username, String email, String primeiroNome, String ultimoNome, int telemovel, String morada) {
        this.id = id;
        this.telemovel = telemovel;
        this.username = username;
        this.email = email;
        this.primeiroNome = primeiroNome;
        this.ultimoNome = ultimoNome;
        this.morada = morada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(int telemovel) {
        this.telemovel = telemovel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }
}
