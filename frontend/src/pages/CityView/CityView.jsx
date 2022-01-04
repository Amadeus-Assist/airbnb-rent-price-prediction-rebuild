import React, {Component} from 'react';
import GeoVisual from "../../components/Display/GeoVisual/GeoVisual";
import Charts from "../../components/Display/Charts/Charts";

class CityView extends Component {

    render() {
        return (
                <div>
                    <GeoVisual/>
                    <Charts/>
                </div>
        );
    }
}

export default CityView;