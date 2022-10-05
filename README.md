# ECinema
## Spring Boot Project by John Lavender

-----------------------------------------

This is a mock website application with the main purpose of allowing customers to purchase tickets to see movie showings.
There are two types of user authorities: customers and admins. A user account can be either a customer, moderator, or admin 
account or any combination of the three. Customers are able to save payment card information to their account and purchase 
tickets. Admins are able to add, edit, and delete entities such as movies, showtimes, showrooms, and so on. Admins can also 
lock users' accounts and reset a user's password. Moderators have the single ability to delete reviews if necessary.

The app has many cool features like the ability to write reviews for movies, book multiple seats each with a different
ticket type, like and dislike tickets, and so much more!

To run this app yourself, you will need to provide an email address and an app-specific password as environment variables. 
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