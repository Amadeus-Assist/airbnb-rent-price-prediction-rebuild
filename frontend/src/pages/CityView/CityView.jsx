import React, {Component} from 'react';
import Charts from "../../components/Display/Charts/Charts";
import LeafletMap from "../../components/LeafletMap/LeafletMap";

class CityView extends Component {
    state = {ready: false}

    async componentDidMount() {
        console.log("Cityview.props: ", this.props)
        const {city} = this.props.match.params
        const geoResponse = await fetch(`/static/data/${city}-neighborhoods.geojson`)
        const geoJson = await geoResponse.json()

        const cityAttrResponse = await fetch(`/api/cityview/${city}`)
        const cityAttrData = await cityAttrResponse.json()
        const {center, zoom} = cityAttrData

        this.setState({center: center, zoom: zoom, geoJson: geoJson, ready: true})
    }

    render() {
        const {center, zoom, geoJson, ready} = this.state

        return (
            <div>
                <div className="geovisual">
                    {ready ? <LeafletMap center={center} zoom={zoom} geoJson={geoJson}/> :
                        <div/>}
                </div>
                <Charts/>
            </div>
        );
    }
}

export default CityView;