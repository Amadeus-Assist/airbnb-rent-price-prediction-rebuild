import React, {useEffect} from 'react';
import '../Chart.css'

function SimpleLineChart(props) {
    const {id, data, config} = props

    useEffect(() => {
        const dataSource = {
            chart: config,
            data: data
        };

        window.FusionCharts.ready(function () {
            let myChart = new window.FusionCharts({
                type: "line",
                renderAt: id,
                width: "100%",
                height: "100%",
                dataFormat: "json",
                dataSource: dataSource
            }).render();
        });
    }, [data, id, config])

    return (
        <div className="chart" id={id}>

        </div>
    );
}

export default SimpleLineChart;