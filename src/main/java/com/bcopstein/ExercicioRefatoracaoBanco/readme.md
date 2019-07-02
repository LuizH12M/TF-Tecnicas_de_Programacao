# ESPECIFICACAO DE SOFTWARE
## Modulo de acesso a conta corrente do Banco Nossa Grana

O módulo de acesso a conta corrente é um módulo simples que deve permitir ao funcionário do banco executar operações básicas sobre as contas correntes:
* Consultar saldo
* Consultar últimos movimentos
* Consultar a categoria da conta
* Efetuar depósitos
* Efetuar retiradas
* Consultar estatísticas sobre a conta corrente

O sistema é composto por três telas:
* Tela de identificação da conta corrente: nesta tela o usuário informa o número da conta corrente que deseja acessar.

* Tela de operações: nesta tela o usuário visualiza o saldo, a categoria da conta, o limite diário para saque e os últimos movimentos da conta informada e pode executar operações de depósito e retirada. *<< BUGFIX: ajustar a categoria da conta quando ela muda !!*

* Tela de estatísticas: nesta tela o usuário visualiza informações gerais sobre a conta tais como: saldo médio no mês/ano indicados; total e quantidade de créditos no mês ano indicados; total e quantidade de débitos no mês ano indicados. O usuário tem acesso a tela de estatisticas a partir da tela de operacoes  *<< ESTA TELA AINDA NÃO ESTA IMPLEMENTADA !!*

  * Esta tela deve apresentar o nome do correntista e o mês selecionado (quando a tela abre deve vir selecionado o mês corrente). Deve haver um botão para permitir a seleção de outro mês.
  * Para o cálculo do saldo médio considerar todos os meses com 30 dias. 
  * Para calcular o saldo médio calcule inicialmente o saldo no primeiro dia do mês. Para tanto é necessário acumular todos os depósitos e retiradas desde a abertura da conta. A partir do primeiro dia o saldo do dia é o saldo do dia anterior mais os depoósitos e retiradas do dia corrente.

Nesta primeira versão os dados das contas são mantidos em um arquivo texto. É necessário garantir que sempre que o sistema é encerrado as movimentações atualizadas das contas são salvas neste arquivo.

As contas desse banco tem um comportamento específico. Quanto mais dinheiro o cliente tem depositado mais o banco valoriza seus depósitos. Todos as contas iniciam na categoria “Silver” e zeradas. Contas “Silver” não têm seus depósitos valorizados, ou seja, o valor creditado é exatamente o valor depositado pelo cliente. Quando o saldo da conta atinge ou ultrapassa R$ 50.000,00, a conta passa para a categoria “Gold”. Contas “Gold” têm seus depósitos valorizados em 1%. Neste caso se o cliente depositar R$ 1.000,00 o valor creditado será de R$ 1.010,00. Finalmente se o saldo da conta atinge ou supera os R$ 200.000,00, a conta passa para a categoria “Platinum”. Contas “Platinum” têm seus depósitos valorizados em 2,5%. A verificação de “upgrade” da conta se dá via operação de depósito, e não é possível que um cliente faça “upgrade” diretamente de “Silver” para “Platinum” em uma única operação.

Quando o saldo da conta diminui, em função de uma operação de retirada/saque, a categoria também pode retroceder. Os limites, porém, não são os mesmos ao verificados quando uma conta sofre “upgrade”. Uma conta só perde sua categoria “Platinum”, e passa para “Gold”, se o saldo cair abaixo de R$ 100.000,00. A conta só perde a categoria “Gold”, e passa para “Silver”, se o saldo cair para menos de R$ 25.000,00. Note que uma conta nunca perde duas categorias em uma única operação de retirada mesmo que o saldo caia abaixo de R$ 25.000,00. Se ele era “Platinum”, cai para “Gold”. Só poderá cair para “Silver” na próxima operação de retirada. Observação: as contas nunca podem ficar negativas (o banco não trabalha com cheque especial).

ATENCAO: contas Silver possuem um limite diário de R$ 10000,00 para saques; contas Gold possuem um limite diário de R$ 100000,00 para saques; contas Platinum possuem um limite diário de 500000,00 para saques. *<<< ESTAS RESTRIÇÕES AINDA NÃO ESTÃO IMPLEMENTADAS !!*

Para efeitos de armazenamento no arquivo a categoria “Silver” é identificada com o número “0”, a categoria “Gold” com o número “1” e a categoria “Platinum” com o número “2”.

O número de conta pode ser qualquer inteiro positivo.

PERSISTENCIA DOS DADOS:
Para simplificar a troca de dados os seguintes arquivos são fornecidos:

* Persistencia.java:
  * modulo Java com métodos para leitura e gravação de dados relativos a contas corrente e movimentações de contas corrente (operações de depósito e retirada).
* BDContasBNG.txt:
  * arquivo exemplo com dados de contas corrente.
* BDOperBNG.txt:
  * arquivo exemplo com dados de operações sobre contas corrente.

# ROTEIRO DE TRABALHO:

## PARTE I: completar a implementação atual
1) Analisar a implementação do sistema
2) Implementar as funcionalidades que estão faltando
3) Identificar os eventuais problemas encontrados

## PARTE II: refatoração

### Adoção do padrão Singleton
Aplicar o padrão singleton na classe "Persistencia".

### Criar as abstraçoes Contas e Operacoes
- A abstração "Contas" irá encapsular o cadastro de contas corrente (Map<Integer,Conta>) juntamente com todas as funcionalidade relativas a uma conta corrente. Esta abstração dependende das classes Persistência e Operacoes. Deixe estas dependências explícitas recebendo referências estas classes no construtor.
- A abstrações "Operacoes" irá encapsular o cadastro de operaçoes (List<Operacao>) juntamente com todas as funcionalidades relativas as operações. Esta abstracao depende da classe Persistencia. Deixe esta dependência explícita no construtor.
- Crie drivers de teste tanto para Contas como para Operacoes. Forneça mocks no lugar das dependências para garantir a independência dos testes.

### Adoção da arquitetura em 3 camadas e do padrão fachada
- Criar uma classe responsável pelas validações eliminando todo o código de negócios da camada de interface com o usuário
- Criar uma classe para fazer o papel de "fachada da camada de negócios". Esta classe deverá disponibilizar um método para cada umadas operações demandadas pela interface com o usuário:
 - Definir conta em uso
 - Operaçao de crédito
 - Operacao de débito
 - Solicita extrato
 - Solicita saldo
 - Solicita saldo medio
 - Total créditos
 - Total débitos
- O construtor da fachada deverá se encarregar também de criar as instancias de Contas, Operacoes e Validacao informando as depenências conforme o caso.
- A fachada deverá ser um singleton

A criação da fachada ira permitir que a dependência da camada de apresentacao para a de lógica se restrinja a apenas uma classe.

- O resultado deve ser semelhante ao da figura:
![Nome](https://github.com/TP-BCopsteinSource/ExercicioRefatoracaoBanco/raw/master/ArquiteturaDaSolucaoRefatorada.jpg)

### Adoção do padrão StatePattern
Cria a interface StateConta
Altera a classe conta para explorar o padrão StatePattern. 
Explora classes aninhadas.

### Adoção do padrão Factory e padrao Factory Method
* para a criação dos StatePattern (Factory)
* para a criacao das operacoes (Factory Methods especificos para deposito e retirada)

### Adoção do padrão Observer - funcionalidade adicional
Criar um dialogo modeless que mantém visível o nome do correntista e o saldo médio com o maior saldo médio da agência. Adotar observer para ser notificado pela persistencia toda vez que esta informação variar.

