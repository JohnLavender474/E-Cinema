# ECinema
## A Spring Boot Project by John Lavender

---------------------

To run this app, you will need to provide an email address and an app-specific password as environment variables. 
This email will be used as the "business" email, i.e. the email from which all "business" or "admin" messages will come from.
To generate an app-specific password using gmail, follow the steps at https://support.google.com/accounts/answer/185833?hl=en.

Make sure that Docker is running, open the Terminal and cd into the directory containing the Dockerfile, and input the 
following commands. Do not include quotation marks for env vars.

        $ docker network create ecinema-network

        $ docker container run —name mysqldb —network ecinema-network -e MYSQL_DATABASE=ecinemadb -e MYSQL_ROOT_PASSWORD=root -d mysql:8

        $ docker image build -t ecinema .

            ("email" = email address)
            ("password" = app-specific password for email)

        $ docker container run --network ecinema-network --name ecinema-container -p 8080:8080 -e emailAddress="email" -e emailPassword="password" -d ecinema

            (optional command to see log output of app, "id" = id of the running container)

        $ docker container logs -f "id"

Now you may visit @ "localhost:8080/" to begin using the app!