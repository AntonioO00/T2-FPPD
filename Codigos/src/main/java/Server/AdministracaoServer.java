
package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import Interface.BancoInterface;

public class AdministracaoServer extends UnicastRemoteObject implements BancoInterface {
    private Map<Integer, Double> contas;
    private int contaAtual;
    private Map<String, Boolean> operacoes;

    public AdministracaoServer() throws RemoteException {
        contas = new HashMap<>();
        operacoes = new HashMap<>();
        contaAtual = 0;
    }

    @Override
    public synchronized int abrirConta() throws RemoteException {
        int numeroConta = ++contaAtual;
        contas.put(numeroConta, 0.0);
        System.out.println("Conta " + numeroConta + " aberta com sucesso.");
        return numeroConta;
    }

    @Override
    public synchronized void fecharConta(int numeroConta) throws RemoteException {
        if (contas.remove(numeroConta) != null) {
            System.out.println("Conta " + numeroConta + " fechada com sucesso.");
        } else {
            System.out.println("Conta " + numeroConta + " não encontrada.");
        }
    }

    @Override
    public synchronized double consultarSaldo(int numeroConta) throws RemoteException {
        return contas.getOrDefault(numeroConta, 0.0);
    }

    @Override
    public synchronized void depositar(int numeroConta, double valor, String operacaoId) throws RemoteException {
        if (operacoes.getOrDefault(operacaoId, false)) {
            System.out.println("Operação de depósito já realizada.");
            return;
        }

        if (contas.containsKey(numeroConta)) {
            contas.put(numeroConta, contas.get(numeroConta) + valor);
            System.out.println("Depósito de " + valor + " na conta " + numeroConta + " realizado.");
            operacoes.put(operacaoId, true);
        } else {
            System.out.println("Conta " + numeroConta + " não encontrada.");
        }
    }

    @Override
    public synchronized boolean sacar(int numeroConta, double valor, String operacaoId) throws RemoteException {
        if (operacoes.getOrDefault(operacaoId, false)) {
            System.out.println("Operação de saque já realizada.");
            return false;
        }

        if (contas.containsKey(numeroConta) && contas.get(numeroConta) >= valor) {
            contas.put(numeroConta, contas.get(numeroConta) - valor);
            System.out.println("Saque de " + valor + " na conta " + numeroConta + " realizado.");
            operacoes.put(operacaoId, true); 
            return true;
        } else {
            System.out.println("Conta " + numeroConta + " não encontrada ou saldo insuficiente.");
        }
        return false;
    }
}
