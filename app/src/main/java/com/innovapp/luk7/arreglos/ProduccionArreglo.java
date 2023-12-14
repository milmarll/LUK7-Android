package com.innovapp.luk7.arreglos;

public class ProduccionArreglo {

    private String id = "";

    private String lpn_inicial = "";
    private String lpn_fin = "";

    private String fecha_inical = "";
    private String fecha_fin = "";

    private String fechainser = "";

    private boolean del2 = false;

    public boolean isDel2() {
        return del2;
    }

    public void setDel2(boolean del2) {
        this.del2 = del2;
    }

    public String getFechainser() {
        return fechainser;
    }

    public void setFechainser(String fechainser) {
        this.fechainser = fechainser;
    }

    @Override
    public String toString() {
        return "ProduccionArreglo{" +
                "id='" + id + '\'' +
                ", lpn_inicial='" + lpn_inicial + '\'' +
                ", lpn_fin='" + lpn_fin + '\'' +
                ", fecha_inical='" + fecha_inical + '\'' +
                ", fecha_fin='" + fecha_fin + '\'' +
                  ", fechainser='" + fechainser + '\'' +
                '}';
    }

    public String get(String columna) {
        String result = "";

        if(columna.equals("id")){
            result = this.id;
        }
        if(columna.equals("lpn_inicial")){
            result = this.lpn_inicial;
        }
        if(columna.equals("lpn_fin")){
            result = this.lpn_fin;
        }
        if(columna.equals("fecha_inical")){
            result = this.fecha_inical;
        }
        if(columna.equals("fecha_fin")){
            result = this.fecha_fin;
        }
           if(columna.equals("fechainser")){
               result = this.fechainser;
           }

        return result;

    }
    public void set(String columna,String value) {


        if(columna.equals("id")){
            this.id = value;
        }
        if(columna.equals("lpn_inicial")){
           this.lpn_inicial = value;
        }
        if(columna.equals("lpn_fin")){
            this.lpn_fin = value;
        }
        if(columna.equals("fecha_inical")){
            this.fecha_inical = value;
        }
        if(columna.equals("fecha_fin")){
            this.fecha_fin = value;
        }
         if(columna.equals("fechainser")){
             this.fechainser = value;
         }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha_inical() {
        return fecha_inical;
    }

    public void setFecha_inical(String fecha_inical) {
        this.fecha_inical = fecha_inical;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getLpn_inicial() {
        return lpn_inicial;
    }

    public void setLpn_inicial(String lpn_inicial) {
        this.lpn_inicial = lpn_inicial;
    }

    public String getLpn_fin() {
        return lpn_fin;
    }

    public void setLpn_fin(String lpn_fin) {
        this.lpn_fin = lpn_fin;
    }




}
