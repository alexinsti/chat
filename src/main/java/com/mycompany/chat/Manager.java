/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author alexm
 */
public class Manager extends Thread {
    
    private int puerto;
    private String clienteIP;
    private ServerSocket serverSocket;
    private OutputStream os;
    
    public Manager(int Puerto, String clienteIP) throws IOException {
        //Guardamos los datos para la conexión
        this.puerto=Puerto;
        this.clienteIP=clienteIP;
        this.serverSocket = new ServerSocket(puerto);    
    }
    
    public void realizarProceso() throws IOException {
        
        String conexionIP;
        Socket socket;
        
        System.out.println("(Gestor Procesos) Conexión Esperada: "+clienteIP+" en puerto "+this.puerto);
        
        do {   
            // Quedamos a la escucha de la conexión
            socket = serverSocket.accept();
        
            // Verificamos que el cliente es quien esperamos
            conexionIP = socket.getRemoteSocketAddress().toString();
            conexionIP = conexionIP.split(":")[0].split("/")[1];
            System.out.println("(Gestor Procesos) Conexión Solicitada: "+conexionIP+" en puerto "+this.puerto);
            if (!clienteIP.equals(conexionIP)) {
                socket.close();
                System.out.println("(Gestor Procesos) Conexión Denegada.");
            } else {
                System.out.println("(Gestor Procesos) Conexión Aceptada.");
            }
        } while (!clienteIP.equals(conexionIP));
        
        // Realizamos el proceso, generar un aleatorio entre 0 y 60.
        // Esperar tantos segundos y enviar dicho número al cliente.
        os = socket.getOutputStream();
        Random generadorNumerosAleatorios = new Random();
        int tiempoEspera = generadorNumerosAleatorios.nextInt(10);
        try {
            TimeUnit.SECONDS.sleep(tiempoEspera);
            os.write(tiempoEspera);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            os.close();
            //serverSocket.close();
        }    
    }
    
    @Override
    public void run() {
        try {
            realizarProceso();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
