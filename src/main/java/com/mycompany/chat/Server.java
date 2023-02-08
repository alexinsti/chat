/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 *
 * @author alexm
 */
public class Server {

    private ServerSocket serverSocket;
    private OutputStream os;
    private PrintWriter pw;
   
    public Server(int puerto) throws IOException {
        
        //Creamos el SocketServer para aceptar peticiones
        serverSocket = new ServerSocket(puerto);
        System.out.println("(Servidor) A la espera de conexiones.");
        
        while (true) {
            // Para cada petición recibida
            Socket socket = serverSocket.accept();
            
            // Preparamos el canal de salida
            os = socket.getOutputStream();
            pw = new PrintWriter(os, true);
            
            // Cogemos la IP
            String clienteIP = socket.getRemoteSocketAddress().toString();
            clienteIP = clienteIP.split(":")[0].split("/")[1];
            System.out.println("(Servidor) Conexión establecida con: "+clienteIP);
            
            // Buscamos un puerto libre entre 59K y 60k
            int nuevoPuerto = buscaPuertoLibre();
            System.out.println("(Servidor) Puerto Generado: " + nuevoPuerto);
            
            // Creo un hilo de Gestor para atender la peticion
            new Manager (nuevoPuerto, clienteIP).start();
            
            // Mando al cliente el puerto generado 
            pw.println(nuevoPuerto);
        }
    }
    
    public int buscaPuertoLibre() {
        
        ServerSocket socketPrueba;
        
        /* La única forma que he encontrado de verificar que un puerto está
           libre es creando un ServerSocket sobre él. Si no genera una excepción
           cierro el ServerSocket y devuelvo el puerto.       
           Se verifican los puertos del 59k al 60k, terminando cuando encuentre 
           el primero libre.
        */
        for (int i=59000;i<=60000;i++) {
            try { 
                socketPrueba = new ServerSocket(i);
                socketPrueba.close();
                return i;
            }catch (IOException ex) {                 
            }
        } 
        return -1;
    }  
    
    public void stop() throws IOException {
        serverSocket.close();
    }
    
    public static void main(String[] args) {
        try {
            Server servidor = new Server(49171);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
