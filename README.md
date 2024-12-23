# Ecommerce

 Um sistema desenvolvido visando colocar em prática estratégias de tolerância a falhas. Composto de 4 serviços: Eccomerce, Store, Fidelity e Exchange.

 Os 4 serviços foram implementados utilizando Java Spring. Para containerização e orquestração foram utilizados Docker, Docker compose e Docker swarm.

## Pré-requisitos

- Docker instalado;
- Docker Compose instalado.

## Passos para rodar o sistema

### Usando Docker Compose

1. Clone o repositório:

    ```bash
    git clone https://github.com/Tolerancia-a-falhas/ecommerce.git
    ```

2. Entre na pasta `ecommerce`:

    ```bash
    cd ecommerce
    ```

3. Execute os seguintes comandos para construir as imagens de cada serviço:

    ```bash
    docker build -t ecommerce:latest ./ecommerce
    docker build -t exchange:latest ./exchange
    docker build -t fidelity:latest ./fidelity
    docker build -t store:latest ./store
    ```

4. Após construir as imagens, o docker swarm deve ser inicializado:

    ```bash
    docker swarm init
    ```

5. Use o comando abaixo para rodar todos os serviços:

    ```bash
    docker stack deploy -c docker-compose.yml ecommerce-stack
    ```

Para remover os serviços e encerrar a stack:

```bash
docker stack rm ecommerce-stack
```
