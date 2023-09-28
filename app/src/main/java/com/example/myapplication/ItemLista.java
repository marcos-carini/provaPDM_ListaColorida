package com.example.myapplication;

public class ItemLista {
    private int id;
    private String texto;
    private int cor;


    public ItemLista(String texto, int cor) {
        this.texto = texto;
        this.cor = cor;
    }
    public ItemLista(int id, String texto, int cor) {
        this.id = id;
        this.texto = texto;
        this.cor = cor;
    }

    public String getTexto() {
        return texto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCor() {
        return cor;
    }

    public int getId(){
        return id;
    }




}