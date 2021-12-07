# IMDB Movies Search Service App

This rating is based on the popular IMDb website that offers movies and TV shows information.
They made their datasets publicly available on IMDb Datasets.
Your mission is to write a web application in Java that, if you choose to accept it, can meet the following requirements:

To-Do #1 (easy):

Typecasting: Given a query by the user where they provide an actor/actress name, the system must determine whether that person is typecast (at least half of their work is a genre).

To-Do #2 (easy):

Find the coincidence: When given a query by the user where the input is two actor/actress names, the app responds with a list of movies or TV shows shared by both people.

Requirement #3 (difficult):

Six grades of Kevin Bacon: Upon the user's query, you must provide the grade of distinction between the person the user is entering (eg Gamer or player) and Kevin Bacon.

### Status of Requests
* Requirement 1 [Done]
* Requirement 2 [Done]
* Requirement 3 [Not Done]


### Required Dependencies (0/2) --- Imdb Csv Dataset
*First of all, we can access the datasets publicly provided by imdb from these links: [https://www.imdb.com/interfaces/](), [https://datasets.imdbws.com/]()

    * We need to download these files and put them in the data folder under resource. For our DataLoader class to work correctly.
        * `name.basics.tsv.gz`
        * `title.akas.tsv.gz`
        * `title.basics.tsv.gz`
        * `title.crew.tsv.gz`
        * `title.episode.tsv.gz`
        * `title.principals.tsv.gz`
        * `title.ratings.tsv.gz`

### Required Dependencies (1/2) --- How to prepare MySQL Database (for Requirements 1-2)

* If you have a running MySQL Server, run `docker-compose up` to start MySQL.

* We have two options for importing the csv files that Imdb offers us to the database:

    * Exporting our database and sending it to you and importing it.

    * The other way is `http://localhost:8181/data/load` by calling this request via swagger and getting our data
      We can first unzip it to the directory we specify and process it by reading it line by line.
      It should write something like this at the bottom of the log line that the load process is complete:
      INFO 7086 --- [pool-1-thread-1] c.c.l.movies.service.data.DataLoader : Total time to load data in ms: ~456223~)
      *This process may vary depending on the speed of your computer. The data size is too much, about 10 million records are thrown.
      Here, index definitions are made so that our tables do not have performance problems. These sqls are defined at the bottom.

### How do we run manual tests?
This project has swagger ui integration. From there, we can manually test our related methods. The swaggerConfig configurations of this framework have been performed.
By entering localhost:8181/swagger-ui.html we can see the links that we can run from there.
* Load data (csv -> mysql) via http endpoint: 
    * localhost:8181/data/load
* Requirement 1, use (GET):     
    * `localhost:8181/search/names/?name=Keanu Reeves&full-fetch=false`
    * `localhost:8181/search/names/?name=David Schwimmer&full-fetch=false`
    * `localhost:8181/search/names/?name=Steve McQueen&full-fetch=false`
    * `localhost:8181/search/names/?name=Matt LeBlanc&full-fetch=false`
* Requirement 2, use: 
    * POST with body:
       ```json
           {
            "names":["Angelina Jolie", "Brad Pitt"]
           }
        ```
       To URL: `localhost:8181/search/names/coincidence`
   
    * POST with body:
       ```json
            {
             "names":["Orlando Bloom", "Keira Knightley"]
            }    
        ```
        To URL: `localhost:8181/search/names/coincidence`
          
    * POST with body:
       ```json
             {
              "names":["David Schwimmer", "Matt LeBlanc"]
             }    
        ```
        To URL: `localhost:8181/search/names/coincidence`
          
    * POST with body:
       ```json
            {
              "names":["George Clooney", "Brad Pitt"]
             }    
        ```     
        To URL: `localhost:8181/search/names/coincidence`
             
    * POST with body:
       ```json
             {
              "names":["George Clooney", "Matt Damon"]
             }    
        ```
        To URL: `localhost:8181/search/names/coincidence`
             
    * POST with body:
       ```json
              {
               "names":["George Clooney", "Matt Damon", "Brad Pitt"]
              }   
        ```    
         To URL: `localhost:8181/search/names/coincidence`
             
    * POST with body:
       ```json
             {
              "names":["John Travolta", "Uma Thurman", "Samuel L. Jackson", "Bruce Willis"]
             }    
       ``` 
        To URL: `localhost:8181/search/names/coincidence`
             
    * POST with body:
        ```json
             {
                "names":["Al Pacino", "Robert De Niro", "Val Kilmer", "Jon Voight"]
             }
        ```
         To URL: `localhost:8181/search/names/coincidence`


### MySQL Indexes Created
1) aka -> title_id
    ```sql
    ALTER TABLE `movies_search_service_db`.`aka` 
    ADD INDEX `title_id_idx` (`title_id` ASC);
    ```
2) basic -> primary_title
    ```sql
    ALTER TABLE `movies_search_service_db`.`basic` 
    ADD INDEX `primary_title_idx` (`primary_title` ASC);
    ```
3) basic -> original_title
    ```sql
    ALTER TABLE `movies_search_service_db`.`basic` 
    ADD INDEX `original_title_idx` (`original_title` ASC);
    ```
4) episode -> parent_tconst
    ```sql
    ALTER TABLE `movies_search_service_db`.`episode` 
    ADD INDEX `parent_tconst_idx` (`parent_tconst` ASC);
    ```
5) basic -> genres
    ```sql
    ALTER TABLE `movies_search_service_db`.`basic` 
    ADD INDEX `genres_idx` (`genres` ASC);
    ```
6) principal -> tconst
    ```sql
    ALTER TABLE `movies_search_service_db`.`principal` 
    ADD INDEX `principal_tconst_idx` (`tconst` ASC);
    ```
7) name -> primary_name
    ```sql
    ALTER TABLE `movies_search_service_db`.`name` 
    ADD INDEX `primary_name_idx` (`primary_name` ASC);
    ```
8) principal -> nconst
    ```sql
    ALTER TABLE `movies_search_service_db`.`principal` 
    ADD INDEX `principal_nconst_idx` (`nconst` ASC);
    ```
##
### Distinct Genres in Database
0) "Film-Noir"
1) "Action"
2) "War"
3) "History"
4) "Western"
5) "Documentary"
6) "Sport"
7) "Thriller"
8) "News"
9) "Biography"
10) "Adult"
11) "Comedy"
12) "Mystery"
13) "Musical"
14) "Short"
15) "Talk-Show"
16) "Adventure"
17) "Horror"
18) "Romance"
19) "Sci-Fi"
20) "Drama"
21) "Music"
22) "Game-Show"
23) "Crime"
24) "Fantasy"
25) "Animation"
26) "Family"
27) "Reality-TV"
##
### Distinct categories in principal table.
* `select distinct category from principal limit 30590275;`
* Results:
  * self
  * director
  * cinematographer
  * composer
  * producer
  * editor
  * actor
  * actress
  * writer
  * production_designer
  * archive_footage
  * archive_sound
  