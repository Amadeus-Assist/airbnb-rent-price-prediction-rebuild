import React, {Component} from 'react';
import ChartMenu from "./ChartMenu/ChartMenu";
import ChartDisplay from "./ChartDisplay/ChartDisplay";

class Charts extends Component {
    render() {
        return (
            <div>
                <ChartMenu/>
                <ChartDisplay/>
            </div>
        );
    }
}

export default Charts;