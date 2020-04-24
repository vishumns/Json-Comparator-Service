Application has following Operation’s and URL’s :

1. Purpose  : To access each request we need JWT token, below URL gives jwt token.
Requirement  : Username and Password in JSON format with the request body  
Credentials  :    Username : “abc”,   Password : “xyz”
URL          :  http://localhost:8080/authenticate    Method : POST


2. Purpose  :  Save the JSON in database.
Requirement  :  1) Give the data in JSON format in body.
               2) Give JWT token in Header with the key “Authorization” (Append the token  with Bearer so it will be like “Bearer akjsdsdlfkasdfkh2r23r233jk23jk”
Return       : It will return JSON id.
URL          : http://localhost:8080/base-json       Method : POST


3.Purpose :  Update/Delete/Get the JSON from database by id.
Requirement  :  1) Give the id in url.
                2) Give JWT token in Header with the key “Authorization” (Append the token  with Bearer so it will be like “Bearer akjsdsdlfkasdfkh2r23r233jk23jk”
Return       :  Delete / Update : will return JSON id.
	              Get  : will return json from database.
URL          :  http://localhost:8080/base-json/{id}      Method : PUT / DELETE / GET


4. Purpose  : Delete/Get all the jsons from database.
Requirement  :  1) Give the id in url.
                2) Give JWT token in Header with the key “Authorization” (Append the token  with Bearer so it will be like “Bearer akjsdsdlfkasdfkh2r23r233jk23jk”
Return       :  Delete / Update : will return all available jsons  or delete all jsons.
	          		Get  : will return JSON from database.
URL  :   http://localhost:8080/base-json       Method : GET / DELETE


5. Purpose  : Compare input json with base json from database and return the difference.
Requirement  : 1) Give the id in url.
		           2) Input json in the body.
               3) Give JWT token in Header with the key “Authorization” (Append the token  with Bearer so it will be like “Bearer akjsdsdlfkasdfkh2r23r233jk23jk”.
Return       : It returns the difference available in base json with respect to input json.
URL          :  http://localhost:8080/base-json/compare/{id}    Method : GET





