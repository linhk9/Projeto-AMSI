package pt.ipleiria.estg.dei.lojacalcado.modelo;

import java.util.List;

public class Carrinho {
    int id, id_userdata;
    String data;
    public List<CarrinhoLinha> carrinhoLinhas;


    public Carrinho(int id, int id_userdata, String data) {
        this.id = id;
        this.id_userdata = id_userdata;
        this.data = data;
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

    public static class CarrinhoLinha {
        int id, id_carrinho, id_produto, quantidade;
        float preco;

        public CarrinhoLinha(int id, int id_carrinho, int id_produto, int quantidade, float preco) {
            this.id = id;
            this.id_carrinho = id_carrinho;
            this.id_produto = id_produto;
            this.quantidade = quantidade;
            this.preco = preco;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId_carrinho() {
            return id_carrinho;
        }

        public void setId_carrinho(int id_carrinho) {
            this.id_carrinho = id_carrinho;
        }

        public int getId_produto() {
            return id_produto;
        }

        public void setId_produto(int id_produto) {
            this.id_produto = id_produto;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public float getPreco() {
            return preco;
        }

        public void setPreco(float preco) {
            this.preco = preco;
        }
    }

}
