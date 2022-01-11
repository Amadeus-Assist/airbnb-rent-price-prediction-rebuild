import React, {useState, useEffect} from 'react';
import {useParams} from "react-router-dom";
import './HistoryCharts.css'

function HistoryCharts(){
    const params = useParams()
    const {city} = params

    useEffect(() => {
        async function fetchHistoryData() {
            const response = await fetch(`/api/history/covid/${city}`)
            const historyData = await response.json()

            const {covidHistoryDate, covidHistoryData} = historyData
            // console.log("Returned Date: ", covidHistoryDate)
            // console.log("Returned Data: ", covidHistoryData)
        }

        fetchHistoryData().catch(console.err)
    }, [city])

    return (
        <div className="historycharts">

        </div>
    )
}

export default HistoryCharts