Projekt se pokreće sljedećim redom:
	1) gradle build za svaki od 5 servisa
		- proizvodi Dockerfile za svaki mikroservis
	2) docker-compose -f docker-compose-infrastructure.yml up --build -d
		- kreira image iz Dockerfilea za eureka server i config server te pokreće containere sa njima
	3) docker-compose -f docker-compose-services.yml up --build -d
		- kreira image iz Dockerfilea za temperature, humidity i aggregator mikroservise te pokreće containere sa njima

Pričekati da se bootaju svi mikroservisi te se zatim može pristupiti:
	1) eureka konzoli na adresi: localhost:8761
	2) agregatoru očitanja na: localhost:8080/get-readings (dobije se json s očitanjima)

Config file koji config server poslužuje agregatoru kad se agregator boota nalazi se ovdje: https://github.com/MarinRadja/rassus-app-prop
