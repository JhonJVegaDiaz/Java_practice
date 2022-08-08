/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reto5;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jhonj
 */
public class Medico extends Persona {
    
    private List<List<String>> pacientesAsMedico = new ArrayList<>();

    public List<List<String>> getPacientesAsMedico() {
        return pacientesAsMedico;
    }

    public void setPacientesAsMedico(List<List<String>> pacientesAsMedico) {
        this.pacientesAsMedico = pacientesAsMedico;
    }

    public Medico(String nombreCompleto, long numeroCedula) {
        super(nombreCompleto, numeroCedula);
    }
    
    
    public Medico() {
        super();
    }
        
    public List<String> pacientes (String seleccion){
        List<String> result = new ArrayList<String>();
        int val=0;
        for (int i = 0; i < pacientesAsMedico.size() ; i++) {
            List<String> medico = pacientesAsMedico.get(i);
            if (medico.get(0).equals(seleccion)){ 
                medico.remove(0);
                result = medico;
                val=1;
                }
            }
        if (val==0){
            result.add( "   Médico sin"); 
            result.add( "pacientes asignados"); 
        }
        return result;
    }
    
}
