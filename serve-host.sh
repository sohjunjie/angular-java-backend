echo "remove generated artefacts and folders"
rm -rf app-frontend-host/dist
rm -rf target
rm -rf keys

echo "create jar artefacts"
mvn clean package -P npm-build

echo "serving jar"
java -jar target/app.jar
