/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reto4;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Daniel
 */
public class Conector {
    
    /**
     * Crea una conexion a una base de datos especifica
     * @return la conexión creada o null
     */
    public Connection crearConexion() {
        String url = "jdbc:sqlite:";
        Path rutaBD = Paths.get("src/assets/Investigacion.db");
        url += rutaBD.toAbsolutePath();
        System.out.println(url);
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            conexion = null;
        }
        return conexion;
    }
    
    /**
     * Ejecuta una consulta en la base de datos y devuelve la tabla con los resultados de la consulta
     * @param conexion la conexión donde se va a hacer la consulta
     * @param query la sentencia SQL a ejecutar
     * @return los resultados de la consulta
     */
    public ResultSet ejecutarQuery(Connection conexion, String query) {
        ResultSet resultados = null;
        try {
            Statement sentencia = conexion.createStatement();
            // Hacer una consulta y obtener la tabla con los resultados
            // Statement.executeQuery(String query)
            resultados = sentencia.executeQuery(query);
        } catch (SQLException ex) {
            resultados = null;
        }
        return resultados;
    }
    
    /**
     * Ejecuta una sentencia SQL y devuelve la cantidad de filas afectadas por la consulta
     * @param conexion la conexión donde se va a hacer la consulta
     * @param query la sentencia SQL a ejecutar
     * @return la cantidad de filas afectadas
     */
    public int ejecutarActualizacion(Connection conexion, String query) {
        int filasAfectadas = 0;
        try {
            Statement sentencia = conexion.createStatement();
            // Hacer una consulta y devolver la cantidad de filas afectadas por la consulta
            // Statement.executeUpdate(String query)
            filasAfectadas = sentencia.executeUpdate(query);
        } catch (SQLException ex) {
            filasAfectadas = -1;
        }
        return filasAfectadas;
    }
}
