#Registration
curl --location 'http://localhost:8090/register' --header 'Content-Type: application/json' \
--data '{"email": "foo","password": "bar"}'

#Get the activation token manually in database

#Activation
curl --location 'http://localhost:8090/activate?activationToken=ActualActivationToken'