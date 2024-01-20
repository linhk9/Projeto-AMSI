package pt.ipleiria.estg.dei.lojacalcado.modelo;

import java.util.List;

public class Fatura {
    private int id;
    private int id_userdata;
    private String data;
    private List<FaturaLinha> faturaLinhas;

    public Fatura(int id, int id_userdata, String data, List<FaturaLinha> faturaLinhas) {
        this.id = id;
        this.id_userdata = id_userdata;
        this.data = data;
        this.faturaLinhas = faturaLinhas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_userdata() {
        return id_userdata;
    }

    public void setId_userdata(int id_userdata) {
        this.id_userdata = id_userdata;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<FaturaLinha> getFaturaLinhas() {
        return faturaLinhas;
    }

    public void setFaturaLinhas(List<FaturaLinha> faturaLinhas) {
        this.faturaLinhas = faturaLinhas;
    }

    public double getPrecoTotal() {
        double total = 0;
        for (FaturaLinha faturaLinha : faturaLinhas) {
            total += faturaLinha.getPrecoTotal();
        }
        return total;
    }

    public int getTotalProdutosComprados() {
        int total = 0;
        for (FaturaLinha faturaLinha : faturaLinhas) {
            total += faturaLinha.getQuantidade();
        }
        return total;
    }
}