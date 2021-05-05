# IMDB Movies Search Service App

##### Atanan: Arif Çavuş(arcavus - arcavus5428@hotmail.com)
### Tanım

Bu değerlendirme, filmler ve TV şovları bilgileri sunan popüler IMDb web sitesine dayanmaktadır.
Veri kümelerini IMDb Veri Kümelerinde kamuya açık hale getirdiler.
Misyonunuz, kabul etmeyi seçerseniz, aşağıdaki gereksinimleri karşılayabilecek Java'da bir web uygulaması yazmaktır:

Yapılacaklar #1 (kolay):

Typecasting: Kullanıcı tarafından bir oyuncu / aktris adı sağladığı bir sorgu verildiğinde, sistem bu kişinin tiplendirilip yazılmadığını belirlemelidir (çalışmalarının en az yarısı bir türdür).

Yapılacaklar #2 (easy):

Tesadüfü bulun: Kullanıcı tarafından girişin iki oyuncu / oyuncu adı olduğu bir sorgu verildiğinde, uygulama, her iki kişi tarafından paylaşılan bir film veya TV şovu listesiyle yanıt verir.

Requirement #3 (difficult):

Altı dereceli Kevin Bacon: Kullanıcının sorgusu üzerine, kullanıcının girdiği kişi (ör. Oyuncu veya oyuncu) ile Kevin Bacon arasındaki ayrımın derecesini sağlamalısınız.

### İsteklerin Durumu
* Requirement 1 [Yapıldı]
* Requirement 2 [Yapıldı]
* Requirement 3 [Yapıldı]


### Gerekli Bağımlılıklar (0/2) --- Imdb Csv Dataset
*Öncelikle imdb denin public olarak verdiği datasetlere bu linklerden ulaşabiliriz: [https://www.imdb.com/interfaces/](), [https://datasets.imdbws.com/]()
        
    * Bu dosyaları indirip resource altında data klasörüne koymamız gerekmektedir. DataLoader sınıfımızın doğru çalışması için.
        * `name.basics.tsv.gz`
        * `title.akas.tsv.gz`
        * `title.basics.tsv.gz`
        * `title.crew.tsv.gz`
        * `title.episode.tsv.gz`
        * `title.principals.tsv.gz`
        * `title.ratings.tsv.gz`

### Gerekli Bağımlılıklar (1/2) --- MySQL Veritabanı nasıl hazırlanır (for Requirements 1-2)

* Çalışan bir MySQL Sunucunuz varsa, MySQL'i başlatmak için `docker-compose up` komutunu çalıştırın.

* Imdb nin bize sunduğu csv dosyalarını database e atmak için iki seçeneğimiz var:

    * Database imizi export edip onu size göndermem ve import edilmesi.

    * Diğer yol ise `http://localhost:8181/data/load` bu isteği swagger üzerinden çağırıp datalarımızın 
       önce unzip işlemi yapılarak belirttiğimiz dizine atılması ve satır satır okunarak işlenmesi sağlayabiliriz. 
       Load işleminin tamamlandığını log satırının en altında şöyle bir şey yazması gerekli:
       INFO 7086 --- [pool-1-thread-1] c.c.l.movies.service.data.DataLoader     : Total time to load data in ms: ~456223~)    
    *Bu işlem bilgisayarınızın hızına göre değişebiliyor. Data boyutu baya fazla yaklaşık 10 000 000 milyon kayıt atılıyor.
       Burada tablolarımızın performans sorunu yaşamaması için index tanımlamaları yapılmıştır bu sql ler en altta tanımı  bulunmaktadır.
        
### Required Dependencies (2/2) --- How to prepare the Neo4j (for Requirement 3, shortestPath)
* 3 maddenin gerçekleştirilmesi için neo4j graph database i kullandık. Docker ile çalışması aşağıdaki işlemleri gerçekleştiririz. 
    ```docker
      docker run \
              --publish=7474:7474 --publish=7687:7687 \
              --volume=$HOME/neo4j/data:/data \
              --volume=$HOME/neo4j/logs:/logs \
              --volume=$HOME/plugins:/plugins \
              --volume=$HOME/import:/var/lib/neo4j/import \
              neo4j:3.0
    ```
* Genel yetkiler: `neo4j/neo4j` || `neo4j/1234`
        
* Verileri MySQL'den Neo4j'ye yükleme zamanı:

    * Bunun bir yolu, $ HOME / neo4j / data içine cd yapmak (docker komutuna bakın) ve içeriği klasörün içindekilerle değiştirmektir: Dump_for_Neo4j,
     aynısını $ HOME / neo4j / logs için yapın
    
    * Bu işlem için yapabileceğimiz diğer yol ise:
        * `http://localhost:8181/acquaintance-links/prepare` post methodunu çağırarak,
          `ETL işini başlatmak (from MySQL -> to Neo4j)`,tamamlamak çok zaman alıyor
          bu yüzden size 5-8 tamamlanmış adım beklemenizi (loglara bakın) ve ardından derece şartıyla oynamanızı tavsiye ederim.

### Manuel testleri nasıl yapıyoruz?
Bu projede swagger ui entegrasyonu vardır. Oradan ilgili methodlarımızı manuel test edebiliriz. Bu framework un ayarlamalarını swaggerConfig gerçekleştirildi.
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

* Requirement 3, POST `localhost:8181/acquaintance-links` with body:
    ```json
          {
            "sourceFullName":"Matt Damon",
            "targetFullName":"Kevin Bacon"
           } 
    ```
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
  