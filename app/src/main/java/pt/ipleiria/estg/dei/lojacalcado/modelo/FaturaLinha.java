package pt.ipleiria.estg.dei.lojacalcado.modelo;

public class FaturaLinha {
    private int id;
    private int id_fatura;
    private int id_produto;
    private int quantidade;
    private double preco;

    public FaturaLinha(int id, int id_fatura, int id_produto, int quantidade, double preco) {
        this.id = id;
        this.id_fatura = id_fatura;
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

    public int getId_fatura() {
        return id_fatura;
    }

    public void setId_fatura(int id_fatura) {
        this.id_fatura = id_fatura;
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

    public double getPrecoTotal() {
        return preco * quantidade;
    }
}