package com.innovapp.luk7.arreglos;

public class ReportesArreglo {

    private String id = "";

    private String titulo = "";
    private String descripcion = "";
    private String fecha = "";

    private boolean del2 = false;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isDel2() {
        return del2;
    }

    public void setDel2(boolean del2) {
        this.del2 = del2;
    }



    @Override
    public String toString() {
        return "ProduccionArreglo{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }

    public String get(String columna) {
        String result = "";

        if(columna.equals("id")){
            result = this.id;
        }
        if(columna.equals("titulo")){
            result = this.titulo;
        }
        if(columna.equals("descripcion")){
            result = this.descripcion;
        }
        if(columna.equals("fecha")){
            result = this.fecha;
        }

        return result;

    }
    public void set(String columna,String value) {


        if(columna.equals("id")){
            this.id = value;
        }
        if(columna.equals("titulo")){
            this.titulo = value;
        }
        if(columna.equals("descripcion")){
            this.descripcion = value;
        }
        if(columna.equals("fecha")){
            this.fecha = value;
        }

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }






}
