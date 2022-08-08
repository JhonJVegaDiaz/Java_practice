/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package reto4;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author jhonj
 */
public class Inicio extends javax.swing.JFrame {
    
    Conector conector;


    /**
     * Creates new form Inicio
     */
    public Inicio() {
        initComponents();
        getContentPane().setBackground(new java.awt.Color(232, 249, 248));
        conector = new Conector();
        
    }

    
    private void limpiarTexto_ingreso(){
        textNombre.setText("");
        textCiudad.setText("");
        textEps.setText("");
        textEnfermedad.setText("");
        textCedula.setText("");
        textEdad.setText("");
    }
    
    private void limpiarBusqueda(){
        txtBNombre.setText("");
        txtBCiudad.setText("");
        txtBEps.setText("");
        txtBEnfermedad.setText("");
        txtBCedula.setText("");
        txtBEdad.setText("");
        txtBuscarCedula.setText("");
    }    
    
    private void agregarDatos(){
        
        String nombreCompleto = textNombre.getText().trim();
        String ciudad = textCiudad.getText().trim();
        String eps = textEps.getText().trim();
        String enfermedadDiagnosticada = textEnfermedad.getText().trim(); 
        
        if(!nombreCompleto.isBlank() && !ciudad.isBlank() 
                && !eps.isBlank() && !enfermedadDiagnosticada.isBlank()
                && !textCedula.getText().trim().isBlank() 
                && !textEdad.getText().trim().isBlank()) {
            if (textCedula.getText().trim().matches("[0-9]+")
                && textEdad.getText().trim().matches("[0-9]+") 
                && !textCedula.getText().trim().matches("[-]?\\.?")
                && !textEdad.getText().trim().matches("[-]?\\.?") ){
                    long numeroCedula = Long.parseLong(textCedula.getText().trim());
                    int edad = Integer.parseInt(textEdad.getText().trim());
                    
                    Connection conexion = conector.crearConexion();
                    
                    String sql = "INSERT INTO paciente (cedula, nombre, edad, ciudad, eps, enfermedad_diag) VALUES (%1$d,'%2$s',%3$d,'%4$s','%5$s','%6$s')";
                    String query = String.format(sql, numeroCedula, nombreCompleto, edad, ciudad, eps, enfermedadDiagnosticada);                    
                    conector.ejecutarActualizacion(conexion, query);
                    JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente");                                     
                    limpiarTexto_ingreso();
            }
            else{
                JOptionPane.showMessageDialog(this, "Ingresar solo números en la cedula y la edad, intentar de nuevo"); 
            }          
        }else{
            JOptionPane.showMessageDialog(this, "Diligenciar todos los campos, intentar de nuevo");              
        }
    }

    private void obtenerDatos(){
    
        Connection conexion = conector.crearConexion();
        String sql = "SELECT * FROM paciente";
        ResultSet resultado = conector.ejecutarQuery(conexion, sql);
        String historialTexto = "";
        try {
            while(resultado.next()){
                String nombreCompleto = resultado.getString("nombre");
                String ciudad = resultado.getString("ciudad");
                String eps = resultado.getString("eps");
                String enfermedadDiagnosticada = resultado.getString("enfermedad_diag"); 
                long numeroCedula = resultado.getInt("cedula");
                int edad = resultado.getInt("edad");
                String textoIngresado = nombreCompleto+" "+numeroCedula+" "+edad+" "+ciudad+" "+eps+" "+enfermedadDiagnosticada+"\n";
                historialTexto+=textoIngresado;                
            }
            datosIngresados.setText(historialTexto);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }     
        
    }
    
    private void procesarDatos(){
        
        salidas.setText("");
        ArrayList<Paciente> pacientes = new ArrayList<>();
        int val= 0; int mayor = 0; int menor = 0;
        String [] enfermedades = new String[] {"cancer", "cardiovasculares", 
            "respiratorias", "cerebrovasculares", "hipertension", "diabetes"};
        int [] conteoEnfermedades = new int []{0,0,0,0,0,0};
        List<String> nombreEps = new ArrayList<String>(); 
        List<Integer> conteoEps = new ArrayList<Integer>();
        
        Connection conexion = conector.crearConexion();
        String sql = "SELECT * FROM paciente";
        ResultSet resultado = conector.ejecutarQuery(conexion, sql);
        String historialTexto = "";
        try {
            while(resultado.next()){
                int edad = resultado.getInt("edad");
                String nombreCompleto = resultado.getString("nombre");
                String ciudad = resultado.getString("ciudad");
                String eps = resultado.getString("eps");
                String enfermedadDiagnosticada = resultado.getString("enfermedad_diag"); 
                long numeroCedula = resultado.getInt("cedula");
                Paciente paciente = new Paciente(ciudad, eps, enfermedadDiagnosticada, nombreCompleto,numeroCedula, edad);
                pacientes.add(paciente);
                for (int k = 0; (k < enfermedades.length); k++) {
                    if (enfermedades[k].equalsIgnoreCase(enfermedadDiagnosticada)){
                    conteoEnfermedades[k]++;
                        }
                    } 
                if (nombreEps.contains(eps)){
                    for (int j = 0; j < nombreEps.size(); j++) {
                        if (nombreEps.get(j).equalsIgnoreCase("eps")){
                        conteoEps.set(j, conteoEps.get(j)+1);
                        }
                    }
                }else{
                    nombreEps.add(eps);
                    conteoEps.add(1);
                } 

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } 

        String salida = "";

        for (int j = 0; j < enfermedades.length; j++) {
            if (conteoEnfermedades[j] > val) {
                val = conteoEnfermedades[j];
                mayor = j;
            }
        }
        for (int j = 0; j < enfermedades.length; j++) {
            if (conteoEnfermedades[j] < val) {
                val = conteoEnfermedades[j];
                menor = j;
            }
        }
        salida+=enfermedades[mayor]+"\n";
        salida+=enfermedades[menor]+"\n";

        val=0;
        for (int j = 0; j < nombreEps.size(); j++) {
            if (conteoEps.get(j) > val) {
                val = conteoEps.get(j);
                mayor = j;
            }
        }
        salida+=nombreEps.get(mayor)+"\n";

        for (int i = 0; i < pacientes.size() ; i++) {
            if (pacientes.get(i).clasificarEdad().equals("adulto")){
                salida+=pacientes.get(i).getNombreCompleto()+ " " + pacientes.get(i).getNumeroCedula()+"\n";
            }
        }
        salidas.setText(salida);

    }
    
    private void buscarCedula(){

        if (txtBuscarCedula.getText().trim().matches("[0-9]+") 
           && !txtBuscarCedula.getText().trim().matches("[-]?\\.?")){
            long numeroCedula = Long.parseLong(txtBuscarCedula.getText().trim());
            Connection conexion = conector.crearConexion();
            String sql = "SELECT * FROM paciente WHERE cedula = %1$d";
            String query = String.format(sql, numeroCedula);
            ResultSet resultado = conector.ejecutarQuery(conexion, query);
            int edad = -1;
            try {
                int contador = 0;
                while(resultado.next() && contador == 0){
                    txtBNombre.setText(resultado.getString("nombre"));
                    txtBCiudad.setText(resultado.getString("ciudad"));
                    txtBEps.setText(resultado.getString("eps"));
                    txtBEnfermedad.setText(resultado.getString("enfermedad_diag")); 
                    txtBEdad.setText(Integer.toString(resultado.getInt("edad")));
                    txtBCedula.setText(Long.toString(numeroCedula));
                    edad = resultado.getInt("edad");
                    contador += 1;
                }
                if (edad==-1){
                   JOptionPane.showMessageDialog(this, "Cédula no encontrada"); 
                }
            } catch(SQLException ex) {
            System.out.println(ex.getMessage());
            }  
        }else{
          JOptionPane.showMessageDialog(this, "Ingresar números enteros en la cedula, intentar de nuevo");   
        }        
        
    }

    private void editarDatos(){
    
    if (txtBuscarCedula.getText().trim().matches("[0-9]+") 
        && !txtBuscarCedula.getText().trim().matches("[-]?\\.?")){
        long numeroCedulaOr = Long.parseLong(txtBuscarCedula.getText().trim());
        String nombreCompleto = txtBNombre.getText().trim();
        String ciudad = txtBCiudad.getText().trim();
        String eps = txtBEps.getText().trim();
        String enfermedadDiagnosticada = txtBEnfermedad.getText().trim(); 
        if(!nombreCompleto.isBlank() && !ciudad.isBlank() 
                && !eps.isBlank() && !enfermedadDiagnosticada.isBlank()
                && !txtBCedula.getText().trim().isBlank() 
                && !txtBEdad.getText().trim().isBlank()) {
            if (txtBCedula.getText().trim().matches("[0-9]+")
                && txtBEdad.getText().trim().matches("[0-9]+") 
                && !txtBCedula.getText().trim().matches("[-]?\\.?")
                && !txtBEdad.getText().trim().matches("[-]?\\.?") ){
                    long numeroCedula = Long.parseLong(txtBCedula.getText().trim());
                    int edad = Integer.parseInt(txtBEdad.getText().trim());

                Connection conexion = conector.crearConexion();
                String sql ="UPDATE paciente SET cedula = %1$d, nombre = '%2$s', edad = %3$d, ciudad = '%4$s', eps = '%5$s', enfermedad_diag = '%6$s' WHERE cedula = %7$d";
                String query = String.format(sql, numeroCedula, nombreCompleto, edad, ciudad, eps, enfermedadDiagnosticada, numeroCedulaOr);
                int filas = conector.ejecutarActualizacion(conexion, query);
                if (filas == 1){
                   JOptionPane.showMessageDialog(this, "Datos editados exitosamente"); 
                   limpiarBusqueda();
                   
                }else{
                   JOptionPane.showMessageDialog(this, "Fallo en la edición de los datos, número de cédula repetido");   
                }
                }else{
            JOptionPane.showMessageDialog(this, "Ingresar números enteros en la cedula y la edad, intentar de nuevo");    
                }
            }else{
            JOptionPane.showMessageDialog(this,"Todos los datos deben diligenciarse, intentar de nuevo" );    
                }
        }else{
          JOptionPane.showMessageDialog(this, "Ingresar números enteros en la cedula original, intentar de nuevo");   
        }     
        
    }
    
    private void borrarDatos(){
        
    if (txtBuscarCedula.getText().trim().matches("[0-9]+") 
        && !txtBuscarCedula.getText().trim().matches("[-]?\\.?")){
        long numeroCedulaOr = Long.parseLong(txtBuscarCedula.getText().trim());    
        Connection conexion = conector.crearConexion();
        String sql ="DELETE FROM paciente WHERE cedula = %1$d";
        String query = String.format(sql, numeroCedulaOr);
        int filas = conector.ejecutarActualizacion(conexion, query);
        if (filas == 1){
           JOptionPane.showMessageDialog(this, "Eliminación de los datos del pasiente exitosa"); 
           limpiarBusqueda();

        }else{
           JOptionPane.showMessageDialog(this, "Fallo en la eliminación de los datos paciente, número de cédula no encontrado");    
        }
        }else{
          JOptionPane.showMessageDialog(this, "Ingresar números enteros en la cedula original, intentar de nuevo");   
        }     
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabGeneral = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        labelNombre = new javax.swing.JLabel();
        textNombre = new javax.swing.JTextField();
        labelCedula = new javax.swing.JLabel();
        textCedula = new javax.swing.JTextField();
        labelEdad = new javax.swing.JLabel();
        textEdad = new javax.swing.JTextField();
        labelCiudad = new javax.swing.JLabel();
        textCiudad = new javax.swing.JTextField();
        labelEps = new javax.swing.JLabel();
        textEps = new javax.swing.JTextField();
        labelEnfermedad = new javax.swing.JLabel();
        textEnfermedad = new javax.swing.JTextField();
        buttonIngresar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        labelDatosIngresados = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        datosIngresados = new javax.swing.JTextArea();
        labelSalidas = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        salidas = new javax.swing.JTextArea();
        buttonProcesar = new javax.swing.JButton();
        buttonObtener = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarCedula = new javax.swing.JTextField();
        buttonBuscar = new javax.swing.JButton();
        labelNombre1 = new javax.swing.JLabel();
        labelCedula1 = new javax.swing.JLabel();
        labelEdad1 = new javax.swing.JLabel();
        labelCiudad1 = new javax.swing.JLabel();
        labelEps1 = new javax.swing.JLabel();
        labelEnfermedad1 = new javax.swing.JLabel();
        txtBNombre = new javax.swing.JTextField();
        txtBCedula = new javax.swing.JTextField();
        txtBEdad = new javax.swing.JTextField();
        txtBEps = new javax.swing.JTextField();
        txtBCiudad = new javax.swing.JTextField();
        txtBEnfermedad = new javax.swing.JTextField();
        buttonEditar = new javax.swing.JButton();
        ButtonEliminar = new javax.swing.JButton();
        labelInvEnf = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reto 4 Jhon Jairo Vega Díaz");

        jPanel1.setBackground(new java.awt.Color(219, 255, 219));

        labelNombre.setText("Nombre");

        textNombre.setActionCommand("<Not Set>");

        labelCedula.setText("Cedula");
        labelCedula.setToolTipText("");

        textCedula.setActionCommand("<Not Set>");

        labelEdad.setText("Edad");
        labelEdad.setToolTipText("");

        textEdad.setActionCommand("<Not Set>");

        labelCiudad.setText("Ciudad");
        labelCiudad.setToolTipText("");

        textCiudad.setActionCommand("<Not Set>");

        labelEps.setText("EPS");
        labelEps.setToolTipText("");

        textEps.setActionCommand("<Not Set>");

        labelEnfermedad.setText("Enfermedad diagnosticada");
        labelEnfermedad.setToolTipText("");

        textEnfermedad.setActionCommand("<Not Set>");

        buttonIngresar.setText("Ingresar");
        buttonIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonIngresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelEdad, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textEdad, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelEnfermedad, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textEnfermedad)
                    .addComponent(textCiudad)
                    .addComponent(labelEps, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textEps, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(165, 165, 165))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelEdad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelCiudad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelEps)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textEps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelEnfermedad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textEnfermedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addComponent(buttonIngresar)
                .addContainerGap(90, Short.MAX_VALUE))
        );

        tabGeneral.addTab("Ingresar datos", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 254, 224));

        labelDatosIngresados.setText("Datos Obtenidos");
        labelDatosIngresados.setToolTipText("");

        datosIngresados.setColumns(20);
        datosIngresados.setRows(5);
        jScrollPane2.setViewportView(datosIngresados);

        labelSalidas.setText("Datos procesados");
        labelSalidas.setToolTipText("");

        salidas.setColumns(20);
        salidas.setRows(5);
        jScrollPane1.setViewportView(salidas);

        buttonProcesar.setText("Procesar datos");
        buttonProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonProcesarActionPerformed(evt);
            }
        });

        buttonObtener.setText("Obtener datos");
        buttonObtener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonObtenerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(labelDatosIngresados, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(104, 104, 104)))
                    .addComponent(buttonObtener, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonProcesar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelSalidas, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonObtener)
                    .addComponent(buttonProcesar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDatosIngresados)
                    .addComponent(labelSalidas, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        tabGeneral.addTab("Procesar datos", jPanel2);

        jPanel3.setBackground(new java.awt.Color(221, 221, 250));

        jLabel1.setText("Cédula paciente");

        buttonBuscar.setText("Buscar");
        buttonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBuscarActionPerformed(evt);
            }
        });

        labelNombre1.setText("Nombre");

        labelCedula1.setText("Cedula");
        labelCedula1.setToolTipText("");

        labelEdad1.setText("Edad");
        labelEdad1.setToolTipText("");

        labelCiudad1.setText("Ciudad");
        labelCiudad1.setToolTipText("");

        labelEps1.setText("EPS");
        labelEps1.setToolTipText("");

        labelEnfermedad1.setText("Enfermedad diagnosticada");
        labelEnfermedad1.setToolTipText("");

        buttonEditar.setText("Editar");
        buttonEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEditarActionPerformed(evt);
            }
        });

        ButtonEliminar.setText("Eliminar");
        ButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBEdad, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(buttonEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(ButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonBuscar)
                                .addGap(67, 67, 67))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelEdad1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelCedula1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelEnfermedad1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtBEnfermedad, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(labelCiudad1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtBCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtBEps, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(labelEps1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap(28, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelEps1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBEps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCedula1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCiudad1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelEdad1)
                    .addComponent(labelEnfermedad1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBEnfermedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonEliminar)
                    .addComponent(buttonEditar))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        tabGeneral.addTab("Editar / Eliminar", jPanel3);

        labelInvEnf.setFont(new java.awt.Font("MV Boli", 1, 14)); // NOI18N
        labelInvEnf.setText("Investigación de enfermedades");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelInvEnf, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(labelInvEnf, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabGeneral.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonIngresarActionPerformed
        // TODO add your handling code here:
        agregarDatos();
        
    }//GEN-LAST:event_buttonIngresarActionPerformed

    
    
    private void buttonProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonProcesarActionPerformed
        // TODO add your handling code here:
        procesarDatos();

    }//GEN-LAST:event_buttonProcesarActionPerformed

    private void buttonObtenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonObtenerActionPerformed
        // TODO add your handling code here:
       obtenerDatos();
        
    }//GEN-LAST:event_buttonObtenerActionPerformed

    private void buttonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBuscarActionPerformed
        // TODO add your handling code here:
        buscarCedula();
    }//GEN-LAST:event_buttonBuscarActionPerformed

    private void buttonEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditarActionPerformed
        // TODO add your handling code here:
        editarDatos();
    }//GEN-LAST:event_buttonEditarActionPerformed

    private void ButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEliminarActionPerformed
        // TODO add your handling code here:
        borrarDatos();
    }//GEN-LAST:event_ButtonEliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inicio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonEliminar;
    private javax.swing.JButton buttonBuscar;
    private javax.swing.JButton buttonEditar;
    private javax.swing.JButton buttonIngresar;
    private javax.swing.JButton buttonObtener;
    private javax.swing.JButton buttonProcesar;
    private javax.swing.JTextArea datosIngresados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelCedula;
    private javax.swing.JLabel labelCedula1;
    private javax.swing.JLabel labelCiudad;
    private javax.swing.JLabel labelCiudad1;
    private javax.swing.JLabel labelDatosIngresados;
    private javax.swing.JLabel labelEdad;
    private javax.swing.JLabel labelEdad1;
    private javax.swing.JLabel labelEnfermedad;
    private javax.swing.JLabel labelEnfermedad1;
    private javax.swing.JLabel labelEps;
    private javax.swing.JLabel labelEps1;
    private javax.swing.JLabel labelInvEnf;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel labelNombre1;
    private javax.swing.JLabel labelSalidas;
    private javax.swing.JTextArea salidas;
    private javax.swing.JTabbedPane tabGeneral;
    private javax.swing.JTextField textCedula;
    private javax.swing.JTextField textCiudad;
    private javax.swing.JTextField textEdad;
    private javax.swing.JTextField textEnfermedad;
    private javax.swing.JTextField textEps;
    private javax.swing.JTextField textNombre;
    private javax.swing.JTextField txtBCedula;
    private javax.swing.JTextField txtBCiudad;
    private javax.swing.JTextField txtBEdad;
    private javax.swing.JTextField txtBEnfermedad;
    private javax.swing.JTextField txtBEps;
    private javax.swing.JTextField txtBNombre;
    private javax.swing.JTextField txtBuscarCedula;
    // End of variables declaration//GEN-END:variables
}
