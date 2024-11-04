package Client;

import Interface.BancoInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.UUID;
import java.util.concurrent.*;

public class ClienteHelper {
    private BancoInterface banco;
    private String url;
    private static final int RECONEXAO_MAX = 3; 
    private static final int TIMEOUT_SECONDS = 5; 
    private int tentativasConexao = 0;

    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ClienteHelper(String url) {
        this.url = url;
        conectar();
    }

    private void conectar() {
        try {
            banco = (BancoInterface) Naming.lookup(url);
            tentativasConexao = 0; 
        } catch (Exception e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
        }
    }

    public int abrirConta() {
        return executarComTimeout(() -> banco.abrirConta(), "abrir conta");
    }

    public void depositar(int numeroConta, double valor) {
        String operacaoId = UUID.randomUUID().toString();
        executarComTimeout(() -> {
            banco.depositar(numeroConta, valor, operacaoId);
            return null;
        }, "depositar");
    }

    public boolean sacar(int numeroConta, double valor) {
        String operacaoId = UUID.randomUUID().toString();
        return executarComTimeout(() -> banco.sacar(numeroConta, valor, operacaoId), "sacar");
    }

    public double consultarSaldo(int numeroConta) {
        return executarComTimeout(() -> banco.consultarSaldo(numeroConta), "consultar saldo");
    }

    public void fecharConta(int numeroConta) {
        executarComTimeout(() -> {
            banco.fecharConta(numeroConta);
            return null;
        }, "fechar conta");
    }

    private void reconectar() {
        if (tentativasConexao < RECONEXAO_MAX) {
            System.out.println("Tentando reconectar...");
            tentativasConexao++;
            conectar();
        } else {
            System.out.println("Falha ao reconectar após " + RECONEXAO_MAX + " tentativas.");
        }
    }
    
    private <T> T executarComTimeout(Callable<T> operacao, String descricaoOperacao) {
        try {
            return executor.submit(operacao).get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Operação " + descricaoOperacao + " excedeu o tempo limite de " + TIMEOUT_SECONDS + " segundos.");
            reconectar();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Erro durante a operação " + descricaoOperacao + ": " + e.getMessage());
            reconectar();
        }
        return null;
    }

    public void encerrar() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

}
