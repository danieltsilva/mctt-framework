# mctt-framework

Markov Chain Text Transformer framework (MCTT) is a Spring Boot Rest API created to generate random text files from a base text 
file, using Markov Chain Algorithm.

## REST API

**Generate Text Using Markov Chain Algorithm**
----
  Returns json data with the text generated.

* **URL**

  /api/markov
  
* **Request**

      HTTP Method = GET
      Request URI = /api/markov
       Parameters = {}          
          Headers = [Content-Type:"application/problem+json;charset=UTF-8", Origin:"http://localhost:3000"]
             Body = {"outputSize":[integer],
                     "fileName":[string],
                     "order":[integer],
                     "cleanText":[boolean]}

* **Success Response:**

               Status = 200
        Error message = null
              Headers = [Vary:"Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", Access-Control-Allow-Origin:"http://localhost:3000", Content-Type:"application/json;charset=UTF-8"]
         Content type = application/json;charset=UTF-8
                 Body = {"markovText": "STRING GENERATED USING MARKOV CHAIN"}
 
* **Error Response:**

               Status = 404
        Error message = null
              Headers = [Vary:"Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"]
         Content type = null
                 Body = File not found

  OR

               Status = 403
        Error message = null
              Headers = [Vary:"Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"]
         Content type = null
                 Body = Invalid CORS request

**Upload File**
----
  Returns json with file metadata.

* **URL**

  /file/upload/:fileName
  
* **Request**

        HTTP Method     =   POST
        Content-Type    =   multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
        cache-control   =   no-cache
        Content-Disposition = form-data; name="file"; filename="/filepath/fileName.txt
        Headers         = [Content-Type:"application/problem+json;charset=UTF-8", Origin:"http://localhost:3000"]


* **Success Response:**

        {
            "fileName": "sample.txt",
            "fileDownloadUri": "http://localhost:8080/file/download/sample.txt",
            "fileType": "text/plain",
            "size": 388
        }
 
* **Error Response:**

        {
            "timestamp": "2019-05-20T06:07:57.474+0000",
            "status": 404,
            "error": "Not Found",
            "message": "No message available",
            "path": "/file/upload"
        }

  OR

               Status = 403
        Error message = null
              Headers = [Vary:"Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"]
         Content type = null
                 Body = Invalid CORS request


## Run with Docker containers

To run the framework in a Docker container you just need to run the command:

```sh
$ docker build -t mctt-framework .
$ docker run --name mctt-framework -v m2:/root/m2 -p 8080:8080 mctt-framework
```

#### Volume

It is highly recommended the use of volumes for maven dependencies. You can create a named volume with the following code:

```sh
$ docker volume create m2
```

This volume is already being used on the previous `docker run` command.

#### DB Container

The MCTT Framework depends on a running MySQL database, which you can easily be started with the following Docker command:

```sh
$ docker volume create mctt-mysql-data
$ docker run --name mctt-mysql -v mctt-mysql-data:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=animallogic -e MYSQL_DATABASE=animallogic -e MYSQL_USER=admin -e MYSQL_PASSWORD=animallogic -d mysql:5.7
```