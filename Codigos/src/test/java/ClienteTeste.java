import Interface.BancoInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class ClienteTeste implements Runnable {
    private BancoInterface banco;
    private int numeroConta;
    private Set<String> transacoesRealizadas;

    public ClienteTeste(BancoInterface banco, int numeroConta) {
        this.banco = banco;
        this.numeroConta = numeroConta;
        this.transacoesRealizadas = new HashSet<>();
    }

    @Override
    public void run() {
        try {
            realizarOperacoes();
        } catch (RemoteException e) {
            System.err.println("Erro ao acessar o banco: " + e.getMessage());
        }
    }

    private void realizarOperacoes() throws RemoteException {
        for (int i = 0; i < 5; i++) {
            realizarDeposito();
            realizarSaque();
        }
    }

    private void realizarDeposito() {
        double valorDeposito = Math.random() * 100;
        String idTransacao = "deposito_" + numeroConta + "_" + System.currentTimeMillis();
        try {

            if (Math.random() < 0.1) { // 10% de chance de falha
                throw new RemoteException("Simulação de falha no depósito");
            }

            if (!transacoesRealizadas.contains(idTransacao)) {
                banco.depositar(numeroConta, valorDeposito, idTransacao);
                transacoesRealizadas.add(idTransacao);
                System.out.println("Depósito de " + valorDeposito + " na conta " + numeroConta);
            } else {
                System.out.println("Depósito já realizado: " + idTransacao);
            }
        } catch (RemoteException e) {
            System.out.println("Erro ao realizar depósito na conta " + numeroConta + ": " + e.getMessage());
        }
    }

    private void realizarSaque() {
        double valorSaque = Math.random() * 50;
        String idTransacao = "saque_" + numeroConta + "_" + System.currentTimeMillis();
        try {

            if (Math.random() < 0.1) { // 10% de chance de falha
                throw new RemoteException("Simulação de falha no saque");
            }


            if (!transacoesRealizadas.contains(idTransacao)) {
                if (banco.sacar(numeroConta, valorSaque, idTransacao)) {
                    transacoesRealizadas.add(idTransacao);
                    System.out.println("Saque de " + valorSaque + " da conta " + numeroConta);
                } else {
                    System.out.println("Saque de " + valorSaque + " falhou na conta " + numeroConta);
                }
            } else {
                System.out.println("Saque já realizado: " + idTransacao);
            }
        } catch (RemoteException e) {
            System.out.println("Erro ao realizar saque na conta " + numeroConta + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            BancoInterface banco = (BancoInterface) Naming.lookup("rmi://localhost/Banco");
            int numeroContas = 100;
            Thread[] threads = new Thread[numeroContas];

            criarEIniciarContas(banco, threads, numeroContas);
            aguardarThreads(threads);
            exibirSaldosFinais(banco, numeroContas);

        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void criarEIniciarContas(BancoInterface banco, Thread[] threads, int numeroContas) throws RemoteException {
        for (int i = 0; i < numeroContas; i++) {
            int numeroConta = banco.abrirConta();
            System.out.println("Conta " + numeroConta + " criada.");
            threads[i] = new Thread(new ClienteTeste(banco, numeroConta));
            threads[i].start();
        }
    }

    private static void aguardarThreads(Thread[] threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private static void exibirSaldosFinais(BancoInterface banco, int numeroContas) throws RemoteException {
        for (int i = 0; i < numeroContas; i++) {
            double saldoFinal = banco.consultarSaldo(i + 1);
            System.out.println("Saldo final da conta " + (i + 1) + ": " + saldoFinal);
        }
    }
}
