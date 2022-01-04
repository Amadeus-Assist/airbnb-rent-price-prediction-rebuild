import React, {Component} from 'react';
import {MapContainer, TileLayer, Marker, GeoJSON} from 'react-leaflet'
import './LeafletMap.css'

class LeafletMap extends Component {

    componentDidMount() {
        window.$.getJSON('/static/data/nyc-neighborhoods.geojson', data => {
            console.log('data: ', data)
            this.setState({geo: data})
        })
    }

    render() {
        let {center, zoom, markers} = this.props

        let geo
        if(this.state != null && this.state.geo !== undefined){
            geo = this.state.geo
        }

        if (markers === undefined) {
            markers = []
        }

        // const city = 'nyc'

        // import geo from '/public/static/data/nyc-neighborhoods.geojson'

        // let geo = require(`../../../public/static/data/${city}-neighborhoods.geojson`)
        // console.log(geo)

        // let geo
        //
        console.log('geo: ', geo)


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
                    markers.map((markerObj) => {
                        return <Marker key={markerObj.city} position={markerObj.location}/>
                    })
                }

                {
                    geo === undefined ? <div/> : <GeoJSON data={geo} style={{
                        color: "rgba(44,66,194,0.95)",
                        weight: 2,
                        fillColor: "#416272",
                        fillOpacity: .4
                    }}/>
                }
            </MapContainer>
        );
    }
}

export default LeafletMap;