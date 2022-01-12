import React, {useEffect} from 'react';
import './LineChart.css'

function LineChart(props) {
    const {id, data, theme, title, yAxisMinValue, labelStep} = props

    useEffect(() => {
        const dataSource = {
            chart: {
                caption: title,
                adjustDiv: "0",
                yAxisMinValue: yAxisMinValue,
                labelFontSize: "10",
                drawAnchors: "0",
                labelStep: labelStep,
                theme: theme,
                labelDisplay: "rotate",
                slantLabel: "1"
            },
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
    }, [data, id, theme, title, yAxisMinValue, labelStep])

    return (
        <div className="chart" id={id}>

        </div>
    );
}

export default LineChart;