import React, {Component} from 'react';

class WorldMap extends Component {

    componentDidMount() {
        // world map layer
        let myMap = window.L.map('worldmapid').setView([40.77, -73.97], 3.5);

        window.L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
            attribution: '<a href="https://www.mapbox.com/">Mapbox</a> by Xiangcong Kong',
            maxZoom: 18,
            id: 'mapbox/streets-v11',
            tileSize: 512,
            zoomOffset: -1,
            accessToken: 'pk.eyJ1IjoiZ3JhdmVzY243IiwiYSI6ImNrdzFiMmNsbmEyeGwybnFwdzdwdXh3bWgifQ.yrP_vrJ8VRA-1uqTUwkPig'
        }).addTo(myMap);

        // const cityMarkers = new Map();

        window.city_localtion.forEach(function (value, key){
            let city_marker = window.L.marker([value[0], value[1]]).addTo(myMap);
            city_marker.on('click', function (e) {
                window.location.replace('cityview/'+key);
            });
            // cityMarkers.set(item[0], city_marker)
        });
    }

    render() {
        return (
            <div>
                <div className="outer-container">
                    <div id="worldmapid" className="worldmap"></div>
                </div>
            </div>
        );
    }
}

export default WorldMap;