/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reto4;

/**
 *
 * @author jhonj
 */
public class Paciente extends Persona {

    private String ciudad;
    private String eps;
    private String enfermedadDiagnosticada;

    public Paciente(String ciudad, String eps, String enfermedadDiagnosticada, String nombreCompleto, long numeroCedula, int edad) {
        super(nombreCompleto, numeroCedula, edad);
        this.ciudad = ciudad;
        this.eps = eps;
        this.enfermedadDiagnosticada = enfermedadDiagnosticada;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getEnfermedadDiagnosticada() {
        return enfermedadDiagnosticada;
    }

    public void setEnfermedadDiagnosticada(String enfermedadDiagnosticada) {
        this.enfermedadDiagnosticada = enfermedadDiagnosticada;
    }
    
    public String clasificarEdad (){
        String res = "";
        if (this.getEdad()>21 && this.getEdad()<=30) {
             res="joven adulto";
        }else if (this.getEdad()>30 && this.getEdad()<=60){
            res="adulto";
        }else if (this.getEdad()>60){
            res="tercera edad";
        }
        return res;
    }

}
