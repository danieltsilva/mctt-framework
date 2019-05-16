# mctt-framework

Markov Chain Text Transformer framework.


## DB Container

The MCTT Framework depends on a running MySQL database, which you can easily start a Docker MySQL container with the following command:

```sh
$ docker run --name mctt-mysql -v mctt-mysql-data:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=markovchain -e MYSQL_DATABASE=markovchain -e MYSQL_USER=admin -e MYSQL_PASSWORD=markovchain -d mysql:5.7
```