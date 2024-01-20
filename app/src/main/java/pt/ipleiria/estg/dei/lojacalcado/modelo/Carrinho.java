package pt.ipleiria.estg.dei.lojacalcado.modelo;

import java.util.List;

public class Carrinho {
    private int id, id_userdata;
    private String data;
    private List<CarrinhoLinha> carrinhoLinhas;

    public Carrinho(int id, int id_userdata, String data, List<CarrinhoLinha> carrinhoLinhas) {
        this.id = id;
        this.id_userdata = id_userdata;
        this.data = data;
        this.carrinhoLinhas = carrinhoLinhas;
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

    public List<CarrinhoLinha> getCarrinhoLinhas() {
        return carrinhoLinhas;
    }

    public void setCarrinhoLinhas(List<CarrinhoLinha> carrinhoLinhas) {
        this.carrinhoLinhas = carrinhoLinhas;
    }
}