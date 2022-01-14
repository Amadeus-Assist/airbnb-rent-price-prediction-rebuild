import React, {useState, useEffect} from 'react';
import {useParams} from "react-router-dom";
import './HistoryCharts.css'
import SimpleLineChart from "../../../components/Chart/SimpleLineChart/SimpleLineChart";
import MultipleLineChart from "../../../components/Chart/MultipleLineChart/MultipleLineChart";

function HistoryCharts() {
    const params = useParams()
    const {city} = params
    const [covidHistory, setCovidHistory] = useState()
    const [covidReady, setCovidReady] = useState(false)
    const [housingCategories, setHousingCategories] = useState()
    const [housingDataset, setHousingDataset] = useState()
    const [housingReady, setHousingReady] = useState(false)

    const covidHisConfig = {
        id: "covidHistory",
        theme: "candy",
        title: "Daily New Cases Trend of COVID-19",
        yAxisMinValue: "0",
        labelStep: "28",
    }

    const housingHisConfig = {
        caption: "Airbnb Housing Price History",
        yaxisname: "price/$",
        drawAnchors: "0",
        showvalues: "0",
        labelStep: "60",
        compactdatamode: "1",
        slantLabel: "1",
        drawcrossline: "1",
        plottooltext: "$seriesName is <b>$dataValue</b>",
        theme: "fusion"
    }

    useEffect(() => {
        async function fetchCovidHistoryData() {
            const response = await fetch(`/api/history/covid/${city}`)
            const historyData = await response.json()

            const {covidHistory} = historyData
            setCovidHistory(covidHistory)
            setCovidReady(true)
            // console.log("Returned Date: ", covidHistoryDate)
            // console.log("Returned Data: ", covidHistoryData)
        }

        async function fetchHousingHistoryData() {
            const response = await fetch(`/api/history/housing/${city}`)
            const historyData = await response.json()

            const {housingHistory} = historyData

            let categories = [{
                category: []
            }]
            let dataset = [{
                seriesname: "average price",
                data: []
            }, {
                seriesname: "median price",
                data: []
            }]

            for (let idx in housingHistory) {
                const housingPoint = housingHistory[idx]
                const {label, avg_price, median_price} = housingPoint
                categories[0]["category"].push({label: label})
                dataset[0]["data"].push({value: avg_price})
                dataset[1]["data"].push({value: median_price})
                // categories[0]["category"].push(housingPoint["label"])
                // dataset[0]["data"].push(housingPoint["avg_price"])
                // dataset[1]["data"].push(housingPoint["median_price"])
            }

            setHousingCategories(categories)
            setHousingDataset(dataset)
            setHousingReady(true)
        }

        fetchCovidHistoryData().catch(console.err)
        fetchHousingHistoryData().catch(console.err)
    }, [city])

    return (
        <div className="historycharts">
            <div className="covidHistoryChart">
                {covidReady ? <SimpleLineChart data={covidHistory} {...covidHisConfig}/> : <div/>}
            </div>
            <div className="housingHistoryChart">
                {housingReady ?
                    <MultipleLineChart config={housingHisConfig} categories={housingCategories} dataset={housingDataset}
                                       id="housingHistory"/> : <div/>}
            </div>
        </div>
    )
}

export default HistoryCharts