package pt.ipleiria.estg.dei.lojacalcado.modelo;

public class CarrinhoLinha {
    private int id, id_carrinho, id_produto, quantidade;
    private double preco;

    public CarrinhoLinha(int id, int id_carrinho, int id_produto, int quantidade, double preco) {
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}