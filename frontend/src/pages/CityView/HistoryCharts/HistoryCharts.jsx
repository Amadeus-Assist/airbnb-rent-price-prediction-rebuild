import React, {useState, useEffect} from 'react';
import {useParams} from "react-router-dom";
import './HistoryCharts.css'
import LineChart from "../../../components/Chart/LineChart";

function HistoryCharts() {
    const params = useParams()
    const {city} = params
    const [covidHistory, setCovidHistory] = useState()
    const [covidReady, setCovidReady] = useState(false)

    const covidHisConfig = {
        id: "covidHistory",
        theme: "candy",
        title: "Daily New Cases Trend of COVID-19",
        yAxisMinValue: "0",
        labelStep: "28",
    }

    useEffect(() => {
        async function fetchHistoryData() {
            const response = await fetch(`/api/history/covid/${city}`)
            const historyData = await response.json()

            const {covidHistory} = historyData
            setCovidHistory(covidHistory)
            setCovidReady(true)
            // console.log("Returned Date: ", covidHistoryDate)
            // console.log("Returned Data: ", covidHistoryData)
        }

        fetchHistoryData().catch(console.err)
    }, [city])

    return (
        <div className="historycharts">
            { covidReady? <LineChart data={covidHistory} {...covidHisConfig}/>:<div/>}
        </div>
    )
}

export default HistoryCharts