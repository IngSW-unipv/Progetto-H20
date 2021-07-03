
// to submit stuff without reloading the page
// https://code-boxx.com/submit-form-without-refreshing-page/

//for map styling and initialization
var input_partenza = '';
var input_destinazione = '';
var posto_partenza ='';
var posto_destinazione='';
var coord_partenza =null;
var coord_destinazione =null;
var map; 

var _directionsRenderer;


var activeSlider = "request";
var switchButton; 			
var switchBtnRight; 		
var switchBtnLeft; 			
var activeSwitch; 	

var xhttp_response;	
var xhttp_response_corrente;

var response_posizione_partenza;
var response_posizione_destinazione;
var response_id_viaggio;

var percorsi_response_processati;

var infoWindow;
var div_infoWindow;

var bottone_prenotaViaggio;
var form_infoWindow;
var polylines_cerca;
var boundaries_for_map_refit; 
var marker_for_user;
var input_data;
var distanza_percorso_autista;

var check_selezione="passeggero";


function initMap() {
	
	document.getElementById("form_infoWindow").style.display="none";

	//loads elements for autocomplete
	input_partenza = document.getElementById('partenza');
	posto_partenza = new google.maps.places.Autocomplete(input_partenza); 

	input_destinazione = document.getElementById('destinazione');
	posto_destinazione = new google.maps.places.Autocomplete(input_destinazione);


	// Styles a map in night mode.
	map = new google.maps.Map(document.getElementById('googlemaps'), {
		center: {lat: 45.1840878, lng: 9.1544816},
		zoom: 17,
		styles: [
			{elementType: 'geometry', stylers: [{color: '#242f3e'}]},
			{elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
			{elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
			{
				featureType: 'administrative.locality',
				elementType: 'labels.text.fill',
				stylers: [{color: '#d59563'}]
			},
			{
				featureType: 'poi',
				// for POI remove
				stylers: [{ "visibility": "off" }]
			//elementType: 'labels.text.fill',
			//stylers: [{color: '#d59563'}]
			},
			{
				featureType: 'poi.park',
				elementType: 'geometry',
				stylers: [{color: '#263c3f'}]
			},
			{
				featureType: 'poi.park',
				elementType: 'labels.text.fill',
				stylers: [{color: '#6b9a76'}]
			},
			{
				featureType: 'road',
				elementType: 'geometry',
				stylers: [{color: '#38414e'}]
			},
			{
				featureType: 'road',
				elementType: 'geometry.stroke',
				stylers: [{color: '#212a37'}]
			},
			{
				featureType: 'road',
				elementType: 'labels.text.fill',
				stylers: [{color: '#9ca5b3'}]
			},
			{
				featureType: 'road.highway',
				elementType: 'geometry',
				stylers: [{color: '#746855'}]
			},
			{
				featureType: 'road.highway',
				elementType: 'geometry.stroke',
				stylers: [{color: '#1f2835'}]
			},
			{
				featureType: 'road.highway',
				elementType: 'labels.text.fill',
				stylers: [{color: '#f3d19c'}]
			},
			{
				featureType: 'transit',
				elementType: 'geometry',
				stylers: [{color: '#2f3948'}]
			},
			{
				featureType: 'transit.station',
				elementType: 'labels.text.fill',
				stylers: [{color: '#d59563'}]
			},
			{
				featureType: 'water',
				elementType: 'geometry',
				stylers: [{color: '#17263c'}]
			},
			{
				featureType: 'water',
				elementType: 'labels.text.fill',
				stylers: [{color: '#515c6d'}]
			},
			{
				featureType: 'water',
				elementType: 'labels.text.stroke',
				stylers: [{color: '#17263c'}]
			}
			]
	});
	
	var today = new Date();
	var dd = String(today.getDate()).padStart(2, '0');
	var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
	var yyyy = today.getFullYear();
	today = yyyy+ '-'+mm+'-'+dd;
	
	input_data=document.getElementById("input_data");
	input_data.setAttribute("min", today);

	switchButton 			= document.querySelector('.switch-button');
	switchBtnRight 			= document.querySelector('.switch-button-case.right');
	switchBtnLeft 			= document.querySelector('.switch-button-case.left');
	activeSwitch 			= document.querySelector('.active');

	switchBtnLeft.addEventListener('click', function(){
		switchLeft();
		document.getElementById("div_data").style.gridColumn ="span 2";
		document.getElementById("form_ora_autista").style.display="none";
		document.getElementById("form_field_interno2").style.display="none";
		document.getElementById("BtnCerca").value="Cerca";
		check_selezione = "passeggero";		
		document.getElementById("BtnRegistra").style.display="none";

	}, false);

	switchBtnRight.addEventListener('click', function(){
		switchRight();
		document.getElementById("div_data").style.gridColumn ="span 1";
		document.getElementById("form_ora_autista").style.display="flex";
		document.getElementById("form_field_interno2").style.display="grid";
		document.getElementById("BtnCerca").value="Disegna";
		check_selezione = "autista";
	}, false);
	
	
	//listener for hover on routes
	
}

//for slider settings
function switchLeft(){
	switchBtnRight.classList.remove('active-case');
	switchBtnLeft.classList.add('active-case');
	activeSwitch.style.left 						= '0%';
	activeSlider="request";
}

function switchRight(){
	switchBtnRight.classList.add('active-case');
	switchBtnLeft.classList.remove('active-case');
	activeSwitch.style.left 						= '50%';
	activeSlider="offer";
}


function submitViaggio(){
	document.getElementById("form_infoWindow2").submit();
}


/*
 * per disegnare percorso
 */
function drawroute(){
	boundaries_for_map_refit = new google.maps.LatLngBounds();

	coord_partenza = [
			posto_partenza.getPlace().geometry.location.lat(),
			posto_partenza.getPlace().geometry.location.lng()
			];
	
	coord_destinazione = [
			posto_destinazione.getPlace().geometry.location.lat(),
			posto_destinazione.getPlace().geometry.location.lng()
			];
			
			
	if(_directionsRenderer)
		_directionsRenderer.setMap(null);
	//You can calculate directions (using a variety of methods of transportation) by using the DirectionsService object.    
	var directionsService = new google.maps.DirectionsService();    
	//Define a variable with all map points.    
	var _mapPoints = new Array();    
	//Define a DirectionsRenderer variable.    
	_directionsRenderer = new google.maps.DirectionsRenderer({
		suppressPolylines: true});    
	
	//InitializeMap() function is used to initialize google map on page load. 
	
	   
	_request = {    
                origin: posto_partenza.getPlace().geometry.location,
                destination: posto_destinazione.getPlace().geometry.location,
                travelMode: google.maps.DirectionsTravelMode.DRIVING    
            };    
    
	//per aggiungere coordinate di chi cerca a lista di boundaries per rifit
	boundaries_for_map_refit.extend(posto_partenza.getPlace().geometry.location);
	boundaries_for_map_refit.extend(posto_destinazione.getPlace().geometry.location);

        
    _directionsRenderer.setOptions({
		polylineOptions: {
		strokeColor: "red",
		strokeWeight: 7
		}
	});
	
	polylines_cerca=[];
	
    directionsService.route(_request, function (_response, _status) {    
    	if (_status == google.maps.DirectionsStatus.OK) {    
        	disegna_percorso_principale(_response, polylines_cerca);    
        }    
    }); 
          

	    // funzione ajax che manda roba con post
	    
	var lat1="".concat(coord_partenza[0]);
	var lon1="".concat(coord_partenza[1]);
	 
	var lat2="".concat(coord_destinazione[0]);
	var lon2="".concat(coord_destinazione[1]);
	    
	if(check_selezione==="passeggero"){

	    var coordinate="partenza_lat=".concat(lat1).concat("&partenza_lon=").concat(lon1).concat("&destinazione_lat=").concat(lat2).concat("&destinazione_lon=").concat(lon2);
	    var richiesta_completa="data_ricerca=".concat(input_data.value).concat("&").concat(coordinate);

	    var xhttp = new XMLHttpRequest();	
	    xhttp.open("POST", "mappa_prova", true);
	    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	    xhttp.send(richiesta_completa);
	
	
	
		xhttp.onreadystatechange = function() {
	    	if (this.readyState == 4 && this.status == 200) {
	       		//to fetch the response and parse in json
				xhttp_response = JSON.parse(xhttp.responseText);
				
				//to cycle through the length of the response
				for (i=0; i<xhttp_response.length; i++){
					percorsi_response_processati = [];
					//to save each response position and specs in it's own array
					xhttp_response_corrente=xhttp_response[i];
					response_id_viaggio = xhttp_response[i].tripId;
					var response_partenza_lat = parseFloat(xhttp_response[i].startX);
					var response_partenza_lon = parseFloat(xhttp_response[i].startY);
					response_posizione_partenza = new google.maps.LatLng (response_partenza_lat, response_partenza_lon);
					var response_destinazione_lat = parseFloat(xhttp_response[i].endX);
					var response_destinazione_lon = parseFloat(xhttp_response[i].endY);
					response_posizione_destinazione = new google.maps.LatLng (response_destinazione_lat, response_destinazione_lon);
					
					//per aggiungere coordinate di estremi del viaggio di cisacun offerente a lista di coordinate su cui fare refit
					boundaries_for_map_refit.extend(response_posizione_partenza);
					boundaries_for_map_refit.extend(response_posizione_destinazione);
					
					//for each object to draw a route
					
					disegna_percorsi_autisti(i, response_posizione_partenza, response_posizione_destinazione);
	
					//for each adds a listener to mouseover
					
				}
	       }
	    };
	}
	
	
	if(check_selezione==="autista"){
		
		document.getElementById("BtnRegistra").style.display="flex";
		//altrimenti devo creare un form per disegnare percorso di autista
		document.getElementById("indirizzo_partenza_autista").setAttribute("value", posto_partenza.getPlace().formatted_address);
		document.getElementById("X_partenza_autista").setAttribute("value", coord_partenza[0]);
		document.getElementById("Y_partenza_autista").setAttribute("value", coord_partenza[1]);
		document.getElementById("indirizzo_destinazione_autista").setAttribute("value", posto_destinazione.getPlace().formatted_address);
		document.getElementById("X_destinazione_autista").setAttribute("value", coord_destinazione[0]);
		document.getElementById("Y_destinazione_autista").setAttribute("value", coord_destinazione[1]);
		document.getElementById("posti_totali_autista").setAttribute("value", document.getElementById("input_posti").value);
		document.getElementById("viaggio_date_autista").setAttribute("value", document.getElementById("input_data").value);
		document.getElementById("viaggio_ora_autista").setAttribute("value", document.getElementById("input_ora").value);
		document.getElementById("tariffa_km_form").setAttribute("value", document.getElementById("tariffa_km").value);

		/*
		data_complessa = new Date(document.getElementById("input_data").value + " "+ document.getElementById("input_ora").value);
		data_inviare = document.getElementById("input_data").value +" "+document.getElementById("input_ora").value+":00";
		document.getElementById("viaggio_date_autista").setAttribute("value", "2012:02:01");
		*/
		map.fitBounds(boundaries_for_map_refit);
		map.setZoom(map.getZoom()-1);
		map.panBy(300,0);

	}
   
}

function disegna_percorsi_autisti(contatore, posizione_partenza, posizione_destinazione){
	
	var this_directions_service = new google.maps.DirectionsService(); 
	var this_directions_renderer = new google.maps.DirectionsRenderer({
		suppressPolylines: true
	}); 
	var _mapPoints = new Array();    
	var this_polylines =[];

	this_directions_renderer.setMap(map);    
	_request = {    
		origin: response_posizione_partenza,
		destination: response_posizione_destinazione,
		travelMode: google.maps.DirectionsTravelMode.DRIVING    
	};    
	this_directions_service.route(_request, function (_response, _status) {    
		if (_status == google.maps.DirectionsStatus.OK) {    
			//this_directions_renderer.setDirections(_response);  
			renderDirectionsPolylines(_response, this_polylines, contatore);  
		}    
	}); 
			    
	
	percorsi_response_processati.push({
		contatore: contatore, 
		service: this_directions_service,
		renderer: this_directions_renderer,
		polyline: this_polylines
	});
	map.fitBounds(boundaries_for_map_refit);
	map.panBy(300,0);
}


function disegna_percorso_principale(response, polylines_param) {
	for (var i=0; i<polylines_param.length; i++) {
    	polylines_param[i].setMap(null);
  	}
	var legs = response.routes[0].legs;
	var coords = [];
	for (i = 0; i < legs.length; i++) {
    	var steps = legs[i].steps;
		for (j = 0; j < steps.length; j++) {
			
			var nextSegment = steps[j].path;			
			for (k = 0; k < nextSegment.length; k++) {
				var this_lat=nextSegment[k].lat();
				var this_lng=nextSegment[k].lng();
				coords.push({
					lat: this_lat,
					lng: this_lng
				});
			}
			
			polylines_param = new google.maps.Polyline({
			    path: coords,
			    strokeColor: "red",
			    strokeOpacity: 1,
			    strokeWeight: 5,
			});
		}
	}
			polylines_param.setMap(map);
			
			distanza_percorso_autista = google.maps.geometry.spherical.computeLength(polylines_param.getPath());
			distanza_percorso_autista=distanza_percorso_autista/1000;
			document.getElementById("lunghezza_percorso").setAttribute("value", distanza_percorso_autista);
}

var posizione;
var idkcosa;
var evento; 

function renderDirectionsPolylines(response, polylines, contatore  ) {
	
	for (var i=0; i<polylines.length; i++) {
    	polylines[i].setMap(null);
  	}
	var legs = response.routes[0].legs;
	var coords = [];
	for (i = 0; i < legs.length; i++) {
    	var steps = legs[i].steps;
		for (j = 0; j < steps.length; j++) {
			
			var nextSegment = steps[j].path;			
			for (k = 0; k < nextSegment.length; k++) {
				var this_lat=nextSegment[k].lat();
				var this_lng=nextSegment[k].lng();
				coords.push({
					lat: this_lat,
					lng: this_lng
				});
			}
			
			polylines = new google.maps.Polyline({
			    path: coords,
			    strokeColor: "cyan",
			    strokeOpacity: 0.2,
			    strokeWeight: 4,
			  });
	
			google.maps.InfoWindow.prototype.opened = false;
			infowindow = new google.maps.InfoWindow();	
			var infoWindow_selezionato=0;		

			polylines.setMap(map);
			
			
			google.maps.event.addListener(polylines, 'click', function(evt) {
				if(marker_for_user) 
					marker_for_user.setMap(null);
				marker_for_user = new google.maps.Marker({
					position: evt.latLng,
					map: map,
					title: "posizione iniziale"
				});
				var indirizzo_di_incontro=document.getElementById("indirizzo_di_incontro");
				var geocoder = new google.maps.Geocoder();
				geocoder.geocode({ location: evt.latLng }, (results, status) => {
	   				if (status === "OK") {
						var var_indirizzo_di_incontro=results[0].address_components[2].long_name+ ", " +results[0].address_components[1].long_name+", "+results[0].address_components[0].long_name;
						indirizzo_di_incontro.value=var_indirizzo_di_incontro;
						document.getElementById("label_indirizzo_di_incontro").innerHTML=""+var_indirizzo_di_incontro;
					}
				});
			});
			
			google.maps.event.addListener(polylines, 'mouseover', function(evt) {
				
				polylineOptions = {
		  			strokeColor: "blue",
		  			strokeOpacity: 1,
		  			strokeWeight: 6,
					
				};
				polylines.setOptions(polylineOptions);
				posizione = evt;
				if(!infoWindow || !infoWindow.opened ){
					infoWindow = new google.maps.InfoWindow({
						position: evt.latLng,
						map: map
				    });
					
					//maby export to geocode function
					
					var response_partenza_lat = parseFloat(xhttp_response[contatore].startX);
					var response_partenza_lon = parseFloat(xhttp_response[contatore].startY);
					response_posizione_partenza = new google.maps.LatLng (response_partenza_lat, response_partenza_lon);
					var response_destinazione_lat = parseFloat(xhttp_response[contatore].endX);
					var response_destinazione_lon = parseFloat(xhttp_response[contatore].endY);
					response_posizione_destinazione = new google.maps.LatLng (response_destinazione_lat, response_destinazione_lon);
					var indirizzo_partenza = xhttp_response[contatore].start;
					var indirizzo_destinazione = xhttp_response[contatore].end;
					//elementi di autista
					var response_posti_disponibili = parseFloat(xhttp_response[contatore].postiLiberi);
					var bagagliaio_libero= parseFloat(xhttp_response[contatore].bagagliaioLibero);
					var licensePlate= xhttp_response[contatore].licensePlate;
					var tariffa= parseFloat(xhttp_response[contatore].tariffa);
					var avgPunteggio= parseFloat(xhttp_response[contatore].avgPunteggio);
					var nomeCognome = xhttp_response[contatore].lastName+" "+xhttp_response[contatore].firstName+ "  ";
					var response_data_viaggio = xhttp_response[contatore].tripDate;
					
					for (var i=0; i<avgPunteggio; i++) {
				    	nomeCognome+="★";
				  	}
									div_infoWindow = document.getElementById("id_infoWindow");
									var dati=document.getElementById("dati");
									dati.innerHTML="<h3>Autista: "+nomeCognome+"</h3><ul><li>partenza: "+indirizzo_partenza +"</li><li>destinazione: "+indirizzo_destinazione +"</li><li>distanza: "+ (google.maps.geometry.spherical.computeLength(polylines.getPath())/1000).toFixed(1) + "km</li></ul>";
									form_infoWindow = document.getElementById("form_infoWindow");
									var totalSeats = document.getElementById("totalSeats");
									totalSeats.innerHTML="liberi: " +response_posti_disponibili ;
									var id_percorso=document.getElementById("tripId");
									id_percorso.value=""+xhttp_response[contatore].tripId;
									var label_data=document.getElementById("data_prenotazione");
									label_data.textContent= response_data_viaggio;
									var cln=document.getElementById("form_infoWindow");
									var itm=cln.cloneNode(true);

									infoWindow.setContent(
										itm
							   		);
									itm.style.display="block";
									
									
									infoWindow.opened = true;
				}
      		});

			google.maps.event.addListener(polylines, 'mouseout', function(evt) {
				polylineOptions = {
				    strokeColor: "cyan",
				    strokeOpacity: 0.2,
				    strokeWeight: 4,
				};
				
				setTimeout(() => {
					polylines.setOptions(polylineOptions);
				}, 3000);
				
				
				setTimeout(() => {
					if(infoWindow) {
						infoWindow.close();
						infoWindow.opened = false;
					}
				}, 100000);
			});
			
			google.maps.event.addListener(map, 'click', function(evt) {
				
				if(infoWindow) {
					infoWindow.close();
					infoWindow.opened = false;
				}
				marker_for_user.setMap(null);

			});

	    }
  	}
}
	/*
 * END per disegnare percorso
 * */


function partenzaout(){
	if(posto_partenza.getPlace()){
		coord_partenza = [
			posto_partenza.getPlace().geometry.location.lat(),
			posto_partenza.getPlace().geometry.location.lng()
			];
	}
}


function destinazioneout(){
	if(posto_destinazione.getPlace()){
		coord_destinazione = [
			posto_destinazione.getPlace().geometry.location.lat(),
			posto_destinazione.getPlace().geometry.location.lng()
			];
	}
}
