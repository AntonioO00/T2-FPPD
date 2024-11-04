package Server;

import Interface.BancoInterface;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class BancoServidor {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); 

            BancoInterface banco = new AdministracaoServer();
            Naming.rebind("rmi://localhost/Banco", banco);
            
            System.out.println("SERVIDOR DE BANCO INICIADO.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
