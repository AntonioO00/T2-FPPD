package Client;

import Interface.BancoInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.UUID;

public class ClienteHelper {
    private BancoInterface banco;
    private String url;

    public ClienteHelper(String url) {
        this.url = url;
        conectar();
    }

    private void conectar() {
        try {
            banco = (BancoInterface) Naming.lookup(url);
        } catch (Exception e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
        }
    }

    public int abrirConta() {
        try {
            return banco.abrirConta();
        } catch (RemoteException e) {
            reconectar();
            return abrirConta();
        }
    }

    public void depositar(int numeroConta, double valor) {
        try {
        String operacaoId = UUID.randomUUID().toString();
            banco.depositar(numeroConta, valor, operacaoId);
        } catch (RemoteException e) {
            reconectar();
            depositar(numeroConta, valor);
        }
    }

    public boolean sacar(int numeroConta, double valor) {
        try {
            String operacaoId = UUID.randomUUID().toString();
            return banco.sacar(numeroConta, valor, operacaoId);
        } catch (RemoteException e) {
            reconectar();
            return sacar(numeroConta, valor);
        }
    }

    public double consultarSaldo(int numeroConta) {
        try {
            return banco.consultarSaldo(numeroConta);
        } catch (RemoteException e) {
            reconectar();
            return consultarSaldo(numeroConta);
        }
    }

    public void fecharConta(int numeroConta) {
        try {
            banco.fecharConta(numeroConta);
        } catch (RemoteException e) {
            reconectar();
            fecharConta(numeroConta);
        }
    }

    private void reconectar() {
        System.out.println("Tentando reconectar...");
        conectar();
    }
}
