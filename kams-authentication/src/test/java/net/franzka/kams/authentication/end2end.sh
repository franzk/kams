#Registration
#Erreurs de contraintes
curl --location 'http://localhost:8090/register' --header 'Content-Type: application/json' \
--data '{"email": "foo","password": "bar"}'

curl --location 'http://localhost:8090/register' --header 'Content-Type: application/json' \
--data '{"email": "az@az.com","password": "Aa1+mmmmmmmm"}'

# !! --> Get the activation token manually in database

#Activation
curl --location 'http://localhost:8090/activate?activationToken=ActualActivationToken'

#Login
curl --location 'http://localhost:8090/auth/login' --header 'Content-Type: application/json' \
--data '{"email": "az@az.com","password": "Aa1+mmmmmmmm"}'