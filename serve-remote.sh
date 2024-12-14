echo "remove generated artefacts and folders"
rm -rf app-frontend-remote/dist

npm --prefix app-frontend-remote run start
