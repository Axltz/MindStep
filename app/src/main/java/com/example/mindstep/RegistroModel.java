package com.example.mindstep;

public class RegistroModel {
    private int id;
    private String fecha;
    private String tituloFecha;
    private String riesgo;
    private String riesgoColorHex;
    private String sueno;
    private String estres;
    private String animo;
    private String tags;

    public RegistroModel(int id, String fecha, String tituloFecha, String riesgo, String riesgoColorHex, String sueno, String estres, String animo, String tags) {
        this.id = id;
        this.fecha = fecha;
        this.tituloFecha = tituloFecha;
        this.riesgo = riesgo;
        this.riesgoColorHex = riesgoColorHex;
        this.sueno = sueno;
        this.estres = estres;
        this.animo = animo;
        this.tags = tags;
    }

    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public String getTituloFecha() { return tituloFecha; }
    public String getRiesgo() { return riesgo; }
    public String getRiesgoColorHex() { return riesgoColorHex; }
    public String getSueno() { return sueno; }
    public String getEstres() { return estres; }
    public String getAnimo() { return animo; }
    public String getTags() { return tags; }
}
