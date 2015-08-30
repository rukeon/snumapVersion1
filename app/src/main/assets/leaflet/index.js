/*
 * @copyright 2015 commenthol
 * @license MIT
 */

/* globals L */




// (function() {

//     init(73.5);
// })();
var map;

function init() {
    var minZoom = 0,
        maxZoom = 5,
        img = [
            5001,  // original width of image
            2501   // original height of image
        ];

    // create the map
    map = new L.map('map',{
            minZoom: minZoom,
            maxZoom: maxZoom,
            zoomControl:false
        });

    // assign map and image dimensions
    var rc = new L.RasterCoords(map, img);
    // set the bounds on map
    rc.setMaxBounds();

    // set the view on a marker ... // 처음 지도뷰가 생성될 때 포인트 세팅 및 zoom 세팅
    map.setView(rc.unproject([2500, 1250]), 1);

    // set marker at the image bound edges
    var layerBounds = L.layerGroup([
        L.marker(rc.unproject([0,0])).bindPopup('[0,0]'),
        L.marker(rc.unproject(img)).bindPopup(JSON.stringify(img))
    ]);
    // map.addLayer(layerBounds);

    // set markers on click events in the map
    map.on('click', function(event){
        var coords = rc.project(event.latlng);
        var marker = L.marker(rc.unproject(coords))
            .addTo(layerBounds);
        marker.bindPopup('['+Math.floor(coords.x)+','+Math.floor(coords.y)+']')
            .openPopup();
    });

    // geoJson definitions for country
    var countries = [
        {   type: "Feature",
            properties: { name: "Iceland" },
            geometry: { type: "Point", coordinates: [1258, 911] }
        },
        {   type: "Feature",
            properties: { name: "Ireland" },
            geometry: { type: "Point", coordinates: [1324, 1580] }
        },
        {   type: "Feature",
            properties: { name: "England" },
            geometry: { type: "Point", coordinates: [1498, 1662] }
        },
        {   type: "Feature",
            properties: { name: "France" },
            geometry: { type: "Point", coordinates: [1608, 1918] }
        },
        {   type: "Feature",
            properties: { name: "Italia" },
            geometry: { type: "Point", coordinates: [1923, 2093] }
        },
        {   type: "Feature",
            properties: { name: "Hispania" },
            geometry: { type: "Point", coordinates: [1374, 2148] }
        },
    ];

    var layerCountries = L.geoJson(countries, {
        // correctly map the geojson coordinates on the image
        coordsToLatLng: function(coords) {
            return rc.unproject(coords);
        },
        // add a popup content to the marker
        onEachFeature: function (feature, layer) {
            if (feature.properties && feature.properties.name) {
                layer.bindPopup(feature.properties.name);
            }
        },
        pointToLayer: function (feature, latlng) {
            // 보라색으로 동그란 원이 생성된다. 
            return L.circleMarker(latlng, {
                    radius: 8,
                    fillColor: "#800080",
                    color: "#D107D1",
                    weight: 1,
                    opacity: 1,
                    fillOpacity: 0.8
                });
        }
    });
    // map.addLayer(layerCountries);


    var imgDir = "leaflet-0.7.3/images/";

    var redMarker = L.icon({
        iconUrl: imgDir + 'marker-icon-red.png',
        iconRetinaUrl: imgDir + 'marker-icon-red-2x.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [-0, -31],
        shadowUrl: imgDir + 'marker-shadow.png',
        shadowSize: [41, 41],
        shadowAnchor: [14, 41]
    });

    // geoJson definitions
    var geoInfo = [
        {   "type": "Feature",
            "properties": { "name": "Mare Germanicum", },
            "geometry": { "type": "Point", "coordinates": [1589, 1447] },
        },
        {   "type": "Feature",
            "properties": { "name": "Mare Balticum", },
            "geometry": { "type": "Point", "coordinates": [2090, 1407] },
        },
        {   "type": "Feature",
            "properties": { "name": "Mare Mediteraneum", },
            "geometry": { "type": "Point", "coordinates": [2028, 2453] },
        },
        {   "type": "Feature",
            "properties": { "name": "Mare Maggiore", },
            "geometry": { "type": "Point", "coordinates": [2623, 1918] },
        },
    ];

    var layerGeo = L.geoJson(geoInfo, {
        // correctly map the geojson coordinates on the image
        coordsToLatLng: function(coords) {
            return rc.unproject(coords);
        },
        // add a popup content to the marker
        onEachFeature: function (feature, layer) {
            if (feature.properties && feature.properties.name) {
                layer.bindPopup(feature.properties.name);
            }
        },
        pointToLayer: function (feature, latlng) {
            return L.marker(latlng, {icon: redMarker});
        },
    });
    // map.addLayer(layerGeo);

    // // add layer control object
    // L.control.layers({},{
    //         "Countries": layerCountries,
    //         "Bounds": layerBounds,
    //         "Info": layerGeo
    //     }).addTo(map);

    // the tile layer containing the image generated with gdal2tiles --leaflet ...
    L.tileLayer('./tiles/{z}/{x}/{y}.png', {
        noWrap: true,
        attribution: '',
    }).addTo(map);
}


function myFunction(p1) {
    // 요 아래 것이 polyline 코드 관련된 부분
    // --- Arrow, with animation to demonstrate the use of setPatterns ---
    var arrow = L.polyline([[p1, -90], [73.5, -75]], {}).addTo(map);
    var arrowHead = L.polylineDecorator(arrow).addTo(map);
    
    var arrowOffset = 0;
    var anim = window.setInterval(function() {
        arrowHead.setPatterns([
            {offset: arrowOffset+'%', repeat: 0, symbol: L.Symbol.arrowHead({pixelSize: 15, polygon: false, pathOptions: {stroke: true}})}
        ])
        if(++arrowOffset > 100)
            arrowOffset = 0;
    }, 50); // 속도다.(화살표가 움직이는) 
}

function convertLat(latitude){
    return Math.round((37.47118-latitude)*190028.45849802);
}

function convertLong(longitude){
    return Math.round((126.96237-longitude)*152649.77242893); 
}

var moveLeaflet = function(value) {
    return function() {
        //moveLeaflet("aa")(); 이렇게 사용해야 한다 

        // 폴리 라인 그리기
        // var arrow = L.polyline([[73.5, -90], [73.5, -75]], {}).addTo(map);
        // var arrowHead = L.polylineDecorator(arrow).addTo(map);

        // var arrowOffset = 0;
        // var anim = window.setInterval(function() {
        //     arrowHead.setPatterns([
        //         {offset: arrowOffset+'%', repeat: 0, symbol: L.Symbol.arrowHead({pixelSize: 15, polygon: false, pathOptions: {stroke: true}})}
        //     ])
        //     if(++arrowOffset > 100)
        //         arrowOffset = 0;
        // }, 50); // 속도다.(화살표가 움직이는) 


       //  // if(str==""){
       //  //     window.map.removeLayer(searchMarker);
       //  //     return;
       //  // }

       // 점 찍는 알고리즘 시작
       // moveLeaflet([37.460424, 126.952948])(); 이렇게 사용하면 된다. 
       var longitude = convertLong(value[1]);
       var latitude = convertLat(value[0]);
       var img = [
           5001,  // original width of image
           2501   // original height of image
       ];
       var rc = new L.RasterCoords(window.map, img);
       searchMarker = new L.marker(rc.unproject([latitude,longitude]));
       window.map.addLayer(searchMarker);
       window.map.panTo(rc.unproject([latitude,longitude]));
       // window.map.setZoom(3);
       window.map.setView(rc.unproject([latitude,longitude]), 3);
    }
}

var removeMarker = function() {
    return function() {
        map.removeLayer(searchMarker);
    }
}

// function draw() {
//     // var data1 = L.latLng(1923, 2093);
//     // var data2 = L.latLng(1374, 2148);
//     var img = [
//        5001,  // original width of image
//        2501   // original height of image
//     ];
//     var rc = new L.RasterCoords(window.map, img);

//     var data1 = rc.unproject([609, 1104]);
//     var data2 = rc.unproject([674, 1102]);
//     // var data3 = [73.5, -105];
//     // var data4 = [70.5, -105];

//     var latlngs = [data1, data2];

//     // create a red polyline from an array of LatLng points
//     var polyline = L.polyline(latlngs, {color: 'red'}).addTo(map);

//     // zoom the map to the polyline
//     map.fitBounds(polyline.getBounds());
// }

// value의 예시: [[lat0, lng0], [lat1, lng1], …]
// lat: [123, 234], long: [123,456]
function addPolyline(value1, value2){
    var num1 = value1.length;
    var res1 = value1.substring(1, num1-1);

    var num2 = value2.length;
    var res2 = value2.substring(1, num2-1);

    var res1Array = res1.split(",");
    var res2Array = res2.split(",");

    var i;
    var result = new Array(); 
    for (i = 0; i < res1Array.length; ++i) { 
        result[i] = new Array();
        result[i][0] = res1Array[i];
        result[i][1] = res2Array[i];
    }
    // if(polyline){
    //     window.map.removeLayer(polyline);
    // }
    
    var img = [
               5001,  // original width of image
               2501   // original height of image
                        ];
    
    var rc = new L.RasterCoords(window.map, img);
    
    // var result = value;
    var i;
    var linePath = [];
    /*
    for(i=0; i<result.length-1; i++){
        linePath.push([rc.unproject([result[i][1],result[i][0]]),rc.unproject([result[i+1][1],result[i+1][0]])]);
    }
    polyline = new L.multiPolyline(linePath);*/
    
    for (i=0; i<result.length; i++){
        linePath.push(rc.unproject([result[i][1], result[i][0]]));
    }
    polyline = new L.polyline(linePath, {}).addTo(window.map);
    window.map.fitBounds(polyline.getBounds());
    //window.map.addLayer(polyline);
}


var drawRoute = function(value1, value2) {
    return function() {
        var num1 = value1.length;
        var res1 = value1.substring(1, num1-1);

        var num2 = value2.length;
        var res2 = value2.substring(1, num2-1);

        var res1Array = res1.split(",");
        var res2Array = res2.split(",");

        var i;
        var result = new Array(); 
        for (i = 0; i < res1Array.length; ++i) { 
            result[i] = new Array();
            result[i][0] = res1Array[i];
            result[i][1] = res2Array[i];
        }
        // if(polyline){
        //     window.map.removeLayer(polyline);
        // }
        
        var img = [
                   5001,  // original width of image
                   2501   // original height of image
                            ];
        
        var rc = new L.RasterCoords(window.map, img);
        
        // var result = value;
        var i;
        var linePath = [];
        /*
        for(i=0; i<result.length-1; i++){
            linePath.push([rc.unproject([result[i][1],result[i][0]]),rc.unproject([result[i+1][1],result[i+1][0]])]);
        }
        polyline = new L.multiPolyline(linePath);*/
        
        for (i=0; i<result.length; i++){
            linePath.push(rc.unproject([result[i][1], result[i][0]]));
        }
        polyline = new L.polyline(linePath, {}).addTo(window.map);
        window.map.fitBounds(polyline.getBounds());
        //window.map.addLayer(polyline);       
    }
}