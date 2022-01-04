import React, {Component} from 'react';

class GeoVisual extends Component {

    componentDidMount() {
        let map = window.L.map('mapid').setView([window.city_localtion.get(city)[0], window.city_localtion.get(city)[1]], window.city_localtion.get(city)[2]);
        window.L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
            attribution: '<a href="https://www.mapbox.com/">Mapbox</a> by Xiangcong Kong',
            maxZoom: 18,
            id: 'mapbox/streets-v11',
            tileSize: 512,
            zoomOffset: -1,
            accessToken: 'pk.eyJ1IjoiZ3JhdmVzY243IiwiYSI6ImNrdzFiMmNsbmEyeGwybnFwdzdwdXh3bWgifQ.yrP_vrJ8VRA-1uqTUwkPig'
        }).addTo(map);


        // add neighborhoods
        $.getJSON("/static/data/" + city + "-neighborhoods.geojson", function (neighborhoods) {
            window.L.geoJson(neighborhoods, {
                style: function () {
                    return {color: "rgba(44,66,194,0.95)", weight: 2, fillColor: "#416272", fillOpacity: .4};
                }
            }).addTo(map);
        });

    }

    render() {
        return (
            <div id="mapid" className="map"/>
        );
    }
}

export default GeoVisual;