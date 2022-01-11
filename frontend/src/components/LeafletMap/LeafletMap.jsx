import React from 'react';
import {useNavigate} from 'react-router-dom'
import {MapContainer, TileLayer, Marker, GeoJSON} from 'react-leaflet'
import './LeafletMap.css'

function LeafletMap(props){
    const {center, zoom, markers, geoJson} = props
    const navigate = useNavigate();

    return (
        <MapContainer center={center} zoom={zoom}>
            <TileLayer
                attribution='<a href="https://www.mapbox.com/">Mapbox</a> by Xiangcong Kong'
                url="https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}"
                maxZoom={18}
                id='mapbox/streets-v11'
                tileSize={512}
                zoomOffset={-1}
                accessToken='pk.eyJ1IjoiZ3JhdmVzY243IiwiYSI6ImNrdzFiMmNsbmEyeGwybnFwdzdwdXh3bWgifQ.yrP_vrJ8VRA-1uqTUwkPig'
            />
            {
                markers === undefined ? <div/> : markers.map((markerObj) => {
                    return <Marker eventHandlers={{
                        click: () => {
                            navigate(`/cityview/${markerObj.city}`)
                        },
                    }} key={markerObj.city} position={markerObj.location}/>
                })
            }

            {
                geoJson === undefined ? <div/> : <GeoJSON data={geoJson} style={{
                    color: "rgba(44,66,194,0.95)",
                    weight: 2,
                    fillColor: "#416272",
                    fillOpacity: .4
                }}/>
            }
        </MapContainer>
    );
}

export default LeafletMap;