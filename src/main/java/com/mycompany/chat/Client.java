/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author alexm
 */
public class Client {
    
    private String serverIP;
    private int serverPort;
    private Socket socket;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader br;
    
    public Client(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }
    
    public void setPuerto(int serverPort) {
        this.serverPort = serverPort;
    }
    
    public void start() throws UnknownHostException, IOException {
        System.out.println("(Cliente) Estableciendo Conexión.");
        socket = new Socket(serverIP, serverPort);
        is = socket.getInputStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        System.out.println("(Cliente) Conexión Establecida.");
    }
    
    public void stop() throws IOException {
        System.out.println("(Cliente) Cerrando Conexión.");
        is.close();
        socket.close();
        System.out.println("(Cliente) Conexión Cerrada.");
    }
    
    public static void main(String[] args) {
        Client cliente = new Client("localhost", 49171);
        int puerto;
        try {
            //Solicitamos conexión
            cliente.start();
            String cadena = cliente.br.readLine();
            puerto = Integer.parseInt(cadena);
            System.out.println("(Cliente) Puerto para servicio:" + puerto);
            cliente.stop();
            
            //Nos conectamos al puerto solicitado
            cliente.setPuerto(puerto);
            cliente.start();
            
            //Recibimos servicio - Un número aleatorio          
             System.out.println("(Cliente) Número generado por el servidor:" + cliente.is.read());
                 
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}
