/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reto4;

/**
 *
 * @author jhonj
 */
public class Persona {
    
    private String nombreCompleto;
    private long numeroCedula;
    private int edad;

    public Persona(String nombreCompleto, long numeroCedula, int edad) {
        this.nombreCompleto = nombreCompleto;
        this.numeroCedula = numeroCedula;
        this.edad = edad;
    }
    
    

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public long getNumeroCedula() {
        return numeroCedula;
    }

    public void setNumeroCedula(long numeroCedula) {
        this.numeroCedula = numeroCedula;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    
    
}
