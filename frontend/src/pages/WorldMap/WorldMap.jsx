import React from 'react';
import LeafletMap from "../../components/LeafletMap/LeafletMap";
import './WorldMap.css'

function WorldMap(){
    return (
        <LeafletMap center={[40.77, -73.97]} zoom={3.5} markers={[{city:'nyc', location: [40.71, -74]}]} />
    );
}

export default WorldMap;