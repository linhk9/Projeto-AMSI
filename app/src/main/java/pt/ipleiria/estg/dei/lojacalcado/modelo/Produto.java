package pt.ipleiria.estg.dei.lojacalcado.modelo;

public class Produto {
    int id, id_categoria, stock;
    float preco, preco_antigo;
    String nome, categoria, descricao, imagem, marca, tamanho, cores;

    public Produto(int id, int id_categoria, int stock, float preco, float preco_antigo, String nome, String categoria, String descricao, String imagem, String marca, String tamanho, String cores) {
        this.id = id;
        this.id_categoria = id_categoria;
        this.stock = stock;
        this.preco = preco;
        this.preco_antigo = preco_antigo;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.imagem = imagem;
        this.marca = marca;
        this.tamanho = tamanho;
        this.cores = cores;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public float getPreco_antigo() {
        return preco_antigo;
    }

    public void setPreco_antigo(float preco_antigo) {
        this.preco_antigo = preco_antigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getCores() {
        return cores;
    }

    public void setCores(String cores) {
        this.cores = cores;
    }
}
