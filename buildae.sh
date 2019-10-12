# limpa qualquer container do projeto que esteja rodando ou parado
./scripts/clean_docker.sh

./mvnw clean && ./mvnw package

# builda a imagem do projeto e depois a inicia
./scripts/setup_docker.sh
