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

function initMap() {


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
	


	switchButton 			= document.querySelector('.switch-button');
	switchBtnRight 			= document.querySelector('.switch-button-case.right');
	switchBtnLeft 			= document.querySelector('.switch-button-case.left');
	activeSwitch 			= document.querySelector('.active');

	switchBtnLeft.addEventListener('click', function(){
		switchLeft();
	}, false);

	switchBtnRight.addEventListener('click', function(){
		switchRight();
	}, false);

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

/*
 * per disegnare percorso
 */
function drawroute(){
	coord_partenza = [
			posto_partenza.getPlace().geometry.location.lat(),
			posto_partenza.getPlace().geometry.location.lng()
			];
	
	coord_destinazione = [
			posto_destinazione.getPlace().geometry.location.lat(),
			posto_destinazione.getPlace().geometry.location.lng()
			];

    // funzione ajax che manda roba con post
    
    
 
    var lat1="".concat(coord_partenza[0]);
    var lon1="".concat(coord_partenza[1]);
    
    var lat2="".concat(coord_destinazione[0]);
    var lon2="".concat(coord_destinazione[1]);
	//var date = document.getElementById("passage_date").value;
	
    
    var coordinate="start=".concat(posto_partenza.getPlace().formatted_address).
    				concat("&lat1=").concat(lat1).
    				concat("&lon1=").concat(lon1).
    				concat("&dest=").concat(posto_destinazione.getPlace().formatted_address).
    				concat("&lat2=").concat(lat2).
    				concat("&lon2=").concat(lon2);
					//concat("&date=").concat(date);
				

    var richiesta_completa="tipo_richiesta=".concat(activeSlider).concat("&").concat(coordinate);
 /*   
    $(document).ready(function() {
     function doAjaxPost() {
            $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/coordinate_register",
            data:{"id": 0,"lat1": lat1, "lon1": lon1, "lat2": lat2, "lon2": lon2},
            }
      
    
  
    var xhttp = new XMLHttpRequest();	
    xhttp.open("POST", "/main", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send(richiesta_completa);
    xhttp.onreadystatechange=function()
	{
    	if (this.readyState==4 && activeSlider == "request")
		{
    		passages = JSON.parse(xhttp.response);	//Parso la risposta in un array di passaggi
    		for (i = 0; i < passages.length; i++) 
			{
    			drawRoadFromServer(passages[i]);
    		} 		
    	}
    }
 */
 /*   
    var lat1="".concat(coord_partenza[0]);
    var lon1="".concat(coord_partenza[1]);
    
    var lat2="".concat(coord_destinazione[0]);
    var lon2="".concat(coord_destinazione[1]);
    
    var coordinate="partenza_lat=".concat(lat1).concat("&partenza_lon=").concat(lon1).concat("&destinazione_lat=").concat(lat2).concat("&destinazione_lon=").concat(lon2);
    var richiesta_completa="tipo_richiesta=".concat(activeSlider).concat("&").concat(coordinate);
    
    var xhttp = new XMLHttpRequest();	
    xhttp.open("POST", "Map", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send(richiesta_completa);


    if(_directionsRenderer)
		_directionsRenderer.setMap(null);
	//You can calculate directions (using a variety of methods of transportation) by using the DirectionsService object.    
	var directionsService = new google.maps.DirectionsService();    
	//Define a variable with all map points.    
	var _mapPoints = new Array();    
	//Define a DirectionsRenderer variable.    
	_directionsRenderer = new google.maps.DirectionsRenderer();    
	
	//InitializeMap() function is used to initialize google map on page load. 
	_directionsRenderer.setMap(map);    
	_request = {    
                origin: posto_partenza.getPlace().geometry.location,
                destination: posto_destinazione.getPlace().geometry.location,
                travelMode: google.maps.DirectionsTravelMode.DRIVING    
            };    
    directionsService.route(_request, function (_response, _status) {    
    	if (_status == google.maps.DirectionsStatus.OK) {    
        	_directionsRenderer.setDirections(_response);    
        }    
    });  
}
*/
}

/*
END per disegnare percorso
*/

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
