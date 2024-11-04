# Projeto Cliente/Servidor: Sistema Bancário

## Descrição do Projeto

Este projeto consiste no desenvolvimento de um sistema distribuído baseado no modelo Cliente/Servidor utilizando RPC/RMI, que simula a criação de contas de clientes e transações bancárias. O sistema é composto por três tipos de processos: administração, agência e caixa automático, cada um responsável por funcionalidades específicas.

## Funcionalidades

### Processo Administração (Servidor)
- Abertura e fechamento de contas para agências.
- Execução de operações de saque, depósito e consulta de saldo para agências e caixas automáticos.
- Garantia de semântica de execução **exactly-once** para operações não-idempotentes.
- Controle de concorrência para recursos compartilhados.

### Processo Agência (Cliente)
- Solicitação de abertura e fechamento de contas.
- Solicitação de depósito, retirada e consulta de saldo em contas existentes.
- Abertura de conta, depósito e retirada com operações garantidamente não-idempotentes.

### Processo Caixa Automático (Cliente)
- Solicitação de depósito, retirada e consulta de saldo em contas existentes.
- Operações de depósito e retirada com semântica **exactly-once**, mesmo diante de falhas.
cificados no enunciado, garantindo a implementação correta dos processos e funcionalidades, além de um robusto controle de concorrência e idempotência.

