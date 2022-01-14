import React, {useEffect} from 'react';
import '../Chart.css'

function MultipleLineChart(props) {
    const{config, categories, dataset, id} = props

    useEffect(() => {
        const dataSource = {
            chart: config,
            categories: categories,
            dataset: dataset
        };

        window.FusionCharts.ready(function() {
            var myChart = new window.FusionCharts({
                type: "msline",
                renderAt: id,
                width: "100%",
                height: "100%",
                dataFormat: "json",
                dataSource
            }).render();
        });
    })

    return (
        <div className="chart" id={id}>

        </div>
    )
}

export default MultipleLineChart;