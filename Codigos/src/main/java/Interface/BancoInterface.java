package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BancoInterface extends Remote {
    int abrirConta() throws RemoteException;
    void fecharConta(int numeroConta) throws RemoteException;
    double consultarSaldo(int numeroConta) throws RemoteException;
    void depositar(int numeroConta, double valor, String operacaoId) throws RemoteException;
    boolean sacar(int numeroConta, double valor, String operacaoId) throws RemoteException;
}
