package Client;

import java.util.Scanner;

public class Agencia {
    public static void main(String[] args) {
        ClienteHelper clienteBanco = new ClienteHelper("rmi://localhost/Banco");
        Scanner entrada = new Scanner(System.in);
        int nConta = clienteBanco.abrirConta();

        boolean continuar = true;
        while (continuar) {
            System.out.println("MENU DA AGÊNCIA:");
            System.out.println("1. Depositar");
            System.out.println("2. Sacar");
            System.out.println("3. Consultar Saldo");
            System.out.println("4. Fechar Conta");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = entrada.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o valor que deseja depositar: ");
                    double vDeposito = entrada.nextDouble();
                    clienteBanco.depositar(nConta, vDeposito);
                    break;
                case 2:
                    System.out.print("Digite o valor que deseja sacar: ");
                    double vSaque = entrada.nextDouble();
                    clienteBanco.sacar(nConta, vSaque);
                    break;
                case 3:
                    double saldo = clienteBanco.consultarSaldo(nConta);
                    System.out.println("Saldo: " + saldo);
                    break;
                case 4:
                    clienteBanco.fecharConta(nConta);
                    clienteBanco.encerrar();
                    continuar = false;
                    System.exit(0);
                    break;
                case 5:
                    clienteBanco.encerrar();
                    continuar = false;
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
        entrada.close();
    }
}
