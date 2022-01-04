import React, {Component} from 'react';
import LeafletMap from "../../components/LeafletMap/LeafletMap";
import './WorldMap.css'

class WorldMap extends Component {
    render() {
        return (
            <LeafletMap center={[40.77, -73.97]} zoom={3.5} />
        );
    }
}

export default WorldMap;